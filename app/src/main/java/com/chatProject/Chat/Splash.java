package com.chatProject.Chat;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.chatProject.Base.BaseActivity;
import com.chatProject.Chat.FireBaseUtils.Model.User;
import com.google.gson.Gson;

public class Splash extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        String userLogin=getSharedPreferences("pref",MODE_PRIVATE).getString("user","");
        Boolean check=getSharedPreferences("pref",MODE_PRIVATE).getBoolean("check",false);
        if(check){
            Gson gson=new Gson();
       DataHolder.currentUser= gson.fromJson(userLogin, User.class);
            new Handler()
                    .postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(activity, HomeActivity.class));
                            finish();
                        }
                    },2000);

        }
        else{
        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(activity, Registration.class));
                        finish();
                    }
                },2000);
    }}
}
