package com.example.yu.login.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by amand on 4/9/2017.
 */

public class Trip implements Parcelable {
    public static final String TAG = Trip.class.getSimpleName();
    public static final String TABLE = "Trip";

    // Labels Table Columns names
    public static final String KEY_TripId = "TripId";
    public static final String KEY_Miles = "Miles";
    public static final String KEY_State = "State";
    public static final String KEY_Date = "Date";
    public static final String KEY_VehicleId = "VehicleId";

    private int tripId ;
    private String miles;
    private String state;
    private String date;
    private int vehicleId ;



    public Trip() {
        super();
    }

    public Trip(String miles, String state, String date, int vehicleId) {
        super();
        //this.tripId = tripId;
        this.miles = miles;
        this.state = state;
        this.date = date;
        this.vehicleId = vehicleId;
    }

    private Trip(Parcel in) {
        super();
        this.tripId = in.readInt();
        this.miles = in.readString();
        this.state = in.readString();
        this.date = in.readString();
        this.vehicleId = in.readInt();
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public String getMiles() {
        return miles;
    }

    public void setMiles(String miles) {
        this.miles = miles;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }
    @Override
    public String toString() {
        return "Trip ID:" + tripId + ", Miles:" + miles + ", State:" + state
                + ", Date:" + date + ", Vehicle ID Number:" + vehicleId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(getTripId());
        parcel.writeString(getMiles());
        parcel.writeString(getState());
        parcel.writeString(getDate());
        parcel.writeInt(getVehicleId());
    }
    public static final Parcelable.Creator<Trip> CREATOR = new Parcelable.Creator<Trip>() {
        public Trip createFromParcel(Parcel in) {
            return new Trip(in);
        }

        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + tripId;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Trip other = (Trip) obj;
        if (tripId != other.tripId)
            return false;
        return true;
    }
}
