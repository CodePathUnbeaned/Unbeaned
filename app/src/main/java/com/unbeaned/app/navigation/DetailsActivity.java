package com.unbeaned.app.navigation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.unbeaned.app.BR;
import com.unbeaned.app.R;
import com.unbeaned.app.adapters.PlaceFeedAdapter;
import com.unbeaned.app.adapters.ReviewFeedAdapter;
import com.unbeaned.app.databinding.ActivityDetailsBinding;
import com.unbeaned.app.models.Place;
import com.unbeaned.app.models.Review;
import com.unbeaned.app.models.User;
import com.unbeaned.app.utils.Requests;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DetailsActivity extends AppCompatActivity {
    public static final String TAG = "DetailsActivity";
    ActivityDetailsBinding binding;
    private Place place;
    private ImageView ivPlace;
    private TextView tvPlacename;
    private TextView tvAddress;
    private TextView tvRating;
    private TextView tvPrice;
    private Button btnCall;
    private Button btnCompose;
    private RecyclerView rvReviews;
    private ReviewFeedAdapter adapter;
    private List<Review> allReviews;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details);
        place = Parcels.unwrap(getIntent().getParcelableExtra("place"));
        binding.setPlace(place);
        ivPlace = binding.ivPlace;
        tvPlacename = binding.tvPlaceName;
        tvAddress = binding.tvAddress;
        tvRating = binding.tvRating;
        tvPrice = binding.tvPrice;
        btnCall = binding.btnCall;
        btnCompose = binding.btnCompose;
        rvReviews = binding.rvReviews;
        allReviews= new ArrayList<>();
        adapter = new ReviewFeedAdapter(this, allReviews, place);
        rvReviews.setAdapter(adapter);
        rvReviews.setLayoutManager(new LinearLayoutManager(this));
        queryReviews();

        btnCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goComposeActivity();

            }
        });
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+place.getPhone()));

               if (ActivityCompat.checkSelfPermission(DetailsActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "NO phone permissions");
                   ActivityCompat.requestPermissions(DetailsActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 100);

                }
               else{
                   startActivity(callIntent);
               }

            }
        });
        tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:"+place.getLatitude()+","+place.getLongitude()+"?q="+place.getAddress1()+"&mode=d");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:2678844713"));
                    startActivity(callIntent);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void queryReviews() {
        ParseQuery<Review> query = ParseQuery.getQuery(Review.class);

        query.include(Review.KEY_USER);
        query.whereEqualTo(Review.KEY_PLACE_ID, place.getPlaceId());
        query.addDescendingOrder(Review.KEY_CREATED);
        query.findInBackground(new FindCallback<Review>() {
            @Override
            public void done(List<Review> reviews, ParseException e) {
                if (e!=null){
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                Log.i(TAG, "Querying Reviews: " + reviews);
                for (Review review:reviews){
                    review.setImages(review);
                }
                adapter.clear();
                adapter.addAll(reviews);
            }
        });
    }

    private void goComposeActivity() {
        Intent i = new Intent(this, ComposeActivity.class);
        i.putExtra("place", Parcels.wrap(place));
        startActivityForResult(i, 100);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        queryReviews();
//        place.calculateRating(place.getPlaceId());
        tvRating.setText(String.valueOf((Math.round(place.getRating()) * 100) / 100));
    }
}