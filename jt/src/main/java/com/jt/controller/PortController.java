package com.jt.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PortController {

    @Value("${server.port}")
    private Integer port;

    @GetMapping("/getPort")
    public String getPort(){

        return "访问端口:" + port;
    }
}
