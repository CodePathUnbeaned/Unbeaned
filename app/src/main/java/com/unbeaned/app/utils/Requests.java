package com.unbeaned.app.utils;

import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.parse.ParseQuery;
import com.unbeaned.app.adapters.ReviewFeedAdapter;
import com.unbeaned.app.models.Review;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class Requests {

    private static final String baseURL = "unbeand.herokuapp.com";

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
                .host(baseURL)
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

    public static void getAllReviews(List<Review> allReviews, String placeId, RecyclerView.Adapter<?> adapter, String TAG) {
        ParseQuery<Review> query = ParseQuery.getQuery(Review.class);

        query.include(Review.KEY_USER);
        query.whereEqualTo(Review.KEY_PLACE_ID, placeId);
        query.addDescendingOrder(Review.KEY_CREATED);
        query.findInBackground((reviews, e) -> {
            if (e != null) {
                Log.e(TAG, "Issue with getting posts", e);
                return;
            }
            Log.i(TAG, "Querying Reviews: " + reviews);
            for (Review review : reviews) {
                Log.i("Requests", "Review Set Image: " + review.getPlaceName());
                review.setImages();
            }

            if (adapter.getClass() == ReviewFeedAdapter.class) {
                ((ReviewFeedAdapter) adapter).clear();
                ((ReviewFeedAdapter) adapter).addAll(reviews);
            }
        });

    }


}
