package com.thesis.trackinguserapp.models;

import lombok.Data;

@Data
public class Users {
    private String docID;
    private String email;
    private String password;
    private String firstName;
    private String middleName;
    private String lastName;
}
