package com.example.yu.login.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by amand on 4/9/2017.
 */
public class Vehicle implements Parcelable {
    public static final String TAG = Vehicle.class.getSimpleName();
    public static final String TABLE = "Vehicle";
    // Labels Table Columns names
    public static final String KEY_VehicleIdNum = "VehicleIdNum";
    public static final String KEY_VehicleId = "VehicleId";
    public static final String KEY_Make = "Make";
    public static final String KEY_Model = "Model";
    public static final String KEY_Year = "Year";
    public static final String KEY_UserId = "UserId";


    private String vehicleIdNum;
    private int vehicleId;
    private int userId;
    private String make;
    private String model;
    private String year;

    private User user;

    public Vehicle() {
        super();
    }

    private Vehicle(Parcel in) {
        super();
        this.vehicleId = in.readInt();
        this.vehicleIdNum = in.readString();
        this.make = in.readString();
        this.model = in.readString();
        this.year = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
    }

    public int getVehicleId() {
        return vehicleId;
    }
//    public void setVehicleId(int vehicleId) {
//        this.vehicleId = vehicleId;
//    }

    public String getVehicleIdNum() {
        return vehicleIdNum;
    }
    public void setVehicleIdNum(String vehicleIdNum) {
        this.vehicleIdNum = vehicleIdNum;
    }

    public int getUserID() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getYear() {
        return year;
    }
    public void setYear(String year) {
        this.year = year;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User department) {
        this.user = user;
    }
    @Override
    public String toString() {
        return "Vehicle [Vehicle ID=" + vehicleId + ", VIN=" + vehicleIdNum + ", Make=" + make
                + ", Model=" + model + ", Year=" + year + ", User=" + user + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + vehicleId;
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
        Vehicle other = (Vehicle) obj;
        if (vehicleId != other.vehicleId)
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(getVehicleId());
        parcel.writeString(getMake());
        parcel.writeString(getModel());
        parcel.writeString(getYear());
        parcel.writeParcelable(getUser(), flags);
    }

    public static final Parcelable.Creator<Vehicle> CREATOR = new Parcelable.Creator<Vehicle>() {
        public Vehicle createFromParcel(Parcel in) {
            return new Vehicle(in);
        }

        public Vehicle[] newArray(int size) {
            return new Vehicle[size];
        }
    };
}
