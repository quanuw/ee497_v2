package com.example.yu.login;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
/**
 * Created by Quan on 2/26/2017.
 */

// The GPS_Service allows other activities to start a process running in the background. In
// this case, get location coordinates.
public class GPS_Service extends Service {

    private int count = 0;
    private double prevX = 0;
    private double prevY = 0;
    private double nextX = 0;
    private double nextY = 0;
    private LocationListener listener;
    private LocationManager locationManager;

    private static final int TWO_MINUTES = 1000 * 60 * 2;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // pre: Takes 2 coordinate points as parameters.
    // post: Returns the distance between two points.
    public double getDistance(double prevX, double prevY, double nextX, double nextY) {
        return Math.sqrt(Math.pow((nextX - prevX), 2) + Math.pow((nextY - prevY), 2));
    }

    // pre:
    // post: creates a LocationListener that will send intents with coordinate information when
    // the location is changed. Uses Context.sendBroadcast(Intent i) to Context.broadcastReceiver()
    // in Privacy class.
    @Override
    public void onCreate() {
        Log.d("GPS location manager", "This is my message");
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Intent i = new Intent("location_update");
                    // update distance travelled.
                    prevX = nextX;
                    prevY = nextY;
                    nextX = location.getLongitude();
                    nextY = location.getLatitude();
                    double dist = getDistance(prevX, prevY, nextX, nextY);
                    i.putExtra("distance", dist);
                    sendBroadcast(i);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };
        Log.d("GPS location manager", "This is my message");
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        //noinspection MissingPermission
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3000,0,listener);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("On Start", "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if(locationManager != null){
            //noinspection MissingPermission
            locationManager.removeUpdates(listener);
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

