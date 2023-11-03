package com.easyim.comm.message.meeting;

import com.easyim.comm.message.Message;
import com.easyim.comm.message.MessageTypeConstants;

/**
 * 创建会议响应类
 *
 * @author 单程车票
 */
public class MeetingCreateResponseMessage extends Message {

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

    public MeetingCreateResponseMessage() {
    }

    public MeetingCreateResponseMessage(long messageId) {
        super.setMessageId(messageId);
    }

    public MeetingCreateResponseMessage(String meetingID, String creator, String theme) {
        this.meetingID = meetingID;
        this.creator = creator;
        this.theme = theme;
    }

    public MeetingCreateResponseMessage(long messageId, String meetingID, String creator, String theme) {
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
        return MessageTypeConstants.MeetingCreateResponseMessage;
    }

}
