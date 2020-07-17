package com.victor.stream.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DispatcherService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DispatcherService.class);

    private ScheduledExecutorService executor;
    private int heartbeatInterval = 2;
    private int expireTime = 3;

    private TaskProcessor taskProcessor;

    //能处理多少个instance的数据
    private int maxProcessingTaskNum = 5;

    private ConcurrentHashMap<String, String> currentProcessingTasks = new ConcurrentHashMap<>();

    private boolean enableDataMonitor;

    //redis宕机标识
    private boolean redisIsCrash;

    //用于区分不同服务的注册信息，使用时需要首先设置该属性
    private String dispatchedTopic;

    //当前服务的名称，使用时需要重新设置该属性
    private String currentServiceName;

    private String cachedMd5 = "";

    public DispatcherService(int maxProcessingTaskNum, String dispatchedTopic, String currentServiceName) {
        this.maxProcessingTaskNum = maxProcessingTaskNum;
        this.dispatchedTopic = dispatchedTopic;
        this.currentServiceName = currentServiceName;
    }

    public TaskProcessor getTaskProcessor() {
        return taskProcessor;
    }

    public DispatcherService setTaskProcessor(TaskProcessor taskProcessor) {
        this.taskProcessor = taskProcessor;
        return this;
    }

    public int getHeartbeatInterval() {
        return heartbeatInterval;
    }

    public DispatcherService setHeartbeatInterval(int heartbeatInterval) {
        this.heartbeatInterval = heartbeatInterval;
        return this;
    }

    public int getExpireTime() {
        return expireTime;
    }

    public DispatcherService setExpireTime(int expireTime) {
        this.expireTime = expireTime;
        return this;
    }

    public void shutdownDataMonitor() {
        if (enableDataMonitor) {
            synchronized (DispatcherService.class) {
                if (enableDataMonitor) {
                    executor.shutdownNow();
                    enableDataMonitor = false;
                }
            }
        }
    }

    //注册服务信息之后，才开启数据监控功能
    public void startDataMonitor() {
        if (!enableDataMonitor) {
            synchronized (DispatcherService.class) {
                if (!enableDataMonitor) {
                    initMonitor();
                    enableDataMonitor = true;
                }
            }
        }
    }

    private boolean checkMd5IsChanged() throws RedisOperationException {
        Jedis jedis = null;
        String md5;
        try {
            jedis = JedisService.getPool().getResource();
            md5 = jedis.get(dispatchedTopic + "-md5");
        } catch (Exception e) {
            throw new RedisOperationException(e);
        } finally {
            if (jedis != null) {
                //return the instance to pool
                jedis.close();
            }
        }
        if (md5 == null) {
            LOGGER.warn("Can not get data from Redis.");
            cancelAllProcessingTasks();
            return false;
        } else if (cachedMd5.equals(md5)) {
            return false;
        }
        LOGGER.info("MD5 is changed.");
        return true;
    }

    private void cancelAllProcessingTasks() {
        if (taskProcessor != null) {
            for (String task : currentProcessingTasks.keySet()) {
                taskProcessor.terminateProcessing(task);
            }
        }
        currentProcessingTasks.clear();
    }

    //发送心跳（刷新过期时间）,监测配置变化
    private void initMonitor() {
        executor = Executors.newScheduledThreadPool(3);
        executor.scheduleAtFixedRate(() -> {
            try {
                refreshRunningExpireTime();
                checkIfTaskInfoChanged();
                if (redisIsCrash) {
                    cancelAllProcessingTasks();
                    redisIsCrash = false;
                }
            } catch (RedisOperationException e) {
                redisIsCrash = true;
                LOGGER.error("Exception occurred when access redis:" + e.getMessage());
            }
        }, 1, heartbeatInterval, TimeUnit.SECONDS);
    }

    private void refreshRunningExpireTime() throws RedisOperationException {
        if (currentProcessingTasks != null) {
            Jedis jedis = null;
            try {
                jedis = JedisService.getPool().getResource();
                for (String t : currentProcessingTasks.keySet()) {
                    jedis.expire(t, expireTime);
                }
            } catch (Exception e) {
                throw new RedisOperationException(e);
            } finally {
                if (jedis != null) {
                    //return the instance to pool
                    jedis.close();
                }
            }
        }
    }

    private void checkIfTaskInfoChanged() throws RedisOperationException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Current processing task info:");
            for (String t : currentProcessingTasks.keySet()) {
                LOGGER.debug(t);
            }
        }
        if (checkMd5IsChanged() || checkForProcessMoreTask()) {
            requestForDispatchingTask();
        }
    }

    private boolean checkForProcessMoreTask() throws RedisOperationException {
        if (currentProcessingTasks.size() >= maxProcessingTaskNum) {
            return false;
        }
        Jedis jedis = null;
        Map<String, String> allTasks;
        try {
            jedis = JedisService.getPool().getResource();
            allTasks = jedis.hgetAll(dispatchedTopic);
            for (String instanceId : allTasks.keySet()) {
                String dispatchKey = dispatchedTopic + "-dispatch-" + instanceId;
                String val = jedis.get(dispatchKey);
                if (val == null) {
                    LOGGER.debug("Try to process more task.");
                    return true;
                }
            }
        } catch (Exception e) {
            throw new RedisOperationException(e);
        } finally {
            if (jedis != null) {
                //return the instance to pool
                jedis.close();
            }
        }

        return false;
    }

    public void requestForDispatchingTask() throws RedisOperationException {
        Jedis jedis = null;
        String uuid = UUID.randomUUID().toString();
        try {
            jedis = JedisService.getPool().getResource();
            //获取全局锁（分布式锁）
            while (!tryToGetGlobalLock(jedis, uuid)) {
                LOGGER.info("Try to get global lock again.");
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    LOGGER.warn("Get InterruptedException when sleep.");
                }
            }
            dispatchTask();
            if (!releaseGlobalLock(jedis, uuid)) {
                LOGGER.warn("Failed to release global lock.");
            }
        } catch (Exception e) {
            throw new RedisOperationException(e);
        } finally {
            if (jedis != null) {
                //return the instance to pool
                jedis.close();
            }
        }
    }

    private boolean tryToGetGlobalLock(Jedis jedis, String uuid) {
        String lockKey = dispatchedTopic + "-lock";
        SetParams setParams = SetParams.setParams().nx().px(5000);
        String ret = jedis.set(lockKey, uuid, setParams);
        return "OK".equals(ret);
    }

    private boolean refreshLockExpireTime(Jedis jedis) {
        String lockKey = dispatchedTopic + "-lock";
        Long ret = jedis.expire(lockKey, 20);
        return ret == 1;
    }

    private boolean releaseGlobalLock(Jedis jedis, String uuid) {
        String lockKey = dispatchedTopic + "-lock";
        String script = "if redis.call('get',KEYS[1]) == ARGV[1] then" +
                "   return redis.call('del',KEYS[1]) " +
                "else" + "   return 0 " + "end";
        Object ret = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(uuid));
        return "1".equals(ret.toString());
    }

    private void dispatchTask() throws RedisOperationException {
        Jedis jedis = null;
        try {
            jedis = JedisService.getPool().getResource();
            String md5 = jedis.get(dispatchedTopic + "-md5");
            if (md5 == null) {
                LOGGER.info("MD5 info is null.");
                return;
            }
            Map<String, String> allTasks = jedis.hgetAll(dispatchedTopic);
            HashMap<String, String> unprocessedTasks = new HashMap<>();
            HashMap<String, String> inProcessingTasks = new HashMap<>();
            HashMap<String, String> removeTasks = new HashMap<>();

            for (String instanceId : allTasks.keySet()) {
                String dispatchKey = dispatchedTopic + "-dispatch-" + instanceId;
                String val = jedis.get(dispatchKey);
                if (val == null) {
                    unprocessedTasks.put(dispatchKey, dispatchKey);
                } else {
                    inProcessingTasks.put(dispatchKey, dispatchKey);
                }
            }

            //检查当前处理的任务是否有被取消
            for (Map.Entry<String, String> entry : currentProcessingTasks.entrySet()) {
                if (!inProcessingTasks.containsKey(entry.getKey())) {
                    removeTasks.put(entry.getKey(), entry.getValue());
                }
            }
            //当前处理任务数达到最大值，且没有任务被取消，则不再领取任务
            if(currentProcessingTasks.size() == maxProcessingTaskNum && removeTasks.isEmpty()) {
                cachedMd5 = md5;
                return;
            }
            //结束过期任务
            if (removeTasks.size() > 0 && taskProcessor != null) {
                for (String task : removeTasks.keySet()) {
                    taskProcessor.terminateProcessing(task);
                    currentProcessingTasks.remove(task);
                }
            }
            //添加新任务
            for (String task : unprocessedTasks.keySet()) {
                if (currentProcessingTasks.size() < maxProcessingTaskNum) {
                    jedis.set(task, currentServiceName);
                    jedis.expire(task, expireTime);
                    currentProcessingTasks.put(task, unprocessedTasks.get(task));
                    if (taskProcessor != null) {
                        taskProcessor.process(task);
                    } else {
                        LOGGER.warn("Processor is null.");
                    }
                }
            }
            cachedMd5 = md5;
        } catch (Exception e) {
            throw new RedisOperationException(e);
        } finally {
            if (jedis != null) {
                //return the instance to pool
                jedis.close();
            }
        }

    }
}
