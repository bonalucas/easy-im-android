package com.esayim.comm.message.chat;

import com.esayim.comm.message.Message;
import com.esayim.comm.message.MessageTypeConstants;

import java.util.Date;

/**
 * 聊天响应信息
 *
 * @author 单程车票
 */
public class ChatResponseMessage extends Message {

    /**
     * 发送者ID
     */
    private String senderId;

    /**
     * 接收者ID
     */
    private String receiverId;

    /**
     * 内容
     */
    private String content;

    /**
     * 消息时间
     */
    private Date now;

    public ChatResponseMessage() {
    }

    public ChatResponseMessage(String senderId, String receiverId, String content, Date now) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.now = now;
    }

    public ChatResponseMessage(String messageId, Boolean status) {
        super.setMessageId(messageId);
        super.setStatus(status);
    }

    public ChatResponseMessage(String messageId, Boolean status, String senderId, String receiverId, String content, Date now) {
        super.setMessageId(messageId);
        super.setStatus(status);
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.now = now;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getNow() {
        return now;
    }

    public void setNow(Date now) {
        this.now = now;
    }

    @Override
    public Byte getConstant() {
        return MessageTypeConstants.ChatResponseMessage;
    }

}
