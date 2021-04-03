package com.unbeaned.app.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("User")
public class User extends ParseUser {

    public static final String KEY_ID= "objectId";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHOTO = "picture";
    public static final String KEY_NAME = "name";
    public static final String KEY_BIO = "bio";
    public static final String KEY_REVIEW_COUNT = "reviewCount";

    public String getId (){return getString(KEY_ID);}

    public String getUsername(){
        return getString(KEY_USERNAME);
    }

    public void setUsername(String username){
        put(KEY_USERNAME, username);
    }

    public String getEmail(){
        return getString(KEY_EMAIL);
    }

    public void setEmail(String email){
        put(KEY_EMAIL, email);
    }

    public String getName(){
        return getString(KEY_NAME);
    }

    public void setName(String name){
        put(KEY_NAME, name);
    }

    public String getBio(){
        return getString(KEY_BIO);
    }

    public void setBio(String bio){
        put(KEY_BIO, bio);
    }

    public String getPassword(){
        return getString(KEY_PASSWORD);
    }

    public void setPassword(String password){ put(KEY_PASSWORD, password); }

    public int getReviewCount(){
        return getInt(KEY_REVIEW_COUNT);
    }

    public void setKeyReviewCount(int count){
        put(KEY_REVIEW_COUNT, count);
    }

    public ParseFile getPicture(){
        return getParseFile(KEY_PHOTO);
    }

    public void setPicture(ParseFile parseFile){
        put(KEY_PHOTO, parseFile);
    }

    public ParseUser getUser() { return ParseUser.getCurrentUser(); }

}
