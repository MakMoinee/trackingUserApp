package com.thesis.trackinguserapp.interfaces;

public interface FragmentListener {
    void onHomeClick();

    void onSettingsClick();

    void exitApp();

    default void openDevices() {

    }

    default void openDependents(){

    }

    default void openTracking(){

    }
}
