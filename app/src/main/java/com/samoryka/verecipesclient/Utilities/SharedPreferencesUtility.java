package com.samoryka.verecipesclient.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.samoryka.verecipesclient.Model.AppUser;

/**
 * Created by kasam on 17/01/2017.
 */

public class SharedPreferencesUtility {
    private final static String PREFS_NAME = "veRecipesPrefs";
    private final static String USER_LOGIN = "userLoggedIn";
    private final static String USER_DETAILS = "userDetails";

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static boolean checkUserLoggedIn(Context context) {
        SharedPreferences prefs = getPreferences(context);
        return prefs.getBoolean(USER_LOGIN, false);
    }

    public static AppUser getLoggedInUser(Context context) {
        SharedPreferences prefs = getPreferences(context);

        Gson gson = new Gson();
        String json = prefs.getString(USER_DETAILS, "");

        return gson.fromJson(json, AppUser.class);
    }

    public static void setLoggedInUser(Context context, AppUser user) {
        SharedPreferences.Editor prefsEditor = getPreferences(context).edit();

        Gson gson = new Gson();
        String json = gson.toJson(user);

        prefsEditor.putString(USER_DETAILS, json);
        prefsEditor.putBoolean(USER_LOGIN, true);
        prefsEditor.commit();

    }
}
