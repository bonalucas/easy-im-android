package com.esayim.comm.message.friend.dto;

/**
 * 用户 Dto
 *
 * @author 单程车票
 */
public class UserDto {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户昵称
     */
    private String userNickname;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户状态
     */
    private Integer status;

    public UserDto() {
    }

    public UserDto(String userId, String userNickname, String userAvatar, Integer status) {
        this.userId = userId;
        this.userNickname = userNickname;
        this.userAvatar = userAvatar;
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
