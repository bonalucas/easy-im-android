package com.easyim.comm.message.file;

import com.dyuproject.protostuff.Tag;
import com.easyim.comm.message.Message;
import com.easyim.comm.message.MessageTypeConstants;

/**
 * 文件请求消息
 *
 * @author 单程车票
 */
public class FileRequestMessage extends Message {

    /**
     * 文件名
     */
    @Tag(2)
    private String fileName;

    /**
     * 文件类型
     */
    @Tag(3)
    private String mimeType;

    /**
     * 文件二进制字节数组
     */
    @Tag(4)
    private byte[] file;

    public FileRequestMessage() {
    }

    public FileRequestMessage(String fileName, String mimeType, byte[] file) {
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.file = file;
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
        return MessageTypeConstants.FileRequestMessage;
    }

}
