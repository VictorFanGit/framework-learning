package com.victor.demo.client.multiconnection;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
public class LightDataHandler extends ChannelInboundHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LightDataHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("New channel active, channel id:{}, ip:{}", ctx.channel().id().asLongText(), ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("Channel is inactive, channel id:{}, ip:{}", ctx.channel().id().asLongText(), ctx.channel().remoteAddress());
        removeInfoFromContainer(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.warn("Channel got exception: {}", cause.getMessage());
        LOGGER.warn("Exception for channel id:{}, ip:{}", ctx.channel().id().asLongText(), ctx.channel().remoteAddress());
        LOGGER.warn(cause.getCause().getMessage()); removeInfoFromContainer(ctx);
    }

    private void removeInfoFromContainer(ChannelHandlerContext ctx) {
        //TODO: delete the item from cache
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        if (!(msg instanceof ByteBuf)) {
            LOGGER.warn("Received error message!");
            return;
        }
        if(((ByteBuf) msg).readableBytes() < 1) {
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
