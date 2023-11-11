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

/**
 * 发起屏幕共享活动
 *
 * @author 单程车票
 */
public class ScreenSharingActivity extends AppCompatActivity {

    /**
     * 日志标识
     */
    private static final String TAG = ScreenSharingActivity.class.getSimpleName();

    /**
     * 麦克风权限请求码
     */
    private static final int RECORD_AUDIO_REQUEST_CODE = 1001;

    /**
     * 屏幕录制权限请求码
     */
    private static final int SCREEN_RECORD_REQUEST_CODE = 1002;

    /**
     * 会议号
     */
    private String meetingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_launch);
        // 获取界面元素
        ImageButton endShareButton = findViewById(R.id.endShareButton);
        TextView meetingTheme = findViewById(R.id.meetingTitle);
        // 初始化屏幕共享页面
        Intent intent = getIntent();
        meetingId = intent.getStringExtra("meetingId");
        String theme = intent.getStringExtra("theme");
        meetingTheme.setText(theme);
        // 获取麦克风权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            // 请求麦克风权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO_REQUEST_CODE);
        } else {
            // 直接请求屏幕录制权限
            requestScreenRecordPermission();
        }

        endShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "退出屏幕共享并断开信令服务器连接");
                // 广播通知前台服务关闭并停止音视频流
                Intent intent = new Intent(ScreenRecordingService.ACTION_STOP_STREAM);
                sendBroadcast(intent);
                // 结束页面返回会议页面
                finish();
            }
        });
    }

    /**
     * 请求屏幕录制权限
     */
    private void requestScreenRecordPermission() {
        MediaProjectionManager projectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        Intent permissionIntent = projectionManager.createScreenCaptureIntent();
        startActivityForResult(permissionIntent, SCREEN_RECORD_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RECORD_AUDIO_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 获得麦克风权限后请求屏幕录制权限
                requestScreenRecordPermission();
            } else {
                Toast.makeText(ScreenSharingActivity.this, "用户拒绝开启麦克风，无法进行语音聊天", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCREEN_RECORD_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Intent serviceIntent = new Intent(this, ScreenRecordingService.class);
                serviceIntent.putExtra("data", data);
                serviceIntent.putExtra("meetingId", meetingId);
                startForegroundService(serviceIntent);
            } else {
                Toast.makeText(ScreenSharingActivity.this, "用户拒绝开启屏幕录制，无法进行屏幕共享", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
