package com.samoryka.verecipesclient.Views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ncapdevi.fragnav.FragNavController;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;
import com.samoryka.verecipesclient.Model.AppUser;
import com.samoryka.verecipesclient.Model.Recipe;
import com.samoryka.verecipesclient.R;
import com.samoryka.verecipesclient.Utilities.SharedPreferencesUtility;
import com.samoryka.verecipesclient.Views.DailyRecipes.DailyRecipesFragment;
import com.samoryka.verecipesclient.Views.SavedRecipes.RecipeListFragment;
import com.samoryka.verecipesclient.Web.RetrofitHelper;
import com.samoryka.verecipesclient.Web.VeRecipesService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements DailyRecipesFragment.OnFragmentInteractionListener, RecipeListFragment.OnListFragmentInteractionListener {

    //indices to fragments
    private final int TAB_FIRST = FragNavController.TAB1;
    private final int TAB_SECOND = FragNavController.TAB2;
    private final int TAB_THIRD = FragNavController.TAB3;

    private BottomBar mBottomBar;
    private FragNavController fragNavController;
    private VeRecipesService veRecipesService;
    private AppUser loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        veRecipesService = RetrofitHelper.initializeVeRecipesService();
        // If the user is already logged in, we redirect him to the main activity
        // Otherwise, we refresh his id and ake sure that his account is still available
        if (!SharedPreferencesUtility.checkUserLoggedIn(this)) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        } else {
            loggedInUser = SharedPreferencesUtility.getLoggedInUser(this);
            refreshLoggedInUser();
        }

        //FragNav
        //list of fragments
        List<Fragment> fragments = new ArrayList<>(3);
        fragments.add(new PlaceHolderFragment());
        fragments.add(DailyRecipesFragment.newInstance());
        fragments.add(RecipeListFragment.newInstance(1));
        //link fragments to container
        fragNavController = new FragNavController(getSupportFragmentManager(),R.id.container,fragments);

        //BottomBar menu
        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.setItems(R.menu.menu_bottombar);
        mBottomBar.setOnMenuTabClickListener(new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                //switch between tabs
                switch (menuItemId) {
                    case R.id.bottomBarItemOne:
                        fragNavController.switchTab(TAB_FIRST);
                        break;
                    case R.id.bottomBarItemSecond:
                        fragNavController.switchTab(TAB_SECOND);
                        break;
                    case R.id.bottomBarItemThird:
                        fragNavController.switchTab(TAB_THIRD);
                        break;
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bottomBarItemOne) {
                    fragNavController.clearStack();
                }
            }
        });

        mBottomBar.selectTabAtPosition(1,false);

    }
    @Override
    public void onBackPressed() {
        if (fragNavController.getCurrentStack().size() > 1) {
            fragNavController.pop();
        } else {
            super.onBackPressed();
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(Recipe recipe) {

    }

    private void refreshLoggedInUser() {
        Call<AppUser> call = veRecipesService.refreshLoggedInUser(loggedInUser.getUsername(), loggedInUser.getPassword());
        call.enqueue(new Callback<AppUser>() {
            @Override
            public void onResponse(Call<AppUser> call, Response<AppUser> response) {
                SharedPreferencesUtility.setLoggedInUser(getApplicationContext(), response.body());
            }

            @Override
            public void onFailure(Call<AppUser> call, Throwable t) {

            }
        });
    }
}
