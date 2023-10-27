package com.esayim.comm.message.handshake;

import com.esayim.comm.message.Message;
import com.esayim.comm.message.MessageTypeConstants;

/**
 * 握手响应消息
 *
 * @author 单程车票
 */
public class HandShakeResponseMessage extends Message {

    public HandShakeResponseMessage(long messageId) {
        super.setMessageId(messageId);
    }

    @Override
    public Byte getConstant() {
        return MessageTypeConstants.HandShakeResponseMessage;
    }

}
