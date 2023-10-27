package com.esayim.comm.message.register;

import com.esayim.comm.message.Message;
import com.esayim.comm.message.MessageTypeConstants;

/**
 * 注册响应信息
 *
 * @author 单程车票
 */
public class RegisterResponseMessage extends Message {

    /**
     * 响应信息
     */
    private String responseMsg;

    public RegisterResponseMessage(long messageId) {
        super.setMessageId(messageId);
    }

    public RegisterResponseMessage(long messageId, String responseMsg) {
        super.setMessageId(messageId);
        this.responseMsg = responseMsg;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    @Override
    public Byte getConstant() {
        return MessageTypeConstants.RegisterResponseMessage;
    }

}
