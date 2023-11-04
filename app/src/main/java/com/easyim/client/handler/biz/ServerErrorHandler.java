package com.easyim.client.handler.biz;

import com.easyim.client.NettyClient;
import com.easyim.comm.message.error.ErrorResponseMessage;
import com.easyim.service.common.MessageProcessor;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class ServerErrorHandler extends SimpleChannelInboundHandler<ErrorResponseMessage> {

    private static final ServerErrorHandler instance = new ServerErrorHandler();

    public static ServerErrorHandler getInstance() {
        return instance;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ErrorResponseMessage msg) throws Exception {
        // 关闭消息重发
        NettyClient.getInstance().getMessageRetransmissionManager().remove(msg.getMessageId());
        // 回调消息到应用层
        MessageProcessor.getInstance().receiveMessage(msg);
    }

}
