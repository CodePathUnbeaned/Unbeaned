package com.unbeaned.app.utils;

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Requests {

    private static final String reviewsURL = "unbeand.herokuapp.com";

    public static Call getAverageReviewRating(String placeId) {
        JSONObject matchObject = new JSONObject(),
                averageObject = new JSONObject(),
                groupObject = new JSONObject();

        try {
            matchObject.put("placeId", placeId);
            averageObject.put("$avg", "$rating");
            groupObject.put("objectId", "null").put("average", averageObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpUrl url = new HttpUrl.Builder().scheme("https")
                .host(reviewsURL)
                .addPathSegment("parse")
                .addPathSegment("aggregate")
                .addPathSegment("Reviews")
                .addEncodedQueryParameter("match", matchObject.toString())
                .addEncodedQueryParameter("group", groupObject.toString())
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .addHeader("X-Parse-Application-Id", "unbeand")
                .addHeader("X-Parse-Master-Key", "unbeandMasterKey")
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .build();

        return new OkHttpClient().newCall(request);

    }


}
