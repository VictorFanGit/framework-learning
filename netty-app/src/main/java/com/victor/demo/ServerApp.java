package com.victor.demo;

import com.victor.demo.server.NettyServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerApp {
    private static final Logger logger = LoggerFactory.getLogger(ServerApp.class);

    public static void main(String[] args) {
        logger.info("Start...");
        NettyServer server = new NettyServer(7100);
//        SendTaskManager sendTaskManager = new SendTaskManager();
//        sendTaskManager.start();
        System.out.println("Start TCP server...");
        try {
            server.initTcpServer();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Terminate TCP server.");
    }

}
