package com.unbeaned.app.navigation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;
import com.unbeaned.app.R;
import com.unbeaned.app.databinding.ReviewProfileItemBinding;
import com.unbeaned.app.databinding.UserFragmentBinding;
import com.unbeaned.app.databinding.UserReviewPlaceholderBinding;
import com.unbeaned.app.models.Review;
import com.unbeaned.app.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserFragment extends Fragment {

    public static final String TAG = "UserFragment";

    UserFragmentBinding binding;
    CarouselView carouselProfileReview;
    List<Review> allReviews;
    NavController navController;
    private ParseUser user;
    String backPath;

    public UserFragment () {
        // Constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflate the layout for this view
        binding = DataBindingUtil.inflate(inflater, R.layout.user_fragment, container, false);
        navController = Navigation.findNavController(getActivity(), R.id.navHostContainer);

        try {
            ParseUser.getCurrentUser().fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user = ParseUser.getCurrentUser();

        binding.setUser(user);
        binding.setUserData(new User());

        carouselProfileReview = binding.carouselProfileReviews;
        allReviews = new ArrayList<>();

        getUserReviews(allReviews);

        if(allReviews.size() != 0) {
            carouselProfileReview.setPageCount(allReviews.size());
        } else {
            carouselProfileReview.setPageCount(1);
        }

        binding.btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserSettings();
            }
        });

        carouselProfileReview.setViewListener(new ViewListener() {
            @Override
            public View setViewForPosition(int position) {
                if (allReviews.size() == 0) {
                    UserReviewPlaceholderBinding binding = DataBindingUtil.inflate(inflater, R.layout.user_review_placeholder, container, false);
                    return binding.getRoot();
                }

                ReviewProfileItemBinding reviewBinding = DataBindingUtil.inflate(inflater, R.layout.review_profile_item, container, false);

                reviewBinding.setReview(allReviews.get(position));
                Log.i(TAG, "Setting custom view");
                return reviewBinding.getRoot();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    protected void getUserReviews(List<Review> reviewList) {
        ParseQuery<Review> query = ParseQuery.getQuery(Review.class);

        query.include(Review.KEY_USER);
        query.whereEqualTo(Review.KEY_USER, ParseUser.getCurrentUser());
        query.addDescendingOrder(Review.KEY_RATING);
        query.setLimit(5);

        try {
            reviewList.addAll(query.find());
        } catch(ParseException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "Setting custom view");
    }

    protected void updateUserReviews(List<Review> reviewList) {
        reviewList.clear();
        getUserReviews(reviewList);
    }

    private void openUserSettings() {
        navController.navigate(R.id.userSettingsFragment);
    }
}
