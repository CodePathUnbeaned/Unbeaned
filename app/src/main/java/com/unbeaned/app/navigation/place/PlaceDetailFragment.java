package com.unbeaned.app.navigation.place;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.os.Parcel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.unbeaned.app.R;
import com.unbeaned.app.databinding.FragmentPlaceDetailBinding;
import com.unbeaned.app.models.Place;
import com.unbeaned.app.models.PlaceReg;

import org.parceler.Parcels;

public class PlaceDetailFragment extends Fragment {

    FragmentPlaceDetailBinding binding;
    Button btnBackPlaceDetail;
    Button btnPlaceSavedIcon;
    AppBarLayout appBarLayoutPlaceDetail;
    TextView tvPlaceNameToolbar;
    NestedScrollView nestedScrollViewDetails;
    ImageView ivPlaceDisplayImage;
    View toolbarBottomBorder;
    private PlaceReg place;

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
            Log.i("PlaceDetail", " " + getArguments());
            place = getArguments().getParcelable("place");
        }

        ivPlaceDisplayImage = binding.ivPlaceDisplayImage;
        btnBackPlaceDetail = binding.btnBackPlaceDetail;
        btnPlaceSavedIcon = binding.btnPlaceSavedIcon;
        appBarLayoutPlaceDetail = binding.appBarLayoutPlaceDetail;
        tvPlaceNameToolbar = binding.tvPlaceNameToolbar;
        nestedScrollViewDetails = binding.nestedScrollViewDetails;
        toolbarBottomBorder = binding.toolbarBottomBorder;

        btnPlaceSavedIcon.setEnabled(false);
        btnPlaceSavedIcon.setActivated(false);

        nestedScrollViewDetails.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
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
        });

        btnBackPlaceDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBackPlaceDetail.setActivated(true);
            }
        });

        binding.setPlace(place);

    }
}