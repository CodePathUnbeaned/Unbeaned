package com.unbeaned.app.navigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import com.unbeaned.app.R;
import com.unbeaned.app.adapters.PlaceFeedAdapter;
import com.unbeaned.app.adapters.ReviewFeedAdapter;
import com.unbeaned.app.databinding.ActivityDetailsBinding;
import com.unbeaned.app.models.Place;
import com.unbeaned.app.models.Review;
import com.unbeaned.app.models.User;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {
    public static final String TAG = "DetailsActivity";
    ActivityDetailsBinding binding;
    private Place place;
    private ImageView ivPlace;
    private TextView tvPlaceName;
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
        tvPlaceName = binding.tvPlaceName;
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
                for (Review review:reviews){
                    Log.i(TAG,"Review IDs: "+review.getObjectId());
                    Log.i(TAG, "Reviews: "+review.getReview());
                    Log.i(TAG,"Place IDs: "+review.getPlaceId());
                    review.setImages(review);
                    Log.i(TAG, "Images: "+review.images);
                }
                adapter.clear();
                adapter.addAll(reviews);
            }
        });
    }

    private void goComposeActivity() {
        Intent i = new Intent(this, ComposeActivity.class);
        startActivity(i);
    }
    //TODO: when address is clicked navigate to google maps
    //TODO: btn on click listener to launch phone and call
}