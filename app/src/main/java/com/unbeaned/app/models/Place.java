package com.unbeaned.app.models;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Place {

    String placeId;
    String name;
    double rating;
    String address;
    String price;
    String displayPhone;
    String phone;
    String imageUrl;
    String longitude;
    String latitude;
    String address1;

    //need by Parceler library
    public Place(){

    }

    public String getPlaceId(){ return placeId;}

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public double getRating() {
        return rating;
    }

    public String getAddress() {
        return address;
    }

    public String getPrice() {
        return price;
    }

    public String getDisplayPhone(){return displayPhone;}

    public String getPhone() {return phone; }

    public String getLongitude() {return longitude;}

    public String getLatitude() {return latitude;}

    public String getAddress1(){return address1;}


    public Place(JSONObject jsonObject) throws JSONException {
       placeId=jsonObject.getString("id");
       name=jsonObject.getString("name");
       imageUrl=jsonObject.getString("image_url");
        if (jsonObject.has("price")) {
            price =jsonObject.getString("price");
        }
        else{
            price=" ";
        }
        calculateRating(placeId);
        JSONObject locationJSON = jsonObject.getJSONObject("location");
        address1=locationJSON.getString("address1");
        address = formatDisplayAddress(locationJSON);
        JSONObject coordinates = jsonObject.getJSONObject("coordinates");
        longitude = coordinates.getString("longitude");
        latitude = coordinates.getString("latitude");
        phone =jsonObject.getString("phone");
        displayPhone= jsonObject.getString("display_phone");
    }

    public static List<Place> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Place> places = new ArrayList<>();
        for(int i=0; i<jsonArray.length(); i++){
            places.add(new Place(jsonArray.getJSONObject(i)));
        }
        return places;
    }

    public static String formatDisplayAddress(JSONObject location) throws JSONException {
        String address1 = location.getString("address1");
        String address2 = location.getString("address2");
        String city = location.getString("city");
        String state = location.getString("state");
        String zipCode = location.getString("zip_code");
        String address;

        if (address2!="null") {
            address = String.format("%s %s %s, %s %s", address1, address2, city, state, zipCode);
        }
        else{
            address = String.format("%s %s, %s %s", address1, city, state, zipCode);
        }
        return address;
    }

    public void calculateRating(String placeId) {
        //double rating=0.0;
        ParseQuery<Review> query = ParseQuery.getQuery(Review.class);
        query.include(Review.KEY_RATING);
        query.whereEqualTo(Review.KEY_PLACE_ID, placeId);
        try {
            List<Review> queryList = query.find();
            rating=getAverage(queryList);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static double getAverage(List<Review> reviews) {
        double total=0;
        if (reviews.size()!=0){
            for (Review review:reviews){
                total+=review.getRating();
            }
            return total/ reviews.size();
        }
        return 0.0;
    }

}
