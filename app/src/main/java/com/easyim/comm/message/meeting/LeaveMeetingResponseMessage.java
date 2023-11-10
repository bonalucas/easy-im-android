package com.easyim.comm.message.meeting;

import com.dyuproject.protostuff.Tag;
import com.easyim.comm.message.Message;
import com.easyim.comm.message.MessageTypeConstants;

/**
 * 退出会议响应消息
 *
 * @author 单程车票
 */
public class LeaveMeetingResponseMessage extends Message {

    /**
     * 退出用户昵称
     */
    @Tag(2)
    private String nickname;

    public LeaveMeetingResponseMessage() {
    }

    public LeaveMeetingResponseMessage(String nickname) {
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
        return MessageTypeConstants.LeaveMeetingResponseMessage;
    }

}
