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

    public FileUploadResponseMessage() {
    }

    public FileUploadResponseMessage(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public FileUploadResponseMessage(String messageId, Boolean status) {
        super.setMessageId(messageId);
        super.setStatus(status);
    }

    public FileUploadResponseMessage(String messageId, Boolean status, String fileUrl) {
        super.setMessageId(messageId);
        super.setStatus(status);
        this.fileUrl = fileUrl;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @Override
    public Byte getConstant() {
        return MessageTypeConstants.FileUploadResponseMessage;
    }

}
