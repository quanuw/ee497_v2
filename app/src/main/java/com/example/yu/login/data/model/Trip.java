package com.example.yu.login.data.model;

/**
 * Created by amand on 4/9/2017.
 */

public class Trip {
    public static final String TAG = Trip.class.getSimpleName();
    public static final String TABLE = "Trip";

    // Labels Table Columns names
    public static final String KEY_TripID = "TripId";
    public static final String KEY_Miles = "Miles";
    public static final String KEY_VehicleIdNum = "VehicleIdNum";

    private String tripId ;
    private String miles;
    private String vehicleIdNum ;

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getMiles() {
        return miles;
    }

    public void setMiles(String miles) {
        this.miles = miles;
    }

    public String getVehicleIdNum() {
        return vehicleIdNum;
    }

    public void setVehicleIdNum(String vehicleIdNum) {
        this.vehicleIdNum = vehicleIdNum;
    }
}
