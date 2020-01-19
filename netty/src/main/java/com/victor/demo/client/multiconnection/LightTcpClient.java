package com.victor.demo.client.multiconnection;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.nio.ByteOrder;


public class LightTcpClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(LightTcpClient.class);

    private ChannelContainer channelContainer = new ChannelContainer();
    private Bootstrap bootstrap = new Bootstrap();
    private EventLoopGroup group = new NioEventLoopGroup();

    public void start() throws Exception {

        bootstrap.group(group) // 绑定线程池
                .channel(NioSocketChannel.class) // 指定使用的channel
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() { // 绑定客户端连接时候触发操作

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
//                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        ch.pipeline().addLast("decoder1", new LightDataDecoder(ByteOrder.BIG_ENDIAN, 10240,
                                3, 2, 0, 0, true));
                        ch.pipeline().addLast("lightDataHandler", new LightDataHandler()); // 客户端触发操作
                    }
                });
    }

    public void createNewConnection(String host, int port, String deviceEsn) throws InterruptedException {
        LOGGER.info("Try to connect to tcp server: {} port:{}", host, port);
        final Channel channel = channelContainer.getChannel(deviceEsn);
        //if already connected, return the existed
        if (channel != null) {
            LOGGER.info("The device is already connected.");
            LOGGER.info("Current count of active connections is:{} ", channelContainer.getCurrentSize());
            return;
        }
        ChannelFuture future = bootstrap.connect(host, port).sync();
        future.addListener((ChannelFutureListener) future1 -> {
            if (future1.isSuccess()) {
                LOGGER.info("Connect to light controller successfully.");
                channelContainer.addChannel(deviceEsn, future.channel());
                LOGGER.info("Current count of active connections is:{} ", channelContainer.getCurrentSize());
            } else {
                LOGGER.warn("Failed to connect light controller.");
//                future1.channel().eventLoop().schedule(() -> start(), 20, TimeUnit.SECONDS); //loop to retry
                LOGGER.info("Current count of active connections is:{} ", channelContainer.getCurrentSize());
            }
        });
    }

    @PreDestroy
    public void stop() {
        group.shutdownGracefully();
    }
}
