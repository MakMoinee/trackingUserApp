package com.thesis.trackinguserapp.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.thesis.trackinguserapp.interfaces.FirebaseListener;
import com.thesis.trackinguserapp.models.Devices;
import com.thesis.trackinguserapp.models.History;

import java.util.ArrayList;
import java.util.List;

public class HistoryRequest {

    FirebaseFirestore db;

    public HistoryRequest() {
        db = FirebaseFirestore.getInstance();
    }

    public void getHistory(String deviceID, FirebaseListener listener) {
        db.collection("history")
                .whereEqualTo("SerialNumber", deviceID)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        listener.onError();
                    } else {
                        List<History> historyList = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (documentSnapshot.exists()) {
                                History history = documentSnapshot.toObject(History.class);
                                if (history != null) {
                                    historyList.add(history);
                                }
                            }
                        }

                        if (historyList.size() > 0) {
                            listener.onSuccessAny(historyList);
                        } else {
                            listener.onError();
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    if (e != null) {
                        Log.e("getHistory_err", e.getLocalizedMessage());
                    }
                    listener.onError();
                });
    }
}
