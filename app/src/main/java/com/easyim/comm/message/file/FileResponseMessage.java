package com.easyim.comm.message.file;

import com.dyuproject.protostuff.Tag;
import com.easyim.comm.message.Message;
import com.easyim.comm.message.MessageTypeConstants;

/**
 * @author 单程车票
 */
public class FileResponseMessage extends Message {

    /**
     * 发送消息者昵称
     */
    @Tag(2)
    private String nickname;

    /**
     * 文件名
     */
    @Tag(3)
    private String fileName;

    /**
     * 文件类型
     */
    @Tag(4)
    private String mimeType;

    /**
     * 文件二进制字节数组
     */
    @Tag(5)
    private byte[] file;

    public FileResponseMessage() {
    }

    public FileResponseMessage(String nickname, String fileName, String mimeType, byte[] file) {
        this.nickname = nickname;
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.file = file;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    @Override
    public Byte getConstant() {
        return MessageTypeConstants.FileResponseMessage;
    }

}
