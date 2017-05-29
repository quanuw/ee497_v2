package com.example.yu.login;

import android.Manifest;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.yu.login.data.model.Trip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static com.google.android.gms.wearable.DataMap.TAG;



/**
 * Created by Quan on 2/26/2017.
 */


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentGPS extends Fragment {


    private static final int LOCATION_REQUEST_CODE = 100;

    private ToggleButton gpsButton;
    private TextView textView;
    private TextView distance;
    private TextView speed;
    private ImageView imageView;
    private BroadcastReceiver broadcastReceiver;
    private float totalDistance = 0;
    private SQLiteDatabase db;

    private boolean gpsOn = false; // to decide whether or not to account for miles.

    // query list of vehicles from db and store the vehicle user chooses.
    private String myVechicle = "";   // keep track of which vehicle the user is in.

    public FragmentGPS() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_gps, container, false);
        distance = (TextView) rootview.findViewById(R.id.distance);
        speed = (TextView) rootview.findViewById(R.id.speed);
        Log.d(TAG, "onCreateView");
        // check runtime permissions (location).
        if (!runtime_permissions()) {
            Log.d(TAG, "Ask for permissions");
        }
        //The heart and mind of headless fragment is below line. It will keep the fragment alive during configuration change when activities and   //subsequent fragments are "put to death" and recreated
        setRetainInstance(true);

        return rootview;
    }

    // pre:
    // post: Creates broadcastReceiver that accepts intents with string "location_update".
    @Override
    public void onResume() {
        super.onResume();
        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent != null && intent.getExtras().get("distance") != null &&
                            intent.getExtras().get("speed") != null) {
                        totalDistance += (float) intent.getExtras().get("distance");
                        distance.setText(String.valueOf(totalDistance));
                        speed.setText(intent.getExtras().get("speed") + " mph");
                        Trip trip = new Trip(String.valueOf(totalDistance), "WA", "June 27th, 2017", 1);
                        insertTrip(trip);
                    }
                }
            };
        }
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("location_update"));
    }

    // pre:
    // post: Destroy broadcastReceiver after it is made.
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            getActivity().unregisterReceiver(broadcastReceiver);
        }
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    // pre:
    // post: Check runtime permissions. Return true if runtime permissions are needed. Else, return
    // false.
    public boolean runtime_permissions() {
        Log.d("3", "runtime_permissions");
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
                return true;
        }
        return false;
    }

    // pre:
    // post: If permissions are granted then enable the toggle. Else, check for runtime permissions.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: // check FINE and COURSE location permissions
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // do something
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    // pre: Takes a service class as a paramter
    // post: Custom method to determine whether a service is running
    private boolean isServiceRunning(Class<?> serviceClass){
        ActivityManager activityManager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);

        // Loop through the running services
        for(ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                // If the service is running then return true
                Log.d(TAG, "GPS Service is running!");
                return true;
            }
        }
        Log.d(TAG, "GPS Service is NOT running!");
        return false;
    }

    // pre: Takes time as a Long.
    // post: Returns a formatted date using PDT time zone.
    public static String unixToPDT(long unixTime) {
        Date date = new Date(unixTime);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-7"));
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    public void insertTrip(Trip trip) {
        // Create a content values instance (kind of like a map)
        ContentValues values = new ContentValues();
        values.put(Trip.KEY_VehicleId, trip.getVehicleId());
        values.put(Trip.KEY_TripId, trip.getTripId());
        values.put(Trip.KEY_Date, trip.getDate());
        values.put(Trip.KEY_Miles, trip.getMiles());
        values.put(Trip.KEY_State, trip.getState());
        Log.e(TAG, "Content values: " + values.toString());
        // Insert the content values into the chosen table (Vehicle)
        long result = db.insertOrThrow("Trip", null, values);

    }

}


