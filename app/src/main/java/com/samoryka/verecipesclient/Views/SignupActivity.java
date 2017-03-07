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

import com.samoryka.verecipesclient.R;
import com.samoryka.verecipesclient.Security.InputValidator;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    @BindView(R.id.signup_username)
    EditText usernameText;
    @BindView(R.id.signup_email)
    EditText emailText;
    @BindView(R.id.signup_password)
    EditText passwordText;
    @BindView(R.id.signup_confirm_password)
    EditText confirmPasswordText;
    @BindView(R.id.btn_signup)
    Button signupButton;
    @BindView(R.id.link_login)
    TextView loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        signupButton.setEnabled(false);

        // Progress dialog setup
        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.signup_loading));
        progressDialog.show();

        String username = usernameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        // HTTP Request synchronized with the UI thread
        Observable<Boolean> booleanObservable = HomeActivity.veRecipesService.signupUser(username, password, email);
        booleanObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        onSignupFailed();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        progressDialog.dismiss();
                        onSignupSuccess();
                    }
                });
    }


    public void onSignupSuccess() {
        signupButton.setEnabled(true);

        //We put the given credentials in the intent to auto-fill the login fields
        Intent resultIntent = new Intent();
        resultIntent.putExtra("username", usernameText.getText().toString());
        resultIntent.putExtra("password", passwordText.getText().toString());

        setResult(RESULT_OK, resultIntent);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), getString(R.string.signup_error), Toast.LENGTH_LONG).show();
        signupButton.setEnabled(true);
    }

    /**
     * Makes sure that all the input fields are valid
     *
     * @return
     */
    public boolean validate() {

        return InputValidator.validateUsername(usernameText, this)
                && InputValidator.validateEmail(emailText, this)
                && InputValidator.validatePassword(passwordText, this)
                && InputValidator.validateConfirmPassword(passwordText, confirmPasswordText, this);

    }
}
