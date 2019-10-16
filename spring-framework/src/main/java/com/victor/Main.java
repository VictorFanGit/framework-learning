package com.victor;

import com.victor.dao.Dao;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(AppConfig.class);
        applicationContext.refresh();
        Dao dao = (Dao) applicationContext.getBean("indexDao");
        dao.query();
    }

}
