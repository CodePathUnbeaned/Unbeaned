package com.unbeaned.app.navigation.review;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.unbeaned.app.R;
import com.unbeaned.app.adapters.ComposeReviewAdapter;
import com.unbeaned.app.databinding.FragmentComposeReviewBinding;
import com.unbeaned.app.models.BitmapList;
import com.unbeaned.app.models.Images;
import com.unbeaned.app.models.Place;
import com.unbeaned.app.models.Review;
import com.unbeaned.app.utils.ImageUtils;

import java.util.Locale;

public class ComposeReviewFragment extends Fragment {

    FragmentComposeReviewBinding binding;
    Button btnCancelComposeReview;
    Button btnComposeReviewCamera;
    Slider sliderComposeReview;
    TextView tvRatingComposeReview;
    GridView imageGridComposeReview;
    EditText etComposeReviewContent;
    TextView tvComposeReview;
    private Place place;
    private Review review;
    private BitmapList bitmapList;

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
            bitmapList = getArguments().getParcelable("images");
            review = getArguments().getParcelable("review");
            Log.i("ComposeFragment", "Rating: " + review.getReviewRating());
        }

        btnCancelComposeReview = binding.btnCancelComposeReview;
        sliderComposeReview = binding.sliderComposeReview;
        btnComposeReviewCamera = binding.btnComposeReviewCamera;
        tvRatingComposeReview = binding.tvRatingComposeReview;
        imageGridComposeReview = binding.imageGridComposeReview;
        etComposeReviewContent = binding.etComposeReviewContent;
        tvComposeReview = binding.tvComposeReview;

        imageGridComposeReview.setAdapter(new ComposeReviewAdapter(getContext(), bitmapList.getImages()));

        tvRatingComposeReview.setText(String.format(Locale.US, "%.2f", review.getReviewRating()));

        sliderComposeReview.setValue(review.getReviewRating());

        if (!TextUtils.isEmpty(review.getReviewContent())) {
            etComposeReviewContent.setText(review.getReviewContent());
        }

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
                navigateToPlaceDetail();
            }
        });

        btnComposeReviewCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                review.setReviewContent(etComposeReviewContent.getText().toString());
                review.setReviewRating(sliderComposeReview.getValue());

                ComposeReviewFragmentDirections.ActionComposeReviewFragmentToCameraFragment action = ComposeReviewFragmentDirections.actionComposeReviewFragmentToCameraFragment(place, review, bitmapList);
                NavHostFragment.findNavController(ComposeReviewFragment.this).navigate(action);

            }
        });

        tvComposeReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save Review to Database
                postReview();

                // Save Images to Database
                postImages();

                // Navigate Back to Place Detail Fragment
                navigateToPlaceDetail();
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navigateToPlaceDetail();
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(callback);

        binding.setPlace(place);

    }

    private void postReview() {
        review.setReview(etComposeReviewContent.getText().toString());
        review.setRating(Double.valueOf(String.format(Locale.US, "%.2f", (double)sliderComposeReview.getValue())));
        review.setPlaceId(place.getPlaceId());
        review.setPlaceName(place.getName());
        review.setUser(ParseUser.getCurrentUser());
        try {
            review.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void postImages() {
        for (Bitmap image : bitmapList.getImages()) {
            Images parseImage = new Images();
            parseImage.setImage(ImageUtils.convertBitmapParseFile(ImageUtils.transformBitmap(image, 2, -90)));
            parseImage.setReview(review);
            try {
                parseImage.save();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void navigateToPlaceDetail() {
        ComposeReviewFragmentDirections.ActionComposeReviewFragmentToPlaceDetailFragment action = ComposeReviewFragmentDirections.actionComposeReviewFragmentToPlaceDetailFragment(place);
        action.setBackPath("feed");
        NavHostFragment.findNavController(ComposeReviewFragment.this).navigate(action);
    }

}