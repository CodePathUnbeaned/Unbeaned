package com.unbeaned.app.navigation.review;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;
import com.unbeaned.app.R;
import com.unbeaned.app.databinding.FragmentComposeReviewBinding;
import com.unbeaned.app.models.Place;

import java.util.Locale;

public class ComposeReviewFragment extends Fragment {

    FragmentComposeReviewBinding binding;
    Button btnCancelComposeReview;
    Button btnComposeReviewCamera;
    Slider sliderComposeReview;
    TextView tvRatingComposeReview;
    private float rating;
    private Place place;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_compose_review, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            place = getArguments().getParcelable("place");
            rating = getArguments().getFloat("rating");
            Log.i("ComposeFragment", "Rating: " + rating);
        }

        btnCancelComposeReview = binding.btnCancelComposeReview;
        sliderComposeReview = binding.sliderComposeReview;
        btnComposeReviewCamera = binding.btnComposeReviewCamera;
        tvRatingComposeReview = binding.tvRatingComposeReview;

        tvRatingComposeReview.setText(String.format(Locale.US, "%.2f", rating));

        sliderComposeReview.setValue(rating);

        sliderComposeReview.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                tvRatingComposeReview.setText(String.format(Locale.US, "%.2f", value));
            }
        });

        sliderComposeReview.setLabelFormatter(new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                return String.format(Locale.US, "%.2f", value);
            }
        });

        btnCancelComposeReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(ComposeReviewFragment.this).navigateUp();
            }
        });

        btnComposeReviewCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

//        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                NavHostFragment.findNavController(ComposeReviewFragment.this).navigateUp();
//            }
//        };
//
//        requireActivity().getOnBackPressedDispatcher().addCallback(callback);

        binding.setPlace(place);

    }
}