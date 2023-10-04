package com.esayim.client.common;

import com.esayim.client.listener.ClientEventListener;
import com.esayim.comm.message.Message;

/**
 * 消息分发器
 *
 * @author 单程车票
 */
public class MessageDispatcher {

    private ClientEventListener clientEventListener;

    public void setClientEventListener(ClientEventListener clientEventListener) {
        this.clientEventListener = clientEventListener;
    }

    /**
     * 接收消息转发到应用层
     *
     * @param message 消息
     */
    public void receiveMessage(Message message) {
        if (clientEventListener == null) return;
        clientEventListener.dispatchMessage(message);
    }

}
