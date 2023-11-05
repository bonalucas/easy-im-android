package com.easyim.comm.message.meeting;

import com.dyuproject.protostuff.Tag;
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
    @Tag(2)
    private String meetingId;

    /**
     * 会议主题
     */
    @Tag(3)
    private String theme;

    /**
     * 昵称
     */
    @Tag(4)
    private String nickname;

    public JoinMeetingResponseMessage() {
    }

    public JoinMeetingResponseMessage(String meetingId, String theme, String nickname) {
        this.meetingId = meetingId;
        this.theme = theme;
        this.nickname = nickname;
    }

    public JoinMeetingResponseMessage(long messageId) {
        super.setMessageId(messageId);
    }

    public JoinMeetingResponseMessage(long messageId, String meetingId, String theme, String nickname) {
        super.setMessageId(messageId);
        this.meetingId = meetingId;
        this.theme = theme;
        this.nickname = nickname;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
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
