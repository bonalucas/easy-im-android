package com.esayim.comm.message.test;

import com.esayim.comm.message.Message;
import com.esayim.comm.message.MessageTypeConstants;

public class TestRequestMessage extends Message {

    private String content;

    public TestRequestMessage() {
    }

    public TestRequestMessage(String content) {
        this.content = content;
    }

    public TestRequestMessage(long messageId) {
        super.setMessageId(messageId);
    }

    public TestRequestMessage(long messageId, String content) {
        super.setMessageId(messageId);
        this.content = content;
    }

    @Override
    public Byte getConstant() {
        return MessageTypeConstants.TestRequestMessage;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
