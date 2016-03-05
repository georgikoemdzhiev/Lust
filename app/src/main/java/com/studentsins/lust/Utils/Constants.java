package com.studentsins.lust.Utils;

import java.util.regex.Pattern;

/**
 * Class to hold any constants needed elsewhere in the app.
 * Created by koemdzhiev on 22/01/16.
 */
public class Constants {
    public static final String USER_UDID = "user_udid";
    public static final String USER_TOKEN = "user_token";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_IF_LOG_IN = "user_logged_in";
    
    public static final Pattern EMAIL_PATTERN = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    public static final String CANT_DECIDE_PROGRESS_BAR = "cant_decide_progress_bar";
}
