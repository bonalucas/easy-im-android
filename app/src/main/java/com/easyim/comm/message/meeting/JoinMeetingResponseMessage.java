package com.easyim.comm.message.meeting;

import com.easyim.comm.message.Message;
import com.easyim.comm.message.MessageTypeConstants;

/**
 * 加入会议响应消息
 *
 * @author 单程车票
 */
public class JoinMeetingResponseMessage extends Message {

    /**
     * 会议ID
     */
    private String meetingId;

    /**
     * 昵称
     */
    private String nickname;

    public JoinMeetingResponseMessage() {
    }

    public JoinMeetingResponseMessage(String meetingId, String nickname) {
        this.meetingId = meetingId;
        this.nickname = nickname;
    }

    public JoinMeetingResponseMessage(long messageId) {
        super.setMessageId(messageId);
    }

    public JoinMeetingResponseMessage(long messageId, String meetingId, String nickname) {
        super.setMessageId(messageId);
        this.meetingId = meetingId;
        this.nickname = nickname;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public Byte getConstant() {
        return MessageTypeConstants.JoinMeetingResponseMessage;
    }

}
