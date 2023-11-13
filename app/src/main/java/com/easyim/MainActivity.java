package com.easyim;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.easyim.activity.MeetingActivity;
import com.easyim.client.NettyClient;
import com.easyim.client.common.ClientConfig;
import com.easyim.client.common.SnowflakeIDGenerator;
import com.easyim.comm.message.meeting.CreateMeetingRequestMessage;
import com.easyim.comm.message.meeting.CreateMeetingResponseMessage;
import com.easyim.comm.message.meeting.JoinMeetingRequestMessage;
import com.easyim.comm.message.meeting.JoinMeetingResponseMessage;
import com.easyim.common.FastUniqueIDGenerator;
import com.easyim.event.CEventCenter;
import com.easyim.event.Events;
import com.easyim.event.I_CEventListener;
import com.easyim.service.MessageProcessor;
import com.easyim.service.ServiceThreadPoolExecutor;

/**
 * 首页活动
 *
 * @author 单程车票
 */
public class MainActivity extends AppCompatActivity implements I_CEventListener {

    /**
     * 退出会议标识
     */
    private static final int LEAVE_MEETING_CODE = 1000;

    /**
     * 监听事件
     */
    private final String[] interest = { Events.CREATE_MEETING, Events.SERVER_ERROR, Events.JOIN_MEETING };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 初始化 Netty 客户端
        NettyClient nettyClient = NettyClient.getInstance();
        nettyClient.init(ClientConfig.APP_STATUS_FOREGROUND);
        // 注册监听事件
        CEventCenter.onBindEvent(true, this, interest);
        // 获取界面元素的引用
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LEAVE_MEETING_CODE && resultCode == RESULT_OK) {
            CEventCenter.onBindEvent(true, this, interest);
        }
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

                if ("".equals(meetingTitle) || "".equals(nickName)) {
                    Toast.makeText(MainActivity.this, "会议主题或入会昵称不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    CreateMeetingRequestMessage message = new CreateMeetingRequestMessage(SnowflakeIDGenerator.generateID(),
                            generatedMeetingNumber, meetingTitle, nickName);
                    MessageProcessor.getInstance().sendMessage(message);
                    // 关闭弹窗
                    dialog.dismiss();
                }
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

                if ("".equals(meetingNumber) || "".equals(nickName)) {
                    Toast.makeText(MainActivity.this, "会议号或入会昵称不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    JoinMeetingRequestMessage message = new JoinMeetingRequestMessage(SnowflakeIDGenerator.generateID(), meetingNumber, nickName);
                    MessageProcessor.getInstance().sendMessage(message);
                    // 关闭弹窗
                    dialog.dismiss();
                }

            }
        });

        dialog.show();
    }

    @Override
    public void onCEvent(String topic, int msgCode, int resultCode, Object obj) {
        switch (topic) {
            case Events.CREATE_MEETING: {
                if (obj instanceof CreateMeetingResponseMessage) {
                    CreateMeetingResponseMessage msg = (CreateMeetingResponseMessage) obj;
                    ServiceThreadPoolExecutor.runOnMainThread(() -> Toast.makeText(MainActivity.this, String.format("创建会议成功,会议号为【%s】", msg.getMeetingId()), Toast.LENGTH_SHORT).show());
                    // 注销监听器
                    CEventCenter.onBindEvent(false, this, interest);
                    // 跳转进入会议页面
                    Intent intent = new Intent(MainActivity.this, MeetingActivity.class);
                    intent.putExtra("nickname", msg.getNickname());
                    intent.putExtra("theme", msg.getTheme());
                    intent.putExtra("meetingId", msg.getMeetingId());
                    intent.putExtra("type", "create");
                    startActivityForResult(intent, LEAVE_MEETING_CODE);
                }
                break;
            }

            case Events.JOIN_MEETING: {
                if (obj instanceof JoinMeetingResponseMessage) {
                    JoinMeetingResponseMessage msg = (JoinMeetingResponseMessage) obj;
                    ServiceThreadPoolExecutor.runOnMainThread(() -> Toast.makeText(MainActivity.this, String.format("加入会议成功,会议号为【%s】", msg.getMeetingId()), Toast.LENGTH_SHORT).show());
                    // 注销监听器
                    CEventCenter.onBindEvent(false, this, interest);
                    // 跳转进入会议页面
                    Intent intent = new Intent(MainActivity.this, MeetingActivity.class);
                    intent.putExtra("nickname", msg.getNickname());
                    intent.putExtra("theme", msg.getTheme());
                    intent.putExtra("meetingId", msg.getMeetingId());
                    intent.putExtra("type", "join");
                    startActivityForResult(intent, LEAVE_MEETING_CODE);
                }
                break;
            }

            case Events.SERVER_ERROR: {
                String errorMsg = (String) obj;
                ServiceThreadPoolExecutor.runOnMainThread(() -> Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_SHORT).show());
                break;
            }

            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 应用切换到前台时执行的操作
        NettyClient.getInstance().setAppStatus(ClientConfig.APP_STATUS_FOREGROUND);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 应用切换到后台时执行的操作
        NettyClient.getInstance().setAppStatus(ClientConfig.APP_STATUS_BACKGROUND);
    }

}