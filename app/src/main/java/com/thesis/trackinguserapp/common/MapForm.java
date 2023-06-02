package com.thesis.trackinguserapp.common;

import com.thesis.trackinguserapp.models.DT;
import com.thesis.trackinguserapp.models.Dependents;
import com.thesis.trackinguserapp.models.DeviceToken;
import com.thesis.trackinguserapp.models.Devices;
import com.thesis.trackinguserapp.models.Users;

import java.util.HashMap;
import java.util.Map;

public class MapForm {
    public static Map<String, Object> getUserMap(Users users) {
        Map<String, Object> params = new HashMap<>();
        params.put("email", users.getEmail());
        params.put("firstName", users.getFirstName());
        params.put("middleName", users.getMiddleName());
        params.put("lastName", users.getLastName());
        params.put("password", users.getPassword());

        return params;
    }

    public static Map<String, Object> getDeviceMap(Devices devices) {
        Map<String, Object> params = new HashMap<>();
        params.put("deviceID", devices.getDeviceID());
        params.put("deviceUserID", devices.getDeviceUserID());
        params.put("userID", devices.getUserID());
        params.put("dateCreated", devices.getDateCreated());

        return params;
    }

    public static Map<String, Object> getDependentMap(Dependents dependents) {
        Map<String, Object> params = new HashMap<>();
        params.put("dependentName", dependents.getDependentName());
        params.put("userID", dependents.getUserID());
        params.put("dependentEmail", dependents.getDependentEmail());
        params.put("dependentPhoneNumber", dependents.getDependentPhoneNumber());

        return params;
    }

    public static Map<String, Object> getDeviceTokenMap(DT dt) {
        Map<String, Object> params = new HashMap<>();
        params.put("deviceTokens", dt.getDeviceTokenList());

        return params;
    }


    public static Map<String, Object> getDeviceTokenMap(DeviceToken deviceToken) {
        Map<String, Object> params = new HashMap<>();
        params.put("deviceToken", deviceToken.getDeviceToken());
        params.put("userID", deviceToken.getUserID());
        return params;
    }
}
