package com.studentsins.lust;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
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
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private Boolean isPostRequestSuccessful = false;
    Boolean isSuccessfulLogin = false;
    String udid = "c376e418-da42-39fb-0000-d821f1fd2804";

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
        //get the default database
        realm = Realm.getDefaultInstance();
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
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            if(isNetworkAvailable()) {
                showProgress(true);
                final JSONObject myJson = new JSONObject();
                try {
                    //generate random uuid...
                    udid = UUID.randomUUID().toString();
                    //if this is the firs time the user logs in - i.e. the udid is not stored ...
                    if(mUserCredentialses.size() == 0) {
                        //create a new record in the db and store the new udid
                        Log.d(TAG,"mUserCredentials size is 0");
                        realm.beginTransaction();
                        UserCredentials user = realm.createObject(UserCredentials.class); // Create a new object
                        user.setUserUUID(udid);
                        realm.commitTransaction();
                        //if the user exists...
                    }else{
                        //read the udid
                        udid = mUserCredentialses.get(0).getUserUUID();
                        Log.d(TAG,"mUserCredentials size is 1 \nNew User udid is: "+ udid);
                    }

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
                            try {
                                String token = responseJson.getString("token");
                                Log.d(TAG, "Response token: " + token);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //save the user as logged in to the db...

                                UserCredentials toEdit = realm.where(UserCredentials.class).findAll().get(0);
                                realm.beginTransaction();
                                toEdit.setIsUserLoggedIn(true);
                                realm.commitTransaction();
                                Log.d(TAG, "iS USER LOGGED IN: " + mUserCredentialses.get(0).getIsUserLoggedIn());


                            Toast.makeText(LoginActivity.this, "Login Successful!" + message, Toast.LENGTH_LONG).show();
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
        builder.setTitle("Connection Unsuccessful");
        builder.setMessage("There was an error connecting to the server.Please, try again later.");
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.show();
        dialog.show();
    }

    private void showIncorrectUsernameOrPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Login Unsuccessful");
        builder.setMessage("There was an error login you in."+message+"!/nNOTE:Please make sure" +
                " that the account is activated -> check your email.");
        builder.setPositiveButton("OK", null);
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
}

