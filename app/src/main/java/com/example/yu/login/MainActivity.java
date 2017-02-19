package com.example.yu.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                    }
        } );
    }
}
