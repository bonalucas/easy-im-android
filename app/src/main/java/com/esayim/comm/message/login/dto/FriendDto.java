package com.esayim.comm.message.login.dto;

/**
 * 好友 Dto
 *
 * @author 单程车票
 */
public class FriendDto {

    /**
     * 好友用户ID
     */
    private String friendId;

    /**
     * 好友名称
     */
    private String friendName;

    /**
     * 好友头像
     */
    private String friendAvatar;

    public FriendDto() {
    }

    public FriendDto(String friendId, String friendName, String friendAvatar) {
        this.friendId = friendId;
        this.friendName = friendName;
        this.friendAvatar = friendAvatar;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendAvatar() {
        return friendAvatar;
    }

    public void setFriendAvatar(String friendAvatar) {
        this.friendAvatar = friendAvatar;
    }
}
