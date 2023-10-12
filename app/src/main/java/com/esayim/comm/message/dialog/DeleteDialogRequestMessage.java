package com.esayim.comm.message.dialog;

import com.esayim.comm.message.Message;
import com.esayim.comm.message.MessageTypeConstants;

/**
 * 删除对话请求消息
 *
 * @author 单程车票
 */
public class DeleteDialogRequestMessage extends Message {

    /**
     * 对话ID
     */
    private String dialogId;

    /**
     * 发送者ID
     */
    private String senderId;

    public DeleteDialogRequestMessage() {
    }

    public DeleteDialogRequestMessage(String dialogId, String senderId) {
        this.dialogId = dialogId;
        this.senderId = senderId;
    }

    public DeleteDialogRequestMessage(String messageId, Boolean status) {
        super.setMessageId(messageId);
        super.setStatus(status);
    }

    public DeleteDialogRequestMessage(String messageId, Boolean status, String dialogId, String senderId) {
        super.setMessageId(messageId);
        super.setStatus(status);
        this.dialogId = dialogId;
        this.senderId = senderId;
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

    @Override
    public Byte getConstant() {
        return MessageTypeConstants.DeleteDialogRequestMessage;
    }

}
