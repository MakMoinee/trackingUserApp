package com.thesis.trackinguserapp.services;

import com.google.firebase.auth.FirebaseAuth;
import com.thesis.trackinguserapp.interfaces.FirebaseListener;

public class LocalAuth {

    private FirebaseAuth auth;

    public LocalAuth() {
        this.auth = FirebaseAuth.getInstance();
    }

    public void signInWithEmailPassword(String email, String password, FirebaseListener listener) {

    }

    public void signOut() {
        auth.signOut();
    }
}
