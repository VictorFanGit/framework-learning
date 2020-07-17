package com.victor.stream.producer;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 可在测试类DemoAppTest 进行相关操作
 */
public class DemoApp {

    private static int instanceNum = 0;
    private static final String TOPIC = "video-stream";

    public static String generateInstanceId(String topic) {
        instanceNum++;
        if (instanceNum > 1000) {
            instanceNum = 1;
        }
        return topic + "-task-" + instanceNum;
    }

    public static void main(String[] args) {

        BlockingQueue<String> taskQueue = new ArrayBlockingQueue<String>(1000);

        RedisConfigInfo redisConfigInfo = new RedisConfigInfo().setHost("120.78.182.183").setPort(9079);
        JedisService.setConfigInfo(redisConfigInfo);
        //just for testing
/*        Jedis jedis = null;
        try {
            jedis = JedisService.getPool().getResource();
            jedis.flushAll();
        } finally {
            if (jedis != null) {
                //return the instance to pool
                jedis.close();
            }
        }*/

        RegisterService registerService = new RegisterService(TOPIC);
        registerService.startDataMonitor();

        Random random = new Random();
        while (true) {
            try {
                int rad = random.nextInt(2000);
                Thread.sleep(rad);
                String task = generateInstanceId(TOPIC);
                registerService.register(task, "192.168.1.1:8080");
                taskQueue.put(task);
                if (taskQueue.size() > 2 && rad > 1000) {
                    String t = taskQueue.poll(1, TimeUnit.SECONDS);
                    registerService.unregister(t);
                }
                if (taskQueue.size() > 10) {
                    for (int i = 0; i < taskQueue.size(); i++) {
                        String t = taskQueue.poll(1, TimeUnit.SECONDS);
                        registerService.unregister(t);
                    }
                }

            } catch (InterruptedException e) {
                registerService.shutdownDataMonitor();

                //释放连接池
                if (JedisService.getPool() != null) {
                    JedisService.getPool().destroy();
                }
                break;
            } catch (RedisOperationException e) {
                System.out.println("Can not access redis.");
            }
        }
    }
}
