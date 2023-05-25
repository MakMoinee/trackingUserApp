package com.thesis.trackinguserapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.thesis.trackinguserapp.databinding.ActivityCreateAccountBinding;
import com.thesis.trackinguserapp.interfaces.FirebaseListener;
import com.thesis.trackinguserapp.models.Users;
import com.thesis.trackinguserapp.services.UserRequest;

public class CreateAccountActivity extends AppCompatActivity {

    ActivityCreateAccountBinding binding;
    UserRequest userRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        userRequest = new UserRequest(CreateAccountActivity.this);
        setListeners();
    }

    private void setListeners() {
        binding.btnProceed.setOnClickListener(v -> {
            String email = binding.editEmail.getText().toString();
            String password = binding.editPassword.getText().toString();
            String confirmPassword = binding.editConfirmPass.getText().toString();
            String firstName = binding.editFirstName.getText().toString();
            String middleName = binding.editMiddleName.getText().toString();
            String lastName = binding.editLastName.getText().toString();
            if (email.equals("") || password.equals("") || confirmPassword.equals("") || firstName.equals("") || lastName.equals("")) {
                Toast.makeText(CreateAccountActivity.this, "Please Don't Leave Empty Fields", Toast.LENGTH_SHORT).show();
            } else {
                if (password.equals(confirmPassword)) {
                    Users users = new Users.UserBuilder()
                            .setFirstName(firstName)
                            .setMiddleName(middleName)
                            .setLastName(lastName)
                            .setEmail(email)
                            .setPassword(password)
                            .build();
                    userRequest.createUserAccount(users, new FirebaseListener() {
                        @Override
                        public <T> void onSuccessAny(T any) {
                            FirebaseListener.super.onSuccessAny(any);
                        }

                        @Override
                        public void onError() {
                            Toast.makeText(CreateAccountActivity.this, "Failed to create account, Please Try Again Later", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(CreateAccountActivity.this, "Password Don't Match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
