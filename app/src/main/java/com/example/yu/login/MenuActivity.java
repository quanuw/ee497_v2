package com.example.yu.login;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import layout.AddVehicleFragment;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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
                // Made it an activity to avoid dealing with xml overlaps
                Intent settingsIntent = new Intent(MenuActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_manage:
                BlankFragment mangageFrament = new BlankFragment();
                FragmentManager navManager = getSupportFragmentManager();
                navManager.beginTransaction().replace(R.id.content_menu, mangageFrament).commit();
                drawer.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_add_vehicle:
                AddVehicleFragment addVehicleFragment = AddVehicleFragment.newInstance();
                FragmentManager addVehicleManager = getSupportFragmentManager();
                addVehicleManager.beginTransaction().replace(R.id.content_menu, addVehicleFragment).commit();
                drawer.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_send: // Send an image.
                pickImage();
                return true;
            default:
                drawer.closeDrawer(GravityCompat.START);
                return false;
        }
    }
    // TODO: 4/22/17
    /* Following block of code is for sending images. Android does not natively support this.
    Might need to use a library.
    REFERENCE: https://github.com/klinker41/android-smsmms
     */
    private void pickImage() {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK);
        pickContactIntent.setType("image/*"); // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request it is that we're responding to
        if (requestCode == PICK_IMAGE_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Get the URI that points to the selected contact
                Uri contactUri = data.getData();
                // We only need the NUMBER column, because there will be only one row in the result
                String[] projection = {ContactsContract.CommonDataKinds.Photo.PHOTO};

                // Perform the query on the contact to get the NUMBER column
                // We don't need a selection or sort order (there's only one result for the given URI)
                // CAUTION: The query() method should be called from a separate thread to avoid blocking
                // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
                // Consider using CursorLoader to perform the query.
                Cursor cursor = getContentResolver()
                        .query(contactUri, projection, null, null, null);
                cursor.moveToFirst();

                // Retrieve the phone number from the NUMBER column
                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO);
                String number = cursor.getString(column);

                // Do something with the phone number...
            }
        }
    }

}
