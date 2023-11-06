package com.easyim.client;

import com.easyim.client.handler.ExceptionHandler;
import com.easyim.client.handler.HandShakeHandler;
import com.easyim.client.handler.HeartBeatHandler;
import com.easyim.client.handler.biz.ChatHandler;
import com.easyim.client.handler.biz.CreateMeetingHandler;
import com.easyim.client.handler.biz.FileHandler;
import com.easyim.client.handler.biz.JoinMeetingHandler;
import com.easyim.client.handler.biz.ServerErrorHandler;
import com.easyim.comm.protocol.MessageCodec;
import com.easyim.comm.protocol.ProtocolFrameDecoder;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * Netty 服务器调用链
 *
 * @author 单程车票
 */
@ChannelHandler.Sharable
public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // 动态添加心跳检测处理器
        // 添加长度字段解码器
        socketChannel.pipeline().addLast(ProtocolFrameDecoder.class.getSimpleName(), new ProtocolFrameDecoder());
        // 添加消息编解码器
        socketChannel.pipeline().addLast(MessageCodec.class.getSimpleName(), MessageCodec.getInstance());
        // 添加握手处理器
        socketChannel.pipeline().addLast(HandShakeHandler.class.getSimpleName(), HandShakeHandler.getInstance());
        // 添加心跳包响应处理器
        socketChannel.pipeline().addLast(HeartBeatHandler.class.getSimpleName(), HeartBeatHandler.getInstance());
        // 添加自定义业务处理器
        socketChannel.pipeline().addLast(ServerErrorHandler.class.getSimpleName(), ServerErrorHandler.getInstance());
        socketChannel.pipeline().addLast(CreateMeetingHandler.class.getSimpleName(), CreateMeetingHandler.getInstance());
        socketChannel.pipeline().addLast(JoinMeetingHandler.class.getSimpleName(), JoinMeetingHandler.getInstance());
        socketChannel.pipeline().addLast(ChatHandler.class.getSimpleName(), ChatHandler.getInstance());
        socketChannel.pipeline().addLast(FileHandler.class.getSimpleName(), FileHandler.getInstance());
        // 添加监控处理器
        socketChannel.pipeline().addLast(ExceptionHandler.class.getSimpleName(), ExceptionHandler.getInstance());
    }

}
