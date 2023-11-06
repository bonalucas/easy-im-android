package com.easyim.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.easyim.R;
import com.easyim.adapter.ChatAdapter;
import com.easyim.client.common.SnowflakeIDGenerator;
import com.easyim.comm.message.chat.ChatRequestMessage;
import com.easyim.comm.message.chat.ChatResponseMessage;
import com.easyim.comm.message.meeting.JoinMeetingResponseMessage;
import com.easyim.common.Constants;
import com.easyim.event.CEventCenter;
import com.easyim.event.Events;
import com.easyim.event.I_CEventListener;
import com.easyim.service.MessageProcessor;
import com.easyim.service.ServiceThreadPoolExecutor;

import java.util.LinkedList;
import java.util.List;

/**
 * 会议活动
 *
 * @author 单程车票
 */
public class MeetingActivity extends AppCompatActivity implements I_CEventListener {

    // 用于跟踪语音是否开启
    private boolean isMicOn = false;

    // 聊天记录消息列表
    private final List<ChatResponseMessage> chatMessages = new LinkedList<>();

    // 聊天适配器
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);
        // 注册监听事件
        String[] interest = { Events.SERVER_ERROR, Events.JOIN_MEETING, Events.CHAT_RESPONSE };
        CEventCenter.registerEventListener(this, interest);
        // 获取界面元素的引用
        TextView meetingTheme = findViewById(R.id.textViewMeetingTitle);
        ImageButton buttonExitMeeting = findViewById(R.id.buttonExitMeeting);
        ImageButton buttonUploadFile = findViewById(R.id.buttonUploadFile);
        ImageButton buttonScreen = findViewById(R.id.buttonScreen);
        Button buttonSend = findViewById(R.id.buttonSend);
        // 初始化适配器
        RecyclerView recyclerViewChat = findViewById(R.id.recyclerViewChat);
        chatAdapter = new ChatAdapter(chatMessages);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewChat.setAdapter(chatAdapter);
        // 初始化会议界面
        Intent intent = getIntent();
        String nickname = intent.getStringExtra("nickname");
        String theme = intent.getStringExtra("theme");
        String type = intent.getStringExtra("type");
        meetingTheme.setText(theme);
        if ("create".equals(type)) {
            // 显示创建会议
            ChatResponseMessage systemMessage = new ChatResponseMessage(SnowflakeIDGenerator.generateID(), "system", Constants.ChatMessageType.SYSTEM_TYPE, nickname + " 创建会议");
            chatMessages.add(systemMessage);
            chatAdapter.notifyItemInserted(chatMessages.size() - 1);
        } else if ("join".equals(type)) {
            // 显示进入会议
            ChatResponseMessage systemMessage = new ChatResponseMessage(SnowflakeIDGenerator.generateID(), "system", Constants.ChatMessageType.SYSTEM_TYPE, nickname + " 进入会议");
            chatMessages.add(systemMessage);
            chatAdapter.notifyItemInserted(chatMessages.size() - 1);
        }

        // 按钮事件响应
        buttonExitMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理退出会议逻辑
                finish(); // 关闭当前会议页面
            }
        });

        buttonUploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理上传文件逻辑
                // 打开文件选择器等操作
                openFilePicker();
            }
        });

        buttonScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理屏幕共享逻辑
                toggleScreen();
            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理发送消息逻辑
                sendMessage();
            }
        });

    }

    private void toggleScreen() {
//        ImageButton buttonMic = findViewById(R.id.buttonScreen);
//        if (isMicOn) {
//            // 关闭语音
//            buttonMic.setImageResource(R.drawable.ic_voice_off);
//            isMicOn = false;
//        } else {
//            // 开启语音
//            buttonMic.setImageResource(R.drawable.ic_voice_on);
//            isMicOn = true;
//        }
    }

    private void openFilePicker() {
        // 实现打开文件选择器的逻辑
        // 这里你可以使用 Intent 打开系统文件选择器或自定义的文件选择界面
        // 参考 Android 文件选择器文档来实现文件选择逻辑
        Toast.makeText(this, "打开文件选择器", Toast.LENGTH_SHORT).show();
    }

    private void sendMessage() {
        EditText editTextChat = findViewById(R.id.editTextChat);
        String message = editTextChat.getText().toString().trim();

        ChatRequestMessage requestMessage = new ChatRequestMessage(SnowflakeIDGenerator.generateID(), Constants.ChatMessageType.TEXT_TYPE, message);
        MessageProcessor.getInstance().sendMessage(requestMessage);

        if (!message.isEmpty()) {
            editTextChat.setText("");
        }
    }

    @Override
    public void onCEvent(String topic, int msgCode, int resultCode, Object obj) {
        switch (topic) {
            case Events.JOIN_MEETING: {
                if (obj instanceof JoinMeetingResponseMessage) {
                    JoinMeetingResponseMessage msg = (JoinMeetingResponseMessage) obj;
                    // 渲染系统入会提示
                    ServiceThreadPoolExecutor.runOnMainThread(() -> {
                        ChatResponseMessage systemMessage = new ChatResponseMessage(SnowflakeIDGenerator.generateID(), "system", Constants.ChatMessageType.SYSTEM_TYPE, msg.getNickname() + " 进入会议");
                        chatMessages.add(systemMessage);
                        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
                    });
                }
                break;
            }

            case Events.CHAT_RESPONSE: {
                if (obj instanceof ChatResponseMessage) {
                    // 聊天消息渲染
                    ServiceThreadPoolExecutor.runOnMainThread(() -> {
                        ChatResponseMessage msg = (ChatResponseMessage) obj;
                        chatMessages.add(msg);
                        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
                    });
                }
                break;
            }

            case Events.SERVER_ERROR: {
                String errorMsg = (String) obj;
                ServiceThreadPoolExecutor.runOnMainThread(() -> Toast.makeText(MeetingActivity.this, errorMsg, Toast.LENGTH_SHORT).show());
                break;
            }

            default:
                break;
        }
    }

}
