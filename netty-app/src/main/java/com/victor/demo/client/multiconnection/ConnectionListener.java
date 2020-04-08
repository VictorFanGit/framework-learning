package com.victor.demo.client.multiconnection;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

public class ConnectionListener implements ChannelFutureListener {
    private ConnectionInfo connectionInfo;
    private Bootstrap bootstrap;

    public ConnectionListener(ConnectionInfo connectionInfo, Bootstrap bootstrap) {
        this.connectionInfo = connectionInfo;
        this.bootstrap = bootstrap;
    }

    @Override
    public void operationComplete(ChannelFuture channelFuture) throws Exception {
        if(!channelFuture.isSuccess()) {

        } else {

        }
    }
}
