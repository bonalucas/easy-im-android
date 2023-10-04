package com.esayim.client.common;

import android.util.Log;

import com.esayim.client.NettyClient;
import com.esayim.comm.message.Message;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.util.internal.StringUtil;

/**
 * 消息超时重发管理器
 *
 * @author 单程车票
 */
public class MessageTimeoutTimerManager {

    /**
     * 消息超时重发 Map
     */
    private final Map<String, MessageTimeoutTimer> messageTimeoutMap = new ConcurrentHashMap<>();

    /**
     * 客户端
     */
    private final NettyClient nettyClient;

    public MessageTimeoutTimerManager(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    /**
     * 添加消息到发送超时管理器
     *
     * @param message 消息
     */
    public void add(Message message) {
        String messageId = message.getMessageId();
        if (!messageTimeoutMap.containsKey(messageId)) {
            MessageTimeoutTimer timer = new MessageTimeoutTimer(nettyClient, message);
            messageTimeoutMap.put(messageId, timer);
        }
        Log.i("MessageTimeoutTimerManager", "添加消息超发送超时管理器，message=" + message);
    }

    /**
     * 移除已发送消息，停止计时器
     *
     * @param messageId 消息ID
     */
    public void remove(String messageId) {
        if (StringUtil.isNullOrEmpty(messageId)) {
            return;
        }
        MessageTimeoutTimer timer = messageTimeoutMap.remove(messageId);
        if (timer != null) {
            timer.cancel();
        }
        Log.i("MessageTimeoutTimerManager", "移除已发送消息，停止计时器");
    }

    /**
     * 重连成功回调，重发消息发送超时管理器中所有的消息
     */
    public synchronized void onResetConnected() {
        for (Map.Entry<String, MessageTimeoutTimer> entry : messageTimeoutMap.entrySet()) {
            entry.getValue().sendMessage();
        }
    }
}
