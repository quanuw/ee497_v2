package com.example.yu.login;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Quan on 4/10/2017.
 */

public class GeofenceTransitionsIntentService extends IntentService {
    protected static final String TAG = "GeofenceTransitionsIS";
    public static final String GEOFENCE_TRANSITION_ACTION = "GeofenceTransition";
    public static final String GEOFENCE_TRANSITION_TYPE = "geofenceTransitionType";
    public static final String GEOFENCE_TRANSITION_DETAILS = "geofenceTransitionDetails";

    public GeofenceTransitionsIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GeofencingEvent event = GeofencingEvent.fromIntent(intent);

        Intent broadcastIntent = new Intent();
        intent.setAction(GEOFENCE_TRANSITION_ACTION);
        if (event.hasError()) {
            Log.e(TAG, "GeofencingEvent Error: " + event.getErrorCode());
            return;
        }
        // Turn on gps switch automatically if leaving Geofence. Turn off if in Geofence.
        if (event.getGeofenceTransition() == Geofence.GEOFENCE_TRANSITION_EXIT) {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
            editor.putBoolean("gps", true);
            editor.commit();
            broadcastIntent.putExtra(GEOFENCE_TRANSITION_TYPE, Geofence.GEOFENCE_TRANSITION_EXIT);
            Toast.makeText(getApplicationContext(), "TURNED ON GPS!", Toast.LENGTH_LONG).show();
        } else {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
            editor.putBoolean("gps", false);
            editor.commit();
            broadcastIntent.putExtra(GEOFENCE_TRANSITION_TYPE, Geofence.GEOFENCE_TRANSITION_ENTER);
            Toast.makeText(getApplicationContext(), "TURNED OFF GPS!", Toast.LENGTH_LONG).show();
        }
        String description = getGeofenceTransitionDetails(event);
        broadcastIntent.putExtra(GEOFENCE_TRANSITION_DETAILS, description);
        sendBroadcast(broadcastIntent); // Send a broadcast intent to auto gps control
        sendNotification(description);



        Log.v(TAG, "in onHandleIntent");
    }

    private static String getGeofenceTransitionDetails(GeofencingEvent event) {
        String transitionString =
                GeofenceStatusCodes.getStatusCodeString(event.getGeofenceTransition());
        List triggeringIDs = new ArrayList();
        for (Geofence geofence : event.getTriggeringGeofences()) {
            triggeringIDs.add(geofence.getRequestId());
            Log.e(TAG, geofence.getRequestId());
        }
        Log.v(TAG, String.format("%s: %s", transitionString, TextUtils.join(", ", triggeringIDs)));
        return String.format("%s: %s", transitionString, TextUtils.join(", ", triggeringIDs));
    }

    private void sendNotification(String notificationDetails) {
        // Create an explicit content Intent that starts MainActivity.
        // Somehow need to go to gps settings so user can change it.
        // Or can activiate gs once user clicks on the notification.
        // Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);

        Intent notificationIntent = new Intent(getApplicationContext(), MenuActivity.class);

        // Get a PendingIntent containing the entire back stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class).addNextIntent(notificationIntent);
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        // Define the notification settings.
        builder.setColor(Color.RED)
                .setContentTitle(notificationDetails)
                .setContentText("Click notification to return to App")
                .setContentIntent(notificationPendingIntent)
                .setAutoCancel(true)
                .setSmallIcon(android.R.drawable.alert_light_frame);

        // Fire and notify the built Notification.
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }
}
