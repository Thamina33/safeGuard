package com.bu.safeguard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

public class splashScreen extends AppCompatActivity {

    FirebaseAuth auth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        auth = FirebaseAuth.getInstance() ;

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

                    if(auth.getUid() == null){
                        Intent i = new Intent(getApplicationContext() , login_activity.class);
                        startActivity(i);
                        finish();


                    }else {
                        Intent i = new Intent(getApplicationContext() , MainActivity.class);
                        startActivity(i);
                        finish();
                    }



                }


            }
        }, 1300) ;


    }


}
