package com.thesis.trackinguserapp.common;

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
}
