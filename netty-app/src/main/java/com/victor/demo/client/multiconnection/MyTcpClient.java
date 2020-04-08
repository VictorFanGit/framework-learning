package com.victor.demo.client.multiconnection;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;


public class MyTcpClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyTcpClient.class);

    private static ChannelContainer channelContainer = new ChannelContainer();
    private static Bootstrap bootstrap = new Bootstrap();
    private EventLoopGroup group = new NioEventLoopGroup();

    public void start() {
        bootstrap.group(group) // 绑定线程池
                .channel(NioSocketChannel.class) // 指定使用的channel
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() { // 绑定客户端连接时候触发操作

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
//                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        ch.pipeline().addLast("handler1", new MyDataHandler()); // 客户端触发操作
                    }
                });
    }

    public static void createNewConnection(String host, int port, String id) {
        LOGGER.info("Try to connect to tcp server: {} port:{}", host, port);
        final Channel channel = channelContainer.getChannel(id);
        //if already connected, return the existed
        if (channel != null && channel.isActive()) {
            LOGGER.info("The device is already connected.");
            LOGGER.info("Current count of active connections is:{} ", channelContainer.getCurrentSize());
            return;
        }
        ChannelFuture future = bootstrap.connect(host, port);
        future.addListener((ChannelFutureListener) future1 -> {
            if (future1.isSuccess()) {
                LOGGER.info("Connect to light controller successfully.");
                channelContainer.addChannel(id, future.channel());
                LOGGER.info("Current count of active connections is:{} ", channelContainer.getCurrentSize());
            } else {
                LOGGER.warn("Failed to connect light controller, retry it later.");
                LOGGER.info("Current count of active connections is:{} ", channelContainer.getCurrentSize());
                MyTcpClient.retryToConnect(future1.channel(), host, port, id);
            }
        });
    }

    public static void retryToConnect(Channel channel, String host, int port, String id) {
        if(channel != null) {
            channel.eventLoop().schedule(()-> MyTcpClient.createNewConnection(host, port, id), 10, TimeUnit.SECONDS);
        } else {
            LOGGER.warn("Input channel is null.");
        }
    }

    @PreDestroy
    public void stop() {
        group.shutdownGracefully();
    }

    static void removeChannel(Channel channel) {
        channelContainer.removeChannel(channel);
    }

    static String getIdByChannel(Channel channel) {
        if(channel != null) {
            return channelContainer.getIdByChannel(channel);
        }
        return "";
    }
}
