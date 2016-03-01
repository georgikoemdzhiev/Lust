package com.studentsins.lust.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.studentsins.lust.Adapters.LustFragmentPagerAdapter;
import com.studentsins.lust.R;
import com.studentsins.lust.Utils.Constants;
import com.studentsins.lust.Utils.ListenerCollection;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private SharedPreferences sharedPreferences;
    private  SharedPreferences.Editor editor;
    private FloatingActionButton mChangeStatus, mCreatePlan;
    public static FloatingActionsMenu mFloatingActionsMenu;
    public static AppBarLayout appBarLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //disable the title - design requirement.
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        editor = sharedPreferences.edit();

       Boolean isUserLoggedIn = sharedPreferences.getBoolean(Constants.USER_IF_LOG_IN,false);
        if(!isUserLoggedIn){
            navigateToLogin();
        }

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new LustFragmentPagerAdapter(getSupportFragmentManager(), this));
        // Give the TabLayout the ViewPager
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);

        appBarLayout = (AppBarLayout)findViewById(R.id.appBarLayouy);

        mFloatingActionsMenu = (FloatingActionsMenu)findViewById(R.id.floatingActionMenu);

        mChangeStatus = (FloatingActionButton)findViewById(R.id.changeStatusBtn);
        mChangeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"Change Status pressed! | " + viewPager.getCurrentItem());
                mFloatingActionsMenu.collapse();
            }
        });
        mCreatePlan = (FloatingActionButton)findViewById(R.id.createPlanBtn);
        mCreatePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"Create plan pressed! | " + viewPager.getCurrentItem());
                mFloatingActionsMenu.collapse();
            }
        });

        viewPager.addOnPageChangeListener(ListenerCollection.onPageChangeListenerShowFAB);


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
            editor.putBoolean(Constants.USER_IF_LOG_IN,false);
            editor.apply();
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
        String userEmail = sharedPreferences.getString(Constants.USER_EMAIL, "");
        String userUDID = sharedPreferences.getString(Constants.USER_UDID,"");
        Boolean userIfLoggedIn = sharedPreferences.getBoolean(Constants.USER_IF_LOG_IN, false);
        String userToken = sharedPreferences.getString(Constants.USER_TOKEN,"");
        Log.d(TAG,"*********USER*******************");
        Log.d(TAG,"user UUID: " + userUDID);
        Log.d(TAG,"is user logged in: " + userIfLoggedIn);
        Log.d(TAG,"user token: " +userToken);
        Log.d(TAG,"user email: " + userEmail);
        Log.d(TAG,"*******END OF THIS USER*********");
    }
}
