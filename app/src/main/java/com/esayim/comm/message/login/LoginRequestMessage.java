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

    public LoginRequestMessage() {
    }

    public LoginRequestMessage(String userId, String userPassword) {
        this.userId = userId;
        this.userPassword = userPassword;
    }

    public LoginRequestMessage(String messageId, Boolean status) {
        super.setMessageId(messageId);
        super.setStatus(status);
    }

    public LoginRequestMessage(String messageId, Boolean status, String userId, String userPassword) {
        super.setMessageId(messageId);
        super.setStatus(status);
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
