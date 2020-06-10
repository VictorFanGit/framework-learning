package com.victor.rabbitmq.demo2;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Sender {

    private static final String QUEUE_NAME = "hello";

    private ConnectionFactory factory = new ConnectionFactory();
    private Connection connection;
    private Channel channel;

    public static void main(String[] args) {
        System.out.println("===sender===");
        Sender sender = new Sender();
        sender.connectToServer();
        sender.sendData();

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
            //durable 设置为true，消息持久化到磁盘
//            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendData() {
        System.out.println("Start to send:" + System.currentTimeMillis());
        try {
            int counter = 0;
            while (true) {
                String message = "" + counter;
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
                Thread.sleep(100);
                counter++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("End of sending:" + System.currentTimeMillis());
    }

}
