package com.thesis.trackinguserapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.thesis.trackinguserapp.common.Common;
import com.thesis.trackinguserapp.databinding.ActivityMainBinding;
import com.thesis.trackinguserapp.dialogs.CustomProgress;
import com.thesis.trackinguserapp.interfaces.FirebaseListener;
import com.thesis.trackinguserapp.models.Users;
import com.thesis.trackinguserapp.persistence.DeviceTokenPref;
import com.thesis.trackinguserapp.persistence.MyUserPref;
import com.thesis.trackinguserapp.services.UserRequest;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    UserRequest userRequest;
    CustomProgress progress;
    AlertDialog pdLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        userRequest = new UserRequest(MainActivity.this);
        progress = new CustomProgress(MainActivity.this);
        progress.setDialog();
        pdLoad = progress.getDialog();
        setViews();
        setListeners();
    }

    private void setViews() {
        if (Common.deviceToken.equals("")) {
            Common.deviceToken = new DeviceTokenPref(MainActivity.this).getToken();
        }
        Users users = new MyUserPref(MainActivity.this).getUsers();
        if (users != null) {
            if (users.getDocID() != null) {
                if (users.getDocID() != "") {
                    Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }

    private void setListeners() {
        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.editEmail.getText().toString();
            String password = binding.editPassword.getText().toString();
            if (email.equals("") || password.equals("")) {
                Toast.makeText(this, "Please Don't Leave Empty Fields", Toast.LENGTH_SHORT).show();
            } else {
                pdLoad.show();
                Users u = new Users.UserBuilder()
                        .setEmail(email)
                        .setPassword(password)
                        .build();
                userRequest.getLogin(u, new FirebaseListener() {
                    @Override
                    public void onSuccessUser(Users users) {
                        pdLoad.dismiss();
                        new MyUserPref(MainActivity.this).storeLogin(users);
                        Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError() {
                        pdLoad.dismiss();
                        Toast.makeText(MainActivity.this, "Wrong Username or Password", Toast.LENGTH_SHORT).show();
                    }
                });
            }


        });

        binding.btnSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GoogleSignInActivity.class);
            startActivity(intent);
        });

        binding.btnCreateAccount.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Users users = new MyUserPref(MainActivity.this).getUsers();
        if (users != null) {
            if (users.getDocID() == null || users.getDocID().equals("")) {
                return;
            }
            Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        }
    }


}