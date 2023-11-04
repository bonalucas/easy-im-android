package com.easyim.service.handler.impl;

import com.easyim.comm.message.Message;
import com.easyim.comm.message.meeting.JoinMeetingResponseMessage;
import com.easyim.event.CEventCenter;
import com.easyim.event.Events;
import com.easyim.service.handler.AbstractBaseMessageHandler;

/**
 * 加入会议信息处理器
 *
 * @author 单程车票
 */
public class JoinMeetingServiceHandler extends AbstractBaseMessageHandler {

    @Override
    public void execute(Message message) {
        if (message instanceof JoinMeetingResponseMessage) {
            JoinMeetingResponseMessage msg = (JoinMeetingResponseMessage) message;
            CEventCenter.dispatchEvent(Events.JOIN_MEETING, 0, 0, msg);
        }
    }

}
