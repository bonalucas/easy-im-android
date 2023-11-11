package com.easyim.comm.message.screen;

import com.dyuproject.protostuff.Tag;
import com.easyim.comm.message.Message;
import com.easyim.comm.message.MessageTypeConstants;

/**
 * 屏幕共享响应消息
 *
 * @author 单程车票
 */
public class ShareScreenResponseMessage extends Message {

    /**
     * 会议主题
     */
    @Tag(2)
    private String theme;

    /**
     * 屏幕共享发起者
     */
    @Tag(3)
    private String nickname;

    /**
     * 会议ID
     */
    @Tag(4)
    private String meetingId;

    public ShareScreenResponseMessage() {
    }

    public ShareScreenResponseMessage(String theme, String nickname, String meetingId) {
        this.theme = theme;
        this.nickname = nickname;
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

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    @Override
    public Byte getConstant() {
        return MessageTypeConstants.ShareScreenResponseMessage;
    }

}
