package com.victor.demo.client.multiconnection;

import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelContainer {
    //key:id value:channel
    private ConcurrentHashMap<String, Channel> map = new ConcurrentHashMap<>();

    public void addChannel(String key, Channel channel) {
        if (!StringUtils.isEmpty(key) && channel != null) {
            map.put(key, channel);
        }
    }

    public void removeChannel(Channel channel) {
        if (channel != null) {
            map.values().remove(channel);
        }
    }

    public void removeChannelByKey(String key) {
        if (!StringUtils.isEmpty(key)) {
            map.remove(key);
        }
    }

    public Channel getChannel(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        return map.get(key);
    }

    public String getIdByChannel(Channel channel) {
        for (Map.Entry<String, Channel> entry : map.entrySet()) {
            if(entry.getValue() == channel) {
                return entry.getKey();
            }
        }
        return "";
    }

    public int getCurrentSize() {
        return map.size();
    }

}
