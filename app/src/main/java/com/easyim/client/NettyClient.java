package com.easyim.client;

import android.util.Log;

import com.easyim.client.common.ClientConfig;
import com.easyim.client.common.ClientExecutorService;
import com.easyim.client.common.MessageRetransmissionManager;
import com.easyim.client.handler.EasyIMIdleStateHandler;
import com.easyim.comm.message.Message;
import com.easyim.comm.protocol.MessageCodec;
import com.easyim.comm.protocol.ProtocolFrameDecoder;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.internal.StringUtil;

/**
 * Netty 客户端
 *
 * @author 单程车票
 */
public class NettyClient {

    /**
     * 客户端实例（单例模式）
     */
    private static volatile NettyClient instance;

    /**
     * bootstrap
     */
    private Bootstrap bootstrap;

    /**
     * 连接通道
     */
    private Channel channel;

    /**
     * 日志标识
     */
    private static final String TAG = "NettyClient";

    /**
     * 客户端关闭连接标识
     */
    private boolean isClosed = false;

    /**
     * 客户端正在连接标识
     */
    private boolean isReconnecting = false;

    /**
     * 客户端线程池
     */
    private ClientExecutorService clientExecutorService;

    /**
     * 消息重发管理器
     */
    private MessageRetransmissionManager messageRetransmissionManager;

    /**
     * 客户端连接状态
     */
    private int connectStatus = ClientConfig.CONNECT_STATE_FAILURE;

    /**
     * 心跳间隔时间（默认前台）
     */
    private int heartbeatInterval = ClientConfig.DEFAULT_HEARTBEAT_INTERVAL_FOREGROUND;

    /**
     * app状态
     */
    private int appStatus = ClientConfig.APP_STATUS_FOREGROUND;

    /**
     * 服务器地址
     */
    private static final String serverIp = ClientConfig.SERVER_IP;

    /**
     * 服务器端口
     */
    private static final int serverPort = ClientConfig.SERVER_PORT;

    /**
     * 调用链
     */
    private static final ClientChannelInitializer CLIENT_CHANNEL_INITIALIZER = new ClientChannelInitializer();

    /**
     * 双重锁检查方式创建单例
     *
     * @return 客户端实例
     */
    public static NettyClient getInstance() {
        if (null == instance) {
            synchronized (NettyClient.class) {
                if (null == instance) {
                    instance = new NettyClient();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化客户端并首次连接服务器
     *
     * @param appStatus APP状态
     */
    public void init(int appStatus) {
        close();
        isClosed = false;
        this.clientExecutorService = new ClientExecutorService();
        clientExecutorService.initReconnectPool();
        messageRetransmissionManager = new MessageRetransmissionManager(this);
        // 设置初始化 APP 状态
        setAppStatus(appStatus);
        // 首次连接
        reconnect(true);
    }

    /**
     * 客户端连接方法
     *
     * @param isFirst 首次连接标识
     */
    public void reconnect(boolean isFirst) {
        // 判断是否是首次连接
        if (!isFirst) {
            try {
                // 非首次连接，等待一个重连周期间隔时长
                Thread.sleep(ClientConfig.DEFAULT_RECONNECT_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 加双重锁保证只有一个线程执行重连任务
        if (!isClosed && !isReconnecting) {
            synchronized (this) {
                if (!isClosed && !isReconnecting) {
                    // 标识正在连接
                    isReconnecting = true;
                    // 回调客户端连接状态
                    dispatchConnectStatusCallback(ClientConfig.CONNECT_STATE_CONNECTING);
                    // 关闭channel
                    closeChannel();
                    // 执行重连任务
                    clientExecutorService.execReconnectTask(new ReconnectTask(isFirst));
                }
            }
        }
    }

    /**
     * 发送消息
     *
     * @param message 消息
     * @param enabled 开启消息重发器
     */
    public void sendMessage(Message message, boolean enabled) {
        // 判断通道是否为空
        if (channel == null) {
            Log.e(TAG, "发送消息失败 【cause：Channel is null】");
            return;
        }
        // 开启消息重发器（延迟 10s 重发消息）
        if (enabled && message.getMessageId() != null) {
            messageRetransmissionManager.add(message);
        }
        // 发送消息
        try {
            channel.writeAndFlush(message);
        } catch (Exception e) {
            Log.e(TAG, String.format("发送消息失败 【cause：%s】", e.getMessage()));
        }
    }

    /**
     * 增加心跳检测处理器（保持应用长连接）
     */
    public void addHeartbeatHandler() {
        if (channel == null || !channel.isActive() || channel.pipeline() == null) return;
        try {
            String name = EasyIMIdleStateHandler.class.getSimpleName();
            // 移除之前的处理器
            removeHandler(name);
            // 添加新处理器（添加在最前面）
            channel.pipeline().addFirst(name, new EasyIMIdleStateHandler(this, getHeartbeatInterval()));
            Log.i(TAG, String.format("添加心跳检测处理器成功,周期为 %s ms", getHeartbeatInterval()));
        } catch (Exception e) {
            Log.e(TAG, String.format("添加心跳检测处理器失败 【cause：%s】", e.getMessage()));
        }
    }

    /**
     * 设置 app 前后状态
     *
     * @param appStatus app状态
     */
    public void setAppStatus(int appStatus) {
        this.appStatus = appStatus;
        if (this.appStatus == ClientConfig.APP_STATUS_FOREGROUND) {
            heartbeatInterval = getForegroundHeartbeatInterval();
        } else if (this.appStatus == ClientConfig.APP_STATUS_BACKGROUND) {
            heartbeatInterval = getBackgroundHeartbeatInterval();
        }
        // 初始化时无法添加心跳检测处理器（此时channel为空）
        addHeartbeatHandler();
    }

    /**
     * 关闭连接释放资源
     */
    public void close() {
        if (isClosed) return;
        // 标识关闭
        isClosed = true;
        try {
            // 关闭channel
            closeChannel();
            // 关闭bootstrap线程组
            if (bootstrap != null) {
                bootstrap.config().group().shutdownGracefully();
            }
            // 关闭客户端线程池
            if (clientExecutorService != null) {
                clientExecutorService.destroy();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            // 释放引用
            isReconnecting = false;
            channel = null;
            bootstrap = null;
        }
    }

    /**
     * 获取连接超时时长
     */
    public int getConnectTimeout() {
        return ClientConfig.DEFAULT_CONNECT_TIMEOUT;
    }

    /**
     * 获取心跳间隔时长
     */
    public int getHeartbeatInterval() {
        return this.heartbeatInterval;
    }

    /**
     * 获取重连间隔时长
     */
    public int getReconnectInterval() {
        return ClientConfig.DEFAULT_RECONNECT_BASE_DELAY_TIME;
    }

    /**
     * 获取客户端关闭状态
     */
    public boolean isClosed() {
        return this.isClosed;
    }

    /**
     * 获取心跳间隔时长（前台）
     */
    public int getForegroundHeartbeatInterval() {
        return ClientConfig.DEFAULT_HEARTBEAT_INTERVAL_FOREGROUND;
    }

    /**
     * 获取心跳间隔时长（后台）
     */
    public int getBackgroundHeartbeatInterval() {
        return ClientConfig.DEFAULT_HEARTBEAT_INTERVAL_BACKGROUND;
    }

    /**
     * 获取消息超时重发次数
     *
     * @return 消息超时重发次数
     */
    public int getResendCount() {
        return ClientConfig.DEFAULT_RESEND_COUNT;
    }

    /**
     * 获取消息发送超时重发间隔
     */
    public int getResendInterval() {
        return ClientConfig.DEFAULT_RESEND_INTERVAL;
    }

    /**
     * 获取APP状态
     */
    public int getAppStatus() {
        return this.appStatus;
    }

    /**
     * 获取客户端线程池
     */
    public ClientExecutorService getClientExecutorService() {
        return this.clientExecutorService;
    }

    /**
     * 获取消息超时管理器
     */
    public MessageRetransmissionManager getMessageRetransmissionManager() {
        return this.messageRetransmissionManager;
    }

    // =============================== 私有方法 ===============================

    /**
     * 构建bootstrap
     */
    private void buildBootstrap() {
        EventLoopGroup workerGroup = new NioEventLoopGroup(4);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                // 设置连接超时5s
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, getConnectTimeout())
                // 开启 TCP 心跳机制
                .option(ChannelOption.SO_KEEPALIVE, true)
                // 开启 Nagle 算法（保证高实时性）
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(CLIENT_CHANNEL_INITIALIZER);
        this.bootstrap = bootstrap;
    }

    /**
     * 回调客户端连接状态
     *
     * @param connectStatus 连接状态
     */
    private void dispatchConnectStatusCallback(int connectStatus) {
        this.connectStatus = connectStatus;
        switch (connectStatus) {
            case ClientConfig.CONNECT_STATE_CONNECTING: {
                Log.i(TAG, "客户端连接中...");
                break;
            }

            case ClientConfig.CONNECT_STATE_SUCCESS: {
                Log.i(TAG, String.format("客户端连接成功，host『%s』, port『%s』", serverIp, serverPort));
                break;
            }

            case ClientConfig.CONNECT_STATE_FAILURE:
            default: {
                Log.i(TAG, "客户端连接失败...");
                break;
            }
        }
    }

    /**
     * 关闭 Channel
     */
    private void closeChannel() {
        try {
            if (channel != null) {
                removeHandler(EasyIMIdleStateHandler.class.getSimpleName());
                removeHandler(ProtocolFrameDecoder.class.getSimpleName());
                removeHandler(MessageCodec.class.getSimpleName());
            }
        } catch (Exception ex) {
            Log.e(TAG, "channel 关闭异常:" + ex.getMessage());
        } finally {
            try {
                if (channel != null) {
                    channel.close();
                    channel.eventLoop().shutdownGracefully();
                    channel = null;
                }
            } catch (Exception e) {
                Log.e(TAG, "channel 关闭异常：" + e.getMessage());
            }
        }
    }

    /**
     * 移除指定handler
     *
     * @param handlerName 处理器名称
     */
    private void removeHandler(String handlerName) {
        try {
            if (channel.pipeline().get(handlerName) != null) {
                channel.pipeline().remove(handlerName);
            }
        } catch (Exception e) {
            Log.e(TAG, String.format("移除handler失败，handlerName= %s", handlerName));
        }
    }

    /**
     * 连接服务器
     */
    private void toServer() {
        try {
            // 连接服务器并获取与客户端之间的通道（同步阻塞获取）
            channel = bootstrap.connect(new InetSocketAddress(serverIp, serverPort)).sync().channel();
            // 重新添加初始化时的心跳检测处理器（此时channel不为空）
            addHeartbeatHandler();
            // 连接成功后重发未发送成功的消息
            messageRetransmissionManager.onResetConnected();
        } catch (Exception e) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            Log.e(TAG, String.format("连接Server(ip[%s], port[%s])失败", serverIp, serverPort));
            channel = null;
        }
    }

    /**
     * 重连任务
     */
    private class ReconnectTask implements Runnable {

        /**
         * 是否首次连接
         */
        private final boolean isFirst;

        public ReconnectTask(boolean isFirst) {
            this.isFirst = isFirst;
        }

        @Override
        public void run() {
            // 非首次进行重连，执行到这里即代表已经连接失败，回调连接状态到应用层
            if (!isFirst) {
                dispatchConnectStatusCallback(ClientConfig.CONNECT_STATE_FAILURE);
            }
            try {
                // 重连时停止心跳任务
                clientExecutorService.destroyHeartbeatPool();
                while (!isClosed) {
                    // 连接服务器获取连接状态标识
                    int status = reconnectTask();
                    // 连接成功
                    if (status == ClientConfig.CONNECT_STATE_SUCCESS) {
                        dispatchConnectStatusCallback(status);
                        break;
                    }
                    // 连接失败
                    if (status == ClientConfig.CONNECT_STATE_FAILURE) {
                        dispatchConnectStatusCallback(status);
                        try {
                            Thread.sleep(ClientConfig.DEFAULT_RECONNECT_INTERVAL);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } finally {
                // 标识未正在连接
                isReconnecting = false;
            }
        }

        /**
         * 执行重连客户端任务
         */
        private int reconnectTask() {
            // 保证客户端处于未关闭状态
            if (!isClosed) {
                try {
                    // 释放EventLoop线程组
                    if (bootstrap != null) {
                        bootstrap.config().group().shutdownGracefully();
                    }
                } finally {
                    bootstrap = null;
                }
                // 构建bootstrap
                buildBootstrap();
                return connectServer();
            }
            return ClientConfig.CONNECT_STATE_FAILURE;
        }

        /**
         * 连接服务器
         */
        private int connectServer() {
            if (isClosed || StringUtil.isNullOrEmpty(serverIp)) {
                return ClientConfig.CONNECT_STATE_FAILURE;
            }
            // 循环尝试重连服务器
            for (int i = 1; i <= ClientConfig.DEFAULT_RECONNECT_COUNT; i++) {
                if (isClosed) {
                    return ClientConfig.CONNECT_STATE_FAILURE;
                }
                if (connectStatus != ClientConfig.CONNECT_STATE_CONNECTING) {
                    dispatchConnectStatusCallback(ClientConfig.CONNECT_STATE_CONNECTING);
                }
                Log.i(TAG, String.format("正在尝试进行第『%d』次连接，当前连接延时时长为『%dms』", i, i * getReconnectInterval()));
                try {
                    // 连接服务器
                    toServer();
                    if (channel != null) {
                        return ClientConfig.CONNECT_STATE_SUCCESS;
                    } else {
                        Thread.sleep((long) i * getReconnectInterval());
                    }
                } catch (InterruptedException e) {
                    close();
                    break;
                }
            }
            // 连接失败
            return ClientConfig.CONNECT_STATE_FAILURE;
        }
    }
}
