package com.easyim.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.easyim.R;
import com.easyim.comm.message.screen.ExitScreenResponseMessage;
import com.easyim.event.CEventCenter;
import com.easyim.event.Events;
import com.easyim.event.I_CEventListener;
import com.easyim.rtc.PeerConnectionAdapter;
import com.easyim.rtc.SdpAdapter;
import com.easyim.rtc.SignalingClient;
import com.easyim.service.ServiceThreadPoolExecutor;

import org.json.JSONObject;
import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.EglBase;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SessionDescription;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoTrack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 屏幕共享活动
 *
 * @author 单程车票
 */
public class ScreenShareActivity extends AppCompatActivity implements SignalingClient.Callback, I_CEventListener {

    /**
     * 连接工厂
     */
    private PeerConnectionFactory peerConnectionFactory;

    /**
     * 媒体流渲染上下文
     */
    private final EglBase.Context eglBaseContext = EglBase.create().getEglBaseContext();

    /**
     * ICE服务列表
     */
    private List<PeerConnection.IceServer> iceServers;

    /**
     * 媒体流显示控件
     */
    private SurfaceViewRenderer mediaStreamView;

    /**
     * 连接集合
     */
    private final Map<String, PeerConnection> peerConnectionMap = new HashMap<>();

    /**
     * 监听事件
     */
    private final String[] interest = { Events.EXIT_RESPONSE };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);
        // 初始化
        iceServers = new ArrayList<>();
        iceServers.add(PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer());
        initWebRTC();
        // 获取界面元素
        mediaStreamView = findViewById(R.id.mediaStreamView);
        mediaStreamView.setMirror(false);
        mediaStreamView.init(eglBaseContext, null);
        // 初始化屏幕共享页面
        Intent intent = getIntent();
        String meetingId = intent.getStringExtra("meetingId");
        // 注册监听事件
        CEventCenter.onBindEvent(true, this, interest);
        // 连接信令服务器
        SignalingClient.getInstance().init(this, meetingId);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // 退出屏幕共享逻辑
        shutdown();
    }

    @Override
    public void onCEvent(String topic, int msgCode, int resultCode, Object obj) {
        if (Events.EXIT_RESPONSE.equals(topic)) {
            if (obj instanceof ExitScreenResponseMessage) {
                ExitScreenResponseMessage msg = (ExitScreenResponseMessage) obj;
                ServiceThreadPoolExecutor.runOnMainThread(() -> Toast.makeText(ScreenShareActivity.this, msg.getNickname() + " 结束屏幕共享", Toast.LENGTH_SHORT).show());
                shutdown();
            }
        }
    }

    /**
     * 退出屏幕共享
     */
    private void shutdown() {
        // 注销监听事件
        CEventCenter.onBindEvent(false, this, interest);
        // 断开信令服务器连接
        SignalingClient.getInstance().destroy();
        // 释放 SurfaceViewRenderer 资源
        if (mediaStreamView != null) {
            mediaStreamView.release();
            mediaStreamView = null;
        }
        // 结束页面返回会议页面
        finish();
    }

    /**
     * 初始化 WebRTC
     */
    private void initWebRTC() {
        PeerConnectionFactory.initialize(PeerConnectionFactory.InitializationOptions.builder(this).createInitializationOptions());
        PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();
        DefaultVideoEncoderFactory defaultVideoEncoderFactory = new DefaultVideoEncoderFactory(eglBaseContext, true, true);
        DefaultVideoDecoderFactory defaultVideoDecoderFactory = new DefaultVideoDecoderFactory(eglBaseContext);
        peerConnectionFactory = PeerConnectionFactory.builder()
                .setOptions(options)
                .setVideoEncoderFactory(defaultVideoEncoderFactory)
                .setVideoDecoderFactory(defaultVideoDecoderFactory)
                .createPeerConnectionFactory();
    }

    /**
     * 获取 PeerConnection 对象
     */
    private synchronized PeerConnection getOrCreatePeerConnection(String socketId) {
        PeerConnection peerConnection = peerConnectionMap.get(socketId);
        if(peerConnection != null) {
            return peerConnection;
        }
        peerConnection = peerConnectionFactory.createPeerConnection(iceServers, new PeerConnectionAdapter(socketId) {
            @Override
            public void onIceCandidate(IceCandidate iceCandidate) {
                super.onIceCandidate(iceCandidate);
                SignalingClient.getInstance().sendIceCandidate(iceCandidate, socketId);
            }
            @Override
            public void onAddStream(MediaStream mediaStream) {
                super.onAddStream(mediaStream);
                // 显示远端屏幕视频流
                VideoTrack videoTrack = mediaStream.videoTracks.get(0);
                runOnUiThread(() -> videoTrack.addSink(mediaStreamView));
            }
        });
        peerConnectionMap.put(socketId, peerConnection);
        return peerConnection;
    }

    @Override
    public void onPeerJoined(String socketId) {
        PeerConnection peerConnection = getOrCreatePeerConnection(socketId);
        peerConnection.createOffer(new SdpAdapter("createOfferSdp:" + socketId) {
            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {
                super.onCreateSuccess(sessionDescription);
                peerConnection.setLocalDescription(new SdpAdapter("setLocalSdp:" + socketId), sessionDescription);
                SignalingClient.getInstance().sendSessionDescription(sessionDescription, socketId);
            }
        }, new MediaConstraints());
    }

    @Override
    public void onOfferReceived(JSONObject data) {
        runOnUiThread(() -> {
            final String socketId = data.optString("from");
            PeerConnection peerConnection = getOrCreatePeerConnection(socketId);
            peerConnection.setRemoteDescription(new SdpAdapter("setRemoteSdp:" + socketId),
                    new SessionDescription(SessionDescription.Type.OFFER, data.optString("sdp")));
            peerConnection.createAnswer(new SdpAdapter("localAnswerSdp") {
                @Override
                public void onCreateSuccess(SessionDescription sdp) {
                    super.onCreateSuccess(sdp);
                    peerConnectionMap.get(socketId).setLocalDescription(new SdpAdapter("setLocalSdp:" + socketId), sdp);
                    SignalingClient.getInstance().sendSessionDescription(sdp, socketId);
                }
            }, new MediaConstraints());
        });
    }

    @Override
    public void onAnswerReceived(JSONObject data) {
        String socketId = data.optString("from");
        PeerConnection peerConnection = getOrCreatePeerConnection(socketId);
        peerConnection.setRemoteDescription(new SdpAdapter("setRemoteSdp:" + socketId),
                new SessionDescription(SessionDescription.Type.ANSWER, data.optString("sdp")));
    }

    @Override
    public void onIceCandidateReceived(JSONObject data) {
        String socketId = data.optString("from");
        PeerConnection peerConnection = getOrCreatePeerConnection(socketId);
        peerConnection.addIceCandidate(new IceCandidate(
                data.optString("id"),
                data.optInt("label"),
                data.optString("candidate")
        ));
    }

}
