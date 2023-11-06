package com.easyim.service.handler.impl;

import com.easyim.comm.message.Message;
import com.easyim.comm.message.chat.ChatResponseMessage;
import com.easyim.event.CEventCenter;
import com.easyim.event.Events;
import com.easyim.service.handler.AbstractBaseMessageHandler;

/**
 * 聊天信息处理器
 *
 * @author 单程车票
 */
public class ChatServiceHandler extends AbstractBaseMessageHandler {

    @Override
    public void execute(Message message) {
        if (message instanceof ChatResponseMessage) {
            ChatResponseMessage msg = (ChatResponseMessage) message;
            CEventCenter.dispatchEvent(Events.CHAT_RESPONSE, 0, 0, msg);
        }
    }

}
