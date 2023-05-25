package com.thesis.trackinguserapp.services.realtime;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RDBDeviceRequest {

    FirebaseDatabase db;

    public RDBDeviceRequest() {
        this.db = FirebaseDatabase.getInstance();
    }


    public DatabaseReference getReference(String name){
        return db.getReference(name);
    }
}
