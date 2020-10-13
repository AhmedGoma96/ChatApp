package com.chatProject.Chat;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.chatProject.Base.BaseActivity;
import com.chatProject.Chat.FireBaseUtils.Model.Room;
import com.chatProject.Chat.FireBaseUtils.RoomsDao;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddRoom extends BaseActivity implements View.OnClickListener {

    protected EditText roomName;
    protected EditText roomDesc;
    protected Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_add_room);
        initView();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add) {
            String name = roomName.getText().toString();
            String desc = roomDesc.getText().toString();
            if(name.trim().isEmpty()){
                Toast.makeText(this,"please enter room name",Toast.LENGTH_LONG).show();
            }
            else if(desc.trim().isEmpty()){
                Toast.makeText(this,"please enter room desc",Toast.LENGTH_LONG).show();
            } else{
            Room room =new Room();
            room.setName(name);
            room.setDescription(desc);
            SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            String currentTime = s.format(new Date());
            room.setCreatedAt(currentTime);
            room.setCurrentActiveUsers(0);
            showProgressBar(R.string.loading);
            RoomsDao.addNewRoom(room
                    , new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            hideProgressBar();
                            showConfirmationMessage(R.string.success, R.string.room_added_successfully, R.string.ok
                                    , new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            finish();
                                        }
                                    })
                                    .setCancelable(false);
                        }
                    }, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            hideProgressBar();
                            showMessage(getString(R.string.success),
                                    e.getMessage(),getString(R.string.ok));

                        }
                    });


        }}
    }

    private void initView() {
        roomName = (EditText) findViewById(R.id.room_name);
        roomDesc = (EditText) findViewById(R.id.room_desc);
        add = (Button) findViewById(R.id.add);
        add.setOnClickListener(AddRoom.this);
    }
}
