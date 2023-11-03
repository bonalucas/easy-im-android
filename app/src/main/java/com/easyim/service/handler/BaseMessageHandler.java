package com.easyim.service.handler;

import com.easyim.comm.message.Message;

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
