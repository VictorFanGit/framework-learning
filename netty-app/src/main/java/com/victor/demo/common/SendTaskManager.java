package com.victor.demo.common;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.util.CharsetUtil;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SendTaskManager {
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);

    public void start() {
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                ConcurrentHashMap<String, Channel> channels = ActiveChannelCache.getContextMap();
                for (Channel channel : channels.values()) {
                    if (channel.isActive()) {
                        //send data solution1
                        /*int t = (int) (System.currentTimeMillis() / 1000L + 2208988800L);
                        String hexString = Integer.toHexString(t);
                        ByteBuf buf = Unpooled.copiedBuffer(hexString.getBytes());
                        channel.writeAndFlush(buf);*/


                        //send data solution2
                        /*int t = (int) (System.currentTimeMillis() / 1000L + 2208988800L);
                        ByteBuf buf = Unpooled.copiedBuffer(Integer.toHexString(t), CharsetUtil.UTF_8);
                        System.out.println("bytebuf capacity:" + buf.capacity());
                        channel.writeAndFlush(buf.duplicate());*/

                        //send data solution3
                        ByteBuf buffer = channel.alloc().buffer(40000);
//                        ByteBuf buffer = channel.alloc().directBuffer(40000);
//                        ByteBuf buffer = Unpooled.directBuffer(40000);
                        int t2 = (int) (System.currentTimeMillis() / 1000L + 2208988800L);
                        for (int i = 0; i < 10000; i++) {
                            buffer.writeInt(t2);
                        }
//                        System.out.println(ByteBufUtil.hexDump(buffer));
                        channel.writeAndFlush(buffer);

                    }
                }
            }
        }, 1000L, 1000L, TimeUnit.MILLISECONDS);
    }
}
