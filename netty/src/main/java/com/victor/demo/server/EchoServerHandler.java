package com.victor.demo.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;

public class EchoServerHandler extends ChannelDuplexHandler {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ctx.fireChannelRead(msg);
//        ctx.write("OK");
        byte[] bytes = {'O', 'K'};
        ByteBuf buf = Unpooled.copiedBuffer(bytes);
        ctx.channel().writeAndFlush(buf).syncUninterruptibly();
    }


}
