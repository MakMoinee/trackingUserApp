package com.thesis.trackinguserapp.models;

import lombok.Data;

@Data
public class History {
    private String LastCommunication;
    private Float Latitude;
    private Float Longitude;
    private String SerialNumber;
    private String Status;
}
