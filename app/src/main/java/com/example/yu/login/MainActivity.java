package com.example.yu.login;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.yu.login.data.DBHelper;
import com.example.yu.login.data.DatabaseManager;
import com.example.yu.login.data.model.User;
import com.example.yu.login.data.model.Vehicle;
import com.example.yu.login.data.model.Trip;
import com.example.yu.login.data.repo.UserRepo;
import com.example.yu.login.data.repo.VehicleRepo;
import com.example.yu.login.data.repo.TripRepo;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private static Context context;
    private static  DBHelper dbHelper;
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
        signin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent registerIntent = new Intent(MainActivity.this, RegisterAcivity.class);
                MainActivity.this.startActivity(registerIntent);
            }
        });

        registerButton.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v) {
                        Intent MenuIntent = new Intent(MainActivity.this, MenuActivity.class);
                        MainActivity.this.startActivity(MenuIntent);
//                        UserRepo userRepo = new UserRepo();
//                        User registerUser = new User();
//                        registerUser.setLoginName(etUsername.getText().toString());
//                        registerUser.setLoginPW(etPassword.getText().toString());
//                        userRepo.insert(registerUser);
                    }
        } );
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
