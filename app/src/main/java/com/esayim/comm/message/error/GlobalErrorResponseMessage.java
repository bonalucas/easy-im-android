package com.esayim.comm.message.error;

import com.esayim.comm.message.Message;
import com.esayim.comm.message.MessageTypeConstants;

/**
 * 全局异常响应消息
 *
 * @author 单程车票
 */
public class GlobalErrorResponseMessage extends Message {

    private String error;

    @Override
    public Byte getConstant() {
        return MessageTypeConstants.GlobalErrorResponseMessage;
    }

    public GlobalErrorResponseMessage() {
    }

    public GlobalErrorResponseMessage(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
