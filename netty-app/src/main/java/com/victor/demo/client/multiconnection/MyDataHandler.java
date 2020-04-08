package com.victor.demo.client.multiconnection;


import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public class MyDataHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyDataHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("New channel active, channel id:{}, ip:{}", ctx.channel().id().asLongText(), ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("Channel is inactive, channel id:{}, ip:{}", ctx.channel().id().asLongText(), ctx.channel().remoteAddress().toString());

        String addr = ctx.channel().remoteAddress().toString();
        String host = addr.substring(addr.indexOf('/') + 1, addr.indexOf(':'));
        String port = addr.substring(addr.indexOf(':') + 1);
        String id = MyTcpClient.getIdByChannel(ctx.channel());
        MyTcpClient.retryToConnect(ctx.channel(), host, Integer.parseInt(port), id);
//        removeInfoFromContainer(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.warn("Channel got exception: {}", cause.getMessage());
        LOGGER.warn("Exception for channel id:{}, ip:{}", ctx.channel().id().asLongText(), ctx.channel().remoteAddress());
        LOGGER.warn(cause.getCause().getMessage());
//        removeInfoFromContainer(ctx.channel());
//        ctx.close();

        String addr = ctx.channel().remoteAddress().toString();
        String host = addr.substring(addr.indexOf('/') + 1, addr.indexOf(':'));
        String port = addr.substring(addr.indexOf(':') + 1);
        String id = MyTcpClient.getIdByChannel(ctx.channel());
        MyTcpClient.retryToConnect(ctx.channel(), host, Integer.parseInt(port), id);
    }

    private void removeInfoFromContainer(Channel channel) {
        MyTcpClient.removeChannel(channel);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof ByteBuf)) {
            LOGGER.warn("Received error message!");
            return;
        }
        if (((ByteBuf) msg).readableBytes() < 1) {
            return;
        }

        ByteBuf messageBuf = (ByteBuf) msg;
        printData(messageBuf);
        messageBuf.release();
    }

    private void printData(ByteBuf data) {
        byte b;
        System.out.println("=========msg============");
        for (int i = 0; i < data.readableBytes(); i++) {
            b = data.getByte(i);
            System.out.printf("%02X ", b);
        }
        System.out.println("\n=========msg==========");
    }

}
