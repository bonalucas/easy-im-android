package com.easyim.comm.message.meeting;

import com.easyim.comm.message.Message;
import com.easyim.comm.message.MessageTypeConstants;

/**
 * 创建会议请求类
 *
 * @author 单程车票
 */
public class MeetingCreateRequestMessage extends Message {

    /**
     * 会议ID
     */
    private String meetingID;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 会议主题
     */
    private String theme;

    public MeetingCreateRequestMessage() {
    }

    public MeetingCreateRequestMessage(long messageId) {
        super.setMessageId(messageId);
    }

    public MeetingCreateRequestMessage(String meetingID, String creator, String theme) {
        this.meetingID = meetingID;
        this.creator = creator;
        this.theme = theme;
    }

    public MeetingCreateRequestMessage(long messageId, String meetingID, String creator, String theme) {
        super.setMessageId(messageId);
        this.meetingID = meetingID;
        this.creator = creator;
        this.theme = theme;
    }

    public String getMeetingID() {
        return meetingID;
    }

    public void setMeetingID(String meetingID) {
        this.meetingID = meetingID;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    @Override
    public Byte getConstant() {
        return MessageTypeConstants.MeetingCreateRequestMessage;
    }

}
