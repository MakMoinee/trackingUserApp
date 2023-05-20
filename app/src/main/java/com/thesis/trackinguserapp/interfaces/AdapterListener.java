package com.thesis.trackinguserapp.interfaces;

public interface AdapterListener {
    void onClick(int position);

    default void onLongClick(int position) {

    }
}
