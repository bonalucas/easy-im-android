package com.easyim.rtc;

import android.util.Log;

import org.webrtc.DataChannel;
import org.webrtc.IceCandidate;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.RtpReceiver;

import java.util.Arrays;

/**
 * WebRTC连接适配器
 *
 * @author 单程车票
 */
public class PeerConnectionAdapter implements PeerConnection.Observer{

    private final String TAG;

    public PeerConnectionAdapter(String TAG) {
        this.TAG = "【Client: " + TAG + " 】";
    }

    private void log(String msg) {
        Log.d(TAG, msg);
    }

    @Override
    public void onSignalingChange(PeerConnection.SignalingState signalingState) {
        log("onSignalingChange " + signalingState);
    }

    @Override
    public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
        log("onIceConnectionChange " + iceConnectionState);
    }

    @Override
    public void onIceConnectionReceivingChange(boolean res) {
        log("onIceConnectionReceivingChange " + res);
    }

    @Override
    public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {
        log("onIceGatheringChange " + iceGatheringState);
    }

    @Override
    public void onIceCandidate(IceCandidate iceCandidate) {
        log("onIceCandidate " + iceCandidate);
    }

    @Override
    public void onIceCandidatesRemoved(IceCandidate[] iceCandidates) {
        log("onIceCandidatesRemoved " + Arrays.toString(iceCandidates));
    }

    @Override
    public void onAddStream(MediaStream mediaStream) {
        log("onAddStream " + mediaStream);
    }

    @Override
    public void onRemoveStream(MediaStream mediaStream) {
        log("onRemoveStream " + mediaStream);
    }

    @Override
    public void onDataChannel(DataChannel dataChannel) {
        log("onDataChannel " + dataChannel);
    }

    @Override
    public void onRenegotiationNeeded() {
        log("onRenegotiationNeeded ");
    }

    @Override
    public void onAddTrack(RtpReceiver rtpReceiver, MediaStream[] mediaStreams) {
        log("onAddTrack " + Arrays.toString(mediaStreams));
    }

}
