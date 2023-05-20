package com.thesis.trackinguserapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.thesis.trackinguserapp.adapters.DeviceAdapter;
import com.thesis.trackinguserapp.databinding.ActivityDevicesBinding;
import com.thesis.trackinguserapp.databinding.DialogAddDeviceBinding;
import com.thesis.trackinguserapp.interfaces.AdapterListener;
import com.thesis.trackinguserapp.interfaces.FirebaseListener;
import com.thesis.trackinguserapp.models.Devices;
import com.thesis.trackinguserapp.models.Users;
import com.thesis.trackinguserapp.persistence.MyUserPref;
import com.thesis.trackinguserapp.services.DevicesRequest;

import java.util.ArrayList;
import java.util.List;

public class DevicesActivity extends AppCompatActivity {

    ActivityDevicesBinding binding;
    DevicesRequest request;
    List<Devices> devicesList = new ArrayList<>();
    DeviceAdapter adapter;
    DialogAddDeviceBinding addDeviceBinding;
    AlertDialog addDeviceDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDevicesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        request = new DevicesRequest(DevicesActivity.this);
        setTitle("Devices");
        loadData();
        setListeners();
    }

    private void setListeners() {
        binding.btnAddDevice.setOnClickListener(v -> showAddDeviceDialog());
    }

    private void showAddDeviceDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(DevicesActivity.this);
        addDeviceBinding = DialogAddDeviceBinding.inflate(getLayoutInflater(), null, false);
        setDialogListeners();
        mBuilder.setView(addDeviceBinding.getRoot());
        addDeviceDialog = mBuilder.create();
        addDeviceDialog.show();
    }

    private void setDialogListeners() {
        addDeviceBinding.btnSave.setOnClickListener(v -> {
            String deviceID = addDeviceBinding.editDeviceID.getText().toString();
            String deviceUserID = addDeviceBinding.editDeviceUserID.getText().toString();

            if (deviceID.equals("") || deviceUserID.equals("")) {
                Toast.makeText(DevicesActivity.this, "Please Don't Leave Empty Fields", Toast.LENGTH_SHORT).show();
            } else {
                Users users = new MyUserPref(DevicesActivity.this).getUsers();
                Devices devices = new Devices.DeviceBuilder()
                        .setDeviceID(Integer.parseInt(deviceID))
                        .setDeviceUserID(Integer.parseInt(deviceUserID))
                        .setUserID(users.getDocID())
                        .build();
                request.addDevice(devices, new FirebaseListener() {
                    @Override
                    public <T> void onSuccessAny(T any) {
                        Toast.makeText(DevicesActivity.this, "Successfully Added Device", Toast.LENGTH_SHORT).show();
                        binding.recycler.setAdapter(null);
                        loadData();
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(DevicesActivity.this, "Failed to add device", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void loadData() {
        request.getDevices(new FirebaseListener() {
            @Override
            public <T> void onSuccessAny(T any) {
                if (any instanceof List<?>) {
                    List<?> tmpList = (List<?>) any;
                    if (tmpList.size() > 0) {
                        devicesList = (List<Devices>) tmpList;
                        adapter = new DeviceAdapter(DevicesActivity.this, devicesList, position -> {

                        });
                        binding.recycler.setLayoutManager(new LinearLayoutManager(DevicesActivity.this));
                        binding.recycler.setAdapter(adapter);

                    }
                }
            }

            @Override
            public void onError() {
                Toast.makeText(DevicesActivity.this, "There are no devices added", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }


}
