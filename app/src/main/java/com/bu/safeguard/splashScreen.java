package com.bu.safeguard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class splashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getSupportActionBar().hide();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                boolean fstart = prefs.getBoolean("firstStart", true);
                if (fstart) {

                    Intent i = new Intent(getApplicationContext() , IntroActivity.class);
                    startActivity(i);
                    finish();
                }
                else {
                    // not the first install

                    Intent i = new Intent(getApplicationContext() , login_activity.class);
                    startActivity(i);
                    finish();
                }


            }
        }, 1300) ;


    }
}
