package com.ndtv.mediaprima.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by NAGARAJ on 7/29/2016.
 */
public class PreferenceManager {
    private SharedPreferences sharedPrefs;
    private static PreferenceManager sInstance;
    private SharedPreferences.Editor editor;
    private static Context context;

    public interface PreferenceKeys {
        String SHARED_PREFS = "Drama Sangat";
        String IS_LOGGEN_IN = "is_logged_in";
        String AUTHORIZATION_TOKEN ="Authorization";
    }

    public static PreferenceManager getsInstance(Context ctx) {
        context = ctx;
        if (sInstance == null) {
            synchronized (PreferenceManager.class) {
                if (sInstance == null) {
                    sInstance = new PreferenceManager(ctx);
                }
            }
        }
        return sInstance;
    }


    private PreferenceManager(Context ctx) {
        sharedPrefs = ctx.getSharedPreferences(PreferenceKeys.SHARED_PREFS, Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();
    }


    public void setSharedPreferencesListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        sharedPrefs.registerOnSharedPreferenceChangeListener(listener);
    }

    public void setAuthorizationToken(String accessToken) {
        editor.putString(PreferenceKeys.AUTHORIZATION_TOKEN, accessToken);
        editor.commit();
    }

    public String getAuthorizationToken() {
        return sharedPrefs.getString(PreferenceKeys.AUTHORIZATION_TOKEN, "");
    }

    /*public void clearLogin() {
        editor.clear();
        editor.commit();
    }

    public void createUserLoginSession() {
        editor.putBoolean(PreferenceKeys.IS_LOGGEN_IN, true);
        editor.commit();
    }*/
}
