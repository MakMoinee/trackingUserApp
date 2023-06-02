package com.thesis.trackinguserapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.thesis.trackinguserapp.common.Common;
import com.thesis.trackinguserapp.databinding.ActivityDashboardBinding;
import com.thesis.trackinguserapp.fragments.HomeFragment;
import com.thesis.trackinguserapp.fragments.SettingsFragment;
import com.thesis.trackinguserapp.interfaces.FirebaseListener;
import com.thesis.trackinguserapp.interfaces.FragmentListener;
import com.thesis.trackinguserapp.models.DT;
import com.thesis.trackinguserapp.models.DeviceToken;
import com.thesis.trackinguserapp.models.Devices;
import com.thesis.trackinguserapp.models.Users;
import com.thesis.trackinguserapp.persistence.MyUserPref;
import com.thesis.trackinguserapp.services.DeviceTokenRequest;
import com.thesis.trackinguserapp.services.DevicesRequest;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity implements FragmentListener {

    ActivityDashboardBinding binding;
    Fragment fragment;
    FragmentTransaction ft;
    FragmentManager fm;
    DevicesRequest request;
    DeviceTokenRequest deviceTokenRequest;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        request = new DevicesRequest(DashboardActivity.this);
        deviceTokenRequest = new DeviceTokenRequest();
        setListeners();
        onHomeClick();
        loadDevices();
    }

    private void loadDevices() {
        Users users = new MyUserPref(DashboardActivity.this).getUsers();
        request.getDevices(users.getDocID(), new FirebaseListener() {
            @Override
            public <T> void onSuccessAny(T any) {

                if (any instanceof List<?>) {
                    List<?> tmpList = (List<?>) any;
                    if (tmpList.size() > 0) {
                        List<Devices> devicesList = (List<Devices>) tmpList;
                        Devices devices = devicesList.get(0);
                        Log.e("HERE", devices.toString());
                        Common.currentDeviceID = devices.getDeviceID();
                        DeviceToken deviceToken = new DeviceToken.DeviceTokenBuilder()
                                .setDeviceToken(Common.deviceToken)
                                .setUserID(users.getDocID())
                                .build();
                        List<DeviceToken> deviceTokenList = new ArrayList<>();
                        deviceTokenList.add(deviceToken);
                        DT dt = new DT();
                        dt.setDeviceID(devices.getDeviceID());
                        dt.setDeviceTokenList(deviceTokenList);
                        deviceTokenRequest = new DeviceTokenRequest();
                        deviceTokenRequest.createDeviceToken(dt, new FirebaseListener() {

                            @Override
                            public <T> void onSuccessAny(T any) {
                                Log.e("DEVICE_CREATE_TOKEN", "success");
                            }

                            @Override
                            public void onError() {
                                Log.e("DEVICE_CREATE_TOKENERR", "error creating token");
                            }
                        });
                    }
                }
            }

            @Override
            public void onError() {
                Log.e("getDevices_err", "empty devices");
            }
        });
    }

    private void setListeners() {
        binding.bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_home) {
                onHomeClick();
                return true;
            } else if (id == R.id.action_settings) {
                onSettingsClick();
                return true;
            } else {
                return false;
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onHomeClick() {
        fragment = new HomeFragment(DashboardActivity.this, DashboardActivity.this);
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.frame, fragment);
        ft.commit();
        setTitle("Home");
    }

    @Override
    public void onSettingsClick() {
        fragment = new SettingsFragment(DashboardActivity.this, DashboardActivity.this);
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.frame, fragment);
        ft.commit();
        setTitle("Settings");
    }

    @Override
    public void exitApp() {
        try {
            if (!Common.currentDeviceID.equals("")) {
                DT dt = new DT();
                dt.setDeviceID(Common.currentDeviceID);
                deviceTokenRequest.deleteOldTokens(dt, new FirebaseListener() {
                    @Override
                    public <T> void onSuccessAny(T any) {
                        new MyUserPref(DashboardActivity.this).storeLogin(new Users());
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(DashboardActivity.this, "Successfully Logout", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(DashboardActivity.this, "Failed to log out", Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                new MyUserPref(DashboardActivity.this).storeLogin(new Users());
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(DashboardActivity.this, "Successfully Logout", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        } catch (Exception e) {
            new MyUserPref(DashboardActivity.this).storeLogin(new Users());
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(DashboardActivity.this, "Successfully Logout", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void openDevices() {
        Intent intent = new Intent(DashboardActivity.this, DevicesActivity.class);
        startActivity(intent);
    }

    @Override
    public void openDependents() {
        Intent intent = new Intent(DashboardActivity.this, DependentsActivity.class);
        startActivity(intent);
    }

    @Override
    public void openTracking() {
        Intent intent = new Intent(DashboardActivity.this, TrackActivity.class);
        startActivity(intent);
    }

    @Override
    public void openHistory() {
        Intent intent = new Intent(DashboardActivity.this, HistoryActivity.class);
        startActivity(intent);
    }
}
