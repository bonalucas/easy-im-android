package com.esayim.comm.message;

import com.esayim.comm.message.chat.ChatRequestMessage;
import com.esayim.comm.message.chat.ChatResponseMessage;
import com.esayim.comm.message.dialog.DeleteDialogRequestMessage;
import com.esayim.comm.message.dialog.DialogNoticeRequestMessage;
import com.esayim.comm.message.dialog.DialogNoticeResponseMessage;
import com.esayim.comm.message.file.FileUploadRequestMessage;
import com.esayim.comm.message.file.FileUploadResponseMessage;
import com.esayim.comm.message.friend.AddFriendRequestMessage;
import com.esayim.comm.message.friend.AddFriendResponseMessage;
import com.esayim.comm.message.friend.SearchFriendRequestMessage;
import com.esayim.comm.message.friend.SearchFriendResponseMessage;
import com.esayim.comm.message.handshake.HandShakeRequestMessage;
import com.esayim.comm.message.handshake.HandShakeResponseMessage;
import com.esayim.comm.message.heartbeat.PingMessage;
import com.esayim.comm.message.heartbeat.PongMessage;
import com.esayim.comm.message.login.LoginRequestMessage;
import com.esayim.comm.message.login.LoginResponseMessage;
import com.esayim.comm.message.reconnect.ReconnectRequestMessage;
import com.esayim.comm.message.register.RegisterRequestMessage;
import com.esayim.comm.message.register.RegisterResponseMessage;
import com.esayim.comm.message.test.TestRequestMessage;
import com.esayim.comm.message.test.TestResponseMessage;

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
        MAP.put(MessageTypeConstants.LoginRequestMessage, LoginRequestMessage.class);
        MAP.put(MessageTypeConstants.LoginResponseMessage, LoginResponseMessage.class);
        MAP.put(MessageTypeConstants.DialogNoticeRequestMessage, DialogNoticeRequestMessage.class);
        MAP.put(MessageTypeConstants.DialogNoticeResponseMessage, DialogNoticeResponseMessage.class);
        MAP.put(MessageTypeConstants.DeleteDialogRequestMessage, DeleteDialogRequestMessage.class);
        MAP.put(MessageTypeConstants.SearchFriendRequestMessage, SearchFriendRequestMessage.class);
        MAP.put(MessageTypeConstants.SearchFriendResponseMessage, SearchFriendResponseMessage.class);
        MAP.put(MessageTypeConstants.AddFriendRequestMessage, AddFriendRequestMessage.class);
        MAP.put(MessageTypeConstants.AddFriendResponseMessage, AddFriendResponseMessage.class);
        MAP.put(MessageTypeConstants.ChatRequestMessage, ChatRequestMessage.class);
        MAP.put(MessageTypeConstants.ChatResponseMessage, ChatResponseMessage.class);
        MAP.put(MessageTypeConstants.FileUploadRequestMessage, FileUploadRequestMessage.class);
        MAP.put(MessageTypeConstants.FileUploadResponseMessage, FileUploadResponseMessage.class);
        MAP.put(MessageTypeConstants.RegisterRequestMessage, RegisterRequestMessage.class);
        MAP.put(MessageTypeConstants.RegisterResponseMessage, RegisterResponseMessage.class);
        MAP.put(MessageTypeConstants.ReconnectRequestMessage, ReconnectRequestMessage.class);
        MAP.put(MessageTypeConstants.TestRequestMessage, TestRequestMessage.class);
        MAP.put(MessageTypeConstants.TestResponseMessage, TestResponseMessage.class);
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
