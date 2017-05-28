package com.example.yu.login;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;

import com.example.yu.login.data.DBHelper;
import com.example.yu.login.data.model.Vehicle;

import java.util.ArrayList;


// TODO: 4/29/17
// Using a workaround right now for back navigation to menu activity. Declared MenuActivity as the
// parent activity in manifest.
public class SettingsActivity extends PreferenceActivity{
    private static final String TAG = "SETTINGSACTIVITY";

    private static DBHelper dbHelper;
    private SQLiteDatabase db;

    String[] entries;
    String[] entryValues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
//        ListPreference vehiclePrefs = (ListPreference) getSharedPreferences("vehiclePref", Context.MODE_PRIVATE);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

//        int userId = sharedPreferences.getInt("userId", -1);
        int userId = 5;
                // Get vehicles associated with user's id
        String[] projection = {Vehicle.KEY_VehicleId, Vehicle.KEY_Make};
        String selection =  Vehicle.KEY_UserId + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};
        String sortOrder = Vehicle.KEY_VehicleId + " ASC";
        Cursor cursor = db.query(Vehicle.TABLE, projection, selection, selectionArgs, null, null, sortOrder);
        ArrayList<String> entriesList = new ArrayList<>();
        ArrayList<String> entryValuesList = new ArrayList<>();

        // Display the fragment as the main content.
        cursor.moveToFirst();
        String vehicleId = cursor.getString(cursor.getColumnIndexOrThrow(Vehicle.KEY_VehicleId));
        String vehicleMake = cursor.getString(cursor.getColumnIndexOrThrow(Vehicle.KEY_Make));
        Log.e(TAG, "vehicleId: " + vehicleId + " vehicleMake: " + vehicleMake);
        entriesList.add(vehicleId);
        entryValuesList.add(vehicleMake);
        while (cursor.moveToNext()) {
            vehicleId = cursor.getString(cursor.getColumnIndexOrThrow(Vehicle.KEY_VehicleId));
            vehicleMake = cursor.getString(cursor.getColumnIndexOrThrow(Vehicle.KEY_Make));
            entriesList.add(vehicleId);
            entryValuesList.add(vehicleMake);
        }
        // List to array
        entries = entriesList.toArray(new String[0]);
        entryValues = entryValuesList.toArray(new String[0]);
        // TODO: 5/27/17
        // Cannot get a reference to the ListPreference
        PreferenceScreen root = this.getPreferenceScreen();
        if (root == null) {
            Log.e(TAG, "root is NULL!");
        }
        ListPreference vehiclePrefs = (ListPreference) root.findPreference("vehiclePref");
        Log.e(TAG, "entriesSize: " + entries.length + " entryValuesSize: " + entryValues.length);
        // Update list vehicle preference
        if (vehiclePrefs == null) {
            Log.e(TAG, "vehiclePrefs is NULL!");
        }
        vehiclePrefs.setEntries(entries);
        vehiclePrefs.setEntryValues(entryValues);
        getFragmentManager().beginTransaction()
                .replace(R.id.settingsContainer, new SettingsFragment())
                .commit();
    }

    public static class SettingsFragment extends PreferenceFragment {

            @Override
            public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                // Load the preferences from an XML resource
                addPreferencesFromResource(R.xml.preferences);
            }
        }

    // TODO: 4/26/17
    // Should extend from a baseclass to avoid repeating this menu code

}
