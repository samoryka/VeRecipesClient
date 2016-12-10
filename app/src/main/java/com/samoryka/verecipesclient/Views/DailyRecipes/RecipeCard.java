package com.samoryka.verecipesclient.Views.DailyRecipes;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mindorks.placeholderview.SwipePlaceHolderView;
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

@Layout(R.layout.recipe_card_view)
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

    public RecipeCard(Context context, Recipe recipe, SwipePlaceHolderView swipeView) {
        mContext = context;
        mRecipe = recipe;
        mSwipeView = swipeView;
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

    @SwipeOut
    private void onSwipedOut(){
        //Log.d("EVENT", "onSwipedOut");
    }

    @SwipeCancelState
    private void onSwipeCancelState(){
        //Log.d("EVENT", "onSwipeCancelState");
    }

    @SwipeIn
    private void onSwipeIn(){
        //Log.d("EVENT", "onSwipedIn");
    }

    @SwipeInState
    private void onSwipeInState(){
        //Log.d("EVENT", "onSwipeInState");
    }

    @SwipeOutState
    private void onSwipeOutState(){
        //Log.d("EVENT", "onSwipeOutState");
    }
}