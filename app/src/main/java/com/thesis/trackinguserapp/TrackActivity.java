package com.thesis.trackinguserapp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.thesis.trackinguserapp.databinding.ActivityTrackingBinding;

public class TrackActivity extends AppCompatActivity {

    ActivityTrackingBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrackingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
