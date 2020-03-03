package com.victor.springsecurityjwt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello JWT!";
    }

    @GetMapping("/admin")
    public String admin() {
        return "Hello admin!";
    }
}
