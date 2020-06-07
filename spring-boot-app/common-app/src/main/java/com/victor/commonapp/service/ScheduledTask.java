package com.victor.commonapp.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {

    @Scheduled(initialDelay = 10000, fixedRate = 1000)
    public void task1() {
        System.out.println("Execute scheduled task1.");
    }

    // @Scheduled(cron = "0 * * * * ?")  //每分钟执行一次
    @Scheduled(cron = "0 0 23 * * ?")   //每晚23点执行, 可用于定期删除数旧数据
    public void task2() {
        System.out.println("Execute scheduled task2.");
    }
}
