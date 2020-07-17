package com.victor.stream.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RegisterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterService.class);

    private ScheduledExecutorService executor;

    private boolean enableDataMonitor;
    private ConcurrentHashMap<String, String> registrationInfoCache = new ConcurrentHashMap<>();

    //用于区分不同任务的注册信息，使用时需要首先设置该属性
    private String topic;

    public RegisterService(String topic) {
        this.topic = topic;
    }

    //Map<String,String> key:taskId, value:detail info
    public void register(Map<String, String> taskInfoMap) throws RedisOperationException {
        Jedis jedis = null;
        try {
            jedis = JedisService.getPool().getResource();
            jedis.hset(topic, taskInfoMap);
            for (String k : taskInfoMap.keySet()) {
                LOGGER.info("New register task id: " + k);
                LOGGER.info("New register detailInfo: " + taskInfoMap.get(k));
            }
            updateMd5AndCache(jedis);
        } catch (Exception e) {
            throw new RedisOperationException(e);
        } finally {
            if (jedis != null) {
                //return the instance to pool
                jedis.close();
            }
        }
    }

    private void updateMd5AndCache(Jedis jedis) {
        Map<String, String> all = jedis.hgetAll(topic);
        //update MD5
        StringBuilder sb = new StringBuilder();
        for (String v : all.values()) {
            sb.append(v);
        }
        String md5String = MD5.getInstance().getMD5String(sb.toString());
        jedis.set(topic + "-md5", md5String);

        //update cache
        HashMap<String, String> map = new HashMap<>();
        for (String key : registrationInfoCache.keySet()) {
            String ret = all.remove(key);
            if (ret == null) {
                //失效数据
                map.put(key, registrationInfoCache.get(key));
            }
        }
        //从缓存删除失效数据
        for (String key : map.keySet()) {
            registrationInfoCache.remove(key);
        }
        //添加新数据
        registrationInfoCache.putAll(all);

    }

    public void register(String taskId, String detailInfo) throws RedisOperationException {
        Jedis jedis = null;
        try {
            jedis = JedisService.getPool().getResource();
            jedis.hset(topic, taskId, detailInfo);
            LOGGER.info("Add new register info: " + taskId + " | " + detailInfo);
            updateMd5AndCache(jedis);
        } catch (Exception e) {
            throw new RedisOperationException(e);
        } finally {
            if (jedis != null) {
                //return the instance to pool
                jedis.close();
            }
        }
    }

    public void unregister(String taskId) throws RedisOperationException {
        Jedis jedis = null;
        try {
            jedis = JedisService.getPool().getResource();
            jedis.hdel(topic, taskId);
            LOGGER.info("Unregister info: " + taskId);
            updateMd5AndCache(jedis);
        } catch (Exception e) {
            throw new RedisOperationException(e);
        } finally {
            if (jedis != null) {
                //return the instance to pool
                jedis.close();
            }
        }
    }

    public Map<String, String> getAllActiveTasks() throws RedisOperationException {
        Jedis jedis = null;
        Map<String, String> allTasks;
        try {
            jedis = JedisService.getPool().getResource();
            allTasks = jedis.hgetAll(topic);
        } catch (Exception e) {
            throw new RedisOperationException(e);
        } finally {
            if (jedis != null) {
                //return the instance to pool
                jedis.close();
            }
        }
        return allTasks;
    }

    public void shutdownDataMonitor() {
        if (enableDataMonitor) {
            synchronized (RegisterService.class) {
                if (enableDataMonitor) {
                    executor.shutdownNow();
                    enableDataMonitor = false;
                }
            }
        }
    }

    //注册服务启动之后，开启数据监控功能，该功能可防止Redis服务宕机后数据丢失
    public void startDataMonitor() {
        if (!enableDataMonitor) {
            synchronized (RegisterService.class) {
                if (!enableDataMonitor) {
                    initMonitor();
                    enableDataMonitor = true;
                }
            }
        }
    }

    private void initMonitor() {
        executor = Executors.newScheduledThreadPool(3);
        executor.scheduleWithFixedDelay(() -> {
            Map<String, String> activeTasks;
            try {
                activeTasks = getAllActiveTasks();
            } catch (RedisOperationException e) {
                LOGGER.error("Failed to get data from redis: " + e.getMessage());
                return;
            }
            //redis数据丢失，重建数据
            if (activeTasks.isEmpty() && registrationInfoCache.size() > 0) {
                LOGGER.info("Rebuild data in redis.");
                try {
                    register(registrationInfoCache);
                } catch (RedisOperationException e) {
                    LOGGER.error("Failed to rebuild data: " + e.getMessage());
                }
            }
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug("All active registration info:");
                for (Map.Entry<String, String> entry : activeTasks.entrySet()) {
                    LOGGER.debug(entry.getKey() + " : " + entry.getValue());
                }
            }
        }, 10, 5, TimeUnit.SECONDS);
    }
}
