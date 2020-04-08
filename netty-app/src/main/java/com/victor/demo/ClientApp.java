package com.victor.demo;

import com.victor.demo.client.multiconnection.MyTcpClient;

public class ClientApp {
    public static void main(String[] args) {
        MyTcpClient client = new MyTcpClient();
        client.start();
        client.createNewConnection("10.109.18.174", 7000, "12345");
    }
}
