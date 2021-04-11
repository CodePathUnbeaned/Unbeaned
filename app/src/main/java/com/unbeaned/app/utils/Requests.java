package com.unbeaned.app.utils;

import android.util.Log;
import android.util.Pair;

import androidx.recyclerview.widget.RecyclerView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.unbeaned.app.adapters.CommentFeedAdapter;
import com.unbeaned.app.adapters.ReviewFeedAdapter;
import com.unbeaned.app.models.Comment;
import com.unbeaned.app.models.Review;
import com.unbeaned.app.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class Requests {

    private static final String baseURL = "unbeand.herokuapp.com";
    private static final HashMap<String, Integer> countMap = new HashMap<>();
    private static final int limit = 4;

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
        query.setLimit(3);
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

    public static void getAllComments(List<Comment> allComments, CommentFeedAdapter adapter, String TAG, Review currentReview) {
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);

        query.include(Comment.KEY_USER);
        query.whereEqualTo(Comment.KEY_REVIEW, currentReview);
        query.addDescendingOrder(Comment.KEY_CREATED_AT);
        query.setLimit(6);
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> comments, ParseException e) {
                if (e!=null){
                    Log.e(TAG, "Issue with getting comments", e);
                    return;
                }
                adapter.clear();
                adapter.addAll(comments);
            }
        });

    }

    public static void getNextPageOfComments(List<Comment> allComments, CommentFeedAdapter adapter, String TAG, Review currentReview, int page) {
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);

        query.include(Comment.KEY_USER);
        query.whereEqualTo(Comment.KEY_REVIEW, currentReview);
        query.addDescendingOrder(Comment.KEY_CREATED_AT);
        query.setLimit(6);
        query.setSkip(page*6);
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> comments, ParseException e) {
                if (e!=null){
                    Log.e(TAG, "Issue with getting comments", e);
                    return;
                }

                adapter.clear();
                adapter.addAll(comments);
            }

        });

    }

    public static void refreshCount(String target, ArrayList<?> filter) {
        ParseQuery<?> query;

        switch (target) {
            case "Reviews":
                query = ParseQuery.getQuery(Review.class);
                if (filter != null) {
                    query.whereEqualTo(Review.KEY_PLACE_ID, filter.get(0));
                }
                break;
            case "Comments":
                query = ParseQuery.getQuery(Comment.class);
                if (filter != null) {
                    query.whereEqualTo(Comment.KEY_REVIEW, filter.get(0));
                }
                break;
            default:
                query = ParseQuery.getQuery(User.class);
                break;
        }

        try {
            countMap.put(target.toUpperCase(), query.count());
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public static void getNextPageOfReviews(List<Review> allReviews, String placeId, RecyclerView.Adapter<?> adapter, String TAG, int page, boolean refresh) {
        ParseQuery<Review> query = ParseQuery.getQuery(Review.class);

//        if (refresh) {
//            refreshCount("Reviews", new ArrayList<>(Collections.singletonList(placeId)));
//        }
//
//        if (countMap.containsKey("REVIEWS") && page * limit >= countMap.get("REVIEWS")) {
//            return;
//        }

        query.include(Review.KEY_USER);
        query.whereEqualTo(Review.KEY_PLACE_ID, placeId);
        query.addDescendingOrder(Review.KEY_CREATED);
        query.setLimit(limit);
        query.setSkip(page * limit);

        Log.i(TAG,"skip: "+page * limit);
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
                ((ReviewFeedAdapter) adapter).addAll(reviews);
            }
        });

    }




}
