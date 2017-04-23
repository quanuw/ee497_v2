package com.example.yu.login;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Quan on 2/26/2017.
 */

// The GPS_Service allows other activities to start a process running in the background. In
// this case, get location coordinates.
public class GPS_Service extends Service implements LocationListener {

    private int pointCount = 0;

    private double prevX = 0;
    private double prevY = 0;
    private double nextX = 0;
    private double nextY = 0;
    private LocationManager locationManager;
    private String provider;

    private Location currentLocation;

    private static final int TWO_MINUTES = 1000 * 60 * 2;

    // for distance calculation with Haversine formula.
    public static final double R = 6372.8; // kilometers
    public static final double M = 3959.9;// miles
    public static final int minPointCount = 2;

    @Override
    public void onProviderDisabled(String s) {
        Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // pre: Takes 2 coordinate points as parameters.
    // post: Returns the distance between two coordinate points as km using the Haversine formula.
    // References:
    // https://en.wikipedia.org/wiki/Haversine_formula
    // https://rosettacode.org/wiki/Haversine_formula#Java
    public double getDistance(double prevX, double prevY, double nextX, double nextY) {
            double x = Math.toRadians(nextX - prevX);
            double y = Math.toRadians(nextY - prevY);

            prevX = Math.toRadians(prevX);
            nextX = Math.toRadians(nextX);

            double a = Math.pow(Math.sin(x / 2),2) + Math.pow(Math.sin(y / 2),2) * Math.cos(prevX) * Math.cos(nextX);
            double c = 2 * Math.asin(Math.sqrt(a));
            return M * c;
    }



    // pre:
    // post: creates a LocationListener that will send intents with coordinate information when
    // the location is changed. Uses Context.sendBroadcast(Intent i) to Context.broadcastReceiver()
    // in Privacy class.
    @Override
    public void onCreate() {
        Log.d("GPS location manager", "This is my message");
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        //noinspection MissingPermission
        locationManager.requestLocationUpdates(provider, 5000, 100, this);
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        currentLocation = locationManager.getLastKnownLocation(provider);
        if (currentLocation != null) {
            onLocationChanged(currentLocation);
            Log.d("GPS location manager", "Location is available");
        } else {
            Log.d("GPS location manager", "Location not available");
        }


        Log.d("GPS location manager", "This is my message");

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v("TAG", "" + pointCount);
        // Want 68% chance that the current location estmate is within 200m of the radius.
        float accuracy = location.getAccuracy();
        float speed = location.getSpeed() * (float) 2.24;
        if (accuracy < 200){
            Intent i = new Intent("location_update");
            // update distance.
            prevX = nextX;
            prevY = nextY;
            nextX = location.getLongitude();
            nextY = location.getLatitude();
            // need at least 2 coordinates to begin calculation.
            if (pointCount >= minPointCount) {
                // double dist = getDistance(prevX, prevY, nextX, nextY);
                // Try android built-in static function.
                float results[] = new float[1];
                // Stores the resulting distance in results[0].
                Location.distanceBetween(prevX, prevY, nextX, nextY, results);
                float androidDist = results[0];
                i.putExtra("distance", androidDist);


            }
            // send speed data
            float sp = location.getSpeed() * (float) 2.24; // convert m/s to mi/h
            i.putExtra("speed", sp);
            sendBroadcast(i);
            pointCount++;
            // incase of overflow (very rare)
            if (pointCount == 128) {
                pointCount = 2;
            }
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("On Start", "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }



    // Request updates at startup *
//    @SuppressWarnings({"MissingPermission"})
//    public void onResume() {
//        super.onResume();
//
//        locationManager.requestLocationUpdates(provider, 400, 0, this);
//    }

    /* Remove the locationlistener updates when Activity is paused */
//    @Override
//    protected void onPause() {
//        super.onPause();
//        locationManager.removeUpdates(this);
//    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }



//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if(locationManager != null){
//            //noinspection MissingPermission
//            locationManager.removeUpdates(listener);
//        }
//    }

    /** Determines whether one Location reading is better than the current Location fix
     * @param location  The new Location that you want to evaluate
     * @param currentBestLocation  The current Location fix, to which you want to compare the new one
     */
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

}
