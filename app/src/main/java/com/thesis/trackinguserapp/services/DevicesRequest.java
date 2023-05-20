package com.thesis.trackinguserapp.services;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.thesis.trackinguserapp.common.MapForm;
import com.thesis.trackinguserapp.interfaces.FirebaseListener;
import com.thesis.trackinguserapp.models.Devices;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DevicesRequest {

    Context mContext;
    FirebaseFirestore db;

    public DevicesRequest(Context mContext) {
        this.mContext = mContext;
        db = FirebaseFirestore.getInstance();
    }


    public void getDevices(FirebaseListener listener) {
        db.collection("devices")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            listener.onError();
                        } else {
                            List<Devices> devicesList = new ArrayList<>();
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                if (documentSnapshot.exists()) {
                                    Devices devices = documentSnapshot.toObject(Devices.class);
                                    if (devices != null) {
                                        devices.setDocID(documentSnapshot.getId());
                                        devicesList.add(devices);
                                    }
                                }
                            }
                            if (devicesList.size() > 0) {
                                listener.onSuccessAny(devicesList);
                            } else {
                                listener.onError();
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    if (e != null) {
                        Log.e("GET_DEVICES_ERR", e.getLocalizedMessage());
                    }
                    listener.onError();
                });
    }

    public void addDevice(Devices devices, FirebaseListener listener) {
        Map<String, Object> param = MapForm.getDeviceMap(devices);
        String id = db.collection("devices")
                .document().getId();
        db.collection("devices")
                .document(id)
                .set(param)
                .addOnSuccessListener(unused -> listener.onSuccessAny(null))
                .addOnFailureListener(e -> {
                    if (e != null) {
                        Log.e("ADD_DEVICE_ERR", e.getLocalizedMessage());
                    }
                    listener.onError();
                });
    }

    public void deleteDevice(String docID, FirebaseListener listener) {
        db.collection("devices")
                .document(docID)
                .delete()
                .addOnSuccessListener(unused -> listener.onSuccessAny(null))
                .addOnFailureListener(e -> {
                    if (e != null) {
                        Log.e("DELETE_DEVICE_ERR", e.getLocalizedMessage());
                    }
                    listener.onError();
                });
    }
}
