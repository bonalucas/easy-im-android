package com.easyim.service.handler.impl;

import com.easyim.comm.message.Message;
import com.easyim.comm.message.error.ErrorResponseMessage;
import com.easyim.event.CEventCenter;
import com.easyim.event.Events;
import com.easyim.service.handler.AbstractBaseMessageHandler;

/**
 * 业务异常信息处理器
 *
 * @author 单程车票
 */
public class ErrorMessageServiceHandler extends AbstractBaseMessageHandler {

    @Override
    public void execute(Message message) {
        if (message instanceof ErrorResponseMessage) {
            ErrorResponseMessage msg = (ErrorResponseMessage) message;
            CEventCenter.dispatchEvent(Events.SERVER_ERROR, 0, 0, msg.getError());
        }
    }

}
