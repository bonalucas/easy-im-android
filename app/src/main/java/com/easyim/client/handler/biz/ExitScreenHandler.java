package com.easyim.client.handler.biz;

import com.easyim.comm.message.screen.ExitScreenResponseMessage;
import com.easyim.service.MessageProcessor;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 退出屏幕共享处理器
 *
 * @author 单程车票
 */
@ChannelHandler.Sharable
public class ExitScreenHandler extends SimpleChannelInboundHandler<ExitScreenResponseMessage> {

    private static class ExitScreenHandlerInstance {
        private static final ExitScreenHandler INSTANCE = new ExitScreenHandler();
    }

    public static ExitScreenHandler getInstance() {
        return ExitScreenHandlerInstance.INSTANCE;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ExitScreenResponseMessage msg) throws Exception {
        // 回调消息到应用层
        MessageProcessor.getInstance().receiveMessage(msg);
    }

}
