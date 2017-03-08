package com.example.yu.login;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by amand on 3/2/2017.
 */


public class DB_Controller extends SQLiteOpenHelper {
    public static final String TAG = "DB_Controller";
    //Database Name and version
    public static final String DATABASE_RUC = "ruc.db";
    private static final int DATABASE_VERSION = 2;

    //user table
    public static final String TABLE_USER = "USER_TABLE";
    //user table columns
    public static final String COL_USER_ID = "USER_ID"; //primary key
    public static final String COL_USER_FN = "FIRST_NAME";
    public static final String COL_USER_LN = "LAST_NAME";
    public static final String COL_USER_DOB = "BIRTH_DATE";
    public static final String COL_USER_SSN = "SSN";
    public static final String COL_USER_STREET = "STREET";
    public static final String COL_USER_CITY = "CITY";
    public static final String COL_USER_STATE = "STATE";
    public static final String COL_USER_ZIPCODE = "ZIPCODE";
    public static final String COL_USER_PW = "PASSWORD";
    public static final String COL_USER_UN = "USER_NAME";

    // vehicle table
    public static final String TABLE_VEHICLE = "VEHICLE_TABLE";
    //vehicle table columns
    public static final String COL_VEHICLE_VIN = "VIN"; //Primary key
    public static final String COL_VEHICLE_MAKE = "MAKE";
    public static final String COL_VEHICLE_MODEL = "MODEL";
    public static final String COL_VEHICLE_YEAR = "YEAR";
    public static final String COL_VEHICLE_COLOR = "COLOR";
    public static final String COL_VEHICLE_PLATE = "PLATE";
    public static final String COL_VEHICLE_ID = COL_USER_ID; //foreign key

    //out of state miles table
    public static final String TABLE_TRIP = "TRIP_TABLE";
    //trip table columns
    public static final String COL_TRIP_ID = "TRIP_ID"; //primary key
    public static final String COL_TRIP_VIN = COL_VEHICLE_VIN; //foreign key
    public static final String COL_TRIP_STATE = "TRIP_STATE";
    public static final String COL_TRIP_DISTANCE = "TRIP_DISTANCE";
    public static final String COL_TRIP_DATE = "TRIP_DATE";


    //SQL statement of the user table creation (SQL query)
    private static final String SQL_CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + "("
            + COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_USER_FN + " TEXT NOT NULL, "
            + COL_USER_LN + " TEXT NOT NULL, "
            + COL_USER_DOB + " INTEGER NOT NULL, "
            + COL_USER_SSN + " INTEGER NOT NULL, "
            + COL_USER_STREET + " TEXT NOT NULL, "
            + COL_USER_CITY + " TEXT NOT NULL, "
            + COL_USER_STATE + " TEXT NOT NULL, "
            + COL_USER_ZIPCODE + " INTEGER NOT NULL, "
            + COL_USER_PW + " TEXT NOT NULL, "
            + COL_USER_UN + " TEXT NOT NULL "
            + ");";

    // TODO: will need to create foreign key and ENABLE FOREIGN KEY.
    //SQL statement of the vehicle table creation (SQL query)
    private static final String SQL_CREATE_TABLE_VEHICLE = "CREATE TABLE " + TABLE_VEHICLE + "("
            + COL_VEHICLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_VEHICLE_VIN + " TEXT NOT NULL, "
            + COL_VEHICLE_MAKE + " TEXT NOT NULL, "
            + COL_VEHICLE_MODEL + " TEXT NOT NULL, "
            + COL_VEHICLE_YEAR + " INTEGER NOT NULL, "
            + COL_VEHICLE_COLOR + " TEXT NOT NULL, "
            + COL_VEHICLE_PLATE + " TEXT NOT NULL "
            + COL_VEHICLE_ID + " TEXT NOT NULL "
            + "FOREIGN KEY(" + COL_VEHICLE_ID + ") REFERENCES " + TABLE_USER + "(id)"
            + ");";

    //SQL statement of the trip table creation (SQL query)
    private static final String SQL_CREATE_TABLE_TRIP = "CREATE TABLE " + TABLE_TRIP + "("
            + COL_TRIP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_TRIP_VIN + " TEXT NOT NULL, "
            + "FOREIGN KEY(" + COL_TRIP_VIN + ") REFERENCES " + TABLE_VEHICLE + "(vin) "
            + COL_TRIP_STATE + " TEXT NOT NULL, "
            + COL_TRIP_DISTANCE + " REAL NOT NULL, "
            + COL_TRIP_DATE + " INTEGER NOT NULL "
            + ");";

//    private static DB_Controller instance;
//
//    public static synchronized DB_Controller getHelper(Context context) {
//        if (instance == null)
//            instance = new DB_Controller(context);
//        return instance;
//    }

    //this might have to be private********
    public DB_Controller(Context context) {
        super(context, DATABASE_RUC, null, DATABASE_VERSION);
        //SQLiteDatabase db = this.getWritableDatabase();
    }

//    @Override
//    public void onOpen(SQLiteDatabase db) {
//        super.onOpen(db);
//    }

    //creates tables using the queries set up above
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_USER);
        db.execSQL(SQL_CREATE_TABLE_VEHICLE);
        db.execSQL(SQL_CREATE_TABLE_TRIP);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading the database from from version " + oldVersion + " to " + newVersion);
        //clears all data
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VEHICLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIP);
        //recreates tables
        onCreate(db);
    }

    public DB_Controller(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_RUC, factory, DATABASE_VERSION);
    }
}
