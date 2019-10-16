package com.victor.dao;


import org.springframework.stereotype.Component;

@Component
public class IndexDao implements Dao {
    @Override
    public void query() {
        System.out.println("----query-----");
    }
}
