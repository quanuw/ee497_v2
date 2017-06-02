package com.example.yu.login;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.yu.login.data.DatabaseManager;
import com.google.android.gms.location.Geofence;

import static com.example.yu.login.GeofenceTransitionsIntentService.GEOFENCE_TRANSITION_DETAILS;
import static com.example.yu.login.GeofenceTransitionsIntentService.GEOFENCE_TRANSITION_TYPE;
import static com.example.yu.login.data.model.Trip.KEY_Date;
import static com.example.yu.login.data.model.Trip.KEY_Miles;
import static com.example.yu.login.data.model.Trip.KEY_State;
import static com.example.yu.login.data.model.Vehicle.KEY_VehicleId;
import static com.google.android.gms.wearable.DataMap.TAG;

/**
 * Created by Quan on 5/6/2017.
 */

// This class will listen to Geofence transtition events and start or stop the GPS service depending
// depending on the transition type. A notification will also be sent to the user.
public class GeofenceTransitionReceiver extends BroadcastReceiver {

    public static final String Tag = "GeofenceTransitionReceiver";
    private static final int LOCATION_REQUEST_CODE = 100;

    private int mId = 1;
    private OnTripEndListener callback;

    private boolean gpsOn = false;

    // Interface for adding trip
    public interface OnTripEndListener {
        public void onTripEnd(boolean end);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();

        if (extras.getString(GEOFENCE_TRANSITION_TYPE).equals(Geofence.GEOFENCE_TRANSITION_ENTER)) {
           startGpsService(context);
            gpsOn = true;
        }

        if (extras.getString(GEOFENCE_TRANSITION_TYPE).equals(Geofence.GEOFENCE_TRANSITION_EXIT)) {
            stopGpsService(context);
            if (gpsOn) {
                callback.onTripEnd(true);
                gpsOn = false;
            }
        }
        String details = extras.getString(GEOFENCE_TRANSITION_DETAILS);
        sendNotification(context, details);
    }

    private void startGpsService(Context context) {
        Intent startGps = new Intent(context, GPS_Service.class);
        context.startService(startGps);
        gpsOn = true;
        Log.e(TAG, "GPS SERVICE STARTED");
    }

    private void stopGpsService(Context context) {
        Intent stopGps = new Intent(context, GPS_Service.class);
        context.stopService(stopGps);

        gpsOn = false; // Turn flag off
        Log.e(TAG, "GPS SERVICE STOPPED");
    }

    // TODO: 5/6/17
    // Must get trip data
    private void saveTrip() {
        String miles = String.valueOf(0);
        String state = "N/A";
        String date = FragmentGPS.unixToPDT(System.currentTimeMillis());
        String vehicle = "";

        // Get a database manager
        DatabaseManager databaseManager = new DatabaseManager();

        // Open the database to write
        SQLiteDatabase writable = databaseManager.openDatabase();

        // Create a content values instance (kind of like a map)
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_Miles, miles);
        contentValues.put(KEY_State, state);
        contentValues.put(KEY_Date, date);
        contentValues.put(KEY_VehicleId, vehicle);

        // Insert the content values into the chosen table (Table)
        long result = writable.insert("Trip", null, contentValues);

        // If an error occured during insertion of row
        if (result == -1) {
            Log.e(TAG, "COULD NOT REGISTER TRIP");
            return;
        }
    }

    private void sendNotification(Context context, String notificationDetails) {
        // Go to menu from notification
        Intent notificationIntent = new Intent(context, MenuActivity.class);

        // Get a PendingIntent containing the entire back stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class).addNextIntent(notificationIntent);
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);



        // Define the notification settings.
        builder.setColor(Color.RED)
                .setContentTitle(notificationDetails)
                .setContentText("Click notification to return to App")
                .setContentIntent(notificationPendingIntent)
                .setAutoCancel(true)
                .setSmallIcon(android.R.drawable.alert_light_frame);

        // Fire and notify the built Notification.
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }

}
