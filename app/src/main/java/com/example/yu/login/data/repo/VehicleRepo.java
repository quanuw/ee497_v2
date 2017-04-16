package com.example.yu.login.data.repo;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.example.yu.login.data.DatabaseManager;
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
        return "CREATE TABLE " + Vehicle.TABLE  + "("
                + Vehicle.KEY_VehicleIdNum  + " TEXT PRIMARY KEY  ,"
                + Vehicle.KEY_Make  + " TEXT  ,"
                + Vehicle.KEY_Model  + " TEXT  ,"
                + Vehicle.KEY_Year  + " TEXT )";
    }



    public void insert(Vehicle vehicle) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(Vehicle.KEY_VehicleIdNum, vehicle.getVehicleIdNum());
        values.put(Vehicle.KEY_Make, vehicle.getMake());
        values.put(Vehicle.KEY_Model, vehicle.getModel());
        values.put(Vehicle.KEY_Year, vehicle.getYear());

        // Inserting Row
        db.insert(Vehicle.TABLE, null, values);
        DatabaseManager.getInstance().closeDatabase();
    }



    public void delete( ) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(Vehicle.TABLE, null,null);
        DatabaseManager.getInstance().closeDatabase();
    }
}