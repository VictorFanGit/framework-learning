package com.victor.demo.import_annotation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ColorRegistrarConfiguration和Yellow都会被注册进IOC
 */
@Configuration
public class ColorRegistrarConfiguration {
    @Bean
    public Yellow yellow(){
        return new Yellow();
    }
}
