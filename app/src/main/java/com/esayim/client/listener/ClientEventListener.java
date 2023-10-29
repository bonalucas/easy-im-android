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

}
