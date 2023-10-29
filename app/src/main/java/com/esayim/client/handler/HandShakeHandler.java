package com.esayim.client.handler;

import android.util.Log;

import com.esayim.client.NettyClient;
import com.esayim.comm.message.handshake.HandShakeRequestMessage;
import com.esayim.comm.message.handshake.HandShakeResponseMessage;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class HandShakeHandler extends SimpleChannelInboundHandler<HandShakeResponseMessage> {

    private static final String TAG = HandShakeHandler.class.getSimpleName();

    /**
     * 静态内部类（单例模式）
     */
    private static class HandShakeHandlerInstance {
        private static final HandShakeHandler INSTANCE = new HandShakeHandler();
    }

    /**
     * 获取单例模式下的实例
     */
    public static HandShakeHandler getInstance() {
        return HandShakeHandlerInstance.INSTANCE;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        Log.d(TAG, String.format("与服务器 %s 建立连接", ctx.channel().remoteAddress()));
        ctx.writeAndFlush(new HandShakeRequestMessage());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        Log.e(TAG, String.format("与服务器 %s 断开连接", ctx.channel().remoteAddress()));
        if (ctx.channel() != null) {
            ctx.channel().close();
            ctx.close();
        }
        NettyClient.getInstance().reconnect(false);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HandShakeResponseMessage msg) throws Exception {
        if (msg.getFeedback() == Boolean.FALSE) {
            // 握手失败，断开连接
            ctx.close();
            Log.e(TAG, String.format("与服务器 %s 握手失败，断开连接", ctx.channel().remoteAddress()));
        } else {
            Log.d(TAG, String.format("与服务器 %s 完成握手", ctx.channel().remoteAddress()));
        }
    }

}
