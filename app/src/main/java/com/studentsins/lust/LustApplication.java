package com.studentsins.lust;

import android.app.Application;
import android.util.Log;

/**
 * Application class to handle the local database setup - to be added later on.
 * Created by Georgi on 1/18/2016.
 */
public class LustApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(LustApplication.class.getSimpleName(), "onCreate - Application");
    }

}
