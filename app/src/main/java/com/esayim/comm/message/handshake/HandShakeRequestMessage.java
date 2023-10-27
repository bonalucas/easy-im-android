package com.esayim.comm.message.handshake;

import com.esayim.comm.message.Message;
import com.esayim.comm.message.MessageTypeConstants;

/**
 * 握手请求消息
 *
 * @author 单程车票
 */
public class HandShakeRequestMessage extends Message {

    public HandShakeRequestMessage(long messageId) {
        super.setMessageId(messageId);
    }

    @Override
    public Byte getConstant() {
        return MessageTypeConstants.HandShakeRequestMessage;
    }

}
