package com.esayim.comm.message.login.dto;

import java.util.Date;
import java.util.List;

/**
 * 对话 Dto
 *
 * @author 单程车票
 */
public class DialogDto {

    /**
     * 对话ID
     */
    private String dialogId;

    /**
     * 对话类型
     */
    private Integer dialogType;

    /**
     * 对话名称
     */
    private String name;

    /**
     * 对话头像
     */
    private String avatar;

    /**
     * 对话简述
     */
    private String sketch;

    /**
     * 最新消息时间
     */
    private Date now;

    /**
     * 聊天记录
     */
    private List<RecordDto> RecordList;

    public DialogDto() {
    }

    public DialogDto(String dialogId, Integer dialogType, String name, String avatar, String sketch, Date now, List<RecordDto> recordList) {
        this.dialogId = dialogId;
        this.dialogType = dialogType;
        this.name = name;
        this.avatar = avatar;
        this.sketch = sketch;
        this.now = now;
        RecordList = recordList;
    }

    public String getDialogId() {
        return dialogId;
    }

    public void setDialogId(String dialogId) {
        this.dialogId = dialogId;
    }

    public Integer getDialogType() {
        return dialogType;
    }

    public void setDialogType(Integer dialogType) {
        this.dialogType = dialogType;
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

    public String getSketch() {
        return sketch;
    }

    public void setSketch(String sketch) {
        this.sketch = sketch;
    }

    public Date getNow() {
        return now;
    }

    public void setNow(Date now) {
        this.now = now;
    }

    public List<RecordDto> getRecordList() {
        return RecordList;
    }

    public void setRecordList(List<RecordDto> recordList) {
        RecordList = recordList;
    }

}
