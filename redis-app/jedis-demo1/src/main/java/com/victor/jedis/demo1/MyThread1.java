package com.victor.jedis.demo1;

import redis.clients.jedis.Jedis;

public class MyThread1 implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setString(i);
        }
    }

    private void setString(int i){
        Jedis jedis = null;
        try {
            jedis = JedisConfig.getPool().getResource();
            //选择数据库，默认为0，每次拿到连接都要选择
            if(i > 5) {
                jedis.select(1);
                jedis.set("book", "python" + i);
            } else {
                jedis.set("book", "java" + i);
            }
//            String book = jedis.get("book1");
//            System.out.println(book);
//            jedis.del("book1");
            //每次获取到的连接可能不一样
//            System.out.println(jedis);
        } finally {
            if(jedis != null) {
                //return the instance to pool
                jedis.close();
            }
        }
    }
}
