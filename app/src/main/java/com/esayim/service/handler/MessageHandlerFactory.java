package com.esayim.service.handler;

import com.esayim.comm.message.MessageTypeConstants;

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
        handlerMap.put(MessageTypeConstants.TestResponseMessage, new TestMessageServiceHandler());
    }

    /**
     * 根据消息类型获取对应的处理处理器
     *
     * @param messageType 消息累心
     * @return 处理器
     */
    public static BaseMessageHandler getHandler(Byte messageType) {
        return handlerMap.get(messageType);
    }
}
