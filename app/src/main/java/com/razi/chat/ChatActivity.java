package com.razi.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razi.chat.databinding.ActivityChatBinding;
import com.razi.chat.databinding.ActivityMainBinding;

import java.util.UUID;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;
    private String recieverId;
    DatabaseReference databaseReferenceSender,databaseReferenceReciever;
    String senderRoom,recieverRoom;
    messageRowAdapter messageadpt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        messageadpt= new messageRowAdapter(this);
        binding.chatRecycler.setAdapter(messageadpt);
        binding.chatRecycler.setLayoutManager(new LinearLayoutManager(this));

        recieverId=getIntent().getStringExtra("id");

        senderRoom= FirebaseAuth.getInstance().getUid()+recieverId;
        recieverRoom=recieverId+FirebaseAuth.getInstance().getUid();

        databaseReferenceSender= FirebaseDatabase.getInstance().getReference("Complaints").child(senderRoom);
        databaseReferenceReciever= FirebaseDatabase.getInstance().getReference("Complaints").child(recieverRoom);

        databaseReferenceSender.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    MessageModel messageModel=dataSnapshot.getValue(MessageModel.class);
                    messageadpt.add(messageModel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message=binding.messageEd.getText().toString();
                if(message.trim().length()>0)
                {
                    sendMessage(message);
                }
            }
        });
    }

    private void sendMessage( String message) {
        String  messageId= UUID.randomUUID().toString();
        MessageModel messageModel= new MessageModel(messageId,FirebaseAuth.getInstance().getUid(),message);

        messageadpt.add(messageModel);
        databaseReferenceSender
                .child(messageId)
                .setValue(messageModel);
        databaseReferenceReciever
                .child(messageId)
                .setValue(messageModel);

        binding.messageEd.setText(""); // Clear the EditText view

// Scroll to the bottom of the RecyclerView
        binding.chatRecycler.smoothScrollToPosition(messageadpt.getItemCount() - 1);

    }
}