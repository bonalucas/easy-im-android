package com.esayim.comm.message.login;

import com.esayim.comm.message.Message;
import com.esayim.comm.message.MessageTypeConstants;

/**
 * 登录请求消息类
 *
 * @author 单程车票
 */
public class LoginRequestMessage extends Message {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户密码
     */
    private String userPassword;

    public LoginRequestMessage(long messageId) {
        super.setMessageId(messageId);
    }

    public LoginRequestMessage(long messageId, String userId, String userPassword) {
        super.setMessageId(messageId);
        this.userId = userId;
        this.userPassword = userPassword;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public Byte getConstant() {
        return MessageTypeConstants.LoginRequestMessage;
    }

}
