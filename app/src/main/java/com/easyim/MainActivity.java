package com.easyim;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.easyim.client.NettyClient;
import com.easyim.client.common.ClientConfig;
import com.easyim.comm.message.meeting.MeetingCreateRequestMessage;
import com.easyim.comm.message.meeting.MeetingCreateResponseMessage;
import com.easyim.event.CEventCenter;
import com.easyim.event.Events;
import com.easyim.event.I_CEventListener;
import com.easyim.service.MessageProcessor;
import com.easyim.service.common.FastUniqueIDGenerator;
import com.easyim.service.common.ServiceThreadPoolExecutor;


public class MainActivity extends AppCompatActivity implements I_CEventListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NettyClient nettyClient = NettyClient.getInstance();
        nettyClient.init(ClientConfig.APP_STATUS_BACKGROUND);
        CEventCenter.registerEventListener(this, new String[]{ Events.CREATE_MEETING, Events.SERVER_ERROR });

        Button buttonJoinMeeting = findViewById(R.id.buttonJoinMeeting);
        Button buttonCreateMeeting = findViewById(R.id.buttonCreateMeeting);

        buttonJoinMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理加入会议按钮点击事件
                openEnterMeetingDialog();
            }
        });

        buttonCreateMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理创建会议按钮点击事件
                openCreateMeetingDialog();
            }
        });
    }

    private void openCreateMeetingDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.create_meeting_dialog);
        dialog.setTitle("创建会议");

        TextView textViewMeetingNumber = dialog.findViewById(R.id.textViewMeetingNumber);
        ImageButton buttonCopyMeetingNumber = dialog.findViewById(R.id.buttonCopyMeetingNumber);
        EditText editTextMeetingTitle = dialog.findViewById(R.id.editTextMeetingTitle);
        EditText editTextNickName = dialog.findViewById(R.id.editTextNickName);
        Button buttonCreateMeetingDialog = dialog.findViewById(R.id.buttonCreateMeetingDialog);

        // 生成会议号
        String generatedMeetingNumber = FastUniqueIDGenerator.generateID();

        // 设置会议号文本
        textViewMeetingNumber.setText("会议号: " + generatedMeetingNumber);

        // 复制按钮点击事件
        buttonCopyMeetingNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 复制会议号到剪贴板
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Meeting Number", generatedMeetingNumber);
                clipboardManager.setPrimaryClip(clipData);

                Toast.makeText(MainActivity.this, "会议号已复制", Toast.LENGTH_SHORT).show();
            }
        });

        // 创建会议按钮点击事件
        buttonCreateMeetingDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理创建会议按钮点击事件
                String meetingTitle = editTextMeetingTitle.getText().toString();
                String nickName = editTextNickName.getText().toString();

//                MeetingCreateRequestMessage message = new MeetingCreateRequestMessage(SnowflakeIDGenerator.generateID(),
//                        generatedMeetingNumber, nickName, meetingTitle);
                MeetingCreateRequestMessage message = new MeetingCreateRequestMessage(375635370999549952L,
                        "960994266", "yyp", "yp");
                MessageProcessor.getInstance().sendMessage(message);

                // 关闭弹窗
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void openEnterMeetingDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.enter_meeting_dialog);
        dialog.setTitle("加入会议");

        EditText editTextMeetingNumber = dialog.findViewById(R.id.editTextMeetingNumber);
        EditText editTextNickName = dialog.findViewById(R.id.editTextNickName);
        Button buttonJoinMeetingDialog = dialog.findViewById(R.id.buttonJoinMeetingDialog);

        buttonJoinMeetingDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理加入会议按钮点击事件
                String meetingNumber = editTextMeetingNumber.getText().toString();
                String nickName = editTextNickName.getText().toString();

                // 这里可以执行加入会议的逻辑，例如打开另一个 Activity 或执行相应操作
                // 你可以根据自己的需求来实现加入会议的功能

                // 关闭弹窗
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onCEvent(String topic, int msgCode, int resultCode, Object obj) {
        switch (topic) {
            case Events.CREATE_MEETING: {
                if (obj instanceof MeetingCreateResponseMessage) {
                    MeetingCreateResponseMessage msg = (MeetingCreateResponseMessage) obj;
                    ServiceThreadPoolExecutor.runOnMainThread(() -> Toast.makeText(MainActivity.this, String.format("创建会议成功,会议号为【%s】", msg.getMeetingID()), Toast.LENGTH_SHORT).show());
                }
                break;
            }
            default:
                break;
        }
    }
}