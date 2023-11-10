package com.easyim.client.handler.biz;

import com.easyim.comm.message.meeting.LeaveMeetingResponseMessage;
import com.easyim.service.MessageProcessor;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 退出会议处理器
 *
 * @author 单程车票
 */
@ChannelHandler.Sharable
public class LeaveMeetingHandler extends SimpleChannelInboundHandler<LeaveMeetingResponseMessage> {

    private static class LeaveMeetingHandlerInstance {
        private static final LeaveMeetingHandler INSTANCE = new LeaveMeetingHandler();
    }

    public static LeaveMeetingHandler getInstance() {
        return LeaveMeetingHandlerInstance.INSTANCE;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LeaveMeetingResponseMessage msg) throws Exception {
        // 回调消息到应用层
        MessageProcessor.getInstance().receiveMessage(msg);
    }

}
