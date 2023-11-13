package com.easyim.service.handler.impl;

import com.easyim.comm.message.Message;
import com.easyim.comm.message.file.FileResponseMessage;
import com.easyim.event.CEventCenter;
import com.easyim.event.Events;
import com.easyim.service.handler.AbstractBaseMessageHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件信息处理器
 *
 * @author 单程车票
 */
public class FileServiceHandler extends AbstractBaseMessageHandler {

    private final Map<String, byte[][]> fileCache = new HashMap<>();

    @Override
    public void execute(Message message) {
        if (message instanceof FileResponseMessage) {
            FileResponseMessage msg = (FileResponseMessage) message;
            if (fileCache.containsKey(msg.getFileId())) {
                byte[][] file = fileCache.get(msg.getFileId());
                assert file != null;
                file[msg.getChunkNo()] = msg.getFile();
                if (checkFileArray(file)) {
                    // 合并数组
                    byte[] result = mergeByteArray(file);
                    msg.setFile(result);
                    CEventCenter.dispatchEvent(Events.FILE_RESPONSE, 0, 0, msg);
                    fileCache.remove(msg.getFileId());
                } else {
                    fileCache.put(msg.getFileId(), file);
                }
            } else {
                if (msg.getChunkCount() == 1) {
                    CEventCenter.dispatchEvent(Events.FILE_RESPONSE, 0, 0, msg);
                } else {
                    byte[][] file = new byte[msg.getChunkCount()][];
                    file[msg.getChunkNo()] = msg.getFile();
                    fileCache.put(msg.getFileId(), file);
                }
            }
        }
    }

    /**
     * 合并字节数组
     */
    private byte[] mergeByteArray(byte[][] array) {
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            for (byte[] bytes : array) {
                bos.write(bytes);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 检查分块字节数组是否全部接收
     */
    private boolean checkFileArray(byte[][] array) {
        for (byte[] bytes : array) {
            if (bytes == null || bytes.length == 0) return false;
        }
        return true;
    }

}
