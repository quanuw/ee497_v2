package com.example.yu.login;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.yu.login.data.DBHelper;
import com.example.yu.login.data.model.Trip;
import com.example.yu.login.data.model.Vehicle;
import com.example.yu.login.data.repo.TripRepo;
import com.example.yu.login.data.repo.VehicleRepo;

public class MenuActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        AddVehicleFragment.OnAddVehicleListener,
        FragmentGPS.OnAddTripListener {

    private final static String TAG = "MENUACTIVITY";
    static final int PICK_IMAGE_REQUEST = 1;  // The request code

    private static DBHelper dbHelper;

    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Log.i(TAG, "USERID IS: " + sharedPreferences.getInt("userId", 44));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent vehicleSelect = new Intent(MenuActivity.this, SettingsActivity.class);
            startActivity(vehicleSelect);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        int id = item.getItemId();
        switch (id) {
            // Decide which fragment to load into activity
            case R.id.nav_camera:
                // Handle the camera action
                FragmentCammar Cammar = new FragmentCammar();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.content_menu, Cammar).commit();
                drawer.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_gps:
                // Go directly to app settings
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_manage:
                FragmentGPS gpsFragment = new FragmentGPS();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_menu, gpsFragment).commit();
                drawer.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_add_vehicle:
                AddVehicleFragment addVehicleFragment = AddVehicleFragment.newInstance();
                FragmentManager addVehicleManager = getSupportFragmentManager();
                addVehicleManager.beginTransaction().replace(R.id.content_menu, addVehicleFragment).commit();
                drawer.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_send: // Send an image.
                return true;
            default:
                drawer.closeDrawer(GravityCompat.START);
                return false;
        }
    }

    // TODO: 5/12/17
    // Interface method for adding vehicle to db.
    // This is where you should insert the vehicle information into the db.
    @Override
    public void onAddVehicle(String model, String make, String year, String vin, int currUserId) {
        //create a vehicle object to store values
        Vehicle vehicle = new Vehicle();
        //set the values of the vehicle object
        vehicle.setModel(model);
        vehicle.setMake(make);
        vehicle.setYear(year);
        vehicle.setVehicleIdNum(vin);
        //add this parameter back in once we figure out getCurrentUserId method in mainactivity
        vehicle.setUserId(currUserId);
        String modelTest = vehicle.getModel();
        String makeTest = vehicle.getMake();
        String yearTest = vehicle.getYear();
        String vinTest = vehicle.getVehicleIdNum();
        int idTest = vehicle.getUserID();
        //create a user repo to insert user into table
        Log.e(TAG, "VEHICLE INFO: " + makeTest + ", " + modelTest + ", " + yearTest + ", " + vinTest + ", " + idTest);

        VehicleRepo vehicleRepo = new VehicleRepo();
        //insert vehicle
        insertVehicle(vehicle);
        Toast.makeText(this, "ADD VEHICLE!!!!", Toast.LENGTH_LONG).show();
    }

    public void insertVehicle(Vehicle vehicle) {
        // Get a database manager
//        SQLiteDatabase writable = DatabaseManager.getInstance().openDatabase();
//        Log.e(TAG, "table string: " + writable.toString());
        // Create a content values instance (kind of like a map)
        ContentValues values = new ContentValues();
//        values.put(Vehicle.KEY_UserId, vehicle.getUserID());
        // TODO: 5/24/17
        // Must get user's  current id
        // If id is 0 then insert will fail since there is no user with id of 0
        // So hardcode to 1 for now
        values.put(Vehicle.KEY_UserId, vehicle.getUserID());
        values.put(Vehicle.KEY_VehicleIdNum, vehicle.getVehicleIdNum());
        //values.put(Vehicle.KEY_VehicleId, vehicle.getVehicleId());
        values.put(Vehicle.KEY_Make, vehicle.getMake());
        values.put(Vehicle.KEY_Model, vehicle.getModel());
        values.put(Vehicle.KEY_Year, vehicle.getYear());
        Log.e(TAG, "Content values: " + values.toString());
        // Insert the content values into the chosen table (Vehicle)
        // TODO: 5/17/17
        // Can't insert into vehicle table
        // android.database.sqlite.SQLiteException: foreign key mismatch -
        // "Vehicle" referencing "User" (code 1): , while compiling:
        // INSERT INTO Vehicle(UserId,Model,Make,VehicleIdNum,Year) VALUES (?,?,?,?,?)
        // Error Code : 1 (SQLITE_ERROR)

        long result = db.insertOrThrow("Vehicle", null, values);
    }

    @Override
    public void onAddTrip(String miles, String state, String date, int vehicleId) {
            //create a vehicle object to store values
            Trip trip = new Trip();
            trip.setMiles(miles);
            trip.setState(state);
            trip.setDate(date);
            trip.setVehicleId(vehicleId);
            String mileTest = trip.getMiles();
            String stateTest = trip.getState();
            String dateTest = trip.getDate();
            int idTest = trip.getVehicleId();
            //create a user repo to insert user into table
            Log.e(TAG, "    TRIP INFO: " + mileTest + ", " + stateTest + ", " + dateTest + ", " + idTest);
            TripRepo tripRepo = new TripRepo();
            //insert trip
            insertTrip(trip);
            Toast.makeText(this, "ADD TRIP!!!!", Toast.LENGTH_LONG).show();
    }

    public void insertTrip(Trip trip) {
            // Create a content values instance (kind of like a map)
            ContentValues values = new ContentValues();
            values.put(Trip.KEY_VehicleId, trip.getVehicleId());
            values.put(Trip.KEY_TripId, trip.getTripId());
            values.put(Trip.KEY_Date, trip.getDate());
            values.put(Trip.KEY_Miles, trip.getMiles());
            values.put(Trip.KEY_State, trip.getState());
            Log.e(TAG, "Content values: " + values.toString());
            // Insert the content values into the chosen table (Vehicle)
            long result = db.insertOrThrow("Trip", null, values);

            }

}
