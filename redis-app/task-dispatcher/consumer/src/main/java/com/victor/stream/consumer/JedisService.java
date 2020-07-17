package com.victor.stream.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisService {
    private static final Logger LOGGER = LoggerFactory.getLogger(JedisService.class);

    private static volatile JedisPool pool;

    private static volatile RedisConfigInfo configInfo;

    public static RedisConfigInfo getConfigInfo() {
        return configInfo;
    }

    public static void setConfigInfo(RedisConfigInfo configInfo) {
        JedisService.configInfo = configInfo;
    }

    private JedisService() {
    }

    public static JedisPool getPool() {
        if (pool == null) {
            synchronized (JedisService.class) {
                if (pool == null) {
                    JedisPoolConfig poolConfig = new JedisPoolConfig();
                    poolConfig.setMaxTotal(10);
                    if(configInfo == null) {
                        LOGGER.warn("Redis config info is not set, use default.");
                        pool = new JedisPool(poolConfig, "localhost", 6379);
                    } else {
                        pool = new JedisPool(poolConfig, configInfo.getHost(), configInfo.getPort());
                    }
                }
            }
        }
        return pool;
    }

}
