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
    private void onResolved(){
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
    private void onSwipedOut(){
        mFragment.checkCardsLeft();
    }

    @SwipeCancelState
    private void onSwipeCancelState(){
    }

    @SwipeIn
    private void onSwipeIn(){
        mFragment.checkCardsLeft();
    }

    @SwipeInState
    private void onSwipeInState(){
    }

    @SwipeOutState
    private void onSwipeOutState(){
    }

    private void cardClicked() {
        RecipeChromeCustomTab.openRecipeTab(mContext, mRecipe);
    }

}