package com.studentsins.lust;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements Callback {
    private Realm realm;
    RealmResults<UserCredentials> mUserCredentialses;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private Boolean isPostRequestSuccessful = false;
    Boolean isSuccessfulLogin = false;
    String udid = "";
    //Stores the user email after it has been verified as an email - containing '@' symbol
    private String userEmailVerified;
    private static final String TAG = LoginActivity.class.getSimpleName();
    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //get the shared preferences to retrieve the udid
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //hide toolbar
        getSupportActionBar().hide();
        //get the default database
        realm = Realm.getDefaultInstance();
        //generate a random uuid if this is the first time the user opens the app...
        if(sharedPreferences.getString(Constants.USERS_UDID, "").equals("")) {
            udid = UUID.randomUUID().toString();
            editor.putString(Constants.USERS_UDID, udid);
            editor.apply();
        }else{
            //if this is not the first time... i.e. we have a UDID stored -> retrive it...
            udid = sharedPreferences.getString(Constants.USERS_UDID, "");
        }

        //finds all the users
        mUserCredentialses = realm.where(UserCredentials.class).findAll();
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    /**
     * Attempts to sign in the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            cancel = true;

            focusView = mEmailView;
        }

        if(TextUtils.isEmpty(password)){
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            userEmailVerified = mEmailView.getText().toString();
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            if(isNetworkAvailable()) {
                showProgress(true);
                final JSONObject myJson = new JSONObject();
                try {
                    myJson.put("udid", udid);
                    myJson.put("email", mEmailView.getText().toString());
                    myJson.put("password", mPasswordView.getText().toString());

                    post("https://api.studentsins.com/v1/auth", myJson.toString());

                }catch (IOException e ) {
                    e.printStackTrace();
                }catch (JSONException j) {
                    j.printStackTrace();
                }
            }else{
                Toast.makeText(LoginActivity.this,"No internet connection!",Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 8;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Log.d(TAG,"OnFailure "+e.getMessage());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showProgress(false);
                showErrorConnectingToServerDialog();
            }
        });
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showProgress(false);
            }
        });
        Log.d(TAG,"OnResponse "+response.toString());
            isPostRequestSuccessful = response.isSuccessful();
            Log.d(TAG, "WAS THIS LOGIN SUCCESSFUL: " + isPostRequestSuccessful);

        if(isPostRequestSuccessful) {
            try {
                 final JSONObject responseJson = new JSONObject(response.body().string());
                isSuccessfulLogin = responseJson.getBoolean("success");
                message = responseJson.getString("message");
                Log.d(TAG, "Response message: " + message);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isSuccessfulLogin) {
                            String token = null;
                            try {
                                token = responseJson.getString("token");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d(TAG, "Response token: " + token);

                            Log.d(TAG, "users size is: " + mUserCredentialses.size());
                            //create a new record in the db and store the udid...
                            UserCredentials newEntry = new UserCredentials();
                            newEntry.setUserEmail(userEmailVerified);
                            newEntry.setIsUserLoggedIn(true);
                            newEntry.setToken(token);
                            newEntry.setUserUUID(udid);
                            realm.beginTransaction();
                            //clear previous users...
                            mUserCredentialses.clear();
                            realm.copyToRealmOrUpdate(newEntry);
                            realm.commitTransaction();

                            Log.d(TAG,"users size is: " + mUserCredentialses.size());



                            Toast.makeText(LoginActivity.this, "Login Successful! toEdit size: " + mUserCredentialses.size() + message, Toast.LENGTH_LONG).show();
                            logUserInfo();
                            navigateToApp();
                        } else {
                            message = "This email or password is incorrect";
                            showIncorrectUsernameOrPasswordDialog();

//                                Toast.makeText(LoginActivity.this, "Login Unsuccessful! " + message, Toast.LENGTH_LONG).show();
                        }
                    }

                });
            }catch (JSONException j){
                j.printStackTrace();
            }
        }else{
            showErrorConnectingToServerDialog();
        }
    }




    private void post(String url, String json) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(this);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        //contition to check if there is a network and if the device is connected
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }

        return isAvailable;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private void showErrorConnectingToServerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle(getString(R.string.error_dialog_title_connection_to_server));
        builder.setMessage(getString(R.string.error_dialog_message_connection_to_server));
        builder.setPositiveButton(getString(R.string.error_dialog_positive_button_connection_to_server), null);
        AlertDialog dialog = builder.show();
        dialog.show();
    }

    private void showIncorrectUsernameOrPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle(getString(R.string.error_dialog_title_incorrect_credentials));
        builder.setMessage("There was an error while logging you in. "+message+"!\nNOTE:Please make sure" +
                " that the account is activated -> check your email.");
        builder.setPositiveButton(getString(R.string.error_dialog_positive_button_connection_to_server), null);
        AlertDialog dialog = builder.show();
        dialog.show();
    }

    private void navigateToApp() {
        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void logUserInfo(){
        List<UserCredentials> userCredentials = realm.where(UserCredentials.class).findAll();
        for (UserCredentials uc :userCredentials){
            Log.d(TAG,"*********USER*******************");
            Log.d(TAG,"user UUID: " + uc.getUserUUID());
            Log.d(TAG,"is user logged in: " + uc.getIsUserLoggedIn());
            Log.d(TAG,"user token: " + uc.getToken());
            Log.d(TAG,"user email: " + uc.getUserEmail());
            Log.d(TAG,"*******END OF THIS USER*********");
        }
    }
}

