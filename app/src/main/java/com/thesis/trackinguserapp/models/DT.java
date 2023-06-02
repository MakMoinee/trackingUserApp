package com.thesis.trackinguserapp.models;

import java.util.List;

import lombok.Data;

@Data
public class DT {
    private String deviceID;
    private String userID;
    private List<DeviceToken> deviceTokenList;
}
