package com.samoryka.verecipesclient.Views.DailyRecipes;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;
import com.samoryka.verecipesclient.Model.Recipe;
import com.samoryka.verecipesclient.R;
import com.samoryka.verecipesclient.Utilities.RecipeChromeCustomTab;
import com.samoryka.verecipesclient.Utilities.SharedPreferencesUtility;
import com.samoryka.verecipesclient.Views.HomeActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Layout(R.layout.recipe_daily_card_view)
public class RecipeCard {

    @View(R.id.recipeImage)
    private ImageView recipeImageView;

    @View(R.id.recipeName)
    private TextView recipeName;

    @View(R.id.recipePreparationTime)
    private TextView preparationTime;

    private Recipe mRecipe;
    private Context mContext;
    private SwipePlaceHolderView mSwipeView;
    private DailyRecipesFragment mFragment;

    public RecipeCard(Context context, Recipe recipe, SwipePlaceHolderView swipeView, DailyRecipesFragment fragment) {
        mContext = context;
        mRecipe = recipe;
        mSwipeView = swipeView;
        mFragment = fragment;
    }

    @Resolve
    private void onResolved() {
        Glide.with(mContext)
                .load(mRecipe.getImageURL())
                .placeholder(R.color.colorPrimaryLight)
                .into(recipeImageView);
        recipeName.setText(mRecipe.getName());
        preparationTime.setText(mRecipe.getPreparationTime() + " min");
    }

    @Click(R.id.recipeImage)
    private void onImageClick() {
        cardClicked();
    }

    @Click(R.id.recipeName)
    private void onNameClick() {
        cardClicked();
    }

    @Click(R.id.recipePreparationTime)
    private void onPreparationTimeClick() {
        cardClicked();
    }

    @SwipeOut
    private void onSwipedOut() {
        mFragment.checkCardsLeft();
        SharedPreferencesUtility.setLastSwipedRecipe(mContext, mRecipe.getId());
    }

    @SwipeCancelState
    private void onSwipeCancelState() {
    }

    @SwipeIn
    private void onSwipeIn() {
        mFragment.checkCardsLeft();
        saveRecipe();
        SharedPreferencesUtility.setLastSwipedRecipe(mContext, mRecipe.getId());
    }

    @SwipeInState
    private void onSwipeInState() {
    }

    @SwipeOutState
    private void onSwipeOutState() {
    }

    private void cardClicked() {
        RecipeChromeCustomTab.openRecipeTab(mContext, mRecipe);
    }

    private void saveRecipe() {
        long userId = SharedPreferencesUtility.getLoggedInUser(mContext).getId();

        Call<String> call = HomeActivity.veRecipesService.saveUserRecipe(userId, mRecipe.getId());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

}