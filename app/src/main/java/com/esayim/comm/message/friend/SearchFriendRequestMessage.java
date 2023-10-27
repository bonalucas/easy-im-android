package com.esayim.comm.message.friend;

import com.esayim.comm.message.Message;
import com.esayim.comm.message.MessageTypeConstants;

/**
 * 查找好友请求消息
 *
 * @author 单程车票
 */
public class SearchFriendRequestMessage extends Message {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 搜索字段
     */
    private String searchKey;

    public SearchFriendRequestMessage(long messageId) {
        super.setMessageId(messageId);
    }

    public SearchFriendRequestMessage(long messageId, String userId, String searchKey) {
        super.setMessageId(messageId);
        this.userId = userId;
        this.searchKey = searchKey;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    @Override
    public Byte getConstant() {
        return MessageTypeConstants.SearchFriendRequestMessage;
    }

}
