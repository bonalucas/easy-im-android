package com.esayim.client.common;

import android.util.Log;

import com.esayim.client.NettyClient;
import com.esayim.comm.message.Message;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 消息发送定时器
 *
 * @author 单程车票
 */
public class MessageTimer extends Timer {

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
    private MessageTimeTask task;

    public MessageTimer(NettyClient nettyClient, Message message) {
        this.nettyClient = nettyClient;
        this.message = message;
        this.resendCount = 0;
        task = new MessageTimeTask();
        // 首次调用延长一个重发时长，周期延长一个重发时长
        this.schedule(task, nettyClient.getResendInterval(), nettyClient.getResendInterval());
    }


    /**
     * 消息超时发送任务
     */
    private class MessageTimeTask extends TimerTask {

        @Override
        public void run() {
            if (nettyClient.isClosed()) {
                if (nettyClient.getMessageRetransmissionManager() != null) {
                    nettyClient.getMessageRetransmissionManager().remove(message.getMessageId());
                }
                return;
            }
            resendCount++;
            if (resendCount > nettyClient.getResendCount()) {
                Log.e("MessageTimer", "重发消息失败...");
                nettyClient.getMessageRetransmissionManager().remove(message.getMessageId());
                nettyClient.reconnect(false);
                resendCount = 0;
            } else {
                // 重新发送消息
                sendMessage();
            }
        }
    }

    public void sendMessage() {
        Log.i(MessageTimer.class.getSimpleName(), "正在重发消息，message=" + message);
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
