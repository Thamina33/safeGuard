package com.bu.safeguard;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class safeguard extends  Application {

    @Override
    public void onCreate() {
        super.onCreate();

    //    FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
