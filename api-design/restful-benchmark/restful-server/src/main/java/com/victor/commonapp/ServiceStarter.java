package com.victor.commonapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ServiceStarter implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceStarter.class);

    @Autowired
    RestTemplate restTemplate;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
/*        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                LOGGER.info("Start to init service...");
                long startTime = System.currentTimeMillis();
                for (int i = 0; i < 100; i++) {
                    ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8090/hello", "", String.class);
//                    restTemplate.getForEntity("http://localhost:8090/hello",String.class);
//                    System.out.println("call...");
                }
                long endTime = System.currentTimeMillis();
                System.out.println("num-per-sec: " + 100000 / (endTime - startTime));
            }
        }).start();*/

    }
}
