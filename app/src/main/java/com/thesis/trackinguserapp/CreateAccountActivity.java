package com.thesis.trackinguserapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.thesis.trackinguserapp.common.Constants;
import com.thesis.trackinguserapp.databinding.ActivityCreateAccountBinding;
import com.thesis.trackinguserapp.dialogs.CustomProgress;
import com.thesis.trackinguserapp.interfaces.FirebaseListener;
import com.thesis.trackinguserapp.models.Users;
import com.thesis.trackinguserapp.services.LocalEmail;
import com.thesis.trackinguserapp.services.UserRequest;

public class CreateAccountActivity extends AppCompatActivity {

    ActivityCreateAccountBinding binding;
    UserRequest userRequest;
    LocalEmail localEmail;
    CustomProgress progress;
    AlertDialog pdLoad;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        userRequest = new UserRequest(CreateAccountActivity.this);
        localEmail = new LocalEmail(CreateAccountActivity.this);
        progress = new CustomProgress(CreateAccountActivity.this);
        progress.setDialog();
        pdLoad = progress.getDialog();
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
                    pdLoad.show();
                    Users users = new Users.UserBuilder()
                            .setFirstName(firstName)
                            .setMiddleName(middleName)
                            .setLastName(lastName)
                            .setEmail(email)
                            .setPassword(password)
                            .build();
                    userRequest.createUserAccount(users, new FirebaseListener() {
                        @Override
                        public void onSuccessUser(Users u) {
                            pdLoad.dismiss();
                            localEmail.sendEmail(email, Constants.welcomeEmailSubject, String.format(Constants.welcomeEmail, firstName, Constants.emailAdd));
                            Toast.makeText(CreateAccountActivity.this, "Successfully Created Account", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onError() {
                            pdLoad.dismiss();
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
