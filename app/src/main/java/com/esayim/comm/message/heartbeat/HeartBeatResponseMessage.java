package com.esayim.comm.message.heartbeat;

import com.esayim.comm.message.Message;
import com.esayim.comm.message.MessageTypeConstants;

/**
 * 心跳包响应消息
 *
 * @author 单程车票
 */
public class HeartBeatResponseMessage extends Message {

    @Override
    public Byte getConstant() {
        return MessageTypeConstants.HeartBeatResponseMessage;
    }

}
