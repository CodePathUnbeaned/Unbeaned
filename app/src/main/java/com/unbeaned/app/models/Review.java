package com.unbeaned.app.models;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;


@ParseClassName("Review")
public class Review extends ParseObject {
    public List<Images> images= new ArrayList<>();
    public static final String KEY_TITLE = "title";
    public static final String KEY_PLACE_ID = "placeId";
    public static final String KEY_REVIEW_ID = "objectId";
    public static final String KEY_RATING = "rating";
    public static final String KEY_REVIEW= "review";
    public static final String KEY_USER= "user";
    public static final String KEY_CREATED= "createdAt";
    public static final String KEY_PLACE_NAME = "placeDisplayName";

    public String getTitle(){ return getString(KEY_TITLE);}

    public void setTitle(String title){ put(KEY_TITLE,title);}

    public String getPlaceName(){return getString(KEY_PLACE_NAME);}

    public void setPlaceName(String placeName){put(KEY_PLACE_NAME,placeName);}

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

    public void setImages(Review review){
        ParseQuery<Images> query = ParseQuery.getQuery(Images.class);


        query.whereEqualTo(Images.KEY_REVIEW_ID, review);
        try {
            images.clear();
            images.addAll(query.find());
            Log.i("Review", "Images: "+images);

        }catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
