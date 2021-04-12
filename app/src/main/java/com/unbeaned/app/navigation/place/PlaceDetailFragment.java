package com.unbeaned.app.navigation.place;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.slider.Slider;
import com.unbeaned.app.R;
import com.unbeaned.app.adapters.ReviewFeedAdapter;
import com.unbeaned.app.databinding.FragmentPlaceDetailBinding;
import com.unbeaned.app.models.Place;
import com.unbeaned.app.models.Review;
import com.unbeaned.app.utils.EndlessNestedScrollView;
import com.unbeaned.app.utils.EndlessRecyclerViewScrollListener;
import com.unbeaned.app.utils.Requests;

import java.util.ArrayList;
import java.util.List;

public class PlaceDetailFragment extends Fragment {

    private static final String TAG = "PlaceDetailFragment";
    FragmentPlaceDetailBinding binding;
    Button btnBackPlaceDetail;
    Button btnPlaceSavedIcon;
    AppBarLayout appBarLayoutPlaceDetail;
    TextView tvPlaceNameToolbar;
    NestedScrollView nestedScrollViewDetails;
    ImageView ivPlaceDisplayImage;
    View toolbarBottomBorder;
    Button btnCallIcon;
    Button btnMapIcon;
    Button btnLinkIcon;
    Button btnStartReviewAddPhoto;
    Button btnStartReviewWrite;
    String backPath;
    List<Review> allReviews;
    ReviewFeedAdapter adapter;
    RecyclerView rvPlaceDetailsReview;
    Slider placeReviewDetailSlider;
    EndlessRecyclerViewScrollListener scrollListener;
    private Place place;
    private boolean loadData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_place_detail, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            place = getArguments().getParcelable("place");
            backPath = getArguments().getString("backPath");
        }

        ivPlaceDisplayImage = binding.ivPlaceDisplayImage;
        btnBackPlaceDetail = binding.btnBackPlaceDetail;
        btnPlaceSavedIcon = binding.btnPlaceSavedIcon;
        appBarLayoutPlaceDetail = binding.appBarLayoutPlaceDetail;
        tvPlaceNameToolbar = binding.tvPlaceNameToolbar;
        nestedScrollViewDetails = binding.nestedScrollViewDetails;
        toolbarBottomBorder = binding.toolbarBottomBorder;
        btnCallIcon = binding.btnCallIcon;
        btnMapIcon = binding.btnMapIcon;
        btnLinkIcon = binding.btnLinkIcon;
        rvPlaceDetailsReview = binding.rvPlaceDetailsReview;
        placeReviewDetailSlider = binding.placeReviewDetailSlider;
        btnStartReviewAddPhoto = binding.btnStartReviewAddPhoto;
        btnStartReviewWrite = binding.btnStartReviewWrite;

//        loadData = false;
        allReviews = new ArrayList<>();
        adapter = new ReviewFeedAdapter(getContext(), allReviews, place, this);
        rvPlaceDetailsReview.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        rvPlaceDetailsReview.setLayoutManager(layoutManager);
        rvPlaceDetailsReview.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

//        scrollListener= new EndlessRecyclerViewScrollListener(layoutManager) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//                Log.i(TAG, "onLoadMore "+page);
//                loadMoreData(false);
//            }
//        };
//        rvPlaceDetailsReview.addOnScrollListener(scrollListener);


//        Requests.getAllReviews(allReviews, place.getPlaceId(), adapter, TAG);

        btnPlaceSavedIcon.setEnabled(false);
        btnPlaceSavedIcon.setActivated(false);
        loadMoreData(true, 0);


//        nestedScrollViewDetails.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                if (v.getScrollY() >= ivPlaceDisplayImage.getBottom()) {
//                    btnBackPlaceDetail.setActivated(true);
//                    btnBackPlaceDetail.setTextColor(Color.BLACK);
//                    tvPlaceNameToolbar.setVisibility(View.VISIBLE);
//                    appBarLayoutPlaceDetail.setBackgroundColor(Color.WHITE);
//                    toolbarBottomBorder.setVisibility(View.VISIBLE);
//                }
//                else {
//                    btnBackPlaceDetail.setActivated(false);
//                    btnBackPlaceDetail.setTextColor(Color.WHITE);
//                    tvPlaceNameToolbar.setVisibility(View.INVISIBLE);
//                    appBarLayoutPlaceDetail.setBackgroundColor(Color.TRANSPARENT);
//                    toolbarBottomBorder.setVisibility(View.INVISIBLE);
//                }
//
//                if (v.getScrollY() + v.getHeight() >= rvPlaceDetailsReview.getBottom()) {
//                    if (!loadData) {
//                        loadData = true;
//                        loadMoreData(true);
//                    }
//                    else {
//                        loadMoreData(false);
//                    }
//                    page++;
//                }
//            }
//        });

        nestedScrollViewDetails.setOnScrollChangeListener(new EndlessNestedScrollView(rvPlaceDetailsReview, layoutManager) {
            @Override
            public void onUIChange(NestedScrollView v) {
                if (v.getScrollY() >= ivPlaceDisplayImage.getBottom()) {
                    btnBackPlaceDetail.setActivated(true);
                    btnBackPlaceDetail.setTextColor(Color.BLACK);
                    tvPlaceNameToolbar.setVisibility(View.VISIBLE);
                    appBarLayoutPlaceDetail.setBackgroundColor(Color.WHITE);
                    toolbarBottomBorder.setVisibility(View.VISIBLE);
                }
                else {
                    btnBackPlaceDetail.setActivated(false);
                    btnBackPlaceDetail.setTextColor(Color.WHITE);
                    tvPlaceNameToolbar.setVisibility(View.INVISIBLE);
                    appBarLayoutPlaceDetail.setBackgroundColor(Color.TRANSPARENT);
                    toolbarBottomBorder.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.i(TAG, "Loading: " + page);
                loadMoreData(false, page);
            }
        });

        btnCallIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+place.getPhone()));

                if (ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "NO phone permissions");
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 100);
                }
                else {
                    startActivity(callIntent);
                }
            }
        });

        btnMapIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:"+place.getLatitude()+","+place.getLongitude()+"?q="+place.getAddress1()+"&mode=d");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        btnLinkIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(place.getWebsite()));
                startActivity(i);
            }
        });

        placeReviewDetailSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                PlaceDetailFragmentDirections.ActionPlaceDetailFragmentToComposeReviewFragment action = PlaceDetailFragmentDirections.actionPlaceDetailFragmentToComposeReviewFragment(place, new Review());
                action.setRating(slider.getValue());

                NavHostFragment.findNavController(PlaceDetailFragment.this).navigate(action);
            }
        });

        btnStartReviewAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = PlaceDetailFragmentDirections.actionPlaceDetailFragmentToCameraFragment();
                NavHostFragment.findNavController(PlaceDetailFragment.this).navigate(action);
            }
        });

        btnStartReviewWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaceDetailFragmentDirections.ActionPlaceDetailFragmentToComposeReviewFragment action = PlaceDetailFragmentDirections.actionPlaceDetailFragmentToComposeReviewFragment(place, new Review());

                NavHostFragment.findNavController(PlaceDetailFragment.this).navigate(action);
            }
        });

        btnBackPlaceDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resolveBackPath();
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                NavHostFragment.findNavController(PlaceDetailFragment.this).navigateUp();
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(callback);

//        Requests.getAllReviews(allReviews, place.getPlaceId(), adapter, TAG);

        binding.setPlace(place);

    }

    private void loadMoreData(boolean refresh, int page) {
        Requests.getNextPageOfReviews(allReviews, place.getPlaceId(), adapter, TAG, page, refresh);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+place.getPhone()));
                startActivity(callIntent);
            }
        }
    }

    private void resolveBackPath() {
        NavController navController = NavHostFragment.findNavController(PlaceDetailFragment.this);

        switch (backPath) {
            case "feed":
                navController.navigate(PlaceDetailFragmentDirections.actionPlaceDetailFragmentToFeedFragment());
                break;
            default:
                navController.navigateUp();
                break;
        }
    }

}