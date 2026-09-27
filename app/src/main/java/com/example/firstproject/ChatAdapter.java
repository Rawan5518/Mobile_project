package com.example.firstproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_SENT = 1;
    private static final int TYPE_RECEIVED = 2;
    private List<Message> messageList;
    private String currentUserId;

    public ChatAdapter(List<Message> messageList) {
        this.messageList = messageList;
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            this.currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
    }
    @Override
    public int getItemViewType(int position) {
        return messageList.get(position).getSenderId().equals(currentUserId) ? TYPE_SENT : TYPE_RECEIVED;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_SENT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
            return new SentViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
            return new ReceivedViewHolder(view);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);
        TextView bubble;
        if (holder instanceof SentViewHolder) {
            SentViewHolder sentHolder = (SentViewHolder) holder;
            sentHolder.textMsg.setText(message.getText());
            bubble = sentHolder.textMsg;
        } else {
            ReceivedViewHolder recHolder = (ReceivedViewHolder) holder;
            recHolder.textMsg.setText(message.getText());
            recHolder.textName.setText(message.getUsername());
            bubble = recHolder.textMsg;
        }
        bubble.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (message.getSenderId() != null && message.getSenderId().equals(currentUserId)) {
                    if (message.getMessageId() != null) {
                        FirebaseDatabase.getInstance("https://myproject-3eece-default-rtdb.firebaseio.com/")
                                .getReference("messages")
                                .child(message.getMessageId())
                                .removeValue()
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(v.getContext(), "Message Unsent", Toast.LENGTH_SHORT).show();
                                });
                    }
                    return true;
                } else {
                    Toast.makeText(v.getContext(), "You can't delete this message", Toast.LENGTH_SHORT).show();
                    return true;
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return messageList.size();
    }
    static class SentViewHolder extends RecyclerView.ViewHolder {
        TextView textMsg;
        SentViewHolder(View itemView) {
            super(itemView);
            textMsg = itemView.findViewById(R.id.messageText);
        }
    }
    static class ReceivedViewHolder extends RecyclerView.ViewHolder {
        TextView textMsg, textName;
        ReceivedViewHolder(View itemView) {
            super(itemView);
            textMsg = itemView.findViewById(R.id.messageText);
            textName = itemView.findViewById(R.id.senderName);
        }
    }
}