package com.example.yu.login;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yu.login.data.DBHelper;
import com.example.yu.login.data.DatabaseManager;
import com.example.yu.login.data.model.User;
import com.example.yu.login.data.model.Vehicle;
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
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;


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
    private int currentUserId;
    protected ArrayList<Geofence> mGeofenceList;
    protected GoogleApiClient mGoogleApiClient;
    private Button mAddGeofencesButton;



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

        /* TODO: 5/15/17
          Put the user's id into default shared preferences.
          To access the id later use this code:
            Shared sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Context context);
            int id = sharedPreferences.getInt(String key, int default) key would be "username"
            int default is the value returned if the key didn't exist
          Example code can be seen in MenuActivity lines 59-60
        */
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("username", currentUserId); // the key value pair
        editor.commit();
        System.out.println("current UserID is" + Integer.toString(sharedPreferences.getInt("username", 33)));
        Log.i(TAG, "USERID IS: " + sharedPreferences.getInt("username", 33));

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
//            Log.i(TAG, "GEOFENCES ADDED!");
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

        if (checkUserExist(usernameStr)) {
            Toast.makeText(this, "Username already exists", Toast.LENGTH_LONG).show();
            return;
        }

        if (checkEmailExist(emailStr)) {
            Toast.makeText(this, "Email already registered.", Toast.LENGTH_LONG).show();
            return;
        }

        //create a user object to store values
        User user = new User();

        user.setFirstName(firstNameStr);
        user.setLastName(lastNameStr);
        user.setEmail(emailStr);
        user.setDOB(dob);
        user.setLoginName(usernameStr);
        user.setLoginPW(passwordStr);

        //create a user repo to insert user into table
        UserRepo userRepo = new UserRepo();
        currentUserId = userRepo.insertUser(user);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("userId", currentUserId);
        editor.commit();


        if (currentUserId == -1) {
            Toast.makeText(this, "User could not be registered", Toast.LENGTH_LONG).show();
            return;
        } else {
            System.out.println("userID is " + Integer.toString(currentUserId));
        }

        Toast.makeText(this, "REGISTER USER!", Toast.LENGTH_LONG).show();
        //this will be the userID of the person logged in
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
        // Check for credentials before making intent

//        if (!LoginFragment.checkCredentials(username, password)) {
//            Toast.makeText(this, "Username and password don't match!", Toast.LENGTH_LONG).show();
//            return;
//        }

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

    public static boolean checkUserExist(String username) {
        // Get a database manager
        DatabaseManager databaseManager = new DatabaseManager();

        // Open the database to write
        SQLiteDatabase writable = databaseManager.openDatabase();

        // TODO: 5/12/17
        // Read from the database
        // Define a projection that specifies which columns from the database
        // you will actually use after this query
        String[] projection = { User.KEY_UserId, User.KEY_LoginName };

        // Filter results WHERE "LoginName" = "Current registration name"
        String selection = User.KEY_LoginName + " = ?";
        String[] selectionArgs = { username };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = User.KEY_LoginName + " DESC";

        // Look through the rows
        Cursor cursor = writable.query(User.TABLE, projection, selection, selectionArgs, null, null, sortOrder);
        List loginNames = new ArrayList<>();
        while (cursor.moveToNext()) {
            String loginName = cursor.getString(cursor.getColumnIndexOrThrow(User.KEY_LoginName));
            loginNames.add(loginName);
        }

        cursor.close();

        // Check if list contain the registration name
        if (loginNames.contains(username)) {
            return true;
        }
        return false;

    }

    public static boolean checkEmailExist(String email) {

        // Get a database manager
        DatabaseManager databaseManager = new DatabaseManager();

        // Open the database to write
        SQLiteDatabase writable = databaseManager.openDatabase();

        // TODO: 5/12/17
        // Read from the database
        // Define a projection that specifies which columns from the database
        // you will actually use after this query
        String[] projection = { User.KEY_Email };

        // Filter results WHERE "LoginName" = "Current registration name"
        String selection = User.KEY_Email + " = ?";
        String[] selectionArgs = { email };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = User.KEY_Email + " DESC";

        // Look through the rows
        Cursor cursor = writable.query(User.TABLE, projection, selection, selectionArgs, null, null, sortOrder);
        List emails = new ArrayList<>();
        while (cursor.moveToNext()) {
            String address = cursor.getString(cursor.getColumnIndexOrThrow(User.KEY_Email));
            emails.add(address);
        }

        cursor.close();
        // Check if list contain the registration name
        if (emails.contains(email)) {
            return true;
        }
        return false;

    }


    private void insertSampleData() {
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleIdNum("1920319823");
        vehicle.setModel("accord");
        vehicle.setMake("honda");
        vehicle.setYear("ssss");
        VehicleRepo vehicleRepo = new VehicleRepo();
        vehicleRepo.insertVehicle(vehicle);
        System.out.println("vehiclesssss");
    }

}


