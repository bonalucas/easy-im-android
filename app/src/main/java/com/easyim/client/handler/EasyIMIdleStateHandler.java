package com.easyim.client.handler;

import android.util.Log;

import com.easyim.client.NettyClient;
import com.easyim.comm.message.heartbeat.PingMessage;

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
public class EasyIMIdleStateHandler extends IdleStateHandler {

    private static final String TAG = EasyIMIdleStateHandler.class.getSimpleName();

    /**
     * 客户端
     */
    private final NettyClient nettyClient;

    /**
     * 心跳检测时长
     */
    private final int heartbeatInterval;

    /**
     * 心跳任务
     */
    private HeartbeatTask heartbeatTask;

    public EasyIMIdleStateHandler(NettyClient nettyClient, int heartbeatInterval) {
        super(heartbeatInterval * 3L, heartbeatInterval, 0L, TimeUnit.MILLISECONDS);
        this.nettyClient = nettyClient;
        this.heartbeatInterval = heartbeatInterval;
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) {
        IdleState state = evt.state();
        switch (state) {
            case READER_IDLE: {
                Log.i(TAG, String.format("%s ms内未读到数据，自动触发客户端重连", (heartbeatInterval * 3L)));
                // 触发重连
                nettyClient.reconnect(false);
                break;
            }

            case WRITER_IDLE: {
                Log.i(TAG, String.format("%s ms客户端未发送数据，自动触发发送心跳包", heartbeatInterval));
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
                nettyClient.sendMessage(new PingMessage(), false);
            }
        }
    }

}
