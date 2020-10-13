package com.chatProject.Chat;

import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chatProject.Adapters.ChatThreadAdapter;
import com.chatProject.Base.BaseActivity;
import com.chatProject.Chat.FireBaseUtils.MessagesDao;
import com.chatProject.Chat.FireBaseUtils.Model.Message;
import com.chatProject.Chat.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;


import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatRoom extends BaseActivity implements View.OnClickListener {

    protected RecyclerView recyclerView;
    protected EditText message;
    protected ImageButton send;
    ChatThreadAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    public static com.chatProject.Chat.FireBaseUtils.Model.Room currentRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        initView();
        layoutManager = new LinearLayoutManager(activity);
        ((LinearLayoutManager) layoutManager).setStackFromEnd(true);
        adapter = new ChatThreadAdapter(null, com.chatProject.Chat.DataHolder.currentUser.getId());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        query= com.chatProject.Chat.FireBaseUtils.MessagesDao.getMessagesByRoomId(currentRoom.getId());
        query.addChildEventListener(messagesEventListener);


    }
    Query query;
    ChildEventListener messagesEventListener=new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Message message=dataSnapshot.getValue(Message.class);
            adapter.inserNewItem(message);
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        query.removeEventListener(messagesEventListener);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sendo) {
            String content=message.getText().toString();
            if(content.trim().isEmpty()){
                return;
            }else{
            Message message=new Message();
            message.setContent(content);
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
            String date=simpleDateFormat.format(new Date());
            message.setSentAt(date);
            message.setSenderId(com.chatProject.Chat.DataHolder.currentUser.getId());
            message.setSenderName(com.chatProject.Chat.DataHolder.currentUser.getUsername());
            message.setRoomId(currentRoom.getId());
            MessagesDao.sendMessage(message,onMessageAdded,onMessageSendFail);

        }}
    }

    OnSuccessListener onMessageAdded=new OnSuccessListener() {
        @Override
        public void onSuccess(Object o) {
            message.setText("");
        }
    };
    OnFailureListener onMessageSendFail=new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            showMessage(getString(R.string.error),e.getLocalizedMessage(),getString(R.string.ok));
        }
    };

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        message = (EditText) findViewById(R.id.message);
        send = (ImageButton) findViewById(R.id.sendo);
        send.setOnClickListener(this);
    }
}
