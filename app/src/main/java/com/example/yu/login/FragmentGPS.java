package com.example.yu.login;

import android.Manifest;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import com.example.yu.login.data.DatabaseManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static com.example.yu.login.data.model.Trip.KEY_Date;
import static com.example.yu.login.data.model.Trip.KEY_Miles;
import static com.example.yu.login.data.model.Trip.KEY_State;
import static com.example.yu.login.data.model.Trip.KEY_VehicleIdNum;
import static com.google.android.gms.wearable.DataMap.TAG;



/**
 * Created by Quan on 2/26/2017.
 */


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentGPS extends Fragment {




    private ToggleButton gpsButton;
    private TextView textView;
    private TextView distance;
    private TextView speed;
    private ImageView imageView;
    private BroadcastReceiver broadcastReceiver;
    private double totalDistance = 0;


    private boolean gpsWasOn = false; // to decide whether or not to account for miles.

    // query list of vehicles from db and store the vehicle user chooses.
    private String myVechicle = "";   // keep track of which vehicle the user is in.

    public FragmentGPS() {
        // Required empty public constructor
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        gpsButton = (ToggleButton) getView().findViewById(R.id.gpsButton);
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_gps, container, false);
        distance = (TextView) rootview.findViewById(R.id.distance);
        speed = (TextView) rootview.findViewById(R.id.speed);
        Log.d("1", "onCreateView");
        // check runtime permissions (location).
        if (!runtime_permissions()) {
            Log.d("2", "Ask for permissions");
            enable_buttons();
        }
        //The heart and mind of headless fragment is below line. It will keep the fragment alive during configuration change when activities and   //subsequent fragments are "put to death" and recreated
        setRetainInstance(true);

        return rootview;
    }
//
//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState) {
//
//    }
//
//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//
//    }

    // pre:
    // post: Creates broadcastReceiver that accepts intents with string "location_update".
    @Override
    public void onResume() {
        super.onResume();
        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
//                    textView.append("\n"+intent.getExtras().get("coordinates"));
                    //textView.setText((CharSequence) intent.getExtras().get("coordinates"));
                    if (intent != null && intent.getExtras().get("distance") != null &&
                            intent.getExtras().get("speed") != null) {
                        totalDistance += (double) intent.getExtras().get("distance");
                        distance.setText(String.valueOf(totalDistance));
                        speed.setText(intent.getExtras().get("speed") + " mph");
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
    }

    // pre:
    // post: If switch is on then start GPS. Else, stop GPS.
    private void enable_buttons() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        // TODO: 4/26/17
        // Not sure what default boolean value should be.

        boolean isGpsOn = sharedPreferences.getBoolean("gps", false);

        if (isGpsOn) {
            Log.d("", "isChecked: " + isGpsOn);
            Intent start = new Intent(getActivity(), GPS_Service.class);
            getActivity().startService(start);
            if (isServiceRunning(GPS_Service.class)) {
                Log.d(TAG, "GPS Service started");
            }
//              TODO: Don't think stopService() is working.
        } else {
            Log.d("", "isChecked: " + isGpsOn);
            Intent stop = new Intent(getActivity(), GPS_Service.class);
            getActivity().stopService(stop);
            if (!isServiceRunning(GPS_Service.class)) {
                Log.d(TAG, "GPS Service stopped");
            }

            // If GPS is now off but was on then insert trip into database
            if (gpsWasOn) {


                String miles = String.valueOf(totalDistance);
                String state = "N/A";
                String date = unixToPDT(System.currentTimeMillis());
                String vehicle = myVechicle;

                // Get a database manager
                DatabaseManager databaseManager = new DatabaseManager();

                // Open the database to write
                SQLiteDatabase writable = databaseManager.openDatabase();

                // Create a content values instance (kind of like a map)
                ContentValues contentValues = new ContentValues();
                contentValues.put(KEY_Miles, miles);
                contentValues.put(KEY_State, state);
                contentValues.put(KEY_Date, date);
                contentValues.put(KEY_VehicleIdNum, vehicle);

                // Insert the content values into the chosen table (Table)
                long result = writable.insert("Trip", null, contentValues);

                // If an error occured during insertion of row
                if (result == -1) {
                    Log.e(TAG, "COULD NOT REGISTER USER!");
                    return;
                }

                // Turn flag off
                gpsWasOn = false;

            }
        }

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
                Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                return true;
        }
        return false;
    }

    // pre:
    // post: If permissions are granted then enable the toggle. Else, check for runtime permissions.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                enable_buttons();
            } else {
                runtime_permissions();
            }
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
    private String unixToPDT(long unixTime) {
        Date date = new Date(unixTime);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-7"));
        String formattedDate = sdf.format(date);
        return formattedDate;
    }
}


