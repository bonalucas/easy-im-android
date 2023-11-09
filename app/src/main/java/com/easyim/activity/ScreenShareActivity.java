package com.easyim.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.easyim.R;
import com.easyim.rtc.PeerConnectionAdapter;
import com.easyim.rtc.ScreenRecordingService;
import com.easyim.rtc.SdpAdapter;
import com.easyim.rtc.SignalingClient;

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

public class ScreenShareActivity extends AppCompatActivity implements SignalingClient.Callback {

    /**
     * 日志标识
     */
    private static final String TAG = ScreenShareActivity.class.getSimpleName();

    /**
     * 发起者标识
     */
    private boolean isShared;

    /**
     * 麦克风权限请求码
     */
    private static final int RECORD_AUDIO_REQUEST_CODE = 1001;

    /**
     * 屏幕录制权限请求码
     */
    private static final int SCREEN_RECORD_REQUEST_CODE  = 1002;

    /**
     * 连接工厂
     */
    private PeerConnectionFactory peerConnectionFactory;

    /**
     * 连接集合
     */
    private final Map<String, PeerConnection> peerConnectionMap = new HashMap<>();

    /**
     * 媒体流渲染上下文
     */
    private final EglBase.Context eglBaseContext = EglBase.create().getEglBaseContext();

    /**
     * 本地媒体流
     */
    private MediaStream mediaStream;

    /**
     * ICE服务列表
     */
    private List<PeerConnection.IceServer> iceServers;

    /**
     * 媒体流显示控件
     */
    private SurfaceViewRenderer mediaStreamView;

    /**
     * 会议号
     */
    private String meetingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);
        // 初始化
        iceServers = new ArrayList<>();
        iceServers.add(PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer());
        initWebRTC();
        mediaStream = peerConnectionFactory.createLocalMediaStream("mediaStream");
        // 获取界面元素
        ImageButton endShareButton = findViewById(R.id.endShareButton);
        TextView meetingTheme = findViewById(R.id.meetingTitle);
        mediaStreamView = findViewById(R.id.mediaStreamView);
        mediaStreamView.setMirror(false);
        mediaStreamView.init(eglBaseContext, null);
        // 初始化屏幕共享页面
        Intent intent = getIntent();
        meetingId = intent.getStringExtra("meetingId");
        isShared = intent.getBooleanExtra("isShared", false);
        String theme = intent.getStringExtra("theme");
        meetingTheme.setText(theme);

        // 判断是否是发起者从而进行权限授予
        if (isShared) {
            // 获取麦克风权限
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {
                // 请求麦克风权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO_REQUEST_CODE);
            } else {
                // 直接请求屏幕录制权限
                requestScreenRecordPermission();
            }
        } else {
            SignalingClient.getInstance().init(this, meetingId);
        }

        endShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "退出会议并断开信令服务器连接");
                SignalingClient.getInstance().destroy();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RECORD_AUDIO_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 获得麦克风权限后请求屏幕录制权限
                requestScreenRecordPermission();
            } else {
                Toast.makeText(ScreenShareActivity.this, "用户拒绝开启麦克风，无法进行语音聊天", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCREEN_RECORD_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Intent serviceIntent = new Intent(this, ScreenRecordingService.class);
                serviceIntent.putExtra("resultCode", resultCode);
                serviceIntent.putExtra("data", data);
                serviceIntent.putExtra("meetingId", meetingId);
                startForegroundService(serviceIntent);
            } else {
                Toast.makeText(ScreenShareActivity.this, "用户拒绝开启屏幕录制，无法进行屏幕共享", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 请求屏幕录制权限
     */
    private void requestScreenRecordPermission() {
        MediaProjectionManager projectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        Intent permissionIntent = projectionManager.createScreenCaptureIntent();
        startActivityForResult(permissionIntent, SCREEN_RECORD_REQUEST_CODE);
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
                videoTrack.addSink(mediaStreamView);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SignalingClient.getInstance().destroy();
    }

}
