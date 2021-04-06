package com.unbeaned.app.navigation;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;
import com.unbeaned.app.R;
import com.unbeaned.app.databinding.EditReviewItemBinding;
import com.unbeaned.app.databinding.ReviewProfileItemBinding;
import com.unbeaned.app.databinding.UserFragmentBinding;
import com.unbeaned.app.databinding.UserSettingsFragmentBinding;
import com.unbeaned.app.databinding.UserSettingsFragmentBindingImpl;
import com.unbeaned.app.models.Review;
import com.unbeaned.app.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserSettingsFragment extends UserFragment {

    public static final String TAG = "UserSettingsFragment";

    UserSettingsFragmentBinding binding;
    List<Review> userReviews;
    NavController navController;

    public UserSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.user_settings_fragment, container, false);
        navController = Navigation.findNavController(getActivity(), R.id.navHostContainer);
        userReviews = new ArrayList<>();

        try {
            ParseUser.getCurrentUser().fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        binding.setUser(ParseUser.getCurrentUser());
        binding.setUserData(new User());

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToUserFragment();
            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
            }
        });

        getUserReviews(userReviews);
        binding.caroselEditReviews.setPageCount(userReviews.size());
        Log.i(TAG, userReviews.toString());

        binding.caroselEditReviews.setViewListener(new ViewListener() {
            @Override
            public View setViewForPosition(int position) {
                EditReviewItemBinding editReviewBinding = DataBindingUtil.inflate(inflater, R.layout.edit_review_item, container, false);

                editReviewBinding.setReview(userReviews.get(position));
                Log.i(TAG, "Setting custom view in userSettings");
                return editReviewBinding.getRoot();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void logOut() {
        ParseUser.logOut();
        navController.navigate(R.id.splashFragment);
    }

    private void returnToUserFragment() {
        navController.navigate(R.id.userFragment);
    }

    private void saveSettings() {
        String userName = binding.etUserName.getText().toString().toUpperCase();
        String userHandle = binding.etUserHandle.getText().toString();
        String userBio = binding.etUserBio.getText().toString();

        ParseUser.getCurrentUser().put(User.KEY_NAME, userName);
        ParseUser.getCurrentUser().put(User.KEY_USERNAME, userHandle);
        ParseUser.getCurrentUser().put(User.KEY_BIO, userBio);

        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.linearLayoutSettingsContainer.getWindowToken(), 0);

        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e("UserSettings", "Error in user save", e);
                    Toast.makeText(getContext(), "Save Failed!", Toast.LENGTH_LONG).show();
                    return;
                }
                Log.i("UserSettings", "Success in User Save");
                Toast.makeText(getContext(), "Changes Saved!", Toast.LENGTH_LONG).show();
                returnToUserFragment();
            }

        });
    }
}