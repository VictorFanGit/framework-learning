package com.victor.ioc;

import com.victor.ioc.dao.Dao;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
//        applicationContext.register(AppConfig.class);
//        applicationContext.refresh();
        Dao dao = (Dao) applicationContext.getBean("indexDao");
        dao.query();
    }

}
