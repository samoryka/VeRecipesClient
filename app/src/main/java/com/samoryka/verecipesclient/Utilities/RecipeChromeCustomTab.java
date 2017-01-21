package com.samoryka.verecipesclient.Utilities;

import android.content.Context;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;

import com.samoryka.verecipesclient.Model.Recipe;
import com.samoryka.verecipesclient.R;

/**
 * Used to manage the recipe tabs open when the user clicks on a recipe
 */

public class RecipeChromeCustomTab {

    public static void openRecipeTab(Context context, Recipe recipe) {
        Uri uri = Uri.parse(recipe.getRecipeURL());

        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();

        //Custom tab customization
        intentBuilder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
        intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));


        // Share menu in the tab menu
        /*
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, recipe.getName());
        shareIntent.putExtra(Intent.EXTRA_TEXT, uri.toString());
        Log.d("EVENT", uri.toString());
        PendingIntent menuIntent = PendingIntent.getActivity(context, 0, shareIntent, 0);
        intentBuilder.addMenuItem(context.getResources().getString(R.string.recipe_viewer_share), menuIntent);
        */


        CustomTabsIntent customTabsIntent = intentBuilder.build();
        customTabsIntent.launchUrl(context, uri);
    }
}
