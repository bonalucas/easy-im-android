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
public class HeartbeatHandler extends SimpleChannelInboundHandler<PongMessage> {

    private static final HeartbeatHandler instance = new HeartbeatHandler();

    public static HeartbeatHandler getInstance() {
        return instance;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PongMessage msg) throws Exception {
        // 记录心跳日志
        Log.i(HeartbeatHandler.class.getSimpleName(), "收到服务器的心跳响应信息");
    }

}
