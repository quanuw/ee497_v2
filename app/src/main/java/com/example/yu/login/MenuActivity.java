package com.example.yu.login;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.yu.login.data.model.Vehicle;
import com.example.yu.login.data.repo.VehicleRepo;

public class MenuActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        AddVehicleFragment.OnAddVehicleListener {

    private final static String TAG = "MENUACTIVITY";
    static final int PICK_IMAGE_REQUEST = 1;  // The request code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        //create a user repo to insert user into table
        VehicleRepo vehicleRepo = new VehicleRepo();
        //insert vehicle
        vehicleRepo.insertVehicle(vehicle);
        Toast.makeText(this, "ADD VEHICLE!!!!", Toast.LENGTH_LONG).show();
    }
}
