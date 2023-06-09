package com.thesis.trackinguserapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.thesis.trackinguserapp.adapters.DeviceAdapter;
import com.thesis.trackinguserapp.common.Common;
import com.thesis.trackinguserapp.databinding.ActivityDevicesBinding;
import com.thesis.trackinguserapp.databinding.DialogAddDeviceBinding;
import com.thesis.trackinguserapp.interfaces.AdapterListener;
import com.thesis.trackinguserapp.interfaces.FirebaseListener;
import com.thesis.trackinguserapp.models.DT;
import com.thesis.trackinguserapp.models.DeviceToken;
import com.thesis.trackinguserapp.models.Devices;
import com.thesis.trackinguserapp.models.Users;
import com.thesis.trackinguserapp.persistence.MyUserPref;
import com.thesis.trackinguserapp.services.DeviceTokenRequest;
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
    DeviceTokenRequest deviceTokenRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDevicesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        request = new DevicesRequest(DevicesActivity.this);
        deviceTokenRequest = new DeviceTokenRequest();
        setTitle("Devices");
        loadData();
        setListeners();
    }

    private void setListeners() {
        binding.btnAddDevice.setOnClickListener(v -> showAddDeviceDialog());
    }

    private void showAddDeviceDialog() {
        if (devicesList.size() > 1) {
            Toast.makeText(DevicesActivity.this, "You can add only one device", Toast.LENGTH_SHORT).show();
            return;
        }
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
                addDeviceBinding.btnSave.setEnabled(false);
                Users users = new MyUserPref(DevicesActivity.this).getUsers();
                Devices devices = new Devices.DeviceBuilder()
                        .setDeviceID(deviceID)
                        .setDeviceUserID(Integer.parseInt(deviceUserID))
                        .setUserID(users.getDocID())
                        .build();
                request.addDevice(devices, new FirebaseListener() {
                    @Override
                    public <T> void onSuccessAny(T any) {
                        addDeviceBinding.btnSave.setEnabled(true);
                        Common.currentDeviceID = deviceID;
                        Toast.makeText(DevicesActivity.this, "Successfully Added Device", Toast.LENGTH_SHORT).show();
                        addDeviceDialog.dismiss();
                        binding.recycler.setAdapter(null);
                        loadData();
                    }

                    @Override
                    public void onError() {
                        addDeviceBinding.btnSave.setEnabled(true);
                        Toast.makeText(DevicesActivity.this, "Failed to add device", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void loadData() {
        Users users = new MyUserPref(DevicesActivity.this).getUsers();
        request.getDevices(users.getDocID(), new FirebaseListener() {
            @Override
            public <T> void onSuccessAny(T any) {
                if (any instanceof List<?>) {
                    List<?> tmpList = (List<?>) any;
                    if (tmpList.size() > 0) {
                        devicesList = (List<Devices>) tmpList;
                        if (devicesList.size() > 0) {
                            createTokens(devicesList);
                        }
                        adapter = new DeviceAdapter(DevicesActivity.this, devicesList, new AdapterListener() {
                            @Override
                            public void onClick(int position) {

                            }

                            @Override
                            public void onLongClick(int position) {
                                Devices d = devicesList.get(position);
                                showDeleteDevice(d);
                            }
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

    private void createTokens(List<Devices> d) {
        Users users = new MyUserPref(DevicesActivity.this).getUsers();
        Devices devices = d.get(0);
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

    private void showDeleteDevice(Devices d) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(DevicesActivity.this);
        DialogInterface.OnClickListener dListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_NEGATIVE:
                    request.deleteDevice(d.getDocID(), new FirebaseListener() {
                        @Override
                        public <T> void onSuccessAny(T any) {
                            Toast.makeText(DevicesActivity.this, "Successfully Deleted Device", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            binding.recycler.setAdapter(null);
                            loadData();
                        }

                        @Override
                        public void onError() {
                            Toast.makeText(DevicesActivity.this, "Failed to delete device, Please try again later", Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                default:
                    dialog.dismiss();
                    break;
            }
        };

        mBuilder.setMessage("Are You Sure You Want To Delete This Device?")
                .setNegativeButton("Yes, Proceed", dListener)
                .setPositiveButton("No", dListener)
                .setCancelable(false)
                .show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }


}
