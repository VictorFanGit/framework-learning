package com.victor.rabbitmq.demo3;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Publisher {

    private ConnectionFactory factory = new ConnectionFactory();
    private Connection connection;
    private Channel channel;
    private volatile int counter = 0;

    public int getCounter() {
        return counter;
    }

    public static void main(String[] args) {
        System.out.println("===sender===");
        Publisher publisher = new Publisher();
        new Thread(() -> {
            while (true) {
                System.out.println(publisher.getCounter());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }).start();
        publisher.connectToServer();
        publisher.sendData();

    }

    private void connectToServer() {
        factory.setHost("localhost");
//        factory.setHost("120.78.182.183");
        factory.setPort(9012);
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
        try {
            //交换器数据默认不持久化
            channel.exchangeDeclare("logs", "fanout");
            //设置交换器持久化
//            channel.exchangeDeclare("logs", "fanout",true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendData() {
        System.out.println("Start to send:" + System.currentTimeMillis());
        try {
            while (true) {
                String message = "counter:" + counter;
                channel.basicPublish("logs", "", null, message.getBytes(StandardCharsets.UTF_8));
//                Thread.sleep(1000);
                counter++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("End of sending:" + System.currentTimeMillis());
    }

}
