package com.thesis.trackinguserapp.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.thesis.trackinguserapp.R;
import com.thesis.trackinguserapp.TrackActivity;
import com.thesis.trackinguserapp.common.Common;
import com.thesis.trackinguserapp.interfaces.FirebaseListener;
import com.thesis.trackinguserapp.models.DeviceToken;

public class PushNotifService extends FirebaseMessagingService {
    private static final String CHANNEL_ID = "MyNotificationChannel";
    DeviceTokenRequest request = new DeviceTokenRequest();

    @Override
    public void onNewToken(@NonNull String token) {
        DeviceToken dt = new DeviceToken.DeviceTokenBuilder()
                .setDocID("KkKENNeNbCT4NMoawCCo")
                .setDeviceToken(token)
                .build();
        request.createDeviceToken(dt, new FirebaseListener() {
            @Override
            public <T> void onSuccessAny(T any) {
                Log.e("SAVE_TOKEN","SUCCESS");
            }

            @Override
            public void onError() {
                Log.e("SAVE_TOKEN","FAIL");
            }
        });
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            // Display notification
            showNotification(title, body);
        }
    }

    private void showNotification(String title, String body) {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Create a notification channel (required for Android Oreo and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "My Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("My Channel Description");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            notificationManager.createNotificationChannel(channel);
        }

        // Build the notification
        Intent intent = new Intent(this, TrackActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.dependent)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        // Show the notification
        notificationManager.notify(0, builder.build());
    }
}
