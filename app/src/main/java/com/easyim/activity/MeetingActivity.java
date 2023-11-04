package com.easyim.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.easyim.R;
import com.easyim.adapter.ChatAdapter;
import com.easyim.comm.message.chat.ChatResponseMessage;
import com.easyim.common.Constants;

import java.util.LinkedList;
import java.util.List;

/**
 * 会议活动
 *
 * @author 单程车票
 */
public class MeetingActivity extends AppCompatActivity {

    // 用于跟踪语音是否开启
    private boolean isMicOn = false;

    // 聊天记录消息列表
    List<ChatResponseMessage> chatMessages = new LinkedList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);

        // 获取界面元素的引用
        ImageButton buttonExitMeeting = findViewById(R.id.buttonExitMeeting);
        ImageButton buttonUploadFile = findViewById(R.id.buttonUploadFile);
        ImageButton buttonMic = findViewById(R.id.buttonMic);
        Button buttonSend = findViewById(R.id.buttonSend);

        // 初始化适配器
        RecyclerView recyclerViewChat = findViewById(R.id.recyclerViewChat);
        ChatAdapter chatAdapter = new ChatAdapter(chatMessages);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewChat.setAdapter(chatAdapter);

        // 示例添加系统消息
        ChatResponseMessage systemMessage = new ChatResponseMessage("system", Constants.ChatMessageType.SYSTEM_TYPE, "lucas 创建会议");
        chatMessages.add(systemMessage);
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);

        // 示例添加用户聊天消息
        ChatResponseMessage userMessage = new ChatResponseMessage("lucas", Constants.ChatMessageType.TEXT_TYPE, "Hello ...");
        chatMessages.add(userMessage);
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);

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

        buttonMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理开启/关闭麦克风逻辑
                toggleMicState();
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

    private void toggleMicState() {
        ImageButton buttonMic = findViewById(R.id.buttonMic);
        if (isMicOn) {
            // 关闭语音
            buttonMic.setImageResource(R.drawable.ic_voice_off);
            isMicOn = false;
        } else {
            // 开启语音
            buttonMic.setImageResource(R.drawable.ic_voice_on);
            isMicOn = true;
        }
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

        if (!message.isEmpty()) {
            editTextChat.setText("");
        }
    }

}
