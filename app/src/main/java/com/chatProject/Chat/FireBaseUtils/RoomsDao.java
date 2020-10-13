package com.chatProject.Chat.FireBaseUtils;

import com.chatProject.Chat.FireBaseUtils.Model.Room;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class RoomsDao {

    public static final String roomsBranch="rooms";
    public static DatabaseReference getRoomsRef(){
        return FirebaseDatabase.getInstance()
                .getReference(roomsBranch);
    }

    public static void addNewRoom(Room room, OnSuccessListener onSuccessListener, OnFailureListener onFailureListener){

        DatabaseReference newNode= getRoomsRef()
                .push();
        room.setId(newNode.getKey());
        newNode.setValue(room)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);


    }
}
