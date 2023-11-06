package com.easyim.client.common;

import android.util.Log;

import com.easyim.client.NettyClient;
import com.easyim.comm.message.Message;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息重发管理器
 *
 * @author 单程车票
 */
public class MessageRetransmissionManager {

    private static final String TAG = MessageRetransmissionManager.class.getSimpleName();

    /**
     * 发送的消息记录 Map
     */
    private final Map<Long, MessageTimer> messageTimeoutMap = new ConcurrentHashMap<>();

    /**
     * 客户端
     */
    private final NettyClient nettyClient;

    public MessageRetransmissionManager(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    /**
     * 添加消息到发送超时管理器
     *
     * @param message 消息
     */
    public void add(Message message) {
        Long messageId = message.getMessageId();
        if (!messageTimeoutMap.containsKey(messageId)) {
            MessageTimer timer = new MessageTimer(nettyClient, message);
            messageTimeoutMap.put(messageId, timer);
        }
        Log.i(TAG, "添加消息超发送超时管理器，message=" + message);
    }

    /**
     * 移除已发送消息，停止计时器
     *
     * @param messageId 消息ID
     */
    public void remove(long messageId) {
        if (messageId == 0L && !messageTimeoutMap.containsKey(messageId)) {
            return;
        }
        MessageTimer timer = messageTimeoutMap.remove(messageId);
        if (timer != null) {
            timer.cancel();
        }
        Log.i(TAG, String.format("移除已发送消息 messageId = %s，停止计时器", messageId));
    }

    /**
     * 重连成功回调，重发消息发送超时管理器中所有的消息
     */
    public synchronized void onResetConnected() {
        for (Map.Entry<Long, MessageTimer> entry : messageTimeoutMap.entrySet()) {
            entry.getValue().sendMessage();
        }
    }
}
