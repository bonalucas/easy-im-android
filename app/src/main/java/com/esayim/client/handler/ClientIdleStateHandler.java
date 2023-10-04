package com.esayim.client.handler;

import android.util.Log;

import com.esayim.client.NettyClient;
import com.esayim.comm.message.heartbeat.HeartBeatRequestMessage;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 心跳检测处理器
 *
 * @author 单程车票
 */
public class ClientIdleStateHandler extends IdleStateHandler {

    private final NettyClient nettyClient;
    private final int heartbeatInterval;
    private HeartbeatTask heartbeatTask;

    public ClientIdleStateHandler(NettyClient nettyClient, int heartbeatInterval) {
        super(heartbeatInterval * 3L, heartbeatInterval, 0L, TimeUnit.MILLISECONDS);
        this.nettyClient = nettyClient;
        this.heartbeatInterval = heartbeatInterval;
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) {
        IdleState state = evt.state();
        switch (state) {
            case READER_IDLE: {
                Log.i("ClientIdleStateHandler", (heartbeatInterval * 3) + " ms内未读到数据，自动触发客户端重连");
                // 触发重连
                nettyClient.reconnect(false);
                break;
            }

            case WRITER_IDLE: {
                Log.i("ClientIdleStateHandler", (heartbeatInterval) + " ms客户端未发送数据，自动触发发送心态包");
                if (heartbeatTask == null) {
                    heartbeatTask = new HeartbeatTask(ctx);
                }
                nettyClient.getClientExecutorService().execHeartbeatTask(heartbeatTask);
                break;
            }
        }

    }

    private class HeartbeatTask implements Runnable {

        private final ChannelHandlerContext ctx;

        public HeartbeatTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            if (ctx.channel().isActive()) {
                nettyClient.sendMessage(new HeartBeatRequestMessage(), false);
            }
        }
    }

}
