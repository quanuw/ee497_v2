package com.example.yu.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UserAcivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_acivity);
        final TextView welcome = (TextView) findViewById(R.id.welcome);
        final Button PrivacyButton = (Button) findViewById(R.id.Privacy);

        PrivacyButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                Intent MenuIntent = new Intent(UserAcivity.this, MenuActivity.class);
                UserAcivity.this.startActivity(MenuIntent);
            }
        } );
    }
}
