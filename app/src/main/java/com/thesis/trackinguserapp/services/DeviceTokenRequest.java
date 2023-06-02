package com.thesis.trackinguserapp.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.thesis.trackinguserapp.common.Common;
import com.thesis.trackinguserapp.common.Constants;
import com.thesis.trackinguserapp.common.MapForm;
import com.thesis.trackinguserapp.interfaces.FirebaseListener;
import com.thesis.trackinguserapp.models.DT;
import com.thesis.trackinguserapp.models.DeviceToken;

import java.util.List;
import java.util.Map;

public class DeviceTokenRequest {

    FirebaseFirestore db;

    public DeviceTokenRequest() {
        db = FirebaseFirestore.getInstance();
    }


    public void createDeviceToken(DT dt, FirebaseListener listener) {
        Map<String, Object> finalParam = MapForm.getDeviceTokenMap(dt);
        DocumentReference docRef = db.collection("deviceTokens")
                .document(dt.getDeviceID());
        docRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        List<Map<String, Object>> deviceTokensList = (List<Map<String, Object>>) documentSnapshot.get("deviceTokens");
                        Boolean breakLoop = false;
                        for (DeviceToken dtt : dt.getDeviceTokenList()) {
                            for (Map<String, Object> rdt : deviceTokensList) {
                                if (rdt.containsValue(dtt.getDeviceToken())) {
                                    breakLoop = true;
                                    break;
                                }
                            }
                            if (breakLoop) {
                                break;
                            }
                            deviceTokensList.add(MapForm.getDeviceTokenMap(dtt));
                        }
                        if (!breakLoop && deviceTokensList.size() > 0) {
                            docRef.update("deviceTokens", deviceTokensList)
                                    .addOnSuccessListener(aVoid -> listener.onSuccessAny(null))
                                    .addOnFailureListener(e -> {
                                        if (e != null) {
                                            Log.e("createDeviceToken_ERR", e.getLocalizedMessage());
                                        }
                                        listener.onError();
                                    });
                        } else {
                            Log.e("createDeviceToken", "existing no need to update document");
                            listener.onSuccessAny(null); // existing no need to update document
                        }
                    }else{
                        docRef.set(finalParam, SetOptions.merge())
                                .addOnSuccessListener(unused -> listener.onSuccessAny(null))
                                .addOnFailureListener(e2 -> {
                                    if (e2 != null) {
                                        Log.e("createDeviceToken_ERR", e2.getLocalizedMessage());
                                    }
                                    listener.onError();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    if (e != null) {
                        Log.e("createDeviceToken_ERR", e.getLocalizedMessage());
                    }
                    docRef.set(finalParam, SetOptions.merge())
                            .addOnSuccessListener(unused -> listener.onSuccessAny(null))
                            .addOnFailureListener(e2 -> {
                                if (e2 != null) {
                                    Log.e("createDeviceToken_ERR", e2.getLocalizedMessage());
                                }
                                listener.onError();
                            });
                });

    }

    public void deleteOldTokens(DT dt, FirebaseListener listener) {
        DocumentReference docRef = db.collection("deviceTokens")
                .document(dt.getDeviceID());
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            List<Map<String, Object>> deviceTokensList = (List<Map<String, Object>>) documentSnapshot.get("deviceTokens");
                            int indexToRemove = -1;
                            for (int i = 0; i < deviceTokensList.size(); i++) {
                                Map<String, Object> token = deviceTokensList.get(i);
                                if (token.get("deviceToken").equals(Common.deviceToken)) {
                                    indexToRemove = i;
                                    break;
                                }
                            }

                            if (indexToRemove != -1) {
                                // Remove the deviceToken from the array
                                deviceTokensList.remove(indexToRemove);

                                // Update the document with the modified deviceTokens array
                                docRef.update("deviceTokens", deviceTokensList)
                                        .addOnSuccessListener(aVoid -> listener.onSuccessAny(null))
                                        .addOnFailureListener(e -> {
                                            if (e != null) {
                                                Log.e("deleteOldTokens_ERR", e.getLocalizedMessage());
                                            }
                                            listener.onError();
                                        });
                            } else {
                                listener.onSuccessAny(null);
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    if (e != null) {
                        Log.e("deleteOldTokens_ERR", e.getLocalizedMessage());
                    }
                    listener.onError();
                });
    }
}
