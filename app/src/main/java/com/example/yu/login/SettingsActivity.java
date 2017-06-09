package com.example.yu.login;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        dbHelper = new DBHelper(this);

        getFragmentManager().beginTransaction()
                .replace(R.id.settingsContainer, new SettingsFragment())
                .commit();
    }

    public static class SettingsFragment extends PreferenceFragment {
        SQLiteDatabase db;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            db = dbHelper.getWritableDatabase();

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            int userId = sharedPreferences.getInt("userId", -1);
            if (userId == -1) {
                Log.e(TAG, "Error failed to get userId");
                return;
            }

            // Get vehicles associated with user's id
            String[] projection = {Vehicle.KEY_VehicleId, Vehicle.KEY_Make};
            String selection =  Vehicle.KEY_UserId + " = ?";
            String[] selectionArgs = {String.valueOf(userId)};
            String sortOrder = Vehicle.KEY_VehicleId + " ASC";
            Cursor cursor = db.query(Vehicle.TABLE, projection, selection, selectionArgs, null, null, sortOrder);
            ArrayList<String> entriesList = new ArrayList<>();
            ArrayList<String> entryValuesList = new ArrayList<>();

            // Load db data into ArrayList
            cursor.moveToFirst();
            String vehicleId = cursor.getString(cursor.getColumnIndexOrThrow(Vehicle.KEY_VehicleId));
            String vehicleMake = cursor.getString(cursor.getColumnIndexOrThrow(Vehicle.KEY_Make));
            // Log.e(TAG, "vehicleId: " + vehicleId + " vehicleMake: " + vehicleMake);
            entriesList.add(vehicleMake);
            entryValuesList.add(vehicleId);
            while (cursor.moveToNext()) {
                vehicleId = cursor.getString(cursor.getColumnIndexOrThrow(Vehicle.KEY_VehicleId));
                vehicleMake = cursor.getString(cursor.getColumnIndexOrThrow(Vehicle.KEY_Make));
                entriesList.add(vehicleMake);
                entryValuesList.add(vehicleId);
            }

            // List to array
            String[] entries = entriesList.toArray(new String[0]);
            String[] entryValues = entryValuesList.toArray(new String[0]);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);

            ListPreference vehiclePrefs = (ListPreference) findPreference("vehiclePref");

            vehiclePrefs.setEntries(entries);
            vehiclePrefs.setEntryValues(entryValues);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            db.close(); // close the db
        }

    }

}
