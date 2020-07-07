package com.victor.jedis.demo1;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisConfig {
    private static volatile JedisPool pool;

    private JedisConfig() {
    }

    public static JedisPool getPool() {
        if (pool == null) {
            synchronized (JedisConfig.class) {
                if (pool == null) {
                    JedisPoolConfig config = new JedisPoolConfig();
                    config.setMaxTotal(10);
                    pool = new JedisPool(config, "120.78.182.183", 9079);
                }
            }
        }
        return pool;
    }

}
