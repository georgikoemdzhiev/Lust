package com.studentsins.lust;

import android.app.Application;
import android.util.Log;

/**
 * Created by Georgi on 1/18/2016.
 */
public class LustApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(LustApplication.class.getSimpleName(),"onCreate - Application");
    }

}
