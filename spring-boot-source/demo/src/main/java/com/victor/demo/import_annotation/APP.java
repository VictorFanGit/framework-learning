package com.victor.demo.import_annotation;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.stream.Stream;

/**
 * 主要展示@Import注解可导入的类型
 * 普通类，Configuration注解的类，ImportSelector 的实现类，ImportBeanDefinitionRegistrar 的实现类
 */
public class APP {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext(ColorConfiguration.class);
        String[] names = ctx.getBeanDefinitionNames();
        Stream.of(names).forEach(System.out::println);

    }
}
