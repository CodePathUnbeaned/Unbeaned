package com.unbeaned.app.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.unbeaned.app.utils.Requests;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

//@Parcel
public class Place implements Parcelable {

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
    String website;

    //need by Parceler library
    public Place(){

    }

    protected Place(Parcel in) {
        placeId = in.readString();
        name = in.readString();
        rating = in.readDouble();
        address = in.readString();
        price = in.readString();
        displayPhone = in.readString();
        phone = in.readString();
        imageUrl = in.readString();
        longitude = in.readString();
        latitude = in.readString();
        address1 = in.readString();
        website = in.readString();
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

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

    public String getWebsite() {return website;}


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
        calculateRating(placeId, jsonObject.getDouble("rating"));
        JSONObject locationJSON = jsonObject.getJSONObject("location");
        address1=locationJSON.getString("address1");
        address = formatDisplayAddress(locationJSON);
        JSONObject coordinates = jsonObject.getJSONObject("coordinates");
        longitude = coordinates.getString("longitude");
        latitude = coordinates.getString("latitude");
        phone =jsonObject.getString("phone");
        displayPhone= jsonObject.getString("display_phone");
        website = jsonObject.getString("url");
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

        if (!address2.equals("null")) {
            address = String.format("%s %s %s, %s %s", address1, address2, city, state, zipCode);
        }
        else{
            address = String.format("%s %s, %s %s", address1, city, state, zipCode);
        }
        return address;
    }

    public double calculateAggregateRating(String placeId, double yelpRating) {
        final double[] average = {0};

        Requests.getAverageReviewRating(placeId).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("PlaceModel", "Error on request", e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Error on response: " + response);
                String responseString = response.body().string();
                Log.i("PlaceModel", "Rating Request: " + responseString);
                try {
                    JSONArray results = new JSONObject(responseString).getJSONArray("results");
                    if (results.length() > 0) {
                        rating = results.getJSONObject(0).getDouble("average");
                        Log.i("PlaceModel", "Rating of Place: " + placeId + " " + average[0]);
                    }
                    else {
                        rating = yelpRating;
                        Log.i("PlaceModel", "Rating of Place Else: " + rating);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return average[0];
    }

    public void calculateRating(String placeId) {
        //double rating=0.0;
        ParseQuery<Review> query = ParseQuery.getQuery(Review.class);
        query.include(Review.KEY_RATING);
        query.whereEqualTo(Review.KEY_PLACE_ID, placeId);
        try {
            List<Review> queryList = query.find();
            rating = getAverage(queryList);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void calculateRating(String placeId, double yelpRating) {
        //double rating=0.0;
        ParseQuery<Review> query = ParseQuery.getQuery(Review.class);
        query.include(Review.KEY_RATING);
        query.whereEqualTo(Review.KEY_PLACE_ID, placeId);
        try {
            List<Review> queryList = query.find();
            if (!queryList.isEmpty()) {
                rating = getAverage(queryList);
            }
            else {
                rating = yelpRating;
            }
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
            return total / reviews.size();
        }
        return 0.0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(placeId);
        dest.writeString(name);
        dest.writeDouble(rating);
        dest.writeString(address);
        dest.writeString(price);
        dest.writeString(displayPhone);
        dest.writeString(phone);
        dest.writeString(imageUrl);
        dest.writeString(longitude);
        dest.writeString(latitude);
        dest.writeString(address1);
        dest.writeString(website);
    }
}
