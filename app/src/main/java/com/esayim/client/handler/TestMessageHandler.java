package com.esayim.client.handler;

import com.esayim.client.NettyClient;
import com.esayim.comm.message.test.TestResponseMessage;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class TestMessageHandler extends SimpleChannelInboundHandler<TestResponseMessage> {

    private static final TestMessageHandler instance = new TestMessageHandler();

    public static TestMessageHandler getInstance() {
        return instance;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TestResponseMessage msg) throws Exception {
        NettyClient.getInstance().getMessageDispatcher().receiveMessage(msg);
    }

}
