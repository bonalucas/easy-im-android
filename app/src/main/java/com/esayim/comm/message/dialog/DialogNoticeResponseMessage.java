package com.esayim.comm.message.dialog;

import com.esayim.comm.message.Message;
import com.esayim.comm.message.MessageTypeConstants;

import java.util.Date;

/**
 * 对话请求消息
 *
 * @author 单程车票
 */
public class DialogNoticeResponseMessage extends Message {

    /**
     * 对话ID
     */
    private String dialogId;

    /**
     * 接收者名称
     */
    private String name;

    /**
     * 接收者头像
     */
    private String avatar;

    /**
     * 对话简讯
     */
    private String sketch;

    /**
     * 对话最新消息时间
     */
    private Date now;

    public DialogNoticeResponseMessage(long messageId) {
        super.setMessageId(messageId);
    }

    public DialogNoticeResponseMessage(long messageId, String dialogId, String name, String avatar, String sketch, Date now) {
        super.setMessageId(messageId);
        this.dialogId = dialogId;
        this.name = name;
        this.avatar = avatar;
        this.sketch = sketch;
        this.now = now;
    }

    public String getDialogId() {
        return dialogId;
    }

    public void setDialogId(String dialogId) {
        this.dialogId = dialogId;
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

    @Override
    public Byte getConstant() {
        return MessageTypeConstants.DialogNoticeResponseMessage;
    }

}
