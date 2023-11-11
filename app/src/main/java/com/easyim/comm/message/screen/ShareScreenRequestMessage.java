package com.easyim.comm.message.screen;

import com.easyim.comm.message.Message;
import com.easyim.comm.message.MessageTypeConstants;

/**
 * 屏幕共享请求消息
 *
 * @author 单程车票
 */
public class ShareScreenRequestMessage extends Message {

    @Override
    public Byte getConstant() {
        return MessageTypeConstants.ShareScreenRequestMessage;
    }

}
