package com.esayim.comm.message.dialog;

import com.esayim.comm.message.Message;
import com.esayim.comm.message.MessageTypeConstants;

/**
 * 对话请求消息
 *
 * @author 单程车票
 */
public class DialogNoticeRequestMessage extends Message {

    /**
     * 发送者ID
     */
    private String senderId;

    /**
     * 接收者ID
     */
    private String receiverId;

    /**
     * 对话类型
     */
    private Integer dialogType;

    public DialogNoticeRequestMessage(long messageId) {
        super.setMessageId(messageId);
    }

    public DialogNoticeRequestMessage(long messageId, String senderId, String receiverId, Integer dialogType) {
        super.setMessageId(messageId);
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.dialogType = dialogType;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public Integer getDialogType() {
        return dialogType;
    }

    public void setDialogType(Integer dialogType) {
        this.dialogType = dialogType;
    }

    @Override
    public Byte getConstant() {
        return MessageTypeConstants.DialogNoticeRequestMessage;
    }

}
