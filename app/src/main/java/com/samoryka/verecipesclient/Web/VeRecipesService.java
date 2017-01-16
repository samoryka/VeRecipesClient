package com.samoryka.verecipesclient.Web;

import com.samoryka.verecipesclient.Model.AppUser;
import com.samoryka.verecipesclient.Model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Retrofit service used to make HTTP requests on the VeRecipes server
 */

public interface VeRecipesService {

    @GET("recipes/date")
    Call<List<Recipe>> listRecipesByDate(@Query("date") String date);

    @GET("user")
    Call<AppUser> loginUser();
}
