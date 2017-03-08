package com.samoryka.verecipesclient.Views.SavedRecipes;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.samoryka.verecipesclient.Model.Recipe;
import com.samoryka.verecipesclient.R;
import com.samoryka.verecipesclient.Utilities.SharedPreferencesUtility;
import com.samoryka.verecipesclient.Views.HomeActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class RecipeListFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    @BindView(R.id.emptyListPlaceHolder)
    View mEmptyListPlaceHolderView;
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private MyRecipeRecyclerViewAdapter mAdapter;
    private List<Recipe> mRecipes;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeListFragment() {
    }

    public static RecipeListFragment newInstance(int columnCount) {
        RecipeListFragment fragment = new RecipeListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        Context context = view.getContext();

        ButterKnife.bind(this, view);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }


        loadRecipes(context, recyclerView);


        createTouchHelper().attachToRecyclerView(recyclerView);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Implementation of swipe to delete
     *
     * @return
     */
    private ItemTouchHelper createTouchHelper() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                int position = viewHolder.getAdapterPosition();
                Recipe recipeToDelete = mRecipes.get(position);
                mRecipes.remove(position);
                mAdapter.notifyItemRemoved(position);
                checkAdapterEmpty();

                confirmUnsaveRecipe(position, recipeToDelete);
            }
        };

        return new ItemTouchHelper(simpleItemTouchCallback);
    }

    /**
     * Checks if there are any recipe left to display. if that's the case, we'll display a placeholder
     */
    private void checkAdapterEmpty() {
        if (mAdapter.getItemCount() == 0)
            mEmptyListPlaceHolderView.setVisibility(View.VISIBLE);
        else
            mEmptyListPlaceHolderView.setVisibility(View.GONE);
    }

    /**
     * Loads user's saved recipes from the VeRecipes server
     *
     * @param context
     * @param recyclerView the RecyclerView in which we want to load the recipes
     */
    private void loadRecipes(Context context, final RecyclerView recyclerView) {
        long userId = SharedPreferencesUtility.getLoggedInUser(context).getId();

        Observable<List<Recipe>> recipeListObservable = HomeActivity.veRecipesService.listRecipesSavedByUser(userId);
        recipeListObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Recipe>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Recipe> recipes) {
                        mAdapter = new MyRecipeRecyclerViewAdapter(recipes, mListener);
                        mRecipes = recipes;
                        recyclerView.setAdapter(mAdapter);
                        checkAdapterEmpty();
                    }
                });


    }

    /**
     * Sends the request to unsave the recipe to the server
     *
     * @param recipeToDelete
     */
    private void unsaveRecipe(Recipe recipeToDelete) {
        long userId = SharedPreferencesUtility.getLoggedInUser(getContext()).getId();

        Call<String> call = HomeActivity.veRecipesService.unSaveUserRecipe(userId, recipeToDelete.getId());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    /**
     * Creates a snackbar enabling the user to decide whether or not we should unsave a recipe
     *
     * @param position position of the recipe in the recipe list
     */
    private void confirmUnsaveRecipe(final int position, final Recipe recipe) {
        Snackbar snackBar = Snackbar.make(getView(), getString(R.string.saved_recipes_deleted_recipe), Snackbar.LENGTH_LONG);

        // Undo (re-insert the recipe in the list)
        snackBar.setAction(getString(R.string.saved_recipes_deleted_recipe_undo), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecipes.add(position, recipe);
                mAdapter.notifyItemInserted(position);
                Snackbar.make(getView(), getString(R.string.saved_recipes_deleted_recipe_cancel), Snackbar.LENGTH_SHORT).show();
            }
        });


        // Delete the recipe in the database once the snack bar is gone
        snackBar.setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                super.onDismissed(snackbar, event);
                if (event == Snackbar.Callback.DISMISS_EVENT_SWIPE
                        || event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT)
                    unsaveRecipe(recipe);
            }
        });

        snackBar.show();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Recipe recipe);
    }
}
