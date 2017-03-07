package com.samoryka.verecipesclient.Views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.ncapdevi.fragnav.FragNavController;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;
import com.samoryka.verecipesclient.Model.AppUser;
import com.samoryka.verecipesclient.Model.Recipe;
import com.samoryka.verecipesclient.R;
import com.samoryka.verecipesclient.Utilities.SharedPreferencesUtility;
import com.samoryka.verecipesclient.Views.DailyRecipes.DailyRecipesFragment;
import com.samoryka.verecipesclient.Views.ProfilePage.ProfilePageFragment;
import com.samoryka.verecipesclient.Views.SavedRecipes.RecipeListFragment;
import com.samoryka.verecipesclient.Web.RetrofitHelper;
import com.samoryka.verecipesclient.Web.VeRecipesService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeActivity extends AppCompatActivity implements DailyRecipesFragment.OnFragmentInteractionListener,
        RecipeListFragment.OnListFragmentInteractionListener, ProfilePageFragment.OnFragmentInteractionListener {

    public static VeRecipesService veRecipesService;
    //indices to fragments
    private final int TAB_FIRST = FragNavController.TAB1;
    private final int TAB_SECOND = FragNavController.TAB2;
    private final int TAB_THIRD = FragNavController.TAB3;
    private BottomBar mBottomBar;
    private FragNavController fragNavController;
    private AppUser loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RetrofitHelper.context = this;
        veRecipesService = RetrofitHelper.initializeVeRecipesService();

        // If the user is already logged in, we redirect him to the main activity
        // Otherwise, we refresh his id and make sure that his account is still available
        if (!SharedPreferencesUtility.checkUserLoggedIn(this)) {
            startLoginActivity();
        } else {
            refreshLoggedInUser(this, savedInstanceState);

        }




    }
    @Override
    public void onBackPressed() {
        if (fragNavController != null && fragNavController.getCurrentStack().size() > 1) {
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
        if (mBottomBar != null)
            mBottomBar.onSaveInstanceState(outState);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(Recipe recipe) {

    }

    private void refreshLoggedInUser(final AppCompatActivity activity, final Bundle savedInstanceState) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.home_loading));
        progressDialog.show();

        loggedInUser = SharedPreferencesUtility.getLoggedInUser(this);
        String username = loggedInUser.getUsername();
        String password = loggedInUser.getPassword();

        // HTTP Request synchronized with the UI thread
        Observable<AppUser> appUserObservable = HomeActivity.veRecipesService.loginUser(username, password);
        appUserObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AppUser>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getBaseContext(), getString(R.string.home_login_error), Toast.LENGTH_LONG).show();

                        progressDialog.dismiss();

                        startLoginActivity();
                    }

                    @Override
                    public void onNext(AppUser appUser) {
                        SharedPreferencesUtility.setLoggedInUser(getApplicationContext(), appUser);
                        SharedPreferencesUtility.setLastVisit(getApplicationContext(), Calendar.getInstance().getTime());

                        veRecipesService = RetrofitHelper.refreshAuthenticationToken(appUser.getUsername(), appUser.getPassword());

                        // We currently have to do the UI Setup in the callback of this method because we have to make sure that the
                        // VeRecipesService in the fragment is up to date (includes the authentication token)
                        // TODO: Do this more cleanly by using a dependency injector like Dagger
                        setUpFragNav();
                        setupBottomBar(activity, savedInstanceState);


                        progressDialog.dismiss();
                    }
                });

        /*
        Call<AppUser> call = veRecipesService.refreshLoggedInUser(loggedInUser.getUsername(), loggedInUser.getPassword());
        call.enqueue(new Callback<AppUser>() {
            @Override
            public void onResponse(Call<AppUser> call, Response<AppUser> response) {
                AppUser refreshedUser = response.body();
                SharedPreferencesUtility.setLoggedInUser(getApplicationContext(), refreshedUser);
                RetrofitHelper.refreshAuthenticationToken(refreshedUser.getUsername(), refreshedUser.getPassword());
                veRecipesService = RetrofitHelper.initializeVeRecipesService();
            }

            @Override
            public void onFailure(Call<AppUser> call, Throwable t) {

            }
        });*/

    }

    private void startLoginActivity() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    private void setUpFragNav() {

        List<Fragment> fragments = new ArrayList<>(3);
        fragments.add(ProfilePageFragment.newInstance());
        fragments.add(DailyRecipesFragment.newInstance());
        fragments.add(RecipeListFragment.newInstance(1));

        //link fragments to container
        fragNavController = new FragNavController(getSupportFragmentManager(), R.id.container, fragments);
    }


    private void setupBottomBar(AppCompatActivity activity, Bundle savedInstanceState) {

        mBottomBar = BottomBar.attach(activity, savedInstanceState);
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

        mBottomBar.selectTabAtPosition(1, false);
    }
}
