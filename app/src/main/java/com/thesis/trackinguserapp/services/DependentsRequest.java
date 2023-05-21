package com.thesis.trackinguserapp.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.thesis.trackinguserapp.interfaces.FirebaseListener;
import com.thesis.trackinguserapp.models.Dependents;

import java.util.ArrayList;
import java.util.List;

public class DependentsRequest {

    FirebaseFirestore fs;

    public DependentsRequest() {
        fs = FirebaseFirestore.getInstance();
    }


    public void getDependents(FirebaseListener listener) {
        fs.collection("dependents")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()) {
                            listener.onError();
                        } else {
                            List<Dependents> dependentsList = new ArrayList<>();
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                if (documentSnapshot.exists()) {
                                    Dependents dependents = documentSnapshot.toObject(Dependents.class);
                                    if (dependents != null) {
                                        dependents.setDocID(documentSnapshot.getId());
                                        dependentsList.add(dependents);
                                    }
                                }
                            }
                            if (dependentsList.size() > 0) {
                                listener.onSuccessAny(dependentsList);
                            } else {
                                listener.onError();
                            }
                        }


                    }
                })
                .addOnFailureListener(e -> {
                    if (e != null)
                        Log.e("GET_DEPENDENTS_ERR", e.getLocalizedMessage());
                    listener.onError();
                });
    }

    public void insertDependent(Dependents dependents, FirebaseListener listener) {

    }

    public void deleteDependent(String docID, FirebaseListener listener) {
        fs.collection("dependents")
                .document(docID)
                .delete()
                .addOnSuccessListener(unused -> listener.onSuccessAny(null))
                .addOnFailureListener(e -> {
                    if (e != null)
                        Log.e("DELETE_DEPENDENT_ERR", e.getLocalizedMessage());
                    listener.onError();
                });
    }
}
