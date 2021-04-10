package com.unbeaned.app.utils;

import android.util.Log;

import com.unbeaned.app.BuildConfig;

import java.util.Map;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class YelpClient {

    private final String CLIENT_ID = BuildConfig.YELP_CLIENT_ID;
    private static final String API_KEY = BuildConfig.YELP_API_KEY;
    private static final String BUSINESS_URL = "api.yelp.com";

    private static HttpUrl buildUrl(String baseUrl, String[] pathSegments, Map<String, String> params) {
        // Initialize url builder with "https" scheme and host as baseUrl
        HttpUrl.Builder url = new HttpUrl.Builder().scheme("https")
                .host(baseUrl);

        for (String s : pathSegments) {
            url.addPathSegment(s);
        }
        url.addQueryParameter("limit", "10");
        // Loop through key-value pair and assign as query parameters
        for (Map.Entry<String, String> entry : params.entrySet()) {
            url.addQueryParameter(entry.getKey(), entry.getValue());
        }

        // Return built url
        return url.build();
    }

    private static HttpUrl buildUrl(String baseUrl, String[] pathSegments, Map<String, String> params, int skip) {
        // Initialize url builder with "https" scheme and host as baseUrl
        HttpUrl.Builder url = new HttpUrl.Builder().scheme("https")
                .host(baseUrl);

        for (String s : pathSegments) {
            url.addPathSegment(s);
        }
        url.addQueryParameter("limit", "10");
        url.addQueryParameter("skip", String.valueOf(skip));
        // Loop through key-value pair and assign as query parameters
        for (Map.Entry<String, String> entry : params.entrySet()) {
            url.addQueryParameter(entry.getKey(), entry.getValue());
        }

        // Return built url
        return url.build();
    }

    private static HttpUrl buildUrl(String baseUrl, String[] pathSegments) {
        // Initialize url builder with "https" scheme and host as baseUrl
        HttpUrl.Builder url = new HttpUrl.Builder().scheme("https")
                .host(baseUrl);

        for (String s : pathSegments) {
            url.addPathSegment(s);
        }

        // Return built url
        return url.build();
    }

    private static Request getRequest(HttpUrl url) {
        return new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .build();
    }

    public static Call getBusinessDetails(String businessId) {
        Request request = getRequest(buildUrl(BUSINESS_URL, new String[]{"v3", "businesses", businessId}));
        return new OkHttpClient().newCall(request);
    }

    public static Request getBusinessBySearch(Map<String,String> params) {
        // Gets all Businesses around a longitude, latitude or location supplied in the params Map
        // Build request using authorization and url
        params.put("categories", "coffee");

        Request request = getRequest(buildUrl(BUSINESS_URL, new String[]{"v3", "businesses", "search"}, params));
        Log.i("LoginActivity", String.valueOf(request));
        return request;
    }

    public static Request getNextPageOfBusinesses(Map<String,String> params, int skip) {
        // Gets all Businesses around a longitude, latitude or location supplied in the params Map
        // Build request using authorization and url
        params.put("categories", "coffee");

        Request request = getRequest(buildUrl(BUSINESS_URL, new String[]{"v3", "businesses", "search"}, params, skip));
        Log.i("LoginActivity", String.valueOf(request));
        return request;
    }

    public static Call getBusinessBySearch(String place, Map<String, String> params) {
        // Searches for a particular business defined by place
        // Add place into params Map
        params.put("categories", "coffee");
        params.put("term", place);

        // Build request using authorization and url
        Request request = getRequest(buildUrl(BUSINESS_URL, new String[]{"v3", "businesses", "search"}, params));
        return new OkHttpClient().newCall(request);
    }
}