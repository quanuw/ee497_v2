package com.example.yu.login;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yu.login.data.DBHelper;
import com.example.yu.login.data.DatabaseManager;
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
import java.util.regex.Matcher;

import static com.example.yu.login.data.model.User.KEY_DOB;
import static com.example.yu.login.data.model.User.KEY_Email;
import static com.example.yu.login.data.model.User.KEY_FirstName;
import static com.example.yu.login.data.model.User.KEY_LastName;
import static com.example.yu.login.data.model.User.KEY_LoginName;
import static com.example.yu.login.data.model.User.KEY_LoginPW;


// REFERENCES:
// http://io2015codelabs.appspot.com/codelabs/geofences#4
public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<Status>,
        RegistrationFragment.OnRegisterListener,
        RegistrationFragment.ToSignInListener,
        RegistrationFragment.DobSetListener,
        LoginFragment.OnLoginListener {


    public static final String TAG = MainActivity.class.getSimpleName();
    private static Context context;
    private static  DBHelper dbHelper;

    private String dob = "";

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
        //insertSampleData();


        // Empty list for storing geofences.
        mGeofenceList = new ArrayList<Geofence>();

        // Get the geofences used. Geofence data is hard coded in this sample.
        populateGeofenceList();

        // Kick off the request to build GoogleApiClient.
        buildGoogleApiClient();

        // Display Registration Fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, new RegistrationFragment()).commit();

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
                    .setNotificationResponsiveness(10000) // Set the best effort notification responsiveness
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
            //Get the status code for the error and log it using a user-friendly message.
//            String errorMessage = GeofenceErrorMessages.getErrorString(this,
//                    status.getStatusCode());
        }
    }
    public static Context getContext(){
        return context;
    }

    @Override
    public void onRegistration() {
        EditText firstName = (EditText) findViewById(R.id.firstName);
        EditText lastName = (EditText) findViewById(R.id.lastName);
        EditText email = (EditText) findViewById(R.id.email);

        EditText username = (EditText) findViewById(R.id.username);
        EditText password = (EditText) findViewById(R.id.password);

        String firstNameStr= firstName.getText().toString();
        String lastNameStr = lastName.getText().toString();
        String emailStr = email.getText().toString();
        String usernameStr = username.getText().toString();
        String passwordStr = password.getText().toString();

        // check fields
        if (firstNameStr.equals("") || lastNameStr.equals("") || emailStr.equals("") ||
                usernameStr.equals("") || passwordStr.equals("")) {
            Toast.makeText(this, "Please fill out all fields!", Toast.LENGTH_LONG).show();
            return;
        }
        // check email
        if (!isValidEmailAddress(emailStr)) {
            Toast.makeText(this, emailStr + " is not valid.", Toast.LENGTH_LONG).show();
            return;
        }
        // check dob
        if (dob.equals("")) {
            Toast.makeText(this, "Please enter a date of birth.", Toast.LENGTH_LONG).show();
            return;
        }

        // Get a database manager
        DatabaseManager databaseManager = new DatabaseManager();

        // Open the database to write
        SQLiteDatabase writable = databaseManager.openDatabase();

        // Create a content values instance (kind of like a map)
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_FirstName, firstNameStr);
        contentValues.put(KEY_LastName, lastNameStr);
        contentValues.put(KEY_Email, emailStr);
        contentValues.put(KEY_DOB, dob);
        contentValues.put(KEY_LoginName, usernameStr);
        contentValues.put(KEY_LoginPW, passwordStr);

        // Insert the content values into the chosen table (User)
        long result = writable.insert("User", null, contentValues);

        // If an error occured during insertion of row
        if (result == -1) {
            Log.e(TAG, "COULD NOT REGISTER USER!");
            return;
        }

        Toast.makeText(this, "REGISTER USER!", Toast.LENGTH_LONG).show();

        // Go to menu activity if registration is successful.
        Intent menuIntent = new Intent(MainActivity.this, MenuActivity.class);
        startActivity(menuIntent);


    }

    @Override
    public void toSignIn() {
        LoginFragment loginFragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, loginFragment).commit();
    }

    @Override
    public void onLogin(String username, String password) {
        Intent loginIntent  = new Intent(this, MenuActivity.class);
        startActivity(loginIntent);
    }

    // Sets dob from the DatePickerFragment
    @Override
    public void dobSet(String dob) {
        this.dob = dob;
        EditText setDob = (EditText) findViewById(R.id.dob);
        setDob.setText(dob);
    }

    // Validates email
    public static boolean isValidEmailAddress(String email) {
        Matcher matcher = Patterns.EMAIL_ADDRESS.matcher(email);
        return matcher.matches();
    }

}
