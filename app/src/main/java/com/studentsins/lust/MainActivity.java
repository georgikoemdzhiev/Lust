package com.studentsins.lust;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        realm = Realm.getDefaultInstance();
//        Log.d(TAG,"LAST USER LOGGED INL " + lastUserLoggedin);

       UserCredentials mCurrentUser = realm.where(UserCredentials.class).findFirst();
        if(mCurrentUser == null){
            navigateToLogin();
        }else if (!mCurrentUser.getIsUserLoggedIn()){
            navigateToLogin();
        }

//    logUserInfo();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.action_logout){
            UserCredentials userCredentials = realm.where(UserCredentials.class).findFirst();
            realm.beginTransaction();
            userCredentials.setIsUserLoggedIn(false);
            realm.commitTransaction();
            navigateToLogin();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private void logUserInfo(){
        List<UserCredentials> userCredentials = realm.where(UserCredentials.class).findAll();
        for (UserCredentials uc :userCredentials){
            Log.d(TAG, "*********USER*******************");
            Log.d(TAG,"user UUID: " + uc.getUserUUID());
            Log.d(TAG,"is user logged in: " + uc.getIsUserLoggedIn());
            Log.d(TAG,"user token: " + uc.getToken());
            Log.d(TAG,"user email: " + uc.getUserEmail());
            Log.d(TAG,"*******END OF THIS USER*********");
        }
    }
}
