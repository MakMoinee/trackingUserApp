package com.thesis.trackinguserapp;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.thesis.trackinguserapp.common.Common;
import com.thesis.trackinguserapp.databinding.ActivityTrackingBinding;
import com.thesis.trackinguserapp.interfaces.FirebaseListener;
import com.thesis.trackinguserapp.models.Dependents;
import com.thesis.trackinguserapp.models.Devices;
import com.thesis.trackinguserapp.models.TrackDevice;
import com.thesis.trackinguserapp.models.Users;
import com.thesis.trackinguserapp.persistence.MyUserPref;
import com.thesis.trackinguserapp.services.DependentsRequest;
import com.thesis.trackinguserapp.services.DevicesRequest;
import com.thesis.trackinguserapp.services.realtime.RDBDeviceRequest;

import java.util.List;

public class TrackActivity extends AppCompatActivity implements OnMapReadyCallback {

    ActivityTrackingBinding binding;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    Boolean locationPermissionGranted = false;
    GoogleMap mMap;
    Location lastKnownLocation;
    LatLng currentLocation;
    DevicesRequest devicesRequest;
    RDBDeviceRequest rdbDeviceRequest;
    Devices associatedDevice = new Devices();
    LatLng deviceLocation;
    Marker deviceMarker;
    DependentsRequest dependentsRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrackingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        devicesRequest = new DevicesRequest(TrackActivity.this);
        dependentsRequest = new DependentsRequest();
        rdbDeviceRequest = new RDBDeviceRequest();
        getLocationPermission();

    }

    private void fetchLocation() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            getLocationPermission();
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                5000,
                5000, new LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull Location location) {
                        Log.e("LOCATION:", location.toString());
                    }

                    @Override
                    public void onProviderEnabled(@NonNull String provider) {

                    }

                    @Override
                    public void onProviderDisabled(@NonNull String provider) {

                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }
                });
        boolean isavailable = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (isavailable) {

            Location loc = lm.getLastKnownLocation("gps");

            if (loc != null) {
                double latitude = loc.getLatitude();
                double longitude = loc.getLongitude();
                currentLocation = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions()
                        .position(currentLocation)
                        .title("Your Location")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                //  Toast.makeText(TrackActivity.this, "Longitude is  " + longitude + "   Latitude is   " + latitude, Toast.LENGTH_LONG).show();

            }
        }
    }

    private void initValues() {
        new Handler().postDelayed(() -> {
            if (currentLocation == null) {
                Toast.makeText(TrackActivity.this, "Current Location couldn't detected. Please turn on location services or move to an open space", Toast.LENGTH_SHORT).show();

            }
        }, 5000);
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            ActivityCompat.requestPermissions(this,
                    new String[]{ACCESS_COARSE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        fetchLocation();
        getDeviceLocation();
    }

    private void getDeviceLocation() {
        Users users = new MyUserPref(TrackActivity.this).getUsers();

        devicesRequest.getDevices(users.getDocID(), new FirebaseListener() {
            @Override
            public <T> void onSuccessAny(T any) {
                if (any instanceof List<?>) {
                    List<?> tmpList = (List<?>) any;
                    if (tmpList.size() > 0) {
                        List<Devices> devicesList = (List<Devices>) tmpList;
                        associatedDevice = devicesList.get(0);
                        addValueEvent("Device Location");


                    }
                }
            }

            @Override
            public void onError() {
                Toast.makeText(TrackActivity.this, "There is no device associated to you", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addValueEvent(String name) {
        rdbDeviceRequest.getReference("devices").child(associatedDevice.getDeviceID())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        TrackDevice trackDevice = snapshot.getValue(TrackDevice.class);
                        if (trackDevice != null) {
                            deviceLocation = new LatLng(trackDevice.getLatitude(), trackDevice.getLongitude());
                            if (deviceMarker != null) deviceMarker.remove();
                            String status = Common.getRightStatus(trackDevice.getStatus());
                            deviceMarker = mMap.addMarker(new MarkerOptions()
                                    .position(deviceLocation)
                                    .title(String.format("%s (%s)", name, status))
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(deviceLocation, 15));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        if (requestCode
                == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
                fetchLocation();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }
}
