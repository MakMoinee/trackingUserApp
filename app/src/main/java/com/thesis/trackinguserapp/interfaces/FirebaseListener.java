package com.thesis.trackinguserapp.interfaces;

import com.thesis.trackinguserapp.models.Users;

import java.util.List;

public interface FirebaseListener {

    default void onSuccessListOfUsers(List<Users> usersList) {

    }

    default void onSuccessUser(Users users){

    }

    void onError();
}
