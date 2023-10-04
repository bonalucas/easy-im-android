package com.esayim.comm.message.login;

import com.esayim.comm.message.Message;
import com.esayim.comm.message.MessageTypeConstants;
import com.esayim.comm.message.login.dto.DialogDto;
import com.esayim.comm.message.login.dto.FriendDto;
import com.esayim.comm.message.login.dto.GroupDto;

import java.util.List;

/**
 * 登录响应消息类
 *
 * @author 单程车票
 */
public class LoginResponseMessage extends Message {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户昵称
     */
    private String userNickname;

    /**
     * 对话列表
     */
    private List<DialogDto> dialogList;

    /**
     * 群组列表
     */
    private List<GroupDto> groupList;

    /**
     * 好友列表
     */
    private List<FriendDto> friendList;

    @Override
    public Byte getConstant() {
        return MessageTypeConstants.LoginResponseMessage;
    }

    public LoginResponseMessage() {
    }

    public LoginResponseMessage(String userId, String userAvatar, String userNickname, List<DialogDto> dialogList, List<GroupDto> groupList, List<FriendDto> friendList) {
        this.userId = userId;
        this.userAvatar = userAvatar;
        this.userNickname = userNickname;
        this.dialogList = dialogList;
        this.groupList = groupList;
        this.friendList = friendList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public List<DialogDto> getDialogList() {
        return dialogList;
    }

    public void setDialogList(List<DialogDto> dialogList) {
        this.dialogList = dialogList;
    }

    public List<GroupDto> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<GroupDto> groupList) {
        this.groupList = groupList;
    }

    public List<FriendDto> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<FriendDto> friendList) {
        this.friendList = friendList;
    }
}




