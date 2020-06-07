package com.victor.nacosdemo.controller;

import com.alibaba.nacos.api.config.listener.Listener;

import java.util.concurrent.Executor;

public class MyListenser implements Listener {
    @Override
    public void receiveConfigInfo(String configInfo) {
        System.out.println("listener received: " + configInfo);
    }

    @Override
    public Executor getExecutor() {
        return null;
    }
}
