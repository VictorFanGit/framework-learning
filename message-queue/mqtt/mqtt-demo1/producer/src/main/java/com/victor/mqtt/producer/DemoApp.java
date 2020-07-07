package com.victor.mqtt.producer;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.UUID;

/**
 * 可在测试类DemoAppTest 进行相关操作
 */
public class DemoApp {

    private static volatile int msgCount = 0;

    /**
     * 最大发送量测试
     */
    /*public static void main(String[] args) {
        String clientId = UUID.randomUUID().toString();
//        String server = "tcp://120.78.182.183:9051";
//        String server = "tcp://10.109.30.15:5919";
        String server = "tcp://localhost:9051";
//        String server = "tcp://localhost:5919";
        MyMqttClient client = new MyMqttClient(server, clientId, "mec", "mec");
        client.setReciever(new MyMsgHandler());
        client.runMqtt();

        new Thread(()->{
            while (true) {
                try {
                    Thread.sleep(1000);
                    System.out.println("send msg:" + msgCount);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        while (true) {
            msgCount++;
            String msg = "message" + msgCount;
            try {
                client.publishMessage("demo1", 1, msg.getBytes());
//                System.out.println(msg);
//                Thread.sleep(1000);
            } catch (MqttException e) {
                e.printStackTrace();
                break;
            }
        }
    }*/


    /**
     * 时延测试
     * @param args
     */
    public static void main(String[] args) {
        String clientId = UUID.randomUUID().toString();
//        String server = "tcp://120.78.182.183:9051";
        String server = "tcp://10.109.30.15:5919";
//        String server = "tcp://localhost:9051";
//        String server = "tcp://localhost:5919";
        MyMqttClient client = new MyMqttClient(server, clientId, "mec", "mec");
        client.setReciever(new MyMsgHandler());
        client.runMqtt();

/*        new Thread(()->{
            while (true) {
                try {
                    Thread.sleep(1000);
                    System.out.println("send msg:" + msgCount);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/

        while (true) {
            msgCount++;
//            String msg = "message" + msgCount;
            long time = System.nanoTime();
            try {
                String msg = Long.toString(time);
                client.publishMessage("demo1", 1, msg.getBytes());
                System.out.println(msg);
                Thread.sleep(1000);
            } catch (MqttException | InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
