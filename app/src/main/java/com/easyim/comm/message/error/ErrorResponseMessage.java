package com.easyim.comm.message.error;

import com.easyim.comm.message.Message;
import com.easyim.comm.message.MessageTypeConstants;

/**
 * 业务异常响应消息
 *
 * @author 单程车票
 */
public class ErrorResponseMessage extends Message {

    /**
     * 异常原因
     */
    private String error;

    public ErrorResponseMessage() {
    }

    public ErrorResponseMessage(String error) {
        this.error = error;
    }

    public ErrorResponseMessage(long messageId) {
        super.setMessageId(messageId);
    }

    public ErrorResponseMessage(long messageId, String error) {
        super.setMessageId(messageId);
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public Byte getConstant() {
        return MessageTypeConstants.ErrorResponseMessage;
    }

}
