package com.example.yu.login.data.model;

/**
 * Created by amand on 4/9/2017.
 */
public class Vehicle {
    public static final String TAG = Vehicle.class.getSimpleName();
    public static final String TABLE = "Vehicle";
    // Labels Table Columns names
    public static final String KEY_VehicleIdNum = "VehicleIdNum";
    public static final String KEY_Make = "Make";
    public static final String KEY_Model = "Model";
    public static final String KEY_Year = "Year";


    private String vehicleIdNum;
    private String make;
    private String model;
    private String year;


    public String getVehicleIdNum() {
        return vehicleIdNum;
    }
    public void setVehicleIdNum(String vehicleIdNum) {
        this.vehicleIdNum = vehicleIdNum;
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
}
