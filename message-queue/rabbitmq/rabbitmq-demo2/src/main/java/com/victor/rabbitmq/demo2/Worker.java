package com.victor.rabbitmq.demo2;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class Worker {

    private static final String QUEUE_NAME = "hello";
    private ConnectionFactory factory = new ConnectionFactory();
    private Connection connection;
    private Channel channel;

    private Random random = new Random();

    private static AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) {
        System.out.println("===receiver===");
        Worker worker = new Worker();
        worker.connectToServer();
        worker.receiveMessage();
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
//        factory.setHost("localhost");
        factory.setHost("120.78.182.183");
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
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            //一次分发一个消息给消费者，等待处理完了再分发
            channel.basicQos(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Waiting for messages...");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            counter.incrementAndGet();
            System.out.println("Received message: " + message);
            int c = random.nextInt(1000);
            System.out.println("sleep:" + c);
            try {
                Thread.sleep(c);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };
        //可设置autoAck为false,手动发送ack，这样在消费者挂掉时，消息仍然在服务端，会被重新分发，保证每条消息都被处理
        //参考官网：https://www.rabbitmq.com/tutorials/tutorial-two-java.html
        try {
            channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
