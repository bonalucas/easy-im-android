package com.easyim.service.handler;

import com.easyim.comm.message.MessageTypeConstants;
import com.easyim.service.handler.impl.CreateMeetingServiceHandler;
import com.easyim.service.handler.impl.ErrorMessageServiceHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息处理器工厂
 *
 * @author 单程车票
 */
public class MessageHandlerFactory {

    private static final Map<Byte, BaseMessageHandler> handlerMap = new ConcurrentHashMap<>();

    static {
        handlerMap.put(MessageTypeConstants.ErrorResponseMessage, new ErrorMessageServiceHandler());
        handlerMap.put(MessageTypeConstants.MeetingCreateResponseMessage, new CreateMeetingServiceHandler());
    }

    /**
     * 根据消息类型获取对应的处理处理器
     *
     * @param messageType 消息类型
     * @return 处理器
     */
    public static BaseMessageHandler getHandler(Byte messageType) {
        return handlerMap.get(messageType);
    }
}
