package com.victor.rabbitmq.demo1;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Sender {

    private static final String QUEUE_NAME = "test1";

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
    }

    private void sendData() {
        try {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        String message = "Hello World";
        System.out.println("Start to send:" + System.currentTimeMillis());
        String message = "1234567890";
        try {
            while (true) {
//                String message = System.nanoTime() + "";
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
//                Thread.sleep(1000);
            }
        } catch (IOException e) {
            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
        }
        System.out.println("End of sending:" + System.currentTimeMillis());
//        System.out.println("Send message:" + message);

    }

}
