package ru.iu3.backend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
@RequestMapping("/api/v1")
public class SampleController {

    @GetMapping("/title")
    public String getTitle() {
        return "<title>Hello from Back-end</title>";
    }

    @GetMapping("/data")
    public String getData() throws UnknownHostException {

        // Local address
        String localIp = InetAddress.getLocalHost().getHostName();
        return "<p>" + localIp + "</p>";
//        InetAddress.getLocalHost().getHostName();
//
//        // Remote address
//        InetAddress.getLoopbackAddress().getHostAddress();
//        InetAddress.getLoopbackAddress().getHostName();
    }
}
