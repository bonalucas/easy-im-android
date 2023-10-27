package com.esayim.comm.message.friend;

import com.esayim.comm.message.Message;
import com.esayim.comm.message.MessageTypeConstants;

/**
 * 添加好友响应消息
 *
 * @author 单程车票
 */
public class AddFriendResponseMessage extends Message {

    /**
     * 添加好友ID
     */
    private String friendId;

    /**
     * 添加好友昵称
     */
    private String friendNickname;

    /**
     * 添加好友头像
     */
    private String friendAvatar;

    public AddFriendResponseMessage(long messageId) {
        super.setMessageId(messageId);
    }

    public AddFriendResponseMessage(long messageId, String friendId, String friendNickname, String friendAvatar) {
        super.setMessageId(messageId);
        this.friendId = friendId;
        this.friendNickname = friendNickname;
        this.friendAvatar = friendAvatar;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getFriendNickname() {
        return friendNickname;
    }

    public void setFriendNickname(String friendNickname) {
        this.friendNickname = friendNickname;
    }

    public String getFriendAvatar() {
        return friendAvatar;
    }

    public void setFriendAvatar(String friendAvatar) {
        this.friendAvatar = friendAvatar;
    }

    @Override
    public Byte getConstant() {
        return MessageTypeConstants.AddFriendResponseMessage;
    }

}
