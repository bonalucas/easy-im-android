package com.esayim.service.handler;

import com.esayim.comm.message.Message;

/**
 * 基础消息处理器
 *
 * @author 单程车票
 */
public interface BaseMessageHandler {

    /**
     * 消息处理
     *
     * @param message 消息
     */
    void execute(Message message);

}
