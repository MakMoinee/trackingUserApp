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
                .whereEqualTo("password", users.getPassword())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        listener.onError();
                    } else {
                        List<Users> usersList = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (documentSnapshot.exists()) {
                                Users u = documentSnapshot.toObject(Users.class);
                                if (u != null) {
                                    u.setDocID(documentSnapshot.getId());
                                    usersList.add(u);
                                }
                            }
                        }

                        if (usersList.size() > 0) {
                            listener.onSuccessUser(usersList.get(0));
                        } else {
                            listener.onError();
                        }
                    }
                })
                .addOnFailureListener(e -> listener.onError());
    }

    public void createUserAccount(Users users, FirebaseListener listener) {
        String id = fs.collection("users")
                .document().getId();
        Map<String, Object> params = MapForm.getUserMap(users);

        fs.collection("users")
                .document(id)
                .set(params, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        users.setDocID(id);
                        listener.onSuccessUser(users);
                    }
                })
                .addOnFailureListener(e -> {
                    if (e != null) {
                        Log.e("ERROR_CREATE_ACCOUNT", e.getLocalizedMessage());
                    }
                    listener.onError();
                });
    }
}

