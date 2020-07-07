package com.victor.mqtt.consumer;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyMqttClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyMqttClient.class);

    private static MqttClient mqttClient = null;
    private static MemoryPersistence memoryPersistence = null;
    private static MqttConnectOptions connOpts = null;

    private final String server;
    private final String clientId;
    private final String username;
    private final String pwd;

    private MqttCallback reciever = new MqttCallback() {
        @Override
        public void connectionLost(Throwable cause) {
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
//			LOGGER.info("ignore message.");
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
        }
    };

    public MyMqttClient(String server, String clientId, String username, String pwd) {
        super();
        this.server = server;
        this.clientId = clientId;
        this.username = username;
        this.pwd = pwd;
    }

    public void setReciever(MqttCallback reciever) {
        this.reciever = reciever;
    }

    public void runMqtt() {
        try {
            connect();
        } catch (MqttException e) {
            LOGGER.warn("Failed to run mqtt server.", e);
        }
    }

    private void reconnect() throws MqttException {
        if (null != mqttClient) {
            mqttClient.connect();
        }
    }

    private void connect() throws MqttException {
        LOGGER.info("Start to connect MqttClient");
        if (null == connOpts) {
            connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setAutomaticReconnect(true);
            connOpts.setUserName(username);
            connOpts.setPassword(pwd.toCharArray());
        }
        if (null == memoryPersistence) {
            memoryPersistence = new MemoryPersistence();
        }
        try {
            if (null == mqttClient)
                mqttClient = new MqttClient(server, clientId, memoryPersistence);
        } catch (MqttException e) {
            LOGGER.error("MqttClient init failed!", e);
            throw e;
        }

        if (!mqttClient.isConnected()) {
            mqttClient.setCallback(reciever);
            try {
                mqttClient.connect(connOpts);
                LOGGER.info("mqtt server connected!");
            } catch (MqttException e) {
                LOGGER.error("connect mqtt server failed!", e);
                throw e;
            }
        } else {
            LOGGER.info("MqttClient has connected");
        }
    }

    private void disconnect() {
        if (null != memoryPersistence) {
            try {
                memoryPersistence.close();
            } catch (MqttPersistenceException e) {
                LOGGER.error("close memoryPersistence failed!", e);
            }
        }

        if (null != mqttClient) {
            if (mqttClient.isConnected()) {
                try {
                    mqttClient.disconnect();
                    mqttClient.close();
                } catch (MqttException e) {
                    LOGGER.error("close mqttClient failed!", e);
                }
            } else {
                LOGGER.info("mqttClient is not connect");
            }
        } else {
            LOGGER.info("mqttClient is null");
        }
    }

    public void subTopic(int[] qos, String[] topics) throws MqttException {
        if (null != mqttClient && mqttClient.isConnected()) {
            sub(qos, topics);
        } else {
            LOGGER.warn("subscribe topic failed. mqttClient is not connect");
            reconnect();
            sub(qos, topics);
        }
    }


    private void sub(int[] qos, String[] topics) throws MqttException {
        try {
            mqttClient.subscribe(topics, qos);
        } catch (MqttException e) {
            LOGGER.error("subscribe topic failed!", e);
            throw e;
        }
    }

    public void unsubTopic(String[] topics) throws MqttException {
        if (null != mqttClient && mqttClient.isConnected()) {
            unsub(topics);
        } else {
            LOGGER.warn("unsubscribe topic failed. mqttClient is not connect");
            reconnect();
            unsub(topics);
        }
    }

    private void unsub(String[] topics) throws MqttException {
        try {
            mqttClient.unsubscribe(topics);
        } catch (MqttException e) {
            LOGGER.error("unsubscribe topic failed!", e);
            throw e;
        }
    }

    public void publishMessage(String topic, int qos, byte[] message) throws MqttException {
        if (mqttClient.isConnected()) {
            publish(topic, qos, message);
        } else {
            LOGGER.warn("publish message failed. reconnect mqttClient");
            reconnect();
            publish(topic, qos, message);
        }
    }

    private void publish(String topic, int qos, byte[] message) throws MqttException {
        try {
            MqttMessage msg = new MqttMessage(message);
            msg.setQos(qos);
            mqttClient.publish(topic, msg);
        } catch (MqttException e) {
            LOGGER.error("message publish failed!", e);
            throw e;
        }
    }
}
