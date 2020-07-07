package com.victor.mqtt.consumer;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyMsgHandler implements MqttCallback {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyMsgHandler.class);
    private volatile int msgCount;

    public int getMsgCount() {
        return msgCount;
    }

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
//        LOGGER.debug("received mqtt msg");
        System.out.println("received mqtt msg");
        byte[] payload = message.getPayload();

        //时延测试
//        long end = System.nanoTime();
//        String s = new String(message.getPayload());
//        long start = Long.parseLong(s);
//        System.out.println(end - start);
        msgCount++;
        System.out.println(new String(payload));
    }
}
