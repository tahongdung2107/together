package com.example.dung.togetherfinal11.SharedPreferences;

import android.content.Context;

import java.util.Set;


public class BaseSharePreferences {
    private static final String PREF_NAME = "CLS_PR";
    private Context context;

    private SecurePreferences pref;
    private SecurePreferences.Editor editor;


    public BaseSharePreferences(Context context) {
        this(context, PREF_NAME);
    }

    public BaseSharePreferences(Context context, String prefName) {
        this.context = context;
        pref = new SecurePreferences(context, prefName);
        editor = pref.edit();
    }

    protected void remove(String key){
        editor.remove(key).commit();
    }

    protected void clear(){
        editor.clear().commit();
    }

    protected void putInt(String key, int value) {
        editor.putInt(key, value).commit();
    }

    protected int getInt(String key) {
        return pref.getInt(key, 0);
    }

    protected void putLong(String key, long value) {
        editor.putLong(key, value).commit();
    }

    protected long getLong(String key) {
        return pref.getLong(key, 0L);
    }

    protected void putFloat(String key, float value) {
        editor.putFloat(key, value).commit();
    }

    protected float getFloat(String key) {
        return pref.getFloat(key, 0.0f);
    }

    protected void putString(String key, String value) {
        editor.putString(key, value).commit();
    }

    protected String getString(String key) {
        return pref.getString(key, "");
    }

    protected void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value).commit();
    }

    protected boolean getBoolean(String key) {
        return pref.getBoolean(key, false);
    }

    protected void putStringSet(String key, Set<String> value) {
        editor.putStringSet(key, value).commit();
    }

    protected Set<String> getStringSet(String key) {
        return pref.getStringSet(key, null);
    }
}
