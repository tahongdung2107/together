package com.example.dung.togetherfinal11.SharedPreferences;

import android.content.Context;


public class AppPreference extends BaseSharePreferences {
    private final String USERNAME="USERNAME";
    private final String PASSWORD="PASSWORD";
    public AppPreference(Context context) {
        super(context);
    }
    public void clear() {
        super.clear();
    }
    public String getUsername() {
        return getString(USERNAME);
    }
    public void putUsername(String str) {
        putString(USERNAME, str);
    }
    public String getPassword() {
        return getString(PASSWORD);
    }
    public void putPassword(String str) {
        putString(PASSWORD, str);
    }

}
