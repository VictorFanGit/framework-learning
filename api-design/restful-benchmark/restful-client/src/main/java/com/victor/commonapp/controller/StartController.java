package com.victor.commonapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
public class StartController {
    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/start-test")
    public Map<String, Long> startTesting() {
        Map<String, Long> map = new HashMap<>();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8090/hello", "", String.class);
        }
        long endTime = System.currentTimeMillis();
        map.put("num-per-sec", 10000000 / (endTime - startTime));
        return map;
    }
}
