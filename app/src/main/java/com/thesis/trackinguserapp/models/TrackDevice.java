package com.thesis.trackinguserapp.models;

import lombok.Data;

@Data
public class TrackDevice {
    private String docID;
    private String status;
    private float latitude;
    private float longitude;
    private String lastCommunication;
}
