package com.samoryka.verecipesclient.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by kasam on 08/12/2016.
 */
public class Recipe {

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("sourceId")
    @Expose
    private Long sourceId;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("imageURL")
    @Expose
    private String imageURL;

    @SerializedName("recipeURL")
    @Expose
    private String recipeURL;

    @SerializedName("preparationTime")
    @Expose
    private int preparationTime;

    @SerializedName("publicationDate")
    @Expose
    private Date publicationDate;

    public Recipe(){};

    public Recipe(Long id, Long sourceId, String name, String imageURL, String recipeURL, int preparationTime, Date publicationTime) {
        this.id = id;
        this.sourceId = sourceId;
        this.name = name;
        this.imageURL = imageURL;
        this.recipeURL = recipeURL;
        this.preparationTime = preparationTime;
        this.publicationDate = publicationTime;
    }


    // Corrects the characters that we had to modify for the JSON parsing
    // ex: "@@and" actually is "&"
    public void correctJSONTextFormatting() {

        this.setName( this.getName().replaceAll("@@and","&"));
        this.setName( this.getName().replaceAll("@@hashtag","#"));
        this.setName( this.getName().replaceAll("@@openingCurvedBracket","{"));
        this.setName( this.getName().replaceAll("@@closingCurvedBracket","}"));
        this.setName( this.getName().replaceAll("@@openingSquareBracket","["));
        this.setName( this.getName().replaceAll("@@closingSquareBracket","]"));

        this.setImageURL( this.getImageURL().replaceAll("@@and","&"));
        this.setImageURL( this.getImageURL().replaceAll("@@hashtag","#"));
        this.setImageURL( this.getImageURL().replaceAll("@@openingCurvedBracket","{"));
        this.setImageURL( this.getImageURL().replaceAll("@@closingCurvedBracket","}"));
        this.setImageURL( this.getImageURL().replaceAll("@@openingSquareBracket","["));
        this.setImageURL( this.getImageURL().replaceAll("@@closingSquareBracket","]"));

        this.setRecipeURL( this.getRecipeURL().replaceAll("@@and","&"));
        this.setRecipeURL( this.getRecipeURL().replaceAll("@@hashtag","#"));
        this.setRecipeURL( this.getRecipeURL().replaceAll("@@openingCurvedBracket","{"));
        this.setRecipeURL( this.getRecipeURL().replaceAll("@@closingCurvedBracket","}"));
        this.setRecipeURL( this.getRecipeURL().replaceAll("@@openingSquareBracket","["));
        this.setRecipeURL( this.getRecipeURL().replaceAll("@@closingSquareBracket","]"));
    }
    
    @Override
    public String toString() {
        return "Recipe " + id + ": " + name;
    }

    public Long getId() {
        return id;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public String getName() {
        return name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getRecipeURL() {
        return recipeURL;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setRecipeURL(String recipeURL) {
        this.recipeURL = recipeURL;
    }

    public void setPreparationTime(int preparationTime) {
        this.preparationTime = preparationTime;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }
}
