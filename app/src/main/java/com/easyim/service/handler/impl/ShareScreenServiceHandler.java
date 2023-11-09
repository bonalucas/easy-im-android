package com.easyim.service.handler.impl;

import com.easyim.comm.message.Message;
import com.easyim.comm.message.screen.ShareScreenResponseMessage;
import com.easyim.event.CEventCenter;
import com.easyim.event.Events;
import com.easyim.service.handler.AbstractBaseMessageHandler;

/**
 * 屏幕共享信息处理器
 *
 * @author 单程车票
 */
public class ShareScreenServiceHandler extends AbstractBaseMessageHandler {

    @Override
    public void execute(Message message) {
        if (message instanceof ShareScreenResponseMessage) {
            ShareScreenResponseMessage msg = (ShareScreenResponseMessage) message;
            CEventCenter.dispatchEvent(Events.ShareScreen_RESPONSE, 0, 0, msg);
        }
    }

}
