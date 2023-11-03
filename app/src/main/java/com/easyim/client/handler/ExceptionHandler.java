package com.easyim.client.handler;

import android.util.Log;

import com.easyim.client.NettyClient;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 异常处理器
 *
 * @author 单程车票
 */
@ChannelHandler.Sharable
public class ExceptionHandler extends ChannelInboundHandlerAdapter {

    private static final String TAG = ExceptionHandler.class.getSimpleName();

    private static final ExceptionHandler instance = new ExceptionHandler();

    public static ExceptionHandler getInstance() {
        return instance;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        Log.e(TAG, String.format("与服务器 %s 通信异常，断开连接 【cause：%s】", ctx.channel().remoteAddress(), cause.getMessage()));
        if (ctx.channel() != null) {
            ctx.channel().close();
            ctx.close();
        }
        // 触发重连
        NettyClient.getInstance().reconnect(false);
    }

}
