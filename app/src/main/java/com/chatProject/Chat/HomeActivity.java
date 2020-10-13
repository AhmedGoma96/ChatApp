package com.chatProject.Chat;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chatProject.Chat.FireBaseUtils.MessagesDao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.chatProject.Adapters.ChatRoomsAdapter;
import com.chatProject.Base.BaseActivity;
import com.chatProject.Chat.FireBaseUtils.Model.Room;
import com.chatProject.Chat.FireBaseUtils.RoomsDao;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {

    RecyclerView recyclerView;
    ChatRoomsAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(activity);
        adapter= new ChatRoomsAdapter(null);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ChatRoomsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos, Room room) {
                ChatRoom.currentRoom=room;
                startActivity(new Intent(activity,ChatRoom.class));
            }
        });

        FloatingActionButton fab =  findViewById(R.id.add_room);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity,AddRoom.class));
             }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                RoomsDao.getRoomsRef().child(adapter.getRoomAtPos(viewHolder.getAdapterPosition()).getId()).removeValue();
                Query query=MessagesDao.getMessagesByRoomId(adapter.getRoomAtPos(viewHolder.getAdapterPosition()).getId());
            query.getRef().removeValue();
                Toast.makeText(getApplicationContext(),"Room Deleted",Toast.LENGTH_LONG).show();

            }
        }).attachToRecyclerView(recyclerView);
        RoomsDao.getRoomsRef()
                .addValueEventListener(roomsValueEventListener);
    }


    ValueEventListener roomsValueEventListener= new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List<Room> rooms = new ArrayList<>();
            for(DataSnapshot roomdata :dataSnapshot.getChildren()){
                Room room = roomdata.getValue(Room.class);
                rooms.add(room);
            }
            adapter.changeData(rooms);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            showMessage(getString(R.string.error),databaseError.getMessage(),getString(R.string.ok));
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RoomsDao.getRoomsRef().
                removeEventListener(roomsValueEventListener);

    }
}
