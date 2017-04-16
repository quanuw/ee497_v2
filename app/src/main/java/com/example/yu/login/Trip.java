package com.example.yu.login;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Quan on 3/8/2017.
 */

public class Trip implements Parcelable{

    private int id;

    // TODO: can make state an int too. we only have 50 states.
    private String state;
    private int distance;
    private int date;

    private Vehicle vehicle_vin;

    public Trip() {
        super();
    }

    private Trip(Parcel in) {
        super();
        this.id = in.readInt();
        this.state = in.readString();
        this.distance = in.readInt();
        this.date = in.readInt();


        // load vehicle class into vehicle_vin field
        this.vehicle_vin = in.readParcelable(Vehicle.class.getClassLoader());

    }

    // getters and setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Vehicle getVin() {
        return vehicle_vin;
    }
    public void setVin(Vehicle vehicle_vin) {
        this.vehicle_vin = vehicle_vin;
    }

    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }

    public int getDistance() {
        return distance;
    }
    // TODO: i think this should increment distance not set it...
    // will "set" for now
    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDate() {
        return date;
    }
    public void setDate(int date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Trip [ID=" + id + ", VIN=" + vehicle_vin + ", STATE=" + state + ", DISTANCE=" + distance +
                ", DATE=" + date + "]";
    }


    // abstract class Parcelable requires this method.
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(getId());
        parcel.writeParcelable(getVin(), flags);
        parcel.writeString(getState());
        parcel.writeInt(getDistance());
        parcel.writeInt(getDate());
    }


    // abstract class Parcelable requires this method.
    @Override
    public int describeContents() {
        return 0;
    }

    // TODO: Need to find out what this does. Not sure wtf this is for!!
    public static final Parcelable.Creator<Trip> CREATOR = new Parcelable.Creator<Trip>() {
        public Trip createFromParcel(Parcel in) {
            return new Trip(in);
        }

        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };
}
