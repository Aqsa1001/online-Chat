package com.razi.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class messageRowAdapter extends RecyclerView.Adapter<messageRowAdapter.MyViewHolder>{
    private Context context;
    private List<MessageModel> messageModelList;

    public messageRowAdapter(Context context) {
        this.context = context;
        messageModelList=new ArrayList<>();
    }
    public void add(MessageModel messageModel) {
        // Check if the message already exists in the list
        for (MessageModel model : messageModelList) {
            if (model.getMsgId().equals(messageModel.getMsgId())) {
                return;
            }
        }
        // If the message doesn't exist, add it to the list and notify the adapter
        messageModelList.add(messageModel);
        notifyDataSetChanged();
    }

    public  void clear()
    {
        messageModelList.clear();
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public messageRowAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.message_row,parent,false);
        return new messageRowAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull messageRowAdapter.MyViewHolder holder, int position) {
        MessageModel messageModel = messageModelList.get(position);
        holder.msg.setText(messageModel.getMessage());

        // If the message is sent by the current user
        if (messageModel.getSenderId().equals(FirebaseAuth.getInstance().getUid())) {
            holder.main.setBackgroundResource(R.drawable.corner_right); // Use a different drawable for the right side
            holder.msg.setTextColor(context.getResources().getColor(R.color.black));
            holder.main.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            holder.msg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        }
        // If the message is received by the current user
        else {
            holder.main.setBackgroundResource(R.drawable.corner_left); // Use a different drawable for the left side
            holder.msg.setTextColor(context.getResources().getColor(R.color.white));
            holder.main.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            holder.msg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        }
    }

    @Override
    public int getItemCount() {
        return messageModelList.size();
    }

    public class MyViewHolder   extends RecyclerView.ViewHolder{
        private TextView msg,email;
        private LinearLayout main;
        public  MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            msg=itemView.findViewById(R.id.msg);
            main=itemView.findViewById(R.id.mainMessageLayout);
        }

    }
}

