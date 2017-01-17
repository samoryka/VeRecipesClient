package com.samoryka.verecipesclient.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.samoryka.verecipesclient.Model.AppUser;

import java.util.Date;

/**
 * Used to manage the application's shared preferences (common settings)
 */

public class SharedPreferencesUtility {
    private final static String PREFS_NAME = "veRecipesPrefs";
    private final static String USER_LOGIN = "userLoggedIn";
    private final static String USER_DETAILS = "userDetails";
    private final static String LAST_VISIT = "lastVisit";
    private final static String LAST_RECIPE_SWIPED = "lastRecipeSwipedId";

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

    public static Date getLastVisit(Context context) {
        SharedPreferences prefs = getPreferences(context);

        String lastVisitString = prefs.getString(LAST_VISIT, "1970-01-01");
        return StringFormatUtility.YYYYMMDDToDate(lastVisitString);
    }

    public static void setLastVisit(Context context, Date date) {
        SharedPreferences.Editor prefsEditor = getPreferences(context).edit();

        String dateString = StringFormatUtility.DateToYYYYMMDD(date);

        prefsEditor.putString(LAST_VISIT, dateString);
        prefsEditor.commit();
    }


    public static long getLastSwipedRecipe(Context context) {
        SharedPreferences prefs = getPreferences(context);

        return prefs.getLong(LAST_RECIPE_SWIPED, 0);
    }

    public static void setLastSwipedRecipe(Context context, long recipeId) {
        SharedPreferences.Editor prefsEditor = getPreferences(context).edit();
        long test = getLastSwipedRecipe(context);
        prefsEditor.putLong(LAST_RECIPE_SWIPED, recipeId);
        prefsEditor.commit();
    }

}
