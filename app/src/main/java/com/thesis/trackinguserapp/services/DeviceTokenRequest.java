package com.thesis.trackinguserapp.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.thesis.trackinguserapp.common.MapForm;
import com.thesis.trackinguserapp.interfaces.FirebaseListener;
import com.thesis.trackinguserapp.models.DeviceToken;

import java.util.Map;

public class DeviceTokenRequest {

    FirebaseFirestore db;

    public DeviceTokenRequest() {
        db = FirebaseFirestore.getInstance();
    }


    public void createDeviceToken(DeviceToken deviceToken, FirebaseListener listener) {
        Map<String,Object> finalParam = MapForm.getDeviceTokenMap(deviceToken);
        db.collection("deviceTokens")
                .document(deviceToken.getDocID())
                .set(finalParam, SetOptions.merge())
                .addOnSuccessListener(unused -> listener.onSuccessAny(null))
                .addOnFailureListener(e -> {
                    if (e != null) {
                        Log.e("createDeviceToken_ERR", e.getLocalizedMessage());
                    }
                    listener.onError();
                });
    }
}
