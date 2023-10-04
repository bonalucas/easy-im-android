package com.esayim.client.common;

/**
 * 客户端配置类
 *
 * @author 单程车票
 */
public class ClientConfig {

    /**
     * Netty服务器IP地址
     */
    public static final String SERVER_IP = "100.2.135.237";

    /**
     * Netty服务器端口号
     */
    public static final int SERVER_PORT = 8086;

    /**
     * 重连周期间隔时长
     */
    public static final int DEFAULT_RECONNECT_INTERVAL = 3 * 1000;

    /**
     * 重连周期中尝试连接次数
     */
    public static final int DEFAULT_RECONNECT_COUNT = 3;

    /**
     * 重连周期中尝试连接的间隔时长
     */
    public static final int DEFAULT_RECONNECT_BASE_DELAY_TIME = 3 * 1000;

    /**
     * 心跳消息间隔时长（前台）
     */
    public static final int DEFAULT_HEARTBEAT_INTERVAL_FOREGROUND = 8 * 1000;

    /**
     * 心跳消息间隔时长（后台）
     */
    public static final int DEFAULT_HEARTBEAT_INTERVAL_BACKGROUND = 30 * 1000;

    /**
     * 前台APP标识
     */
    public static final int APP_STATUS_FOREGROUND = 0;

    /**
     * 后台APP标识
     */
    public static final int APP_STATUS_BACKGROUND = -1;

    /**
     * 客户端状态-正在连接
     */
    public static final int CONNECT_STATE_CONNECTING = 0;

    /**
     * 客户端状态-连接成功
     */
    public static final int CONNECT_STATE_SUCCESS = 1;

    /**
     * 客户端状态-连接失败
     */
    public static final int CONNECT_STATE_FAILURE = -1;

    /**
     * 连接超时时长
     */
    public static final int DEFAULT_CONNECT_TIMEOUT = 10 * 1000;

    /**
     * 默认消息发送失败重发次数
     */
    public static final int DEFAULT_RESEND_COUNT = 3;

    /**
     * 默认消息重发间隔时长
     */
    public static final int DEFAULT_RESEND_INTERVAL = 8 * 1000;

    /**
     * 默认服务端请求成功状态
     */
    public static final int DEFAULT_REPORT_SERVER_SEND_MSG_SUCCESS = 1;

    /**
     * 默认服务端请求失败状态
     */
    public static final int DEFAULT_REPORT_SERVER_SEND_MSG_FAILURE = 0;

}
