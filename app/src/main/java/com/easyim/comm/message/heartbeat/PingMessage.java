package com.easyim.comm.message.heartbeat;

import com.easyim.comm.message.Message;
import com.easyim.comm.message.MessageTypeConstants;

/**
 * 心跳请求消息
 *
 * @author 单程车票
 */
public class PingMessage extends Message {

    @Override
    public Byte getConstant() {
        return MessageTypeConstants.PingMessage;
    }

}
