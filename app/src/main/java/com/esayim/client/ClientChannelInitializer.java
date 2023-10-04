package com.esayim.client;

import com.esayim.client.handler.ClientIdleStateHandler;
import com.esayim.client.handler.HeartbeatHandler;
import com.esayim.client.handler.MonitorHandler;
import com.esayim.client.handler.TestMessageHandler;
import com.esayim.comm.protocol.MessageCodec;
import com.esayim.comm.protocol.ProcotolFrameDecoder;

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

    /**
     * 消息编解码器
     */
    private static final MessageCodec MESSAGE_CODEC = new MessageCodec();

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // 动态添加心跳检测处理器（参考 NettyClient # addHeartbeatHandler()）
        // 添加长度字段解码器
        socketChannel.pipeline().addLast(ProcotolFrameDecoder.class.getSimpleName(), new ProcotolFrameDecoder());
        // 添加消息编解码器
        socketChannel.pipeline().addLast(MessageCodec.class.getSimpleName(), MESSAGE_CODEC);
        // 添加监控处理器
        socketChannel.pipeline().addLast(MonitorHandler.class.getSimpleName(), MonitorHandler.getInstance());
        // 添加心跳包响应处理器
        socketChannel.pipeline().addLast(HeartbeatHandler.class.getSimpleName(), HeartbeatHandler.getInstance());
        // 添加自定义业务处理器
        socketChannel.pipeline().addLast(TestMessageHandler.class.getSimpleName(), TestMessageHandler.getInstance());
    }

}
