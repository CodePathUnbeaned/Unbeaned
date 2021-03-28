package com.unbeaned.app.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("Images")
public class Images extends ParseObject {

    public static final String KEY_REVIEW_ID = "reviewId";
    public static final String KEY_IMAGE = "image";

    public String getReviewId(){
        return getString(KEY_REVIEW_ID);
    }

    public void setReviewId(String placeId){
        put(KEY_REVIEW_ID, placeId);
    }

    public ParseFile getImage(){ return getParseFile(KEY_IMAGE);}

    public void setImage(ParseFile parseFile){
        put(KEY_IMAGE, parseFile);
    }
}
