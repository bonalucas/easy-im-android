package com.esayim.comm.message.register;

import com.esayim.comm.message.Message;
import com.esayim.comm.message.MessageTypeConstants;

/**
 * 注册请求信息
 *
 * @author 单程车票
 */
public class RegisterRequestMessage extends Message {

    /**
     * 账户
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    public RegisterRequestMessage(long messageId) {
        super.setMessageId(messageId);
    }

    public RegisterRequestMessage(long messageId, String username, String password, String nickname, String avatar) {
        super.setMessageId(messageId);
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public Byte getConstant() {
        return MessageTypeConstants.RegisterRequestMessage;
    }

}
