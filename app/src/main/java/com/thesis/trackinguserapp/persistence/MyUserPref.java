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
        editor.putString("email",users.getEmail());
        editor.putString("firstName",users.getFirstName());
        editor.putString("middleName",users.getMiddleName());
        editor.putString("lastName",users.getLastName());
        editor.commit();
        editor.apply();
    }
}
