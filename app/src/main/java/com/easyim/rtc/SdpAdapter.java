package com.easyim.rtc;

import android.util.Log;

import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;

/**
 * SDP 协议适配器
 *
 * @author 单程车票
 */
public class SdpAdapter implements SdpObserver {

    private final String TAG;

    public SdpAdapter(String TAG) {
        this.TAG = TAG;
    }

    private void log(String msg) {
        Log.d(TAG, msg);
    }

    @Override
    public void onCreateSuccess(SessionDescription sessionDescription) {
        log("onCreateSuccess " + sessionDescription);
    }

    @Override
    public void onSetSuccess() {
        log("onSetSuccess ");
    }

    @Override
    public void onCreateFailure(String msg) {
        log("onCreateFailure " + msg);
    }

    @Override
    public void onSetFailure(String msg) {
        log("onSetFailure " + msg);
    }

}
