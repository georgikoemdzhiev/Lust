package com.studentsins.lust.UI;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.studentsins.lust.R;
import com.studentsins.lust.Utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;

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
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String TAG = LoginActivity.class.getSimpleName();
    Boolean isSuccessfulLogin = false;
    String udid = "";
    private Boolean isPostRequestSuccessful = false;
    //Stores the user email after it has been verified as an email - containing '@' symbol
    private String userEmailVerified;
    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private TextView mRegister;
    private String message;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
//    private TextView mForgottenPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //get the shared preferences to retrieve the udid
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        editor = sharedPreferences.edit();
        logUserInfo();
        //generate a random uuid if this is the first time the user opens the app...
        if (sharedPreferences.getString(Constants.USER_UDID, "").equals("")) {
            udid = UUID.randomUUID().toString();
            editor.putString(Constants.USER_UDID, udid);
            editor.apply();
        } else {
            //if this is not the first time... i.e. we have a UDID stored -> retrive it...
            udid = sharedPreferences.getString(Constants.USER_UDID, "");
        }
//Set up the register button...
        mRegister = (TextView) findViewById(R.id.register);
        mRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
// Set up the password reset link to the Lust site...
        findViewById(R.id.forgotPasswordLink).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://my.studentsins.com/reset";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });

        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        Button mLogInButton = (Button) findViewById(R.id.email_sign_in_button);
        mLogInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        this.mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId == 0) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Attempts to log in the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString().trim();
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

        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            userEmailVerified = email;
            Log.d(TAG, "|" + email);
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            if (isNetworkAvailable()) {
                showProgress(true);
                final JSONObject myJson = new JSONObject();
                try {
                    myJson.put("udid", udid);
                    myJson.put("email", userEmailVerified);
                    myJson.put("password", password);

                    post("https://api.studentsins.com/v1/auth", myJson.toString());

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException j) {
                    j.printStackTrace();
                }
            } else {
                Toast.makeText(LoginActivity.this, "No internet connection!", Toast.LENGTH_LONG).show();
            }
        }
    }

    //Method to validate an email...
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        Matcher matcher = RegisterActivity.EMAIL_PATTERN.matcher(email);
        return matcher.matches();
        //return email.contains("@");
    }

    //Method to validate a password...
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 8;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        final LinearLayout regCont = (LinearLayout) findViewById(R.id.registerContainer);
        final LinearLayout forgotPassCont = (LinearLayout) findViewById(R.id.forgottenPasContainer);

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

        regCont.setVisibility(show ? View.GONE : View.VISIBLE);
        regCont.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                regCont.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });
        forgotPassCont.setVisibility(show ? View.GONE : View.VISIBLE);
        forgotPassCont.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                forgotPassCont.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Log.d(TAG, "OnFailure " + e.getMessage());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showProgress(false);
                showErrorConnectingToServerDialog();
            }
        });
    }

    //Method to handle the response from the server...
    @Override
    public void onResponse(Call call, Response response) throws IOException {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showProgress(false);
            }
        });
        Log.d(TAG, "OnResponse " + response.toString());
        isPostRequestSuccessful = response.isSuccessful();
        Log.d(TAG, "WAS THIS LOGIN REQUEST SUCCESSFUL: " + isPostRequestSuccessful);

        if (isPostRequestSuccessful) {
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

                            editor.putString(Constants.USER_EMAIL, userEmailVerified);
                            editor.putBoolean(Constants.USER_IF_LOG_IN, true);
                            editor.putString(Constants.USER_TOKEN, token);
                            editor.putString(Constants.USER_UDID, udid);
                            editor.apply();

                            Toast.makeText(LoginActivity.this, "Login Successful!" + message, Toast.LENGTH_LONG).show();
                            logUserInfo();
                            navigateToApp();
                        } else {
                            message = "This email or password is incorrect";
                            showIncorrectUsernameOrPasswordDialog();

//                                Toast.makeText(LoginActivity.this, "Login Unsuccessful! " + message, Toast.LENGTH_LONG).show();
                        }
                    }

                });
            } catch (JSONException j) {
                j.printStackTrace();
            }
        } else {
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
        builder.setMessage("There was an error while logging you in. " + message + "!\nNOTE:Please make sure" +
                " that the account is activated -> check your email.");
        builder.setPositiveButton(getString(R.string.error_dialog_positive_button_connection_to_server), null);
        AlertDialog dialog = builder.show();
        dialog.show();
    }

    private void navigateToApp() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void logUserInfo() {
        String userEmail = sharedPreferences.getString(Constants.USER_EMAIL, "");
        String userUDID = sharedPreferences.getString(Constants.USER_UDID, "");
        Boolean userIfLoggedIn = sharedPreferences.getBoolean(Constants.USER_IF_LOG_IN, false);
        String userToken = sharedPreferences.getString(Constants.USER_TOKEN, "");
        Log.d(TAG, "*********USER*******************");
        Log.d(TAG, "user UUID: " + userUDID);
        Log.d(TAG, "is user logged in: " + userIfLoggedIn);
        Log.d(TAG, "user token: " + userToken);
        Log.d(TAG, "user email: " + userEmail);
        Log.d(TAG, "*******END OF THIS USER*********");
    }
}

