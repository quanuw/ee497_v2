package com.example.yu.login;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

/**
 * Created by Quan on 4/10/2017.
 */

public class Constants {
    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS = 12 * 60 * 60 * 1000;
    public static final float GEOFENCE_RADIUS_IN_METERS = 100;

    public static final HashMap<String, LatLng> LANDMARKS = new HashMap<String, LatLng>();
    static {
        // San Francisco International Airport.
        LANDMARKS.put("Moscone South", new LatLng(37.783888,-122.4009012));

        // Googleplex.
        LANDMARKS.put("Japantown", new LatLng(37.785281,-122.4296384));

        // Test
        LANDMARKS.put("SFO", new LatLng(37.621313,-122.378955));

        // UW Red Square
        LANDMARKS.put("UW Red Square", new LatLng(47.655917, -122.309666));

        // Starbucks on the ave near jjs
        LANDMARKS.put("Starbucks Ave & 42nd", new LatLng(47.658246, -122.313364));

        // Quan's apt
        LANDMARKS.put("Quan's apt", new LatLng(47.658056, -122.315207));

        // Trader Joe's
        LANDMARKS.put("Trader Joe's", new LatLng(47.662706, -122.317748));


    }
}
