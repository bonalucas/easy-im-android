package com.esayim.comm.message.test;

import com.esayim.comm.message.Message;
import com.esayim.comm.message.MessageTypeConstants;

public class TestResponseMessage extends Message {

    private String content;

    public TestResponseMessage() {
    }

    public TestResponseMessage(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public Byte getConstant() {
        return MessageTypeConstants.TestResponseMessage;
    }
}
