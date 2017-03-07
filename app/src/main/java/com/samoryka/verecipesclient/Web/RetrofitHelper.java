package com.samoryka.verecipesclient.Web;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.samoryka.verecipesclient.Security.AuthenticationTokenManager;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Methods used to instantiate and set up different Retrofit components
 */

public class RetrofitHelper {
    private static final String VERECIPES_URL = "http://verecipes.eu-central-1.elasticbeanstalk.com/";
    private static String storedUsername;
    private static String storedPassword;

    public static VeRecipesService initializeVeRecipesService() {

        GsonBuilder gsonBuilder = createGsonBuilder();

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(VERECIPES_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()));

        if (storedUsername != null && !storedUsername.isEmpty() && storedPassword != null && !storedPassword.isEmpty()) {
            retrofitBuilder.client(createOkHttpClient(storedUsername, storedPassword));
        }

        Retrofit retrofit = retrofitBuilder.build();
        return retrofit.create(VeRecipesService.class);
    }

    private static GsonBuilder createGsonBuilder() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });

        return gsonBuilder;
    }

    private static OkHttpClient createOkHttpClient(final String username, final String password) {

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        /* DEBUG : requests logs
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        clientBuilder.addInterceptor(logging);
        */

        clientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("Authorization", AuthenticationTokenManager.generateBasicAuthenticationToken(username, password))
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        return clientBuilder.build();
    }

    public static VeRecipesService refreshAuthenticationToken(final String username, final String password) {
        storedUsername = username;
        storedPassword = password;

        return initializeVeRecipesService();
    }
}
