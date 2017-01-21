package com.samoryka.verecipesclient.Security;

import android.content.Context;
import android.widget.EditText;

import com.samoryka.verecipesclient.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Used to validate the different inputs done by the user
 */

public class InputValidator {

    public static boolean validateUsername(EditText usernameText, Context context) {
        String username = usernameText.getText().toString();

        // Regex for the username
        Pattern userNamePattern = Pattern.compile("^[a-zA-Z0-9]*$");
        Matcher userNameMatcher = userNamePattern.matcher(username);

        if (username.isEmpty() || username.length() < 3) {
            usernameText.setError(context.getString(R.string.input_error_username_length));
            return false;
        } else if (!userNameMatcher.matches()) {
            usernameText.setError(context.getString(R.string.input_error_username_characters));
            return false;
        } else {
            usernameText.setError(null);
            return true;
        }

    }

    public static boolean validateEmail(EditText emailText, Context context) {
        boolean valid = true;

        String email = emailText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError(context.getString(R.string.input_error_email));
            valid = false;
        } else {
            emailText.setError(null);
        }

        return valid;
    }

    public static boolean validatePassword(EditText passwordText, Context context) {
        String password = passwordText.getText().toString();

        // Regex for the password
        Pattern passwordNamePattern = Pattern.compile("^[^ ]+$");
        Matcher passwordNameMatcher = passwordNamePattern.matcher(password);

        if (password.isEmpty() || password.length() < 8) {
            passwordText.setError(context.getString(R.string.input_error_password_length));
            return false;
        } else if (!passwordNameMatcher.matches()) {
            passwordText.setError(context.getString(R.string.input_error_password_characters));
            return false;
        } else {
            passwordText.setError(null);
            return true;
        }

    }

    public static boolean validateConfirmPassword(EditText passwordText, EditText confirmPasswordText, Context context) {
        String password = passwordText.getText().toString();
        String confirmPassword = confirmPasswordText.getText().toString();

        if (passwordText.getError() == null && (confirmPassword.isEmpty() || !confirmPassword.equals(password))) {
            confirmPasswordText.setError(context.getString(R.string.input_error_confirm_password));
            return false;
        } else {
            confirmPasswordText.setError(null);
            return true;
        }
    }


}
