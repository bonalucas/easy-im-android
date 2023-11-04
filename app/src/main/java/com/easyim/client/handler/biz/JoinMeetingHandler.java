package com.easyim.client.handler.biz;

import com.easyim.client.NettyClient;
import com.easyim.client.common.Constants;
import com.easyim.comm.message.meeting.JoinMeetingResponseMessage;
import com.easyim.service.common.MessageProcessor;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;

/**
 * 加入会议消息处理器
 *
 * @author 单程车票
 */
@ChannelHandler.Sharable
public class JoinMeetingHandler extends SimpleChannelInboundHandler<JoinMeetingResponseMessage> {

    private static class JoinMeetingHandlerInstance {
        private static final JoinMeetingHandler INSTANCE = new JoinMeetingHandler();
    }

    public static JoinMeetingHandler getInstance() {
        return JoinMeetingHandlerInstance.INSTANCE;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JoinMeetingResponseMessage msg) throws Exception {
        // 关闭消息重发
        NettyClient.getInstance().getMessageRetransmissionManager().remove(msg.getMessageId());
        // 记录通道属性
        ctx.channel().attr(AttributeKey.valueOf(Constants.AttributeKeyName.USER_NAME)).set(msg.getNickname());
        ctx.channel().attr(AttributeKey.valueOf(Constants.AttributeKeyName.MEETING_ID)).set(msg.getMeetingId());
        // 回调消息到应用层
        MessageProcessor.getInstance().receiveMessage(msg);
    }

}
