package com.easyim.comm.message.chat;

import com.easyim.comm.message.Message;
import com.easyim.comm.message.MessageTypeConstants;

/**
 * 聊天请求消息
 *
 * @author 单程车票
 */
public class ChatRequestMessage extends Message {

    /**
     * 消息类型
     */
    private Byte type;

    /**
     * 消息内容
     */
    private String content;

    public ChatRequestMessage() {
    }

    public ChatRequestMessage(Byte type, String content) {
        this.type = type;
        this.content = content;
    }

    public ChatRequestMessage(long messageId) {
        super.setMessageId(messageId);
    }

    public ChatRequestMessage(long messageId, Byte type, String content) {
        super.setMessageId(messageId);
        this.type = type;
        this.content = content;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public Byte getConstant() {
        return MessageTypeConstants.ChatRequestMessage;
    }

}
