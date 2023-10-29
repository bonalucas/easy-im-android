package com.esayim.client.handler;

import com.esayim.client.NettyClient;
import com.esayim.comm.message.error.ErrorResponseMessage;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class ServiceErrorHandler extends SimpleChannelInboundHandler<ErrorResponseMessage> {

    private static final ServiceErrorHandler instance = new ServiceErrorHandler();

    public static ServiceErrorHandler getInstance() {
        return instance;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ErrorResponseMessage msg) throws Exception {
        // 关闭消息重发
        NettyClient.getInstance().getMessageRetransmissionManager().remove(msg.getMessageId());
        // 回调消息到应用层
        NettyClient.getInstance().getMessageDispatcher().receiveMessage(msg);
    }

}
