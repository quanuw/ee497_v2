package com.example.yu.login;

import java.util.Date;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Quan on 3/8/2017.
 */

public class Vehicle implements Parcelable{

    // vin is made up of chars.
    private String vin;
    private String make;
    private String model;
    private int year;
    private String color;
    private String plate;

    private User user_id;

    public Vehicle() {
        super();
    }

    private Vehicle(Parcel in) {
        super();
        this.vin = in.readString();
        this.make = in.readString();
        this.model = in.readString();
        this.color = in.readString();
        this.plate = in.readString();

        this.user_id = in.readParcelable(User.class.getClassLoader());

    }

    // getters and setters
    public String getVin() {
        return vin;
    }
    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getMake() {
        return make;
    }
    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }

    public String getPlate() {
        return plate;
    }
    public void setPlate(String plate) {
        this.plate = plate;
    }

    public User getId() {
        return user_id;
    }
    public void setId(User user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "Vehicle [VIN=" + vin + ", MAKE=" + make + ", MODEL=" + model +
                ", COLOR=" + color + ", PLATE=" + plate + ", ID=" + user_id + "]";
    }


    // abstract class Parcelable requires this method.
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(getVin());
        parcel.writeString(getMake());
        parcel.writeInt(getYear());
        parcel.writeString(getModel());
        parcel.writeString(getColor());
        parcel.writeString(getPlate());
        parcel.writeParcelable(getId(), flags);
    }

    // abstract class Parcelable requires this method.
    @Override
    public int describeContents() {
        return 0;
    }

    // TODO: Need to find out what this does. Not sure wtf this is for!!
    public static final Parcelable.Creator<Vehicle> CREATOR = new Parcelable.Creator<Vehicle>() {
        public Vehicle createFromParcel(Parcel in) {
            return new Vehicle(in);
        }

        public Vehicle[] newArray(int size) {
            return new Vehicle[size];
        }
    };
}
