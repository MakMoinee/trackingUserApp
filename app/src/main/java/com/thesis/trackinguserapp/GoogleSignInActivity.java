package com.thesis.trackinguserapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.thesis.trackinguserapp.common.Constants;
import com.thesis.trackinguserapp.databinding.ActivityGoogleSiginBinding;
import com.thesis.trackinguserapp.interfaces.FirebaseListener;
import com.thesis.trackinguserapp.models.Users;
import com.thesis.trackinguserapp.persistence.MyUserPref;
import com.thesis.trackinguserapp.services.LocalEmail;
import com.thesis.trackinguserapp.services.UserRequest;

import java.util.List;

public class GoogleSignInActivity extends AppCompatActivity {

    ActivityGoogleSiginBinding binding;
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    ActivityResultLauncher<IntentSenderRequest> oneTapLauncher;
    UserRequest request;
    LocalEmail email;
    ProgressDialog pdLoad;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGoogleSiginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        request = new UserRequest(GoogleSignInActivity.this);
        email = new LocalEmail(GoogleSignInActivity.this);
        pdLoad = new ProgressDialog(GoogleSignInActivity.this);
        pdLoad.setMessage("Sending Request ...");
        pdLoad.setCancelable(false);
        setSignIn();
    }

    private void setSignIn() {
        oneTapLauncher = registerForActivityResult(
                new ActivityResultContracts.StartIntentSenderForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        try {
                            SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(result.getData());
                            String idToken = credential.getGoogleIdToken();
                            String username = credential.getId();
                            String password = credential.getPassword();
                            if (idToken != null) {
                                pdLoad.show();
                                // Got an ID token from Google. Use it to authenticate
                                // with your backend.
                                Users users = new Users();
                                users.setEmail(username);
                                users.setFirstName(credential.getGivenName());
                                users.setLastName(credential.getFamilyName());
                                users.setPassword("default");
                                request.getLogin(users, new FirebaseListener() {
                                    @Override
                                    public void onSuccessUser(Users uu) {
                                        pdLoad.dismiss();
                                        new MyUserPref(GoogleSignInActivity.this).storeLogin(uu);
                                        finish();
                                    }

                                    @Override
                                    public void onError() {
                                        request.createUserAccount(users, new FirebaseListener() {

                                            @Override
                                            public void onSuccessUser(Users u) {
                                                new MyUserPref(GoogleSignInActivity.this).storeLogin(u);
                                                email.sendEmail(u.getEmail(), Constants.welcomeEmailSubject, String.format(Constants.welcomeEmail, u.getFirstName(), Constants.emailAdd));
                                                pdLoad.dismiss();
                                                finish();
                                            }

                                            @Override
                                            public void onError() {
                                                pdLoad.dismiss();
                                                Toast.makeText(GoogleSignInActivity.this, "Failed to login using Google Account", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        });
                                    }
                                });

                            } else if (password != null) {
                                pdLoad.dismiss();
                                // Got a saved username and password. Use them to authenticate
                                // with your backend.
                                Log.e("err", "Got password.");

                            }
                        } catch (ApiException e) {
                            pdLoad.dismiss();
                            Log.e("err", e.getLocalizedMessage());
                            Toast.makeText(GoogleSignInActivity.this, "Failed to login using Google Account", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else {
                        pdLoad.dismiss();
                        Toast.makeText(GoogleSignInActivity.this, "Failed to login using Google Account", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
        oneTapClient = Identity.getSignInClient(this);
        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.web_client_id))
                        // Show all accounts on the device.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();


        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(beginSignInResult -> {
                    try {
                        oneTapLauncher.launch(new IntentSenderRequest.Builder(beginSignInResult.getPendingIntent().getIntentSender()).build());
                    } catch (Exception e) {
                        Log.e("oneTapClientFail", "Couldn't start One Tap UI: " + e.getLocalizedMessage());
                    }
                })
                .addOnFailureListener(e -> Log.e("oneTapClientFail", e.getLocalizedMessage()));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
