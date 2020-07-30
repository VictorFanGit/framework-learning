package com.victor.commonapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class HelloController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);


    public static final String LINE_SEPARATOR = Character.toString((char) 1);

    public static final String WORD_SEPARATOR = Character.toString((char) 2);


    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/add/hello")
    public String addHello() {
        LOGGER.info("Get add hello request.");
        return "hello";
    }


    @GetMapping("/get/hello")
    public String getHello() {
        LOGGER.info("Get get hello request.");
        return "hello";
    }
}
