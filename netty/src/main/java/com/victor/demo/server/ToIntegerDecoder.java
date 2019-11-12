package com.victor.demo.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

public class ToIntegerDecoder extends ByteToMessageDecoder {
    private static final int MAX_FRAME_SIZE = 10;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int len = in.readableBytes();

        if (len > MAX_FRAME_SIZE) {
            in.skipBytes(len);
            throw new TooLongFrameException("Frame is too big.");
        } else if (len >= 4) {
            out.add(in.readInt());
        }
    }
}
