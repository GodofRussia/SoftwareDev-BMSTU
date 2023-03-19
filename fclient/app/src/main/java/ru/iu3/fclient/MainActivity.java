package ru.iu3.fclient;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ru.iu3.fclient.databinding.ActivityMainBinding;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements TransactionEvents {

    // Used to load the 'fclient' library on application startup.
    static {
        System.loadLibrary("fclient");
        System.loadLibrary("mbedcrypto");
    }

    private String pin;

    @Override
    public String enterPin(int ptc, String amount) {
        pin = new String();
        Intent it = new Intent(MainActivity.this, PinpadActivity.class);
        it.putExtra("ptc", ptc);
        it.putExtra("amount", amount);
        synchronized (MainActivity.this) {
            activityResultLauncher.launch(it);
            try {
                MainActivity.this.wait();
            } catch (Exception ex) {
                System.out.println("ERROR");
            }
        }
        return pin;
    }

    protected String getPageTitle(String html)
    {
        Pattern pattern = Pattern.compile("<title>(.+?)</title>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(html);
        String p;
        if (matcher.find())
            p = matcher.group(1);
        else
            p = "Not found";
        return p;
    }

    protected void testHttpClient()
    {
        new Thread(() -> {
            try {
                HttpURLConnection uc = (HttpURLConnection)
                        (new URL("https://www.wikipedia.org").openConnection());
                InputStream inputStream = uc.getInputStream();
                String html = IOUtils.toString(inputStream);
                String title = getPageTitle(html);
                runOnUiThread(() ->
                {
                    Toast.makeText(this, title, Toast.LENGTH_LONG).show();
                });

            } catch (Exception ex) {
                Log.e("fapptag", "Http client fails", ex);
            }
        }).start();
    }

    @Override
    public void transactionResult(boolean result) {
        runOnUiThread(()-> {
            Toast.makeText(MainActivity.this, result ? "ok" : "failed", Toast.LENGTH_SHORT).show();
        });
    }

    private ActivityMainBinding binding;
    ActivityResultLauncher activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback() {
                    @Override
                    public void onActivityResult(Object result) {
                        ActivityResult res = (ActivityResult) result;
                        if (res.getResultCode() == Activity.RESULT_OK) {
                            Intent data = res.getData();
                            // обработка результата
                            // String pin = data.getStringExtra("pin");
                            // Toast.makeText(MainActivity.this, pin, Toast.LENGTH_SHORT).show();
                            assert data != null;
                            pin = data.getStringExtra("pin");
                            synchronized (MainActivity.this) {
                                MainActivity.this.notifyAll();
                            }
                        }
                    }
                });
    }

    public void onButtonClick(View v)
    {
        byte[] trd = stringToHex("9F0206000000000100");
        transaction(trd);
    }

    public static byte[] stringToHex(String s)
    {
        byte[] hex;
        try
        {
            hex = Hex.decodeHex(s.toCharArray());
        }
        catch (DecoderException ex)
        {
            hex = null;
        }
        return hex;
    }

    /**
     * A native method that is implemented by the 'fclient' native library,
     * which is packaged with this application.
     */

    public native boolean transaction(byte[] trd);
    public native String stringFromJNI();
    public native byte[] encrypt(byte[] key, byte[] data);
    public native byte[] decrypt(byte[] key, byte[] data);
    public static native int initRng();
    public static native byte[] randomBytes(int no);
}