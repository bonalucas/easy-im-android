package com.esayim.service;

import com.esayim.client.listener.ClientEventListener;
import com.esayim.comm.message.Message;

/**
 * 业务事件监听器
 *
 * @author 单程车票
 */
public class ServiceEventListener implements ClientEventListener {

    @Override
    public void dispatchMessage(Message message) {
        MessageProcessor.getInstance().receiveMessage(message);
    }

    @Override
    public boolean isNetworkAvailable() {
        // TODO 检查网络可用
        return true;
    }

}
