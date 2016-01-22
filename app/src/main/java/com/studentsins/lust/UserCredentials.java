package com.studentsins.lust;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Georgi on 1/18/2016.
 */
public class UserCredentials extends RealmObject {
    //THESE WILL BE CHANGED AFTER REGISTRATION IS POSSIBLE FROM THE APP
    @PrimaryKey
    private String userEmail;
    private Boolean isUserLoggedIn;
    private String userUUID;
    private String token;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Boolean getIsUserLoggedIn() {
        return isUserLoggedIn;
    }

    public void setIsUserLoggedIn(Boolean isUserLoggedIn) {
        this.isUserLoggedIn = isUserLoggedIn;
    }

    public String getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
