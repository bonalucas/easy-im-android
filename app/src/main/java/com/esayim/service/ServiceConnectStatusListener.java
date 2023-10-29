package com.esayim.service;

import com.esayim.client.listener.ClientConnectStatusCallback;

/**
 * 业务连接状态监听器
 *
 * @author 单程车票
 */
public class ServiceConnectStatusListener implements ClientConnectStatusCallback {

    private static final String TAG = ServiceConnectStatusListener.class.getSimpleName();

    @Override
    public void onConnecting() {
    }

    @Override
    public void onConnected() {
    }

    @Override
    public void onConnectFailed() {
    }

}
