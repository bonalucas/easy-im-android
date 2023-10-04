package com.esayim.client.listener;

/**
 * 客户端连接状态回调类
 *
 * @author 单程车票
 */
public interface ClientConnectStatusCallback {

    /**
     * 连接中
     */
    void onConnecting();

    /**
     * 连接成功
     */
    void onConnected();

    /**
     * 连接失败
     */
    void onConnectFailed();

}
