package com.thesis.trackinguserapp.services;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thesis.trackinguserapp.R;
import com.thesis.trackinguserapp.models.TrackDevice;
import com.thesis.trackinguserapp.models.Users;
import com.thesis.trackinguserapp.persistence.MyUserPref;

public class LocalWorker extends Worker {

    Context mContext;
    String userID;

    public LocalWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.mContext = context;
        Users users = new MyUserPref(this.mContext).getUsers();
        this.userID = users.getDocID();
    }

    @NonNull
    @Override
    public Result doWork() {
        DatabaseReference statusRef = FirebaseDatabase.getInstance().getReference("devices");
        statusRef.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TrackDevice trackDevice = dataSnapshot.getValue(TrackDevice.class);
                if (trackDevice != null && trackDevice.getStatus() != null && trackDevice.getStatus().equalsIgnoreCase("stop")) {
                    // Trigger a notification
                    showNotification("Device Status", "The device is online. Please take immediate action");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if necessary
            }
        });

        // Indicate that the work is completed
        return Result.success();
    }

    private void showNotification(String title, String message) {
        // Create a notification using NotificationCompat.Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "channel_id")
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSmallIcon(R.drawable.dependent);

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(1, builder.build());
    }
}
