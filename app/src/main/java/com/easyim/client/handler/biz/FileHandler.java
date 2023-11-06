package com.easyim.client.handler.biz;

import com.easyim.comm.message.file.FileResponseMessage;
import com.easyim.service.MessageProcessor;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class FileHandler extends SimpleChannelInboundHandler<FileResponseMessage> {

    private static class FileHandlerInstance {
        private static final FileHandler INSTANCE = new FileHandler();
    }

    public static FileHandler getInstance() {
        return FileHandlerInstance.INSTANCE;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FileResponseMessage msg) throws Exception {
        // 回调消息到应用层
        MessageProcessor.getInstance().receiveMessage(msg);
    }

}
