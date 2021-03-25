package com.unbeaned.app.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Place {

    private String placeId;
    private String name;
    private double rating;
    private String address;
    private String price;
    private String phone;
    private String imageUrl;

    //Parceler library

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

    public static Place fromJson(JSONObject jsonObject) throws JSONException {
        Place place = new Place();
        place.placeId=jsonObject.getString("id");
        place.name=jsonObject.getString("name");
        place.imageUrl=jsonObject.getString("image_url");

        if (jsonObject.has("price")) {
            place.price = "$";
        }
        //place.price = jsonObject.getString("price");

        //update to rating from our data later
        place.rating= jsonObject.getDouble("rating");
        JSONObject locationJSON = jsonObject.getJSONObject("location");
        place.address=locationJSON.getString("display_address");
        //maybe change to display_phone
        place.phone= jsonObject.getString("phone");
        return place;
    }

    public static List<Place> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Place> places = new ArrayList<>();
        for(int i=0; i<jsonArray.length(); i++){
            places.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return places;
    }
}
