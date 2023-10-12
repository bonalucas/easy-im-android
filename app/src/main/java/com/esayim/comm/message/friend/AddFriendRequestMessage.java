package com.esayim.comm.message.friend;

import com.esayim.comm.message.Message;
import com.esayim.comm.message.MessageTypeConstants;

/**
 * 添加好友请求消息
 *
 * @author 单程车票
 */
public class AddFriendRequestMessage extends Message {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 添加好友ID
     */
    private String friendId;

    public AddFriendRequestMessage() {
    }

    public AddFriendRequestMessage(String userId, String friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }

    public AddFriendRequestMessage(String messageId, Boolean status) {
        super.setMessageId(messageId);
        super.setStatus(status);
    }

    public AddFriendRequestMessage(String messageId, Boolean status, String userId, String friendId) {
        super.setMessageId(messageId);
        super.setStatus(status);
        this.userId = userId;
        this.friendId = friendId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    @Override
    public Byte getConstant() {
        return MessageTypeConstants.AddFriendRequestMessage;
    }

}
