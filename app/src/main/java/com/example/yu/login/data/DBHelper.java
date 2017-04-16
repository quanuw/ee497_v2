package com.example.yu.login.data;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.yu.login.MainActivity;
import com.example.yu.login.data.model.User;
import com.example.yu.login.data.model.Vehicle;
import com.example.yu.login.data.model.Trip;
import com.example.yu.login.data.repo.UserRepo;
import com.example.yu.login.data.repo.VehicleRepo;
import com.example.yu.login.data.repo.TripRepo;

import static com.example.yu.login.MainActivity.getContext;


public class DBHelper  extends SQLiteOpenHelper {
    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    private static final int DATABASE_VERSION = 10;
    // Database Name
    private static final String DATABASE_NAME = "sqliteDBRUC.db";
    private static final String TAG = DBHelper.class.getSimpleName().toString();

    public DBHelper( ) {
        super(MainActivity.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
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

}