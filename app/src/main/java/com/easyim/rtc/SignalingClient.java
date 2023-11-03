package com.easyim.rtc;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.IceCandidate;
import org.webrtc.SessionDescription;

import java.net.URISyntaxException;
import java.util.Arrays;

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * 信令客户端
 *
 * @author 单程车票
 */
public class SignalingClient {

    /**
     * 日志标签
     */
    private static final String TAG = SignalingClient.class.getSimpleName();

    /**
     * 实例
     */
    private static volatile SignalingClient instance;

    /**
     * WebSocket服务器地址
     */
    private static final String socketUrl = "http://100.2.135.237:9075";

    /**
     * 房间名
     */
    private static final String room = "EasyIM";

    /**
     * 客户端套接字（用于通信）
     */
    private Socket socket;

    /**
     * 采用双重锁检查方式获取单例
     */
    public static SignalingClient getInstance() {
        if (null == instance) {
            synchronized (SignalingClient.class) {
                if (null == instance) {
                    instance = new SignalingClient();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化
     */
    public void init(Callback callback) {
        try {
            // 创建 WebSocket 连接并连接
            socket = IO.socket(socketUrl);

            socket.on("connect_error", error -> {
                Log.e(TAG, String.format("连接失败: %s", error));
            });

            socket.on("connect", arg -> Log.d(TAG, "与信令服务器连接成功"));

            socket.connect();

            socket.emit("create or join", room);

            socket.on("created", args -> {
                Log.d(TAG, String.format("房间建立成功 【socketID: %s】", socket.id()));
            });
            socket.on("full", args -> {
                Log.e(TAG, String.format("超载加入失败 【socketID: %s】", socket.id()));
            });
            socket.on("join", args -> {
                Log.d(TAG, String.format("新的客户端加入 【newPeer: %s】", Arrays.toString(args)));
                callback.onPeerJoined(String.valueOf(args[1]));
            });
            socket.on("joined", args -> {
                Log.d(TAG, String.format("客户端加入成功 【socketID: %s】", socket.id()));
            });
            socket.on("log", args -> {
                Log.d(TAG, String.format("日志回调 【log: %s】", Arrays.toString(args)));
            });
            socket.on("bye", args -> {
                Log.d(TAG, String.format("服务器断开连接 【bye: %s】", Arrays.toString(args)));
            });

            socket.on("message", args -> {
                Log.d(TAG, String.format("消息 【message: %s】", Arrays.toString(args)));
                Object arg = args[0];
                if(arg instanceof JSONObject) {
                    JSONObject data = (JSONObject) arg;
                    String type = data.optString("type");
                    switch (type) {
                        case "offer":
                            callback.onOfferReceived(data);
                            break;
                        case "answer":
                            callback.onAnswerReceived(data);
                            break;
                        case "candidate":
                            callback.onIceCandidateReceived(data);
                            break;
                    }
                }
            });
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭客户端
     */
    public void destroy() {
        socket.emit("bye", socket.id());
        socket.disconnect();
        socket.close();
        instance = null;
    }

    /**
     * 发送 ICE 候选信息
     */
    public void sendIceCandidate(IceCandidate iceCandidate, String to) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "candidate");
            jsonObject.put("label", iceCandidate.sdpMLineIndex);
            jsonObject.put("id", iceCandidate.sdpMid);
            jsonObject.put("candidate", iceCandidate.sdp);
            jsonObject.put("from", socket.id());
            jsonObject.put("to", to);
            socket.emit("message", jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送 SDP 信息
     */
    public void sendSessionDescription(SessionDescription sdp, String to) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", sdp.type.canonicalForm());
            jsonObject.put("sdp", sdp.description);
            jsonObject.put("from", socket.id());
            jsonObject.put("to", to);
            socket.emit("message", jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 回调接口
     */
    public interface Callback {

        void onPeerJoined(String socketId);

        void onOfferReceived(JSONObject data);

        void onAnswerReceived(JSONObject data);

        void onIceCandidateReceived(JSONObject data);

    }

}
