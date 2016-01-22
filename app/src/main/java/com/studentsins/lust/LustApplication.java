package com.studentsins.lust;

import android.app.Application;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Georgi on 1/18/2016.
 */
public class LustApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .schemaVersion(0) // Must be bumped when the schema changes
                .migration(new Migration()) // Migration to run instead of throwing an exception
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Log.d(LustApplication.class.getSimpleName(),"onCreate - Application");
    }

}
