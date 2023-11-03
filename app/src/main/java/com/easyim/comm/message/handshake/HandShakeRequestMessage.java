package com.easyim.comm.message.handshake;

import com.easyim.comm.message.Message;
import com.easyim.comm.message.MessageTypeConstants;

/**
 * 握手请求消息
 *
 * @author 单程车票
 */
public class HandShakeRequestMessage extends Message {

    @Override
    public Byte getConstant() {
        return MessageTypeConstants.HandShakeRequestMessage;
    }

}
