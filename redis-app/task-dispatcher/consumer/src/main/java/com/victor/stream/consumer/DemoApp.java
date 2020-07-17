package com.victor.stream.consumer;

public class DemoApp {

    public static void main(String[] args) {
        RedisConfigInfo redisConfigInfo = new RedisConfigInfo().setHost("120.78.182.183").setPort(9079);
        JedisService.setConfigInfo(redisConfigInfo);

        DispatcherService dispatcherService = new DispatcherService(5, "video-stream", "data-processor");
        dispatcherService.setTaskProcessor(new TaskProcessorImpl());
        try {
            dispatcherService.requestForDispatchingTask();
        } catch (RedisOperationException e) {
            e.printStackTrace();
        }
        dispatcherService.startDataMonitor();
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }

        dispatcherService.shutdownDataMonitor();

        //释放连接池
        if (JedisService.getPool() != null) {
            JedisService.getPool().destroy();
        }

    }


}
