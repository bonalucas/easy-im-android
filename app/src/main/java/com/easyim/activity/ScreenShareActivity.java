package com.easyim.activity;

import androidx.appcompat.app.AppCompatActivity;

import com.easyim.rtc.SignalingClient;

import org.json.JSONObject;

public class ScreenShareActivity extends AppCompatActivity implements SignalingClient.Callback{

//    /**
//     * 屏幕权限
//     */
//    private static final int SCREEN_CAPTURE_REQUEST_CODE = 1;
//
//    /**
//     * 连接工厂
//     */
//    private PeerConnectionFactory peerConnectionFactory;
//
//    /**
//     * 连接集合
//     */
//    private Map<String, PeerConnection> peerConnectionMap;
//
//    /**
//     * 媒体流渲染上下文
//     */
//    private EglBase.Context eglBaseContext;
//
//    /**
//     * 媒体流
//     */
//    private MediaStream mediaStream;
//
//    /**
//     * ICE服务列表
//     */
//    private List<PeerConnection.IceServer> iceServers;
//
//    SurfaceViewRenderer[] remoteViews;
//
//    int remoteViewsIndex = 0;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_screen_share);
//        // 初始化
//        peerConnectionMap = new HashMap<>();
//        iceServers = new ArrayList<>();
//        iceServers.add(PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer());
//        eglBaseContext = EglBase.create().getEglBaseContext();
//        // 请求屏幕捕获权限
//        requestScreenCapturePermission();
//    }
//
//    /**
//     * 授权屏幕捕获权限
//     */
//    private void requestScreenCapturePermission() {
//        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
//        Intent screenCaptureIntent = mediaProjectionManager.createScreenCaptureIntent();
//        startActivityForResult(screenCaptureIntent, SCREEN_CAPTURE_REQUEST_CODE);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == SCREEN_CAPTURE_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                // 用户授予了屏幕捕获权限，可以开始屏幕共享
//                startScreenSharing(data);
//                // 启动前台服务以进行屏幕录制
//                startScreenRecordingService(data);
//            } else {
//                // 用户拒绝了屏幕捕获权限
//                Toast.makeText(this, "屏幕捕获权限被拒绝", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    /**
//     * 开启前台服务以进行屏幕录制
//     */
//    private void startScreenRecordingService(Intent intent) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            Intent serviceIntent = new Intent(this, ScreenRecordingService.class);
//            serviceIntent.putExtra("media_projection_intent", intent);
//            startForegroundService(serviceIntent);
//        }
//    }
//
//    /**
//     * 开启屏幕共享
//     */
//    private void startScreenSharing(Intent intent) {
//        // 初始化 WebRTC 相关资源
//        PeerConnectionFactory.initialize(PeerConnectionFactory.InitializationOptions
//                .builder(this)
//                .createInitializationOptions());
//        PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();
//        DefaultVideoEncoderFactory defaultVideoEncoderFactory = new DefaultVideoEncoderFactory(eglBaseContext, true, true);
//        DefaultVideoDecoderFactory defaultVideoDecoderFactory = new DefaultVideoDecoderFactory(eglBaseContext);
//        peerConnectionFactory = PeerConnectionFactory.builder()
//                .setOptions(options)
//                .setVideoEncoderFactory(defaultVideoEncoderFactory)
//                .setVideoDecoderFactory(defaultVideoDecoderFactory)
//                .createPeerConnectionFactory();
//
//        // 在这里创建一个 VideoCapturer，捕获屏幕内容
//        VideoCapturer videoCapturer = createScreenCapturer(intent);
//        VideoSource videoSource = peerConnectionFactory.createVideoSource(videoCapturer.isScreencast());
//        SurfaceTextureHelper surfaceTextureHelper = SurfaceTextureHelper.create("ScreenThread", eglBaseContext);
//        videoCapturer.initialize(surfaceTextureHelper, this, videoSource.getCapturerObserver());
//        videoCapturer.startCapture(1280, 720, 30);
//
//        // 关联控件
//        SurfaceViewRenderer localView = findViewById(R.id.localView);
//        localView.init(eglBaseContext, null);
//
//        // 创建 VideoTrack 并将 VideoTrack 渲染到 localView
//        VideoTrack videoTrack = peerConnectionFactory.createVideoTrack("screen", videoSource);
//        videoTrack.addSink(localView);
//
//        // 初始化本地媒体流
//        mediaStream = peerConnectionFactory.createLocalMediaStream("mediaStream");
//        mediaStream.addTrack(videoTrack);
//
//        // 连接 WebRTC 服务器的信令服务器进行通信
//        SignalingClient.getInstance().init(this);
//    }
//
//    /**
//     * 获取 PeerConnection 对象
//     */
//    private synchronized PeerConnection getOrCreatePeerConnection(String socketId) {
//        PeerConnection peerConnection = peerConnectionMap.get(socketId);
//        if(peerConnection != null) {
//            return peerConnection;
//        }
//        peerConnection = peerConnectionFactory.createPeerConnection(iceServers, new PeerConnectionAdapter(socketId) {
//            @Override
//            public void onIceCandidate(IceCandidate iceCandidate) {
//                super.onIceCandidate(iceCandidate);
//                // 将ICE候选信息发送给信令服务器
//                SignalingClient.getInstance().sendIceCandidate(iceCandidate, socketId);
//            }
//            @Override
//            public void onAddStream(MediaStream mediaStream) {
//                super.onAddStream(mediaStream);
//                // 接收的媒体流中提取 VideoTrack 并将其渲染到 SurfaceViewRenderer
//                VideoTrack remoteVideoTrack = mediaStream.videoTracks.get(0);
//                runOnUiThread(() -> {
//                    remoteVideoTrack.addSink(remoteViews[remoteViewsIndex++]);
//                });
//            }
//        });
//        peerConnection.addStream(mediaStream);
//        peerConnectionMap.put(socketId, peerConnection);
//        return peerConnection;
//    }
//
//    /**
//     * 创建VideoCapturer
//     */
//    private VideoCapturer createScreenCapturer(Intent intent) {
//        return new ScreenCapturerAndroid(intent, new MediaProjection.Callback() {
//            @Override
//            public void onStop() {
//                super.onStop();
//            }
//        });
//    }
//
    @Override
    public void onPeerJoined(String socketId) {
//        PeerConnection peerConnection = getOrCreatePeerConnection(socketId);
//        peerConnection.createOffer(new SdpAdapter("createOfferSdp:" + socketId) {
//            @Override
//            public void onCreateSuccess(SessionDescription sessionDescription) {
//                super.onCreateSuccess(sessionDescription);
//                peerConnection.setLocalDescription(new SdpAdapter("setLocalSdp:" + socketId), sessionDescription);
//                SignalingClient.getInstance().sendSessionDescription(sessionDescription, socketId);
//            }
//        }, new MediaConstraints());
    }

    @Override
    public void onOfferReceived(JSONObject data) {
//        runOnUiThread(() -> {
//            final String socketId = data.optString("from");
//            PeerConnection peerConnection = getOrCreatePeerConnection(socketId);
//            peerConnection.setRemoteDescription(new SdpAdapter("setRemoteSdp:" + socketId),
//                    new SessionDescription(SessionDescription.Type.OFFER, data.optString("sdp")));
//            peerConnection.createAnswer(new SdpAdapter("localAnswerSdp") {
//                @Override
//                public void onCreateSuccess(SessionDescription sdp) {
//                    super.onCreateSuccess(sdp);
//                    peerConnectionMap.get(socketId).setLocalDescription(new SdpAdapter("setLocalSdp:" + socketId), sdp);
//                    SignalingClient.getInstance().sendSessionDescription(sdp, socketId);
//                }
//            }, new MediaConstraints());
//
//        });
    }

    @Override
    public void onAnswerReceived(JSONObject data) {
//        String socketId = data.optString("from");
//        PeerConnection peerConnection = getOrCreatePeerConnection(socketId);
//        peerConnection.setRemoteDescription(new SdpAdapter("setRemoteSdp:" + socketId),
//                new SessionDescription(SessionDescription.Type.ANSWER, data.optString("sdp")));
    }

    @Override
    public void onIceCandidateReceived(JSONObject data) {
//        String socketId = data.optString("from");
//        PeerConnection peerConnection = getOrCreatePeerConnection(socketId);
//        peerConnection.addIceCandidate(new IceCandidate(
//                data.optString("id"),
//                data.optInt("label"),
//                data.optString("candidate")
//        ));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SignalingClient.getInstance().destroy();
    }

}
