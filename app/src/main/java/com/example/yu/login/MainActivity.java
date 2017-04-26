package com.example.yu.login;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yu.login.data.DBHelper;
import com.example.yu.login.data.DatabaseManager;
import com.example.yu.login.data.model.Trip;
import com.example.yu.login.data.model.User;
import com.example.yu.login.data.model.Vehicle;
import com.example.yu.login.data.repo.TripRepo;
import com.example.yu.login.data.repo.UserRepo;
import com.example.yu.login.data.repo.VehicleRepo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Map;





// REFERENCES:
// http://io2015codelabs.appspot.com/codelabs/geofences#4
public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<Status> {


    public static final String TAG = MainActivity.class.getSimpleName();
    private static Context context;
    private static  DBHelper dbHelper;

    protected ArrayList<Geofence> mGeofenceList;
    protected GoogleApiClient mGoogleApiClient;
    private Button mAddGeofencesButton;
    DB_Controller rubDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        MAKE DB
        context = this.getApplicationContext();
        dbHelper = new DBHelper();
        DatabaseManager.initializeInstance(dbHelper);
        insertSampleData();

        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final Button registerButton = (Button) findViewById(R.id.registerButton);
        final TextView signin = (TextView) findViewById(R.id.signin);

        rubDb = new DB_Controller(this);

        mAddGeofencesButton = (Button) findViewById(R.id.add_geofences_button);
        // Empty list for storing geofences.
        mGeofenceList = new ArrayList<Geofence>();

        // Get the geofences used. Geofence data is hard coded in this sample.
        populateGeofenceList();

        // Kick off the request to build GoogleApiClient.
        buildGoogleApiClient();

        signin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent registerIntent = new Intent(MainActivity.this, RegisterAcivity.class);
                MainActivity.this.startActivity(registerIntent);
            }
        });

        registerButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent MenuIntent = new Intent(MainActivity.this, MenuActivity.class);
                        MainActivity.this.startActivity(MenuIntent);
//                        UserRepo userRepo = new UserRepo();
//                        User registerUser = new User();
//                        registerUser.setLoginName(etUsername.getText().toString());
//                        registerUser.setLoginPW(etPassword.getText().toString());
//                        userRepo.insert(registerUser);
                    }
                });

        // Add geofences in onCreate

    }

    // pre:
    // post: Populate the geofence using Constants class
    public void populateGeofenceList() {
        for (Map.Entry<String, LatLng> entry : Constants.LANDMARKS.entrySet()) {
            mGeofenceList.add(new Geofence.Builder()
                    .setRequestId(entry.getKey())
                    .setCircularRegion(
                            entry.getValue().latitude,
                            entry.getValue().longitude,
                            Constants.GEOFENCE_RADIUS_IN_METERS
                    )
                    .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build());
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    // for apiclient interface
    @Override
    protected void onStart() {
        super.onStart();
        if (!mGoogleApiClient.isConnecting() || !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnecting() || mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        // Add geofences in onConnected callback
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(this, "Google API Client not connected!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    getGeofencingRequest(),
                    getGeofencePendingIntent()
            ).setResultCallback(this); // Result processed in onResult().
            Log.i(TAG, "GEOFENCES ADDED!");
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Do something with result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
    }

    public void addGeofencesButtonHandler(View view) {
//        if (!mGoogleApiClient.isConnected()) {
//            Toast.makeText(this, "Google API Client not connected!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        try {
//            LocationServices.GeofencingApi.addGeofences(
//                    mGoogleApiClient,
//                    getGeofencingRequest(),
//                    getGeofencePendingIntent()
//            ).setResultCallback(this); // Result processed in onResult().
//        } catch (SecurityException securityException) {
//            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
//        }
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling addgeoFences()
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void onResult(Status status) {
        if (status.isSuccess()) {
            Toast.makeText(
                    this,
                    "Geofences Added",
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            Toast.makeText(
                    this,
                    "Geofences can't be added",
                    Toast.LENGTH_SHORT
            ).show();
            // Get the status code for the error and log it using a user-friendly message.
//            String errorMessage = GeofenceErrorMessages.getErrorString(this,
//                    status.getStatusCode());
        }
    }
    public static Context getContext(){
        return context;
    }
    private void insertSampleData(){

        UserRepo userRepo = new UserRepo();
        VehicleRepo vehicleRepo   = new VehicleRepo();
        TripRepo tripRepo = new TripRepo();


//        tripRepo.delete();
//        vehicleRepo.delete();
//        userRepo.delete();

        //Insert Sample data if the table is empty
        User user = new User();

        user.setFirstName("Amanda");
        user.setLastName("Tran");
        user.setLoginName("trana19");
        user.setLoginPW("password");
        user.setDOB("02/01/96");
        user.setEmail("amanda.tran02@gamil.com");
        user.setUserId("1");
        userRepo.insert(user);
        user.setFirstName("Sam");
        user.setLastName("whatever");
        user.setLoginName("Sammie");
        user.setLoginPW("sandwich");
        user.setDOB("02/01/96");
        user.setEmail("email");
        user.setUserId("2");
        userRepo.insert(user);

        Vehicle vehicle = new Vehicle();

        vehicle.setVehicleIdNum("1924809873");
        vehicle.setMake("Honda");
        vehicle.setModel("Accord");
        vehicle.setYear("1995");
        vehicleRepo.insert(vehicle);

        Trip trip = new Trip();

        trip.setVehicleIdNum("1924809873");
        trip.setMiles("200");
        trip.setTripId("1");
        tripRepo.insert(trip);
    }
}
