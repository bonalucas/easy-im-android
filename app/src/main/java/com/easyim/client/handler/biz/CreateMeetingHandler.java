package com.easyim.client.handler.biz;

import com.easyim.client.NettyClient;
import com.easyim.comm.message.meeting.CreateMeetingResponseMessage;
import com.easyim.service.common.MessageProcessor;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class CreateMeetingHandler extends SimpleChannelInboundHandler<CreateMeetingResponseMessage> {

    private static class CreateMeetingHandlerInstance {
        private static final CreateMeetingHandler INSTANCE = new CreateMeetingHandler();
    }

    public static CreateMeetingHandler getInstance() {
        return CreateMeetingHandlerInstance.INSTANCE;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateMeetingResponseMessage msg) throws Exception {
        // 关闭消息重发
        NettyClient.getInstance().getMessageRetransmissionManager().remove(msg.getMessageId());
        // 回调消息到应用层
        MessageProcessor.getInstance().receiveMessage(msg);
    }

}
