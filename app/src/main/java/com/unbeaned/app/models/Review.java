package com.unbeaned.app.models;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.List;

@ParseClassName("Review")
public class Review extends ParseObject {
    public static List<Images> images;
    public static final String KEY_REVIEW_ID="objectId";
    public static final String KEY_PLACE_ID = "placeId";
    public static final String KEY_RATING = "rating";
    public static final String KEY_REVIEW= "review";
    public static final String KEY_USER= "user";
    public static final String KEY_CREATED= "createdAt";

    public String getReviewId(){
        return getString(KEY_REVIEW_ID);
    }

    public String getPlaceId(){
        return getString(KEY_PLACE_ID);
    }

    public void setPlaceId(String placeId){
        put(KEY_PLACE_ID, placeId);
    }

    public String getReview(){
        return getString(KEY_REVIEW);
    }

    public void setReview(String review){
        put(KEY_REVIEW, review);
    }

    public double getRating(){
        return getDouble(KEY_RATING);
    }

    public void setRating(Double rating){
        put(KEY_RATING, rating);
    }

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser parseUser){
        put(KEY_USER, parseUser);
    }


}
