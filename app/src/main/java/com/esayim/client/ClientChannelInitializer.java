package com.esayim.client;

import com.esayim.client.handler.ExceptionHandler;
import com.esayim.client.handler.HandShakeHandler;
import com.esayim.client.handler.HeartBeatHandler;
import com.esayim.client.handler.TestMessageHandler;
import com.esayim.comm.protocol.MessageCodec;
import com.esayim.comm.protocol.ProtocolFrameDecoder;

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
        socketChannel.pipeline().addLast("Easy-IM-ProtocolFrameDecoder", new ProtocolFrameDecoder());
        // 添加消息编解码器
        socketChannel.pipeline().addLast("Easy-IM-MessageCodec", MessageCodec.getInstance());
        // 添加握手处理器
        socketChannel.pipeline().addLast("HandShake-Handler", HandShakeHandler.getInstance());
        // 添加心跳包响应处理器
        socketChannel.pipeline().addLast("HeartBeat-Handler", HeartBeatHandler.getInstance());
        // 添加自定义业务处理器
        socketChannel.pipeline().addLast(TestMessageHandler.class.getSimpleName(), TestMessageHandler.getInstance());
        // 添加监控处理器
        socketChannel.pipeline().addLast("Exception-Handler", ExceptionHandler.getInstance());
    }

}
