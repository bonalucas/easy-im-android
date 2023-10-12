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
import com.esayim.comm.message.heartbeat.HeartBeatRequestMessage;
import com.esayim.comm.message.heartbeat.HeartBeatResponseMessage;
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
     * 消息ID
     */
    private String messageId;

    /**
     * 响应状态
     */
    private Boolean status;

    /**
     * 消息类型 MAP
     */
    private final static Map<Byte, Class<? extends Message>> messageType = new ConcurrentHashMap<>();

    static {
        messageType.put(MessageTypeConstants.LoginRequestMessage, LoginRequestMessage.class);
        messageType.put(MessageTypeConstants.LoginResponseMessage, LoginResponseMessage.class);
        messageType.put(MessageTypeConstants.DialogNoticeRequestMessage, DialogNoticeRequestMessage.class);
        messageType.put(MessageTypeConstants.DialogNoticeResponseMessage, DialogNoticeResponseMessage.class);
        messageType.put(MessageTypeConstants.DeleteDialogRequestMessage, DeleteDialogRequestMessage.class);
        messageType.put(MessageTypeConstants.SearchFriendRequestMessage, SearchFriendRequestMessage.class);
        messageType.put(MessageTypeConstants.SearchFriendResponseMessage, SearchFriendResponseMessage.class);
        messageType.put(MessageTypeConstants.AddFriendRequestMessage, AddFriendRequestMessage.class);
        messageType.put(MessageTypeConstants.AddFriendResponseMessage, AddFriendResponseMessage.class);
        messageType.put(MessageTypeConstants.ChatRequestMessage, ChatRequestMessage.class);
        messageType.put(MessageTypeConstants.ChatResponseMessage, ChatResponseMessage.class);
        messageType.put(MessageTypeConstants.FileUploadRequestMessage, FileUploadRequestMessage.class);
        messageType.put(MessageTypeConstants.FileUploadResponseMessage, FileUploadResponseMessage.class);
        messageType.put(MessageTypeConstants.RegisterRequestMessage, RegisterRequestMessage.class);
        messageType.put(MessageTypeConstants.RegisterResponseMessage, RegisterResponseMessage.class);
        messageType.put(MessageTypeConstants.ReconnectRequestMessage, ReconnectRequestMessage.class);
        messageType.put(MessageTypeConstants.HeartBeatRequestMessage, HeartBeatRequestMessage.class);
        messageType.put(MessageTypeConstants.HeartBeatResponseMessage, HeartBeatResponseMessage.class);
        messageType.put(MessageTypeConstants.TestRequestMessage, TestRequestMessage.class);
        messageType.put(MessageTypeConstants.TestResponseMessage, TestResponseMessage.class);
    }

    public static Class<? extends Message> get(Byte constant) {
        return messageType.get(constant);
    }

    /**
     * 获取消息类型常量
     *
     * @return 返回消息类型常量
     */
    public abstract Byte getConstant();

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}
