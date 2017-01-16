package com.samoryka.verecipesclient.Web;

import android.content.Context;
import android.util.Log;

import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.samoryka.verecipesclient.Model.AppUser;
import com.samoryka.verecipesclient.Model.Recipe;
import com.samoryka.verecipesclient.Views.DailyRecipes.DailyRecipesFragment;
import com.samoryka.verecipesclient.Views.DailyRecipes.RecipeCard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class used to call VeRecipesService requests and format them into views
 */
public class WebRequestManager {
    private final static String LOG_TAG = "RETROFIT";

    /**
     * Loads recipes at a given date on VeRecipesServer and puts them in a PlaceHolderView once the process is finished
     *
     * @param veRecipesService
     * @param mDate                 Date at which we cant to load the recipes
     * @param mSwipePlaceHolderView The view in which we want to put the recipes
     * @param mContext              Context of the said view
     * @param fragment              Fragment of the said view
     */
    public static void dailyRecipesAsSwipePlaceHolderView(VeRecipesService veRecipesService, Date mDate, final SwipePlaceHolderView mSwipePlaceHolderView,
                                                          final Context mContext, final DailyRecipesFragment fragment) {
        Log.d(LOG_TAG, "Fetching date's recipes:  " + mDate.toString());

        final List<Recipe> recipes = new ArrayList<Recipe>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String parsedDate = dateFormat.format(mDate);
        Call<List<Recipe>> call = veRecipesService.listRecipesByDate(parsedDate);

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                recipes.addAll(response.body());

                Log.d(LOG_TAG, "Date's recipes loaded");

                for (Recipe recipe : recipes) {
                    Log.d("UI", "Adding recipe" + recipe + " card");
                    mSwipePlaceHolderView.addView(new RecipeCard(mContext, recipe, mSwipePlaceHolderView, fragment));
                }

            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.d(LOG_TAG, "Failed to get date's recipes");
                Log.d(LOG_TAG, t.toString());
            }
        });

    }

    public static AppUser loginAppUser(VeRecipesService veRecipesService, String username, String password) {
        Log.d(LOG_TAG, "Login user " + username);

        final AppUser user = new AppUser();

        Call<AppUser> call = veRecipesService.loginUser(username, password);
        call.enqueue(new Callback<AppUser>() {
            @Override
            public void onResponse(Call<AppUser> call, Response<AppUser> response) {
                AppUser fetchedUser = response.body();
                if (fetchedUser != null) {
                    user.setId(fetchedUser.getId());
                    user.setUsername(fetchedUser.getUsername());
                    user.setPassword(fetchedUser.getPassword());
                    user.setSignUpDate(fetchedUser.getSignUpDate());
                    Log.d(LOG_TAG, "Login successful");
                }


                Log.d(LOG_TAG, "Login failed: user not signed up");
            }

            @Override
            public void onFailure(Call<AppUser> call, Throwable t) {
                Log.d(LOG_TAG, "Failed to login user");
            }
        });

        return user;
    }
}
