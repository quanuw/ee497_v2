package com.example.yu.login;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Quan on 2/26/2017.
 */

// The GPS_Service allows other activities to start a process running in the background. In
// this case, get location coordinates.
public class GPS_Service extends Service implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final String TAG = "LOCATION";

    private static final double METERS_TO_MILES = 0.000621371;

    private static final int LOCATION_REQUEST_CODE = 1;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private int pointCount = 0;

    private double prevX = 0;
    private double prevY = 0;
    private double nextX = 0;
    private double nextY = 0;

    private Location prevLocation;
    private Location nextLocation;

    private Location currentLocation;

    private static final int TWO_MINUTES = 1000 * 60 * 2;

    // for distance calculation with Haversine formula.
    public static final double R = 6372.8; // kilometers
    public static final double M = 3959.9;// miles
    public static final int minPointCount = 2;


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

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        mGoogleApiClient.connect();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.v(TAG, "GoogleApiClient connected");

        mLocationRequest = new LocationRequest();
        mLocationRequest.setSmallestDisplacement(0);
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck == PackageManager.PERMISSION_GRANTED) {
            //have permission, can go ahead and do stuff

            //assumes location settings enabled

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        else {
            //request permission
            //ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v(TAG, "" + pointCount);
        // Want 68% chance that the current location estmate is within 200m of the radius.
        float accuracy = location.getAccuracy();
        float speed = location.getSpeed() * (float) 2.24;
        if (accuracy < 200){
            Intent i = new Intent("location_update");
            // update distance.
            prevLocation = nextLocation;
            nextLocation = location;
            // need at least 2 coordinates to begin calculation.
            if (pointCount >= minPointCount) {
                float distance = prevLocation.distanceTo(nextLocation) * (float) METERS_TO_MILES;
                i.putExtra("distance", distance);
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
