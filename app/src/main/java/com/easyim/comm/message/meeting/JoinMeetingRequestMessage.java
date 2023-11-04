package com.easyim.comm.message.meeting;

import com.easyim.comm.message.Message;
import com.easyim.comm.message.MessageTypeConstants;

/**
 * 加入会议请求消息
 *
 * @author 单程车票
 */
public class JoinMeetingRequestMessage extends Message {

    /**
     * 会议ID
     */
    private String meetingId;

    /**
     * 入会昵称
     */
    private String nickname;

    public JoinMeetingRequestMessage() {
    }

    public JoinMeetingRequestMessage(String meetingId, String nickname) {
        this.meetingId = meetingId;
        this.nickname = nickname;
    }

    public JoinMeetingRequestMessage(long messageId) {
        super.setMessageId(messageId);
    }

    public JoinMeetingRequestMessage(long messageId, String meetingId, String nickname) {
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
        return MessageTypeConstants.JoinMeetingRequestMessage;
    }

}
