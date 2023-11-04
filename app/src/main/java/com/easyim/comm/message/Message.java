package com.easyim.comm.message;

import com.easyim.comm.message.error.ErrorResponseMessage;
import com.easyim.comm.message.handshake.HandShakeRequestMessage;
import com.easyim.comm.message.handshake.HandShakeResponseMessage;
import com.easyim.comm.message.heartbeat.PingMessage;
import com.easyim.comm.message.heartbeat.PongMessage;
import com.easyim.comm.message.meeting.CreateMeetingRequestMessage;
import com.easyim.comm.message.meeting.CreateMeetingResponseMessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息基础类
 *
 * @author 单程车票
 */
public abstract class Message {

    /**
     * 消息全局ID
     */
    private Long messageId;

    /**
     * 消息引用
     */
    public static final Map<Byte, Class<? extends Message>> MAP = new ConcurrentHashMap<>();

    static {
        // 握手消息
        MAP.put(MessageTypeConstants.HandShakeRequestMessage, HandShakeRequestMessage.class);
        MAP.put(MessageTypeConstants.HandShakeResponseMessage, HandShakeResponseMessage.class);
        // 心跳消息
        MAP.put(MessageTypeConstants.PingMessage, PingMessage.class);
        MAP.put(MessageTypeConstants.PongMessage, PongMessage.class);
        // 业务消息
        MAP.put(MessageTypeConstants.ErrorResponseMessage, ErrorResponseMessage.class);
        MAP.put(MessageTypeConstants.CreateMeetingRequestMessage, CreateMeetingRequestMessage.class);
        MAP.put(MessageTypeConstants.CreateMeetingResponseMessage, CreateMeetingResponseMessage.class);
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    /**
     * 获取消息clazz
     */
    public static Class<? extends Message> get(Byte constant) {
        return MAP.get(constant);
    }

    /**
     * 获取消息类型常量
     */
    public abstract Byte getConstant();

}
