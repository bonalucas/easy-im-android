package com.easyim.service.handler.impl;

import com.easyim.comm.message.Message;
import com.easyim.comm.message.screen.ExitScreenResponseMessage;
import com.easyim.event.CEventCenter;
import com.easyim.event.Events;
import com.easyim.service.handler.AbstractBaseMessageHandler;

/**
 * 退出屏幕共享信息处理器
 *
 * @author 单程车票
 */
public class ExitScreenServiceHandler extends AbstractBaseMessageHandler {

    @Override
    public void execute(Message message) {
        if (message instanceof ExitScreenResponseMessage) {
            ExitScreenResponseMessage msg = (ExitScreenResponseMessage) message;
            CEventCenter.dispatchEvent(Events.EXIT_RESPONSE, 0, 0, msg);
        }
    }

}
