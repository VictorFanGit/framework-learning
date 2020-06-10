package com.victor.rabbitmq.demo3;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class Subscriber {


    private ConnectionFactory factory = new ConnectionFactory();
    private Connection connection;
    private Channel channel;

    private static AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) {
        System.out.println("===receiver===");
        Subscriber subscriber = new Subscriber();
        subscriber.connectToServer();
        subscriber.receiveMessage();
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
        String queueName = "";
        try {
            channel.exchangeDeclare("logs", "fanout");
            queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, "logs", "");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Waiting for messages...");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            counter.incrementAndGet();
//            System.out.println("Received message: " + message);
        };
        try {
            if (!queueName.equals("")) {
                channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
