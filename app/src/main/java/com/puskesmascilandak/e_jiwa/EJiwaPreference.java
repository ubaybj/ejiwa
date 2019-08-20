package com.puskesmascilandak.e_jiwa;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.puskesmascilandak.e_jiwa.responses.LastChecked;

public class EJiwaPreference {
    public static final String LAST_CHECKED="LC";
    private SharedPreferences preferences;
    private final Context context;

    public EJiwaPreference(Context context) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    protected Context getContext() {
        return context;
    }

    void putString(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    String getString(String key) {
        return preferences.getString(key, "");
    }


    public void putLastChecked(String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key) {
        return preferences.getInt(key, 0);
    }

    public void quit() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}
