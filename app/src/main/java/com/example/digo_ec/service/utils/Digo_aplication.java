package com.example.digo_ec.service.utils;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;



public class Digo_aplication extends Application {


    public static FirebaseAnalytics mFirebaseAnalytics;
    @Override
    public void onCreate() {
    //  FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSdk.fullyInitialize();

        AppEventsLogger.activateApp(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);



        super.onCreate();
        //Fabric.with(this, new Crashlytics());
    }






}
