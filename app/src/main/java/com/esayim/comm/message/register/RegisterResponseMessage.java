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

    @Override
    public Byte getConstant() {
        return MessageTypeConstants.RegisterResponseMessage;
    }

    public RegisterResponseMessage() {
    }

    public RegisterResponseMessage(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }
}
