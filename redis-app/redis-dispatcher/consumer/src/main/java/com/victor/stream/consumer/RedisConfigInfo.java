package com.victor.stream.consumer;

public class RedisConfigInfo {
    private String host;
    private int port;

    public String getHost() {
        return host;
    }

    public RedisConfigInfo setHost(String host) {
        this.host = host;
        return this;
    }

    public int getPort() {
        return port;
    }

    public RedisConfigInfo setPort(int port) {
        this.port = port;
        return this;
    }
}
