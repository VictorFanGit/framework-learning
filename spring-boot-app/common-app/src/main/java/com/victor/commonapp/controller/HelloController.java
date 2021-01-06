package com.victor.commonapp.controller;

import com.victor.commonapp.ServiceStarter;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Api(tags = "Hello")
public class HelloController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceStarter.class);

    @GetMapping("/hello")
    public Map<String, String> hello() {
        HashMap<String, String> map = new HashMap<>();
        map.put("message", "hello");
        return map;
    }

    @PostMapping("/hello")
    public Map<String, String> setHello(@RequestParam(value = "code", required = false) String code,
                                        @RequestParam(value = "version", required = false) Integer version) {

        System.out.println("========hello==========");
        System.out.println(code);
        System.out.println(version);
        HashMap<String, String> map = new HashMap<>();
        map.put("result", "ok");
        return map;
    }

}
