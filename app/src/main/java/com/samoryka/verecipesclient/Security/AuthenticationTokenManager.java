package com.samoryka.verecipesclient.Security;

import okhttp3.Credentials;

/**
 * Manages the token used for the authentication
 */

public class AuthenticationTokenManager {
    public static String generateBasicAuthenticationToken(String username, String password) {

        if (!username.isEmpty() && !password.isEmpty())
            return Credentials.basic(username, password);

        else throw new IllegalArgumentException("Username or password empty");

    }
}
