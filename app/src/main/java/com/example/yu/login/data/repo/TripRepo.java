package com.example.yu.login.data.repo;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.example.yu.login.data.DatabaseManager;
import com.example.yu.login.data.model.Trip;
import com.example.yu.login.data.model.Vehicle;

/**
 * Created by amand on 3/31/2017.
 */

public class TripRepo {
    private Trip trip;

    public TripRepo(){
        trip= new Trip();
    }


    public static String createTable(){
        return "CREATE TABLE " + Trip.TABLE  + "("
                + Trip.KEY_TripId  + " INTEGER PRIMARY KEY ,"
                + Trip.KEY_State  + " TEXT  ,"
                + Trip.KEY_Date  + " TEXT  ,"
                + Trip.KEY_Miles  + " TEXT   ,"
                + Trip.KEY_VehicleIdNum  + " INTEGER  ,"
                + "FOREIGN KEY(" + Trip.KEY_VehicleIdNum + ") REFERENCES "
                + Vehicle.KEY_VehicleIdNum + ")";
    }



    public void insert(Trip trip) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(Trip.KEY_TripId, trip.getTripId());
        values.put(Trip.KEY_VehicleIdNum, trip.getVehicleIdNum());
        values.put(Trip.KEY_Miles, trip.getMiles());

        // Inserting Row
        db.insert(Trip.TABLE, null, values);
        DatabaseManager.getInstance().closeDatabase();
    }



    public void delete( ) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(Trip.TABLE, null,null);
        DatabaseManager.getInstance().closeDatabase();
    }
}
