package com.esayim.client.handler;

import android.util.Log;

import com.esayim.client.NettyClient;
import com.esayim.comm.message.Message;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 监控处理器
 *
 * @author 单程车票
 */
@ChannelHandler.Sharable
public class MonitorHandler extends ChannelInboundHandlerAdapter {

    private static final String TAG = MonitorHandler.class.getSimpleName();

    private static final MonitorHandler instance = new MonitorHandler();

    public static MonitorHandler getInstance() {
        return instance;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        // 监控日志
        Log.i(TAG, String.format("服务器建立连接：%s", ctx.channel()));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        // 监控日志
        Log.e(TAG, String.format("服务器断开连接：%s", ctx.channel()));
        // 服务器断开连接，关闭 channel
        if (ctx.channel() != null) {
            ctx.channel().close();
            ctx.close();
        }
        // 触发重连
        NettyClient.getInstance().reconnect(false);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        // 监控日志
        Log.e(TAG, String.format("客户端异常断开连接：%s", ctx.channel()));
        // 服务器断开连接，关闭 channel
        if (ctx.channel() != null) {
            ctx.channel().close();
            ctx.close();
        }
        // 触发重连
        NettyClient.getInstance().reconnect(false);
    }

}
