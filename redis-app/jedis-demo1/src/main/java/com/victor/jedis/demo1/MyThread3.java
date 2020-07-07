package com.victor.jedis.demo1;

import redis.clients.jedis.Jedis;

public class MyThread3 implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            testList();
        }
    }

    private void testList(){
        Jedis jedis = null;
        try {
            jedis = JedisConfig.getPool().getResource();
            jedis.lpush("java framework","spring");
            jedis.lpush("java framework","struts");
            jedis.lpush("java framework","hibernate");
            System.out.println(jedis.lrange("java framework",0,-1));
            jedis.del("java framework");
            System.out.println(jedis);
        } finally {
            if(jedis != null) {
                //return the instance to pool
                jedis.close();
            }
        }
    }
}
