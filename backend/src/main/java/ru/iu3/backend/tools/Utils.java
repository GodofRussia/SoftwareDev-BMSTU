package ru.iu3.backend.tools;

import org.springframework.security.crypto.codec.Hex;
import java.security.MessageDigest;

public class Utils {

    public static String ComputeHash(String pwd, String salt) {
        MessageDigest digest;
        byte[] w = Hex.decode(new String(Hex.encode(pwd.getBytes())) + new String(Hex.encode(salt.getBytes())));
        try {
            digest = MessageDigest.getInstance("SHA-256");
        }
        catch (Exception ex) {
            return new String();
        }
        return new String(Hex.encode(digest.digest(w)));
    }
}