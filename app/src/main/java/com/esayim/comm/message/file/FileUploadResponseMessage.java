package com.esayim.comm.message.file;

import com.esayim.comm.message.Message;
import com.esayim.comm.message.MessageTypeConstants;

/**
 * 文件上传响应消息
 *
 * @author 单程车票
 */
public class FileUploadResponseMessage extends Message {

    /**
     * 文件访问地址
     */
    private String fileUrl;

    @Override
    public Byte getConstant() {
        return MessageTypeConstants.FileUploadResponseMessage;
    }

    public FileUploadResponseMessage() {
    }

    public FileUploadResponseMessage(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
