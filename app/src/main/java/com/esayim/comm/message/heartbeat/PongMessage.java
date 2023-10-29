package com.esayim.comm.message.heartbeat;

import com.esayim.comm.message.Message;
import com.esayim.comm.message.MessageTypeConstants;

/**
 * 心跳响应消息
 *
 * @author 单程车票
 */
public class PongMessage extends Message {

    @Override
    public Byte getConstant() {
        return MessageTypeConstants.PongMessage;
    }

}
