package com.esayim.comm.message.login.dto;

import java.util.Date;

/**
 * 聊天记录 Dto
 *
 * @author 单程车票
 */
public class RecordDto {

    /**
     * 对话ID
     */
    private String dialogId;

    /**
     * 发送者ID
     */
    private String senderId;

    /**
     * 发送者名称
     */
    private String name;

    /**
     * 发送者头像
     */
    private String avatar;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息时间
     */
    private Date recordTime;

    public RecordDto() {
    }

    public RecordDto(String dialogId, String senderId, String name, String avatar, String content, Date recordTime) {
        this.dialogId = dialogId;
        this.senderId = senderId;
        this.name = name;
        this.avatar = avatar;
        this.content = content;
        this.recordTime = recordTime;
    }

    public String getDialogId() {
        return dialogId;
    }

    public void setDialogId(String dialogId) {
        this.dialogId = dialogId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

}
