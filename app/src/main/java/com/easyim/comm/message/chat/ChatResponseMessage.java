package com.easyim.comm.message.chat;

import com.easyim.comm.message.Message;
import com.easyim.comm.message.MessageTypeConstants;

/**
 * 聊天响应消息
 *
 * @author 单程车票
 */
public class ChatResponseMessage extends Message {

    /**
     * 发送消息者昵称
     */
    private String nickname;

    /**
     * 消息类型
     */
    private Byte type;

    /**
     * 消息内容
     */
    private String content;

    public ChatResponseMessage() {
    }

    public ChatResponseMessage(String nickname, Byte type, String content) {
        this.nickname = nickname;
        this.type = type;
        this.content = content;
    }

    public ChatResponseMessage(long messageId) {
        super.setMessageId(messageId);
    }

    public ChatResponseMessage(long messageId, String nickname, Byte type, String content) {
        super.setMessageId(messageId);
        this.nickname = nickname;
        this.type = type;
        this.content = content;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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
        return MessageTypeConstants.ChatResponseMessage;
    }

}
