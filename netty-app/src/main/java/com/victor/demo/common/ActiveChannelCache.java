package com.victor.demo.common;

import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;

public class ActiveChannelCache {
    private static ConcurrentHashMap<String, Channel> CHANNEL_MAP = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, Channel> getContextMap() {
        return CHANNEL_MAP;
    }

    public static void put(String key, Channel val) {
        CHANNEL_MAP.put(key, val);
    }

    public static void remove(String key) {
        CHANNEL_MAP.remove(key);
    }
}
