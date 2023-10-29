package com.esayim.service.handler.impl;

import com.esayim.comm.message.Message;
import com.esayim.comm.message.error.ErrorResponseMessage;
import com.esayim.service.handler.AbstractBaseMessageHandler;

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
            // TODO 调用广播通知应用层回显数据
        }
    }

}
