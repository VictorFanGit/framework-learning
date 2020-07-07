package com.victor.jedis.demo1;

import redis.clients.jedis.Jedis;

public class MyThread2 implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getString(i);
        }
    }

    private void getString(int i){
        Jedis jedis = null;
        try {
            jedis = JedisConfig.getPool().getResource();
            if(i > 5) {
                jedis.select(1);
            }
            String book = jedis.get("book");
            System.out.println("thread2 get: " + book);
        } finally {
            if(jedis != null) {
                //return the instance to pool
                jedis.close();
            }
        }
    }
}
