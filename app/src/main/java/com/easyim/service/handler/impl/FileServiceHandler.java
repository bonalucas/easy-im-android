package com.easyim.service.handler.impl;

import com.easyim.comm.message.Message;
import com.easyim.comm.message.file.FileResponseMessage;
import com.easyim.event.CEventCenter;
import com.easyim.event.Events;
import com.easyim.service.handler.AbstractBaseMessageHandler;

/**
 * 文件信息处理器
 *
 * @author 单程车票
 */
public class FileServiceHandler extends AbstractBaseMessageHandler {

    @Override
    public void execute(Message message) {
        if (message instanceof FileResponseMessage) {
            FileResponseMessage msg = (FileResponseMessage) message;
            CEventCenter.dispatchEvent(Events.FILE_RESPONSE, 0, 0, msg);
        }
    }

}
