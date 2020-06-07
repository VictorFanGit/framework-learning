package com.victor.commonapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

/**
 * An simple example for rest template
 */

@Component
public class RestTemplateInvoker {
    @Autowired
    private RestTemplate restTemplate;

    public String postInvoke(String request) throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.setConnection("keep-alive");
        httpHeaders.add("token", "12345");
        String requestBody = request;
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, httpHeaders);
        String url = "localhost:8080/hello";
        ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        if (resp.getStatusCode().isError()) {
            throw new Exception("Get response with error: " + resp.toString());
        }
        String body = resp.getBody();
        if (body == null) {
            throw new Exception("Response body is null.");
        }
        return body;
    }

    /**
     *该用法比较特殊，在GET请求中传入body参数
     */
    public String getWithBody(String request) throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.setConnection("keep-alive");
        httpHeaders.add("token", "12345");
        String requestBody = request;
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, httpHeaders);
        String url = "localhost:8080/hello";
        ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        if (resp.getStatusCode().isError()) {
            throw new Exception("Get response with error: " + resp.toString());
        }
        String body = resp.getBody();
        if (body == null) {
            throw new Exception("Response body is null.");
        }
        return body;
    }
}
