package com.example.yu.login.data;
//for next time:
//you need to test out the foreign keys in main activities somehow
//add in query, use quan's code for more ideas
//so far you MAY have only completed successful FK for vehicle, if test works, replicate for trip
//useful link: http://androidopentutorials.com/android-sqlite-join-multiple-tables-example/
//think about if the delete and update method in vehicle successfully track back thru user


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.yu.login.data.model.Trip;
import com.example.yu.login.data.model.User;
import com.example.yu.login.data.model.Vehicle;
import com.example.yu.login.data.repo.TripRepo;
import com.example.yu.login.data.repo.UserRepo;
import com.example.yu.login.data.repo.VehicleRepo;


public class DBHelper extends SQLiteOpenHelper {
    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    private static final int DATABASE_VERSION = 18;
    // Database Name
    private static final String DATABASE_NAME = "sqliteDBRUC.db";
    private static final String TAG = DBHelper.class.getSimpleName().toString();

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //super(MainActivity.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //All necessary tables you like to create will create here
        db.execSQL(UserRepo.createTable());
        db.execSQL(VehicleRepo.createTable());
        db.execSQL(TripRepo.createTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, String.format("SQLiteDatabase.onUpgrade(%d -> %d)", oldVersion, newVersion));

        // Drop table if existed, all data will be gone!!!
        db.execSQL("DROP TABLE IF EXISTS " + User.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Vehicle.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Trip.TABLE);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

}