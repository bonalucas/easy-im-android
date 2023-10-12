package com.esayim.comm.message.friend;

import com.esayim.comm.message.Message;
import com.esayim.comm.message.MessageTypeConstants;
import com.esayim.comm.message.friend.dto.UserDto;

import java.util.List;

/**
 * 查找好友响应消息
 *
 * @author 单程车票
 */
public class SearchFriendResponseMessage extends Message {

    /**
     * 查找好友列表
     */
    private List<UserDto> list;

    public SearchFriendResponseMessage() {
    }

    public SearchFriendResponseMessage(List<UserDto> list) {
        this.list = list;
    }

    public SearchFriendResponseMessage(String messageId, Boolean status) {
        super.setMessageId(messageId);
        super.setStatus(status);
    }

    public SearchFriendResponseMessage(String messageId, Boolean status, List<UserDto> list) {
        super.setMessageId(messageId);
        super.setStatus(status);
        this.list = list;
    }

    public List<UserDto> getList() {
        return list;
    }

    public void setList(List<UserDto> list) {
        this.list = list;
    }

    @Override
    public Byte getConstant() {
        return MessageTypeConstants.SearchFriendResponseMessage;
    }

}
