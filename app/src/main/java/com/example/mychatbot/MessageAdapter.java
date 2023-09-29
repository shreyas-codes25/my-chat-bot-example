package com.example.mychatbot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {
    List<Message> msglist ;

    public MessageAdapter(List<Message> msglist) {
        this.msglist = msglist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View chatView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item,null);
        MyViewHolder viewHolder = new MyViewHolder(chatView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Message msg = msglist.get(position);
        if(msg.getSendBy().equals(Message.SEND_BY_ME)){
            holder.leftChat.setVisibility(View.GONE);;
            holder.rightChat.setVisibility(View.VISIBLE);;
            holder.rchtv.setText(msg.getMessage());
        }
        else{
            holder.rightChat.setVisibility(View.GONE);;
            holder.leftChat.setVisibility(View.VISIBLE);;
            holder.lchtv.setText(msg.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return msglist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        LinearLayout leftChat,rightChat;
        TextView lchtv,rchtv;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            leftChat = itemView.findViewById(R.id.left_chat);
            rightChat = itemView.findViewById(R.id.right_chat);
            lchtv = itemView.findViewById(R.id.left_text);
            rchtv = itemView.findViewById(R.id.right_text);

        }
    }
}
