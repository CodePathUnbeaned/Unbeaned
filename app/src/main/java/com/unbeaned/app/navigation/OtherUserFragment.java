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
import com.unbeaned.app.databinding.FragmentOtherUserBinding;
import com.unbeaned.app.databinding.UserReviewPlaceholderBinding;
import com.unbeaned.app.models.Review;
import com.unbeaned.app.models.User;

import java.util.ArrayList;
import java.util.List;

public class OtherUserFragment extends Fragment {

    public static final String TAG = "OtherUserFragment";

    FragmentOtherUserBinding binding;
    CarouselView carouselProfileReview;
    List<Review> allReviews;
    NavController navController;
    private ParseUser user;
    String backPath;

    public OtherUserFragment () {
        // Constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflate the layout for this view
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_other_user, container, false);
        navController = Navigation.findNavController(getActivity(), R.id.navHostContainer);

        if (getArguments() != null) {
            user = getArguments().getParcelable("user");
            backPath = getArguments().getString("backPath");
        }


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
        query.whereEqualTo(Review.KEY_USER, user);
        query.addDescendingOrder(Review.KEY_RATING);
        query.setLimit(5);

        try {
            reviewList.addAll(query.find());
        } catch(ParseException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "Setting custom view");
    }

}
