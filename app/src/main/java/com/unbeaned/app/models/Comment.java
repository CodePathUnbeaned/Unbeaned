package com.unbeaned.app.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Comments")
public class Comment extends ParseObject {
    public static final String KEY_REVIEW = "review";
    public static final String KEY_USER = "user";
    public static final String KEY_COMMENT = "comment";
    public static final String KEY_CREATED_AT = "createdAt";

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

    public ParseObject getReview(){
        return getParseObject(KEY_REVIEW);
    }

    public void setReview(ParseObject review){
        put(KEY_REVIEW, review);
    }

    public String getComment(){ return getString(KEY_COMMENT);}

    public void setComment(String comment){put(KEY_COMMENT, comment);}
}
