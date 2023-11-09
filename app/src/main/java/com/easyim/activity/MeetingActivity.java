package com.easyim.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.easyim.R;
import com.easyim.adapter.ChatAdapter;
import com.easyim.client.common.SnowflakeIDGenerator;
import com.easyim.comm.message.chat.ChatRequestMessage;
import com.easyim.comm.message.chat.ChatResponseMessage;
import com.easyim.comm.message.file.FileRequestMessage;
import com.easyim.comm.message.file.FileResponseMessage;
import com.easyim.comm.message.meeting.JoinMeetingResponseMessage;
import com.easyim.comm.message.screen.ShareScreenRequestMessage;
import com.easyim.comm.message.screen.ShareScreenResponseMessage;
import com.easyim.common.Constants;
import com.easyim.event.CEventCenter;
import com.easyim.event.Events;
import com.easyim.event.I_CEventListener;
import com.easyim.service.MessageProcessor;
import com.easyim.service.ServiceThreadPoolExecutor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * 会议活动
 *
 * @author 单程车票
 */
public class MeetingActivity extends AppCompatActivity implements I_CEventListener {

    /**
     * 文件请求码
     */
    private static final int FILE_PICKER_REQUEST_CODE = 679;

    /**
     * 聊天记录消息列表
     */
    private final List<ChatResponseMessage> chatMessages = new LinkedList<>();

    /**
     * 聊天适配器
     */
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);
        // 注册监听事件
        String[] interest = { Events.SERVER_ERROR, Events.JOIN_MEETING, Events.CHAT_RESPONSE, Events.FILE_RESPONSE, Events.ShareScreen_RESPONSE };
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
                // 关闭当前会议页面
                finish();
            }
        });

        buttonUploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 处理上传文件逻辑
                openFilePicker();
            }
        });

        buttonScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 发送屏幕共享消息
                MessageProcessor.getInstance().sendMessage(new ShareScreenRequestMessage(SnowflakeIDGenerator.generateID()));
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

    /**
     * 打开文件选择器
     */
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(
                    Intent.createChooser(intent, "选择文件"),
                    FILE_PICKER_REQUEST_CODE
            );
        } catch (android.content.ActivityNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 发送文本消息
     */
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_PICKER_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Uri selectedFileUri = data.getData();
                    try {
                        byte[] file = readBytesFromUri(selectedFileUri);
                        String fileName = getFileNameFromUri(selectedFileUri);
                        String mimeType = getFileMimeType(selectedFileUri);
                        FileRequestMessage message = new FileRequestMessage(fileName, mimeType, file);
                        MessageProcessor.getInstance().sendMessage(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
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

            case Events.FILE_RESPONSE: {
                if (obj instanceof FileResponseMessage) {
                    // 文件消息弹出框渲染
                    ServiceThreadPoolExecutor.runOnMainThread(() -> {
                        FileResponseMessage msg = (FileResponseMessage) obj;
                        try {
                            showFileDetailsDialog(this, msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
                break;
            }

            case Events.ShareScreen_RESPONSE: {
                if (obj instanceof ShareScreenResponseMessage) {
                    // 文件消息弹出框渲染
                    ServiceThreadPoolExecutor.runOnMainThread(() -> {
                        ShareScreenResponseMessage msg = (ShareScreenResponseMessage) obj;
                        Toast.makeText(MeetingActivity.this, msg.getNickname() + "发起屏幕共享", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MeetingActivity.this, ScreenShareActivity.class);
                        intent.putExtra("theme", msg.getTheme());
                        intent.putExtra("nickname", msg.getNickname());
                        intent.putExtra("meetingId", msg.getMeetingId());
                        intent.putExtra("isShared", msg.isShared());
                        startActivity(intent);
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

    /**
     * 获取文件类型
     */
    private String getFileMimeType(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        return contentResolver.getType(uri);
    }

    /**
     * 获取文件二进制字节数组
     */
    private byte[] readBytesFromUri(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 获取文件名
     */
    private String getFileNameFromUri(Uri uri) {
        String fileName = null;
        if (uri.getScheme().equals("content")) {
            // 如果 URI 使用 content 协议，使用 ContentResolver 获取文件名
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        if (displayNameIndex != -1) {
                            fileName = cursor.getString(displayNameIndex);
                        }
                    }
                } finally {
                    cursor.close();
                }
            }
        } else if (uri.getScheme().equals("file")) {
            // 如果 URI 使用 file 协议，直接从 URI 中获取文件名
            fileName = new File(uri.getPath()).getName();
        }
        return fileName;
    }

    /**
     * 弹出文件展示对话框
     */
    private void showFileDetailsDialog(Context context, FileResponseMessage msg) throws IOException {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // 获取对话框的布局
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.file_detail_dialog, null);
        // 获取对话框中的视图元素
        ImageView fileIcon = dialogView.findViewById(R.id.fileIcon);
        TextView fileNameTextView = dialogView.findViewById(R.id.fileName);
        Button cancelButton = dialogView.findViewById(R.id.cancelButton);
        Button viewButton = dialogView.findViewById(R.id.viewButton);
        // 设置文件名
        fileNameTextView.setText(msg.getFileName());
        // 设置对话框的视图
        builder.setView(dialogView);
        // 创建对话框
        AlertDialog dialog = builder.create();
        // 创建临时文件得到绝对路径
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String extension = mime.getExtensionFromMimeType(msg.getMimeType());
        final File tempFile = File.createTempFile(msg.getFileName(), "." + extension, context.getCacheDir());
        FileOutputStream fos = new FileOutputStream(tempFile);
        fos.write(msg.getFile());
        fos.close();
        // 设置取消按钮的点击事件
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempFile.delete();
                dialog.dismiss();
            }
        });
        // 设置查看按钮的点击事件
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri contentUri = FileProvider.getUriForFile(context, "com.easyim.fileprovider", tempFile);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(contentUri, msg.getMimeType());
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(intent);
            }
        });
        // 显示对话框
        dialog.show();
    }

}
