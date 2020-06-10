package com.victor.rabbitmq.demo1;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class Receiver {

    private static final String QUEUE_NAME = "test1";

    private ConnectionFactory factory = new ConnectionFactory();
    private Connection connection;
    private Channel channel;

    private static AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) {
        System.out.println("===receiver===");
        Receiver receiver = new Receiver();
        receiver.connectToServer();
        receiver.receiveMessage();
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("message count:" + counter);
        }
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

    private void receiveMessage() {

        try {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Waiting for messages...");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            //测试单个消息时延
//            long m = Long.parseLong(message);
//            long time = System.nanoTime() - m;
//            System.out.println("delay:" + time);
            counter.incrementAndGet();
//            System.out.println("Received message: " + message);
        };
        try {
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
