package com.easyim.service;

import android.util.Log;

import com.easyim.client.NettyClient;
import com.easyim.comm.message.Message;
import com.easyim.service.handler.BaseMessageHandler;
import com.easyim.service.handler.MessageHandlerFactory;

/**
 * 消息处理器
 *
 * @author 单程车票
 */
public class MessageProcessor {

    private static final String TAG = MessageProcessor.class.getSimpleName();

    private static volatile MessageProcessor instance;

    /**
     * 双重检查锁单例模式实例化
     *
     * @return 单例实例
     */
    public static MessageProcessor getInstance() {
        if (instance == null) {
            synchronized (MessageProcessor.class) {
                if (instance == null) {
                    instance = new MessageProcessor();
                }
            }
        }
        return instance;
    }

    /**
     * 接收信息
     *
     * @param message 信息
     */
    public void receiveMessage(Message message) {
        ServiceThreadPoolExecutor.runInBackground(() -> {
            try {
                BaseMessageHandler messageHandler = MessageHandlerFactory.getHandler(message.getConstant());
                if (messageHandler != null) {
                    messageHandler.execute(message);
                } else {
                    Log.e(TAG, "未找到消息处理handler，msgType=" + message.getConstant());
                }
            } catch (Exception e) {
                Log.e(TAG, "消息处理出错，reason=" + e.getMessage());
            }
        });
    }

    /**
     * 发送消息
     *
     * @param message 消息
     */
    public void sendMessage(Message message) {
        ServiceThreadPoolExecutor.runInBackground(() -> {
            NettyClient nettyClient = NettyClient.getInstance();
            if (!nettyClient.isClosed()) {
                nettyClient.sendMessage(message, true);
            } else {
                Log.e(TAG, "发送消息失败");
            }
        });
    }
}
