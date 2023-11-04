package com.easyim.service.handler.impl;

import com.easyim.comm.message.Message;
import com.easyim.comm.message.meeting.CreateMeetingResponseMessage;
import com.easyim.event.CEventCenter;
import com.easyim.event.Events;
import com.easyim.service.handler.AbstractBaseMessageHandler;

/**
 * 创建会议信息处理器
 *
 * @author 单程车票
 */
public class CreateMeetingServiceHandler extends AbstractBaseMessageHandler {

    @Override
    public void execute(Message message) {
        if (message instanceof CreateMeetingResponseMessage) {
            CreateMeetingResponseMessage msg = (CreateMeetingResponseMessage) message;
            CEventCenter.dispatchEvent(Events.CREATE_MEETING, 0, 0, msg);
        }
    }

}
