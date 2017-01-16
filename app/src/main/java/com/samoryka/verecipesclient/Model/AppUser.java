package com.samoryka.verecipesclient.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by kasam on 16/01/2017.
 */

public class AppUser {

    @SerializedName("id")
    @Expose
    private long id;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("mail")
    @Expose
    private String mail;

    @SerializedName("signUpDate")
    @Expose
    private Date signUpDate;


    public AppUser(String username, String password, String mail, Date signupDate) {
        this.username = username;
        this.password = password;
        this.mail = mail;
        this.signUpDate = signupDate;
    }


    public AppUser(String username, String password, String mail) {
        this.username = username;
        this.password = password;
        this.mail = mail;
        this.signUpDate = Calendar.getInstance().getTime();
    }

    public AppUser() {
    }

    @Override
    public String toString() {
        return "User: " + this.username + ", " + this.mail + ", join on " + this.signUpDate;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getSignUpDate() {
        return signUpDate;
    }

    public void setSignUpDate(Date signUpDate) {
        this.signUpDate = signUpDate;
    }
}
