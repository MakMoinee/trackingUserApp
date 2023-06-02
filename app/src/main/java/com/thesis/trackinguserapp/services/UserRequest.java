package com.thesis.trackinguserapp.services;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.thesis.trackinguserapp.common.MapForm;
import com.thesis.trackinguserapp.interfaces.FirebaseListener;
import com.thesis.trackinguserapp.models.Users;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserRequest {
    Context mContext;
    FirebaseFirestore fs;

    public UserRequest(Context mContext) {
        this.mContext = mContext;
        this.fs = FirebaseFirestore.getInstance();
    }

    public void getLogin(Users users, FirebaseListener listener) {
        fs.collection("users")
                .whereEqualTo("email", users.getEmail())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        listener.onError();
                    } else {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (documentSnapshot.exists()) {
                                Users u = documentSnapshot.toObject(Users.class);
                                if (u != null) {
                                    if (u.getPassword().equals(users.getPassword())) {
                                        u.setDocID(documentSnapshot.getId());
                                        listener.onSuccessUser(u);
                                        break;
                                    } else {
                                        listener.onError();
                                        break;
                                    }

                                } else {
                                    listener.onError();
                                    break;
                                }
                            }
                        }

                    }
                })
                .addOnFailureListener(e -> listener.onError());
    }

    public void createUserAccount(Users users, FirebaseListener listener) {

        getLogin(users, new FirebaseListener() {
            @Override
            public <T> void onSuccessAny(T any) {
                listener.onError();
            }

            @Override
            public void onError() {
                String id = fs.collection("users")
                        .document().getId();
                Map<String, Object> params = MapForm.getUserMap(users);

                fs.collection("users")
                        .document(id)
                        .set(params, SetOptions.merge())
                        .addOnSuccessListener(unused -> {
                            users.setDocID(id);
                            listener.onSuccessUser(users);
                        })
                        .addOnFailureListener(e -> {
                            if (e != null) {
                                Log.e("ERROR_CREATE_ACCOUNT", e.getLocalizedMessage());
                            }
                            listener.onError();
                        });
            }
        });

    }
}

