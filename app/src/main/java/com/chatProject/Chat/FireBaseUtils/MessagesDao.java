package com.chatProject.Chat.FireBaseUtils;

import com.chatProject.Chat.FireBaseUtils.Model.Message;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;



public class MessagesDao {

    public static final String messagesBranch="messages";
    public static DatabaseReference getMessagesRef(){
        return FirebaseDatabase.getInstance()
                .getReference(messagesBranch);
    }


    public static void sendMessage(Message message, OnSuccessListener onSuccessListener,
                                   OnFailureListener onFailureListener){
        DatabaseReference messageNode=getMessagesRef()
                .push();
        message.setId(messageNode.getKey());
        messageNode.setValue(message)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);

    }

   public static Query getMessagesByRoomId(String roomId){

       Query query= getMessagesRef()
                .orderByChild("roomId")
                .equalTo(roomId);
       return query;
    }
}
