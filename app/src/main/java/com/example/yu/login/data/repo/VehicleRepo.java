package com.example.yu.login.data.repo;

import android.content.ContentValues;
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
        return "CREATE TABLE " + Vehicle.TABLE  + "("
                + Vehicle.KEY_VehicleId  + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Vehicle.KEY_VehicleIdNum  + " TEXT, "
                + Vehicle.KEY_Make  + " TEXT, "
                + Vehicle.KEY_Model  + " TEXT, "
                + Vehicle.KEY_Year  + " TEXT, "
                + Vehicle.KEY_UserId + " TEXT, "
                + "FOREIGN KEY(" + Vehicle.KEY_UserId + ") REFERENCES "
                + User.KEY_UserId + " )";
    }

    public void insertVehicle(Vehicle vehicle) {

        // Get a database manager
        SQLiteDatabase writable = DatabaseManager.getInstance().openDatabase();
        // Create a content values instance (kind of like a map)
        ContentValues values = new ContentValues();
        values.put(Vehicle.KEY_UserId, vehicle.getUserID());
        values.put(Vehicle.KEY_VehicleIdNum, vehicle.getVehicleIdNum());
        //values.put(Vehicle.KEY_VehicleId, vehicle.getVehicleId());
        values.put(Vehicle.KEY_Make, vehicle.getMake());
        values.put(Vehicle.KEY_Model, vehicle.getModel());
        values.put(Vehicle.KEY_Year, vehicle.getYear());
        // Insert the content values into the chosen table (Vehicle)
        long result = writable.insert("Vehicle", null, values);
    }

//    public void update(Vehicle vehicle) {
//        long id = vehicle.getVehicleId();
//        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
//        ContentValues values = new ContentValues();
//        values.put(Vehicle.KEY_VehicleIdNum, vehicle.getVehicleIdNum());
//        values.put(Vehicle.KEY_VehicleId, vehicle.getVehicleId());
//        values.put(Vehicle.KEY_Make, vehicle.getMake());
//        values.put(Vehicle.KEY_Model, vehicle.getModel());
//        values.put(Vehicle.KEY_Year, vehicle.getYear());
//        values.put(Vehicle.KEY_UserId, vehicle.getUser().getUserId());
//
//        db.update(Vehicle.TABLE, values,
//                Vehicle.KEY_VehicleId + " = " + id, new String[] { String.valueOf(id) });
//        DatabaseManager.getInstance().closeDatabase();
//    }


    public void delete(Vehicle vehicle) {
        long id = vehicle.getVehicleId();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(Vehicle.TABLE, Vehicle.KEY_VehicleId + " = " + id, null);
        System.out.println("the deleted user has the id: " + id);
        DatabaseManager.getInstance().closeDatabase();
    }
}