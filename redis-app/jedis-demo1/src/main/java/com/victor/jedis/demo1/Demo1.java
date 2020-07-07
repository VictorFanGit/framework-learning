package com.victor.jedis.demo1;

public class Demo1 {

    public static void main(String[] args) {
        JedisConfig.getPool().getResource().flushAll();

        Thread thread1 = new Thread(new MyThread1());
        Thread thread2 = new Thread(new MyThread2());
        thread1.start();
        thread2.start();
        while (thread1.isAlive() || thread2.isAlive()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //释放连接池
        if (JedisConfig.getPool() != null) {
            JedisConfig.getPool().destroy();
        }

    }


}
