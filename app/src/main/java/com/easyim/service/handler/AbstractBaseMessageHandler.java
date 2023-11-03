package com.easyim.service.handler;

import com.easyim.comm.message.Message;

/**
 * 抽象基础消息处理器（模板模式：加多一层抽象类方便以后拓展）
 *
 * @author 单程车票
 */
public abstract class AbstractBaseMessageHandler implements BaseMessageHandler{

    /**
     * 消息处理
     *
     * @param message 消息
     */
    public abstract void execute(Message message);

}
