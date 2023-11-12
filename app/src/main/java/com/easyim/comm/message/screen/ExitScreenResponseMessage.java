package com.easyim.comm.message.screen;

import com.dyuproject.protostuff.Tag;
import com.easyim.comm.message.Message;
import com.easyim.comm.message.MessageTypeConstants;

/**
 * 退出屏幕共享响应消息
 *
 * @author 单程车票
 */
public class ExitScreenResponseMessage extends Message {

    /**
     * 发送者昵称
     */
    @Tag(2)
    private String nickname;

    public ExitScreenResponseMessage() {
    }

    public ExitScreenResponseMessage(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public Byte getConstant() {
        return MessageTypeConstants.ExitScreenResponseMessage;
    }
    
}
