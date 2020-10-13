package com.chatProject.Chat;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.chatProject.Base.BaseActivity;
import com.chatProject.Chat.FireBaseUtils.Model.User;
import com.chatProject.Chat.FireBaseUtils.UsersDao;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class Login extends BaseActivity implements View.OnClickListener {
    protected EditText email;
    protected EditText password;
    protected Button Login;
    protected TextView register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.action_bar_login);
        setSupportActionBar(toolbar);
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_inflater,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.login:
                break;
            case R.id.Regeristation:
                startActivity(new Intent(this,Registration.class));
                break;
        }//endSwitch
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.Login) {
            String sEmail=email.getText().toString();
            final String sPassword=password.getText().toString();
            showProgressBar(R.string.loading);
            UsersDao.getUsersByEmail(sEmail)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            hideProgressBar();
                            if(!dataSnapshot.hasChildren()){
                                showMessage(R.string.error,R.string.invalide_email_or_password,R.string.ok);
                            }else {
                                for(DataSnapshot object:dataSnapshot.getChildren()){
                                    User user=object.getValue(User.class);
                                    if(user.getPassword().equals(sPassword)){
                                        //redirect to home
                                        DataHolder.currentUser=user;
                                        Gson gson=new Gson();
                                 String userLogin= gson.toJson(user);
                         SharedPreferences.Editor editor= getSharedPreferences("pref",MODE_PRIVATE).edit();
                                 editor.putString("user",userLogin);
                                 editor.putBoolean("check",true);
                                 editor.apply();

                                        startActivity(new Intent(activity,HomeActivity.class));
                                        finish();
                                    }
                                }
                               // showMessage(R.string.error,R.string.invalide_email_or_password,R.string.ok);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            hideProgressBar();
                            showMessage(getString(R.string.error),databaseError.getMessage(),getString(R.string.ok));

                        }
                    });

        } else if (view.getId() == R.id.register) {
            startActivity(new Intent(activity, com.chatProject.Chat.Registration.class));
            finish();

        }
    }

    private void initView() {
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        Login = (Button) findViewById(R.id.Login);
        Login.setOnClickListener(Login.this);
        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(Login.this);
    }
}
