package com.victor.demo;

import com.victor.demo.common.SendTaskManager;
import com.victor.demo.server.NettyServer;

public class Main {
    public static void main(String[] args) {
        NettyServer server = new NettyServer(7000);
        SendTaskManager sendTaskManager = new SendTaskManager();
        sendTaskManager.start();
        System.out.println("Start TCP server...");
        try {
            server.initTcpServer();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Terminate TCP server.");
    }

}
