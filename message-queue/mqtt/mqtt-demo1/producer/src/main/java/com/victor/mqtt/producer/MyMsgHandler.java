package com.victor.mqtt.producer;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class MyMsgHandler implements MqttCallback {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyMsgHandler.class);

    @Override
    public void connectionLost(Throwable cause) {
        // TODO Auto-generated method stub
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // TODO Auto-generated method stub
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        byte[] payload = message.getPayload();
        System.out.println(Arrays.toString(payload));
    }

}
