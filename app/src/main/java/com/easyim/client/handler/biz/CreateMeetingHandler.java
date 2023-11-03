package com.easyim.client.handler.biz;

import com.easyim.client.NettyClient;
import com.easyim.client.common.Constants;
import com.easyim.comm.message.meeting.MeetingCreateResponseMessage;
import com.easyim.service.MessageProcessor;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;

@ChannelHandler.Sharable
public class CreateMeetingHandler extends SimpleChannelInboundHandler<MeetingCreateResponseMessage> {

    private static class CreateMeetingHandlerInstance {
        private static final CreateMeetingHandler INSTANCE = new CreateMeetingHandler();
    }

    public static CreateMeetingHandler getInstance() {
        return CreateMeetingHandlerInstance.INSTANCE;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MeetingCreateResponseMessage msg) throws Exception {
        // 关闭消息重发
        NettyClient.getInstance().getMessageRetransmissionManager().remove(msg.getMessageId());
        // 记录通道属性
        ctx.channel().attr(AttributeKey.valueOf(Constants.AttributeKeyName.USER_NAME)).set(msg.getCreator());
        ctx.channel().attr(AttributeKey.valueOf(Constants.AttributeKeyName.MEETING_ID)).set(msg.getMeetingID());
        // 回调消息到应用层
        MessageProcessor.getInstance().receiveMessage(msg);
    }

}
