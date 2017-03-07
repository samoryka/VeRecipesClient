package com.samoryka.verecipesclient.Views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.samoryka.verecipesclient.Model.AppUser;
import com.samoryka.verecipesclient.R;
import com.samoryka.verecipesclient.Security.InputValidator;
import com.samoryka.verecipesclient.Utilities.SharedPreferencesUtility;
import com.samoryka.verecipesclient.Web.RetrofitHelper;
import com.samoryka.verecipesclient.Web.VeRecipesService;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    @BindView(R.id.input_username)
    EditText usernameText;
    @BindView(R.id.input_password)
    EditText passwordText;
    @BindView(R.id.btn_login)
    Button loginButton;
    @BindView(R.id.link_signup)
    TextView signupLink;
    private VeRecipesService veRecipesService = RetrofitHelper.initializeVeRecipesService();

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);


        // Progress dialog setup
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.login_loading));
        progressDialog.show();

        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();

        // HTTP Request synchronized with the UI thread
        Observable<AppUser> appUserObservable = veRecipesService.loginUser(username, password);
        appUserObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AppUser>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        onLoginFailed();
                    }

                    @Override
                    public void onNext(AppUser appUser) {
                        Log.d(TAG, "User id: " + appUser.getId() + ", " + appUser.getUsername());
                        progressDialog.dismiss();
                        onLoginSuccess(appUser);
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                usernameText.setText(data.getStringExtra("username"));
                passwordText.setText(data.getStringExtra("password"));
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess(AppUser loggedUser) {
        loginButton.setEnabled(true);
        SharedPreferencesUtility.setLoggedInUser(this, loggedUser);
        RetrofitHelper.refreshAuthenticationToken(loggedUser.getUsername(), loggedUser.getPassword());
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), getString(R.string.login_error), Toast.LENGTH_LONG).show();

        loginButton.setEnabled(true);
    }

    /**
     * Makes sure that all the input fields are valid
     *
     * @return
     */
    public boolean validate() {
        return InputValidator.validateUsername(usernameText, this)
                && InputValidator.validatePassword(passwordText, this);
    }
}
