package com.easyim.service.handler.impl;

import com.easyim.comm.message.Message;
import com.easyim.comm.message.meeting.LeaveMeetingResponseMessage;
import com.easyim.event.CEventCenter;
import com.easyim.event.Events;
import com.easyim.service.handler.AbstractBaseMessageHandler;

/**
 * 退出会议信息处理器
 *
 * @author 单程车票
 */
public class LeaveMeetingServiceHandler extends AbstractBaseMessageHandler {

    @Override
    public void execute(Message message) {
        if (message instanceof LeaveMeetingResponseMessage) {
            LeaveMeetingResponseMessage msg = (LeaveMeetingResponseMessage) message;
            CEventCenter.dispatchEvent(Events.LEAVE_RESPONSE, 0, 0, msg);
        }
    }

}
