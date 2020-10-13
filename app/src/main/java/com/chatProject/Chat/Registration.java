package com.chatProject.Chat;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chatProject.Base.BaseActivity;
import com.chatProject.Chat.FireBaseUtils.Model.User;
import com.chatProject.Chat.FireBaseUtils.UsersDao;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
public class Registration extends BaseActivity implements View.OnClickListener {

    protected EditText userName;
    protected EditText email;
    protected EditText password;
    protected Button register;
    protected TextView login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registration);
        Toolbar toolbar = findViewById(R.id.action_bar);
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
             startActivity(new Intent(this,Login.class));
                break;
            case R.id.Regeristation:
                break;
        }//endSwitch
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.register) {
            //validation

            final String sUsername = userName.getText().toString();
            final String sEmail = email.getText().toString();
            final String sPassword = password.getText().toString();
            if (sUsername.trim().length() == 0) {
                userName.setError(getString(R.string.required));
                return;
            }
            userName.setError(null);
            if (!isValidEmail(sEmail)) {
                email.setError(getString(R.string.invalidEmail));
                return;
            }
            email.setError(null);
            if (sPassword.length() < 6) {
                password.setError(getString(R.string.passwordError));
                return;
            }
            password.setError(null);

            showProgressBar(R.string.loading);
            UsersDao.getUsersByEmail(sEmail)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            hideProgressBar();

                            if (dataSnapshot.hasChildren()) {
                                showMessage(R.string.error, R.string.email_registerd_before, R.string.ok);
                            } else {
                                final User user = new User(sUsername, sEmail, sPassword);
                                registerUser(user);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            hideProgressBar();
                            showMessage(getString(R.string.error), databaseError.getMessage(), getString(R.string.ok));

                        }
                    });


        } else if (view.getId() == R.id.login) {
            startActivity(new Intent(activity, Login.class));
            finish();

        }


    }

    private void registerUser(final User user) {

        showProgressBar(R.string.loading);
        UsersDao.AddNewUser(user, new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                //startActivity
                hideProgressBar();
                DataHolder.currentUser = user;
                showConfirmationMessage(R.string.success, R.string.registered_successfully, R.string.ok
                        , new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                startActivity(new Intent(activity, HomeActivity.class));
                                finish();
                            }
                        })
                        .setCancelable(false);
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideProgressBar();
                showMessage(getString(R.string.success), e.getMessage(), getString(R.string.ok));

            }
        });
    }


    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private void initView() {
        userName = (EditText) findViewById(R.id.user_name);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(Registration.this);
        login = (TextView) findViewById(R.id.login);
        login.setOnClickListener(Registration.this);
    }
}
