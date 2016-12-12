package com.samoryka.verecipesclient.Views.SavedRecipes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.samoryka.verecipesclient.Model.Recipe;
import com.samoryka.verecipesclient.R;
import com.samoryka.verecipesclient.Views.SavedRecipes.RecipeListFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Recipe} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MyRecipeRecyclerViewAdapter extends RecyclerView.Adapter<MyRecipeRecyclerViewAdapter.ViewHolder> {

    private final List<Recipe> mRecipes;
    private final OnListFragmentInteractionListener mListener;

    public MyRecipeRecyclerViewAdapter(List<Recipe> recipes, OnListFragmentInteractionListener listener) {
        mRecipes = recipes;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_recipe_list_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mRecipe = mRecipes.get(position);

        Glide.with(holder.mImageRecipeView.getContext())
                .load(mRecipes.get(position).getImageURL())
                .placeholder(R.color.colorPrimaryLight)
                .into(holder.mImageRecipeView);

        holder.mNameView.setText(mRecipes.get(position).getName());
        holder.mPreparationTimeView.setText(mRecipes.get(position).getPreparationTime() + " min");

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mRecipe);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mPreparationTimeView;
        public final ImageView mImageRecipeView;
        public Recipe mRecipe;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.recipeName);
            mPreparationTimeView = (TextView) view.findViewById(R.id.recipePreparationTime);
            mImageRecipeView = (ImageView) view.findViewById(R.id.recipeImage);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mPreparationTimeView.getText() + "'";
        }
    }
}
