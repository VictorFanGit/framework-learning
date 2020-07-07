package com.victor.nacosdemo.controller;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class HelloController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);

    public static final String LINE_SEPARATOR = Character.toString((char) 1);

    public static final String WORD_SEPARATOR = Character.toString((char) 2);


    @GetMapping("/start")
    public String start() {
        StringBuilder sb = new StringBuilder();
        sb.append("example").append(WORD_SEPARATOR);
        sb.append("DEFAULT_GROUP").append(WORD_SEPARATOR);
        String md5 = MD5.getInstance().getMD5String("useLocalCache=true");
        System.out.println(md5);
        sb.append(md5).append(LINE_SEPARATOR);


        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        String value = sb.toString();
        formparams.add(new BasicNameValuePair("Listening-Configs", value));


        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost("http://120.78.182.183:9006/nacos/v1/cs/configs/listener");
        
        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httpPost.setEntity(uefEntity);
//            httpPost.setHeader("exConfigInfo", "true");
//            httpPost.setHeader("RequestId", UUID.randomUUID().toString());
            httpPost.setHeader("Long-Pulling-Timeout", "30000");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        CloseableHttpResponse response;
        try {
            response = httpClient.execute(httpPost);
            System.out.println(response.toString());
            if (response.toString().contains("Content-Length: 0")) {
                return "no changed";
            } else {
                if (response.toString() != null) {
                    HttpEntity entity = response.getEntity();
                    byte[] bytes = new byte[100];
                    entity.getContent().read(bytes);
                    System.out.println("===content===");
                    String ret = new String(bytes);
                    System.out.println(ret);
                    return ret;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error";
    }
}
