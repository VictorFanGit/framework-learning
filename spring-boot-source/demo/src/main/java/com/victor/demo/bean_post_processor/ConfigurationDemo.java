package com.victor.demo.bean_post_processor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.victor.demo.bean_post_processor")
public class ConfigurationDemo {

    @Bean
    public Cat cat(){
        return new Cat("cat");
    }
}
