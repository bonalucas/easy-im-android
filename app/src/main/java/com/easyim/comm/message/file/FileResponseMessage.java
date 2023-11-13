package com.easyim.comm.message.file;

import com.dyuproject.protostuff.Tag;
import com.easyim.comm.message.Message;
import com.easyim.comm.message.MessageTypeConstants;

/**
 * @author 单程车票
 */
public class FileResponseMessage extends Message {

    /**
     * 文件ID
     */
    @Tag(2)
    private String fileId;

    /**
     * 发送消息者昵称
     */
    @Tag(3)
    private String nickname;

    /**
     * 文件名
     */
    @Tag(4)
    private String fileName;

    /**
     * 文件类型
     */
    @Tag(5)
    private String mimeType;

    /**
     * 分块文件字节数组
     */
    @Tag(6)
    private byte[] file;

    /**
     * 分块序号
     */
    @Tag(7)
    private int chunkNo;

    /**
     * 分块总数
     */
    @Tag(8)
    private int chunkCount;

    public FileResponseMessage() {
    }

    public FileResponseMessage(String fileId, String nickname, String fileName, String mimeType, byte[] file, int chunkNo, int chunkCount) {
        this.fileId = fileId;
        this.nickname = nickname;
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.file = file;
        this.chunkNo = chunkNo;
        this.chunkCount = chunkCount;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
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

    public int getChunkNo() {
        return chunkNo;
    }

    public void setChunkNo(int chunkNo) {
        this.chunkNo = chunkNo;
    }

    public int getChunkCount() {
        return chunkCount;
    }

    public void setChunkCount(int chunkCount) {
        this.chunkCount = chunkCount;
    }

    @Override
    public Byte getConstant() {
        return MessageTypeConstants.FileResponseMessage;
    }

}
