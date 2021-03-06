package com.victor.demo.server;

import com.victor.demo.common.ActiveChannelCache;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@ChannelHandler.Sharable
public class MyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ActiveChannelCache.put(ctx.channel().id().asLongText(), ctx.channel());
        System.out.println("ChannelHandlerContext:" + ctx.name());
        System.out.println("Channel id:" + ctx.channel().id().asLongText());
        System.out.println("Channel:" + ctx.channel().id().asLongText() + " is active");
        System.out.println(ctx.channel().eventLoop());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ActiveChannelCache.remove(ctx.channel().id().asLongText());
        System.out.println("Channel:" + ctx.channel().id().asLongText() + " is inactive");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //print the received data
/*        ByteBuf in = (ByteBuf)msg;
        try {
            while(in.isReadable()) {
                System.out.print((char) in.readByte());
//                System.out.flush();
            }
            System.out.println();
        } finally {
            ReferenceCountUtil.release(msg);
        }*/

        //respond the received data
//        ctx.writeAndFlush(msg);
        if (msg instanceof Integer) {
            System.out.println("received integer: " + msg);
        } else if (msg instanceof String) {
            System.out.println("received string: " + msg);
        }

        Integer data = 2;
        ctx.writeAndFlush(data);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        System.out.println("Exception occurred in handler.");
        ctx.close();
    }
}
