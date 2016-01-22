package com.studentsins.lust;

import io.realm.RealmObject;

/**
 * Created by Georgi on 1/18/2016.
 */
public class UserCredentials extends RealmObject {
    //THESE WILL BE CHANGED AFTER REGISTRATION IS POSSIBLE FROM THE APP
//    private String userName;
//    private String userPassword;
    private Boolean isUserLoggedIn;
    private String userUUID;

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
}
