package com.victor.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OnlineController {
    @GetMapping("/online")
    public String online(@RequestParam String esn) {
        System.out.println(esn);
        return "success";
    }
}
