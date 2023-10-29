package com.esayim.client.handler;

import android.util.Log;

import com.esayim.comm.message.heartbeat.PongMessage;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 心跳包响应处理器
 *
 * @author 单程车票
 */
@ChannelHandler.Sharable
public class HeartBeatHandler extends SimpleChannelInboundHandler<PongMessage> {

    /**
     * 静态内部类（单例模式）
     */
    private static class HeartBeatHandlerInstance {
        private static final HeartBeatHandler INSTANCE = new HeartBeatHandler();
    }

    /**
     * 获取单例模式下的实例
     */
    public static HeartBeatHandler getInstance() {
        return HeartBeatHandlerInstance.INSTANCE;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PongMessage msg) throws Exception {
        // 记录心跳日志
        Log.d(HeartBeatHandler.class.getSimpleName(), "收到服务器的心跳响应信息");
    }

}
