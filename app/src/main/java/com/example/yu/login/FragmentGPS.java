package com.example.yu.login;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * Created by Quan on 2/26/2017.
 */


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentGPS extends Fragment {


    public FragmentGPS() {
        // Required empty public constructor
    }

    private ToggleButton gpsButton;
    private TextView textView;
    private TextView distance;
    private TextView speed;
    private ImageView imageView;
    private BroadcastReceiver broadcastReceiver;
    private double totalDistance = 0;

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
        textView = (TextView) rootview.findViewById(R.id.textView);
        distance = (TextView) rootview.findViewById(R.id.distance);
        speed = (TextView) rootview.findViewById(R.id.speed);
        imageView = (ImageView) rootview.findViewById(R.id.imageView6);
        gpsButton = (ToggleButton) rootview.findViewById(R.id.gpsButton);
        Log.d("1", "onCreateView");
        // check runtime permissions (location).
        if (!runtime_permissions()) {
            Log.d("2", "Ask for permissions");
            enable_buttons();
        }
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
    // post: If toggle is on then start GPS. Else, stop GPS.
    private void enable_buttons() {
        gpsButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                Intent gpsService = new Intent(getActivity(), GPS_Service.class);
                if (isChecked) {
                    Log.d("", "isChecked: " + isChecked);
                    //Intent start = new Intent(getActivity(), GPS_Service.class);
                    getActivity().startService(gpsService);
//              TODO: Don't think stopService() is working.
                } else {
                    Log.d("", "isChecked: " + isChecked);
                    //Intent stop = new Intent(getActivity(), GPS_Service.class);
                    getActivity().stopService(gpsService);
                }
            }
        });
    }

    // pre:
    // post: Check runtime permissions. Return true if runtime permissions are needed. Else, return
    // false.
    private boolean runtime_permissions() {
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
}


