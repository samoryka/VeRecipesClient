package com.samoryka.verecipesclient.Utilities;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.samoryka.verecipesclient.Model.Recipe;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by kasam on 08/12/2016.
 */
public class JSONConversion {


    private static final String TAG = "JSONConverter";

    public static List<Recipe> LoadRecipes(Context context){
        try{
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            });
            Gson gson = builder.create();
            JSONArray array = new JSONArray(LoadJSONFromAsset(context, "dummyRecipes.json"));
            List<Recipe> recipeList = new ArrayList<>();
            for(int i=0;i<array.length();i++){
                Recipe recipe = gson.fromJson(array.getString(i), Recipe.class);
                recipe.correctJSONTextFormatting();
                recipeList.add(recipe);
            }
            return recipeList;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private static String LoadJSONFromAsset(Context context, String jsonFileName) {
        String json = null;
        InputStream is=null;
        try {
            AssetManager manager = context.getAssets();
            Log.d(TAG,"path "+jsonFileName);
            is = manager.open(jsonFileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
