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

    public static String generateInstanceId() {
        instanceNum++;
        if (instanceNum > 1000) {
            instanceNum = 1;
        }
        return RegisterService.getInstance().getTopic() + "-task-" + instanceNum;
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

        RegisterService.getInstance().setTopic("video-stream");
        RegisterService.getInstance().startDataMonitor();

        Random random = new Random();
        while (true) {
            try {
                int rad = random.nextInt(2000);
                Thread.sleep(rad);
                String task = generateInstanceId();
                RegisterService.getInstance().register(task, "192.168.1.1:8080");
                taskQueue.put(task);
                if (taskQueue.size() > 5 && rad > 1000) {
                    String t = taskQueue.poll(1, TimeUnit.SECONDS);
                    RegisterService.getInstance().unregister(t);
                }
                if (taskQueue.size() > 20) {
                    for (int i = 0; i < 20; i++) {
                        String t = taskQueue.poll(1, TimeUnit.SECONDS);
                        RegisterService.getInstance().unregister(t);
                    }
                }

            } catch (InterruptedException e) {
                RegisterService.getInstance().shutdownDataMonitor();

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
