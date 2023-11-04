package com.easyim.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.easyim.R;
import com.easyim.comm.message.chat.ChatResponseMessage;
import com.easyim.common.Constants;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    /**
     * 消息列表
     */
    private final List<ChatResponseMessage> messages;

    public ChatAdapter(List<ChatResponseMessage> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatResponseMessage message = messages.get(position);

        if (message.getType().equals(Constants.ChatMessageType.SYSTEM_TYPE)) {
            // 系统消息的渲染
            holder.systemMessageTextView.setText(message.getContent());
            holder.systemMessageTextView.setVisibility(View.VISIBLE);
            holder.userMessageLayout.setVisibility(View.GONE);
        } else if (message.getType().equals(Constants.ChatMessageType.TEXT_TYPE)) {
            // 用户聊天消息的渲染
            holder.userMessageTextView.setText(message.getNickname() + ": " + message.getContent());
            holder.systemMessageTextView.setVisibility(View.GONE);
            holder.userMessageLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView systemMessageTextView;
        LinearLayout userMessageLayout;
        TextView userMessageTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            systemMessageTextView = itemView.findViewById(R.id.textViewSystemMessage);
            userMessageLayout = itemView.findViewById(R.id.layoutUserMessage);
            userMessageTextView = itemView.findViewById(R.id.textViewUserMessage);
        }

    }
}
