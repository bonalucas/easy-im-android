package com.esayim.client.listener;

import com.esayim.comm.message.Message;

/**
 * 客户端事件监听器
 *
 * @author 单程车票
 */
public interface ClientEventListener {

    /**
     * 转发消息
     *
     * @param message 消息
     */
    void dispatchMessage(Message message);

    /**
     * 网络是否可用
     *
     * @return 反馈结果
     */
    boolean isNetworkAvailable();

    /**
     * 获取重连间隔时长
     *
     * @return 重连间隔时长
     */
    int getReconnectInterval();

    /**
     * 获取连接超时时长
     *
     * @return 连接超时时长
     */
    int getConnectTimeout();

    /**
     * 获取应用在前台心跳间隔时间
     *
     * @return 前台心跳间隔时间
     */
    int getForegroundHeartbeatInterval();

    /**
     * 获取应用在后台心跳间隔时间
     *
     * @return 后台心跳间隔时间
     */
    int getBackgroundHeartbeatInterval();

    /**
     * 获取应用层消息发送超时重发次数
     *
     * @return 消息发送超时重发次数
     */
    int getResendCount();

    /**
     * 获取应用层消息发送超时重发间隔
     *
     * @return 消息发送超时重发间隔
     */
    int getResendInterval();

}
