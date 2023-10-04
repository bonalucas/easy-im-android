package com.esayim.comm.message.reconnect;

import com.esayim.comm.message.Message;
import com.esayim.comm.message.MessageTypeConstants;

/**
 * 断线重连请求消息
 *
 * @author 单程车票
 */
public class ReconnectRequestMessage extends Message {

    /**
     * 用户唯一ID
     */
    private String userId;

    @Override
    public Byte getConstant() {
        return MessageTypeConstants.ReconnectRequestMessage;
    }

    public ReconnectRequestMessage() {
    }

    public ReconnectRequestMessage(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
