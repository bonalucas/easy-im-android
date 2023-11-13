package com.easyim.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.projection.MediaProjection;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.easyim.R;
import com.easyim.comm.message.screen.ShareScreenRequestMessage;
import com.easyim.rtc.PeerConnectionAdapter;
import com.easyim.rtc.SdpAdapter;
import com.easyim.rtc.SignalingClient;
import com.easyim.service.MessageProcessor;
import com.easyim.service.ServiceThreadPoolExecutor;

import org.json.JSONObject;
import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.EglBase;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.ScreenCapturerAndroid;
import org.webrtc.SessionDescription;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 屏幕录制服务类
 *
 * @author 单程车票
 */
public class ScreenRecordingService extends Service implements SignalingClient.Callback {

    private static final int NOTIFICATION_ID = 1;

    private static final String NOTIFICATION_CHANNEL_ID = "screen_recording_channel";

    public static final String ACTION_STOP_STREAM = "ACTION_STOP_STREAM";

    private final EglBase.Context eglBaseContext = EglBase.create().getEglBaseContext();

    private PeerConnectionFactory peerConnectionFactory;

    private List<PeerConnection.IceServer> iceServers;

    private MediaStream mediaStream;

    private final Map<String, PeerConnection> peerConnectionMap = new HashMap<>();

    private final BroadcastReceiver streamControlReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null) {
                if (ACTION_STOP_STREAM.equals(intent.getAction())) {
                    shutdown();
                }
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        // 注册广播接收器
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_STOP_STREAM);
        registerReceiver(streamControlReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 创建通知
        createNotificationChannel();
        Notification notification = createNotification();
        startForeground(NOTIFICATION_ID, notification);

        // 初始化
        iceServers = new ArrayList<>();
        iceServers.add(PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer());
        initWebRTC();
        mediaStream = peerConnectionFactory.createLocalMediaStream("mediaStream");

        // 获取传递元素
        Intent data = intent.getParcelableExtra("data");
        String meetingId = intent.getStringExtra("meetingId");

        // 加入视频流
        VideoCapturer videoCapturer = new ScreenCapturerAndroid(data, new MediaProjection.Callback() {
            @Override
            public void onStop() {
                super.onStop();
            }
        });
        SurfaceTextureHelper surfaceTextureHelper = SurfaceTextureHelper.create("CaptureThread", eglBaseContext);
        VideoSource videoSource = peerConnectionFactory.createVideoSource(videoCapturer.isScreencast());
        videoCapturer.initialize(surfaceTextureHelper, getApplicationContext(),videoSource.getCapturerObserver());
        videoCapturer.startCapture(1280, 720, 30);
        VideoTrack videoTrack = peerConnectionFactory.createVideoTrack("videoRecord", videoSource);
        mediaStream.addTrack(videoTrack);

        // 加入音频流
        AudioSource audioSource = peerConnectionFactory.createAudioSource(new MediaConstraints());
        AudioTrack audioTrack = peerConnectionFactory.createAudioTrack("audioRecord", audioSource);
        mediaStream.addTrack(audioTrack);

        // 启动 WebRTC
        SignalingClient.getInstance().init(this, meetingId);

        // 通知会议内其他人接收屏幕共享
        MessageProcessor.getInstance().sendMessage(new ShareScreenRequestMessage());

        return START_STICKY;
    }

    private void shutdown() {
        // 断开信令服务器连接
        SignalingClient.getInstance().destroy();
        // 关闭媒体流
        if (mediaStream != null) {
            mediaStream.dispose();
            mediaStream = null;
        }
        // 停止前台服务
        stopForeground(true);
        // 停止服务本身
        stopSelf();
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
        });
        if (peerConnection != null) peerConnection.addStream(mediaStream);
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
        ServiceThreadPoolExecutor.runInBackground(() -> {
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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        // 在服务销毁时取消注册广播接收器
        unregisterReceiver(streamControlReceiver);
        super.onDestroy();
    }

    private void createNotificationChannel() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "Screen Recording Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Channel for screen recording");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private Notification createNotification() {
        Intent notificationIntent = new Intent(this, ScreenShareActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        return new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle("Screen Recording")
                .setContentText("Recording your screen")
                .setSmallIcon(R.drawable.ic_screen)
                .setContentIntent(pendingIntent)
                .build();
    }

}
