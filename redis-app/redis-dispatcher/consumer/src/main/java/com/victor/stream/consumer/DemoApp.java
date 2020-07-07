package com.victor.stream.consumer;

public class DemoApp {

    public static void main(String[] args) {
        RedisConfigInfo redisConfigInfo = new RedisConfigInfo().setHost("120.78.182.183").setPort(9079);
        JedisService.setConfigInfo(redisConfigInfo);

        DispatcherService.getInstance().setTaskProcessor(new TaskProcessorImpl())
                .setDispatchedTopic("video-stream").setCurrentServiceName("data-processor");
        try {
            DispatcherService.getInstance().requestForDispatchingTask();
        } catch (RedisOperationException e) {
            e.printStackTrace();
        }
        DispatcherService.getInstance().startDataMonitor();
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }

        DispatcherService.getInstance().shutdownDataMonitor();

        //释放连接池
        if (JedisService.getPool() != null) {
            JedisService.getPool().destroy();
        }

    }


}
