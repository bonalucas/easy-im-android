package com.esayim.service.handler;

import com.esayim.event.CEventCenter;
import com.esayim.event.Events;
import com.esayim.comm.message.Message;
import com.esayim.comm.message.test.TestResponseMessage;

public class TestMessageServiceHandler extends AbstractBaseMessageHandler{

    @Override
    public void execute(Message message) {
        if (message instanceof TestResponseMessage) {
            TestResponseMessage msg = (TestResponseMessage) message;
            CEventCenter.dispatchEvent(Events.CHAT_TEST_MESSAGE, 0, 0, msg.getContent());
        }
    }

}
