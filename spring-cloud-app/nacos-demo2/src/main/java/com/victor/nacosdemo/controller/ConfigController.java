package com.victor.nacosdemo.controller;


import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Properties;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping("config")
public class ConfigController {

    private static Listener listener = new MyListenser();

    @RequestMapping(value = "/get", method = GET)
    @ResponseBody
    public String get() {
        try {
            String serverAddr = "120.78.182.183:9006";
            String dataId = "example";
            String group = "DEFAULT_GROUP";
            Properties properties = new Properties();
            properties.put("serverAddr", serverAddr);
            ConfigService configService = NacosFactory.createConfigService(properties);
            String content = configService.getConfig(dataId, group, 5000);
            System.out.println("===content===");
            System.out.println(content);
        } catch (NacosException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "success, check the result by system out";
    }


    @RequestMapping(value = "/listen", method = GET)
    @ResponseBody
    public String listen() {
        String serverAddr = "120.78.182.183:9006";
        String dataId = "example";
        String group = "DEFAULT_GROUP";
        Properties properties = new Properties();
        properties.put("serverAddr", serverAddr);
        ConfigService configService = null;
        try {
            configService = NacosFactory.createConfigService(properties);
            String content = configService.getConfig(dataId, group, 5000);
            System.out.println("===content===");
            System.out.println(content);
        } catch (NacosException e) {
            e.printStackTrace();
        }

        try {
            configService.addListener(dataId, group, listener);
        } catch (NacosException e) {
            e.printStackTrace();
        }
        return "success, check the result by system out";
    }


    //该接口不生效，需要进一步调试
    @RequestMapping(value = "/delisten", method = GET)
    @ResponseBody
    public String delisten() {
        String serverAddr = "120.78.182.183:9006";
        String dataId = "example";
        String group = "DEFAULT_GROUP";
        Properties properties = new Properties();
        properties.put("serverAddr", serverAddr);
        ConfigService configService = null;
        try {
            configService = NacosFactory.createConfigService(properties);
            configService.removeListener(dataId, group, listener);
        } catch (NacosException e) {
            e.printStackTrace();
        }
        return "success";
    }

}
