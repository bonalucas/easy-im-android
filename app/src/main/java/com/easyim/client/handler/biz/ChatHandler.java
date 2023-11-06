package com.easyim.client.handler.biz;

import com.easyim.client.NettyClient;
import com.easyim.comm.message.chat.ChatResponseMessage;
import com.easyim.service.MessageProcessor;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 聊天处理器
 *
 * @author 单程车票
 */
@ChannelHandler.Sharable
public class ChatHandler extends SimpleChannelInboundHandler<ChatResponseMessage> {

    private static class ChatHandlerInstance {
        private static final ChatHandler INSTANCE = new ChatHandler();
    }

    public static ChatHandler getInstance() {
        return ChatHandlerInstance.INSTANCE;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatResponseMessage msg) throws Exception {
        // 关闭消息重发
        NettyClient.getInstance().getMessageRetransmissionManager().remove(msg.getMessageId());
        // 回调消息到应用层
        MessageProcessor.getInstance().receiveMessage(msg);
    }

}
