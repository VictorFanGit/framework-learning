package com.victor.mqtt.consumer;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.UUID;

public class DemoApp {

    public static void main(String[] args) {
        String clientId = UUID.randomUUID().toString();
        String server = "tcp://120.78.182.183:9051";
//        String server = "tcp://10.109.30.15:5919";
//        String server = "tcp://localhost:9051";
//        String server = "tcp://localhost:5919";
        MyMqttClient client = new MyMqttClient(server, clientId, "mec", "mec");
        MyMsgHandler handler = new MyMsgHandler();
        client.setReciever(handler);
        client.runMqtt();
        try {
            client.subTopic(new int[]{1}, new String[]{"demo1"});
        } catch (MqttException e) {
            e.printStackTrace();
        }
        while (true){
            try {
                Thread.sleep(1000);
                System.out.println("Received msg count:");
                System.out.println(handler.getMsgCount());
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
    }



}
