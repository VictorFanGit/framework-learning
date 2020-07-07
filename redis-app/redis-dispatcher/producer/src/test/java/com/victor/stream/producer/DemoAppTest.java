package com.victor.stream.producer;


import org.junit.*;
import redis.clients.jedis.Jedis;

import java.util.Map;

public class DemoAppTest {
    private RedisConfigInfo redisConfigInfo = new RedisConfigInfo().setHost("120.78.182.183").setPort(9079);

    /*@Before
    public void before() {
        JedisService.setConfigInfo(redisConfigInfo);
        RegisterService.getInstance().setTopic("video-stream");
    }

    @After
    public void after() {
        //释放连接池
        if (JedisService.getPool() != null) {
            JedisService.getPool().destroy();
        }
    }

    @Test
    public void flushRedisData() {
        Jedis jedis = null;
        try {
            jedis = JedisService.getPool().getResource();
            jedis.flushAll();
        } finally {
            if (jedis != null) {
                //return the instance to pool
                jedis.close();
            }
        }
    }

    @Test
    public void addTask() throws RedisOperationException {
        RegisterService.getInstance().register("group1-cam1", "192.168.1.1:8080");
        RegisterService.getInstance().register("group1-cam2", "192.168.1.2:8080");
        RegisterService.getInstance().register("group1-cam3", "192.168.1.3:8080");
        RegisterService.getInstance().register("group1-cam4", "192.168.1.4:8080");
        RegisterService.getInstance().register("group1-cam5", "192.168.1.5:8080");
    }

    @Test
    public void addTask2() throws RedisOperationException {
        RegisterService.getInstance().register("group1-cam1", "192.168.1.6:8080");
    }

    @Test
    public void removeTask() throws RedisOperationException {
        RegisterService.getInstance().unregister("group1-cam1");
    }

    @Test
    public void queryAllTasks() throws RedisOperationException {
        Map<String, String> allActiveServices = RegisterService.getInstance().getAllActiveTasks();
        for (String s : allActiveServices.keySet()) {
            System.out.println("task id: " + s + " detail info: " + allActiveServices.get(s));
        }
    }*/

}