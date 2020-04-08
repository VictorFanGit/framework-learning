package com.victor.demo.bean_post_processor;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext(ConfigurationDemo.class);
        Cat bean = ctx.getBean(Cat.class);
        System.out.println("cat name is:" + bean.name);
    }
}
