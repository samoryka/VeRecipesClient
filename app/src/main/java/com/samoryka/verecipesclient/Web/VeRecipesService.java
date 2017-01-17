package com.samoryka.verecipesclient.Web;

import com.samoryka.verecipesclient.Model.AppUser;
import com.samoryka.verecipesclient.Model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Retrofit service used to make HTTP requests on the VeRecipes server
 */

public interface VeRecipesService {

    //RECIPE
    @GET("recipes/date")
    Observable<List<Recipe>> listRecipesByDate(@Query("date") String date);

    @GET("recipes/user")
    Observable<List<Recipe>> listRecipesSavedByUser(@Query("userId") long userId);

    // USER
    @GET("user")
    Observable<AppUser> loginUser(@Query("username") String username, @Query("password") String password);

    @GET("user")
    Call<AppUser> refreshLoggedInUser(@Query("username") String username, @Query("password") String password);

    @PUT("user")
    Observable<Boolean> signupUser(@Query("username") String username, @Query("password") String password, @Query("mail") String mail);

    // USERRECIPE
    @PUT("userRecipe")
    Call<String> saveUserRecipe(@Query("userId") long userId, @Query("recipeId") long recipeId);

    @DELETE("userRecipe")
    Call<String> unSaveUserRecipe(@Query("userId") long userId, @Query("recipeId") long recipeId);

}
