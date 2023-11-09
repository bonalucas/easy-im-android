package com.easyim.client.handler.biz;

import com.easyim.client.NettyClient;
import com.easyim.comm.message.screen.ShareScreenResponseMessage;
import com.easyim.service.MessageProcessor;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 屏幕共享消息处理器
 *
 * @author 单程车票
 */
@ChannelHandler.Sharable
public class ShareScreenHandler extends SimpleChannelInboundHandler<ShareScreenResponseMessage> {

    private static class ShareScreenHandlerInstance {
        private static final ShareScreenHandler INSTANCE = new ShareScreenHandler();
    }

    public static ShareScreenHandler getInstance() {
        return ShareScreenHandlerInstance.INSTANCE;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ShareScreenResponseMessage msg) throws Exception {
        // 关闭消息重发
        NettyClient.getInstance().getMessageRetransmissionManager().remove(msg.getMessageId());
        // 回调消息到应用层
        MessageProcessor.getInstance().receiveMessage(msg);
    }

}
