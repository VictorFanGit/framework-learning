package com.victor.demo;

import com.victor.demo.server.NettyServer;

public class Main {
    public static void main(String[] args) {
        NettyServer server = new NettyServer();
        try {
            server.initTcpServer();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
