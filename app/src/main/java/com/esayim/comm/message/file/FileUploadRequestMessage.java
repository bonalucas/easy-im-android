package com.esayim.comm.message.file;

import com.esayim.comm.message.Message;
import com.esayim.comm.message.MessageTypeConstants;

/**
 * 文件上传请求消息
 *
 * @author 单程车票
 */
public class FileUploadRequestMessage extends Message {

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件大小
     */
    private long fileSize;

    /**
     * 文件内容
     */
    private byte[] fileContent;

    @Override
    public Byte getConstant() {
        return MessageTypeConstants.FileUploadRequestMessage;
    }

    public FileUploadRequestMessage() {
    }

    public FileUploadRequestMessage(String fileName, long fileSize, byte[] fileContent) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileContent = fileContent;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }
}
