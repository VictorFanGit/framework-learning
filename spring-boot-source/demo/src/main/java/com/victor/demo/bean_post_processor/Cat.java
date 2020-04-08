package com.victor.demo.bean_post_processor;

import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;

public class Cat implements InitializingBean {
    String name;

    public Cat(String name) {
        this.name = name;
        System.out.println("Cat construct run...");
    }

    @PostConstruct
    public void AfterInit() {
        System.out.println("Cat postConstruct run...");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("Cat afterPropertiesSet run...");
    }
}
