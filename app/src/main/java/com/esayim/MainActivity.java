package com.esayim;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.esayim.client.NettyClient;
import com.esayim.client.common.ClientConfig;
import com.esayim.client.common.SnowflakeIDGenerator;
import com.esayim.comm.message.test.TestRequestMessage;
import com.esayim.event.CEventCenter;
import com.esayim.event.Events;
import com.esayim.event.I_CEventListener;
import com.esayim.service.MessageProcessor;
import com.esayim.service.ServiceConnectStatusListener;
import com.esayim.service.ServiceEventListener;
import com.esayim.service.ServiceThreadPoolExecutor;


public class MainActivity extends AppCompatActivity implements I_CEventListener {

    private EditText mEditText;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = findViewById(R.id.et_content);
        mTextView = findViewById(R.id.tv_msg);
        NettyClient nettyClient = NettyClient.getInstance();
        nettyClient.init(new ServiceEventListener(), new ServiceConnectStatusListener(), ClientConfig.APP_STATUS_BACKGROUND);
        CEventCenter.registerEventListener(this, Events.CHAT_TEST_MESSAGE);
    }

    public void sendTestMsg(View view) {
        TestRequestMessage message = new TestRequestMessage(SnowflakeIDGenerator.generateID(), mEditText.getText().toString());
        MessageProcessor.getInstance().sendMessage(message);
    }

    @Override
    public void onCEvent(String topic, int msgCode, int resultCode, Object obj) {
        switch (topic) {
            case Events.CHAT_TEST_MESSAGE: {
                final String content = (String) obj;
                ServiceThreadPoolExecutor.runOnMainThread(() -> mTextView.setText(content));
                break;
            }
            default:
                break;
        }
    }
}