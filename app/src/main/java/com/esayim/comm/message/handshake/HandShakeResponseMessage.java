package com.esayim.comm.message.handshake;

import com.esayim.comm.message.Message;
import com.esayim.comm.message.MessageTypeConstants;

/**
 * 握手响应消息
 *
 * @author 单程车票
 */
public class HandShakeResponseMessage extends Message {

    /**
     * 反馈
     */
    private Boolean feedback;

    public HandShakeResponseMessage() {
    }

    public HandShakeResponseMessage(Boolean feedback) {
        this.feedback = feedback;
    }

    public Boolean getFeedback() {
        return feedback;
    }

    public void setFeedback(Boolean feedback) {
        this.feedback = feedback;
    }

    @Override
    public Byte getConstant() {
        return MessageTypeConstants.HandShakeResponseMessage;
    }

}
