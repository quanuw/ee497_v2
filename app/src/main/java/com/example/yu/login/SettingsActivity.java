package com.example.yu.login;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;

// TODO: 4/29/17
// Using a workaround right now for back navigation to menu activity. Declared MenuActivity as the
// parent activity in manifest.
public class SettingsActivity extends AppCompatActivity {

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_settings);

            // Display the fragment as the main content.
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
