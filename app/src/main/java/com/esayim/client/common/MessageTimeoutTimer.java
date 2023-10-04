package com.esayim.client.common;

import android.util.Log;

import com.esayim.client.NettyClient;
import com.esayim.comm.message.Message;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 消息发送超时器
 *
 * @author 单程车票
 */
public class MessageTimeoutTimer extends Timer {

    /**
     * 客户端
     */
    private final NettyClient nettyClient;

    /**
     * 发送消息
     */
    private final Message message;

    /**
     * 重新发送次数
     */
    private int resendCount;

    /**
     * 超时任务
     */
    private MessageTimeoutTask task;

    public MessageTimeoutTimer(NettyClient nettyClient, Message message) {
        this.nettyClient = nettyClient;
        this.message = message;
        task = new MessageTimeoutTask();
        // 首次调用延长一个重发时长，周期延长一个重发时长
        this.schedule(task, nettyClient.getResendInterval(), nettyClient.getResendInterval());
    }


    /**
     * 消息超时发送任务
     */
    private class MessageTimeoutTask extends TimerTask {

        @Override
        public void run() {
            if (nettyClient.isClosed()) {
                if (nettyClient.getMessageTimeoutTimerManager() != null) {
                    nettyClient.getMessageTimeoutTimerManager().remove(message.getMessageId());
                }
                return;
            }
            resendCount++;
            if (resendCount > nettyClient.getResendCount()) {
                Log.e("MessageTimeoutTimer", "重发消息失败...");
                nettyClient.getMessageTimeoutTimerManager().remove(message.getMessageId());
                nettyClient.reconnect(false);
                resendCount = 0;
            } else {
                // 重新发送消息
                sendMessage();
            }
        }
    }

    public void sendMessage() {
        Log.i("MessageTimeoutTimer", "正在重发消息，message=" + message);
        nettyClient.sendMessage(message, false);
    }

    public Message getMessage() {
        return message;
    }

    @Override
    public void cancel() {
        if (task != null) {
            task.cancel();
            task = null;
        }
        super.cancel();
    }
}
