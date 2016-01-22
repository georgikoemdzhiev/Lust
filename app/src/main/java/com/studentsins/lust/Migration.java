package com.studentsins.lust;

import android.util.Log;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by Georgi on 1/18/2016.
 */
public class Migration implements RealmMigration {
    private static final String TAG = Migration.class.getSimpleName();

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
// Access the Realm schema in order to create, modify or delete classes and their fields.
        RealmSchema schema = realm.getSchema();

        // Migrate from version 0 to version 1
        if (oldVersion == 0) {
            Log.d(TAG, "Migration happened!");
            // Create a new class
            schema.create("UserCredentials")
                    .addField("isUserLoggedIn", Boolean.class)
                    .addField("userUUID", String.class)
                    .addField("token", String.class)
                    .addPrimaryKey("userEmail");

            oldVersion++;
        }

    }

}
