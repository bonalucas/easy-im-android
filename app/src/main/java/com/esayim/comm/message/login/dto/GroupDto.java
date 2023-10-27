package com.esayim.comm.message.login.dto;

/**
 * 群组 Dto
 *
 * @author 单程车票
 */
public class GroupDto {

    /**
     * 群组ID
     */
    private String groupId;

    /**
     * 群组名称
     */
    private String groupName;

    /**
     * 群组头像
     */
    private String groupAvatar;

    public GroupDto() {
    }

    public GroupDto(String groupId, String groupName, String groupAvatar) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupAvatar = groupAvatar;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupAvatar() {
        return groupAvatar;
    }

    public void setGroupAvatar(String groupAvatar) {
        this.groupAvatar = groupAvatar;
    }

}
