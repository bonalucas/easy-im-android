package com.easyim.comm.message.meeting;

import com.easyim.comm.message.Message;
import com.easyim.comm.message.MessageTypeConstants;

/**
 * 退出会议请求消息
 *
 * @author 单程车票
 */
public class LeaveMeetingRequestMessage extends Message {

    @Override
    public Byte getConstant() {
        return MessageTypeConstants.LeaveMeetingRequestMessage;
    }

}
