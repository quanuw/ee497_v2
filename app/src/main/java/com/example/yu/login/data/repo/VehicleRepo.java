package com.example.yu.login.data.repo;

import android.database.sqlite.SQLiteDatabase;

import com.example.yu.login.data.DatabaseManager;
import com.example.yu.login.data.model.User;
import com.example.yu.login.data.model.Vehicle;

/**
 * Created by amand on 3/31/2017.
 */

public class VehicleRepo {
    private Vehicle vehicle;

    public VehicleRepo(){
        vehicle= new Vehicle();
    }

    public static String createTable(){
        return "CREATE TABLE IF NOT EXISTS " + Vehicle.TABLE  + " ("
                + Vehicle.KEY_VehicleId  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Vehicle.KEY_VehicleIdNum  + " TEXT, "
                + Vehicle.KEY_Make  + " TEXT, "
                + Vehicle.KEY_Model  + " TEXT, "
                + Vehicle.KEY_Year  + " TEXT, "
                + Vehicle.KEY_UserId + " INTEGER, "
                + "FOREIGN KEY(" + Vehicle.KEY_UserId + ") REFERENCES "
                + User.TABLE + "(" + User.KEY_UserId +"));";
    }


    public void delete(Vehicle vehicle) {
        long id = vehicle.getVehicleId();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(Vehicle.TABLE, Vehicle.KEY_VehicleId + " = " + id, null);
        System.out.println(" deleted user has the id: " + id);
        DatabaseManager.getInstance().closeDatabase();
    }
}