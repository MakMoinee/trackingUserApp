package com.thesis.trackinguserapp.persistence;

import android.content.Context;
import android.content.SharedPreferences;

import com.thesis.trackinguserapp.models.Users;

public class MyUserPref {
    Context mContext;
    SharedPreferences pref;

    public MyUserPref(Context mContext) {
        this.mContext = mContext;
        this.pref = mContext.getSharedPreferences("users", Context.MODE_PRIVATE);
    }

    public void storeLogin(Users users) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("docID", users.getDocID());
        editor.putString("email", users.getEmail());
        editor.putString("firstName", users.getFirstName());
        editor.putString("middleName", users.getMiddleName());
        editor.putString("lastName", users.getLastName());
        editor.putBoolean("isLogin", true);
        editor.commit();
        editor.apply();
    }

    public Users getUsers() {
        Users users = new Users.UserBuilder()
                .setDocID(pref.getString("docID", ""))
                .setEmail(pref.getString("email", ""))
                .setFirstName(pref.getString("firstName", ""))
                .setMiddleName(pref.getString("middleName", ""))
                .setLastName(pref.getString("lastName", ""))
                .build();

        return users;
    }

    public Boolean isLoggedIn() {
        Boolean isLogin = pref.getBoolean("isLogin", false);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isLogin", false);
        editor.commit();
        editor.apply();
        return isLogin;
    }
}
