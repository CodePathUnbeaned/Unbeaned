package com.unbeaned.app.navigation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.synnapps.carouselview.ViewListener;
import com.unbeaned.app.R;
import com.unbeaned.app.databinding.EditReviewItemBinding;
import com.unbeaned.app.databinding.UserReviewPlaceholderBinding;
import com.unbeaned.app.databinding.UserSettingsFragmentBinding;
import com.unbeaned.app.models.Comment;
import com.unbeaned.app.models.Images;
import com.unbeaned.app.models.Review;
import com.unbeaned.app.models.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UserSettingsFragment extends UserFragment {

    public static final String TAG = "UserSettingsFragment";
    public static final int GET_FROM_GALLERY = 23;

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

        binding.ivProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfilePicture();
            }
        });

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

        updateUserReviews(userReviews);
        if(userReviews.size() != 0) {
            binding.caroselEditReviews.setPageCount(userReviews.size());
        } else {
            binding.caroselEditReviews.setPageCount(1);
        }
        Log.i(TAG, userReviews.toString());

        binding.caroselEditReviews.setViewListener(new ViewListener() {
            @Override
            public View setViewForPosition(int position) {
                if (userReviews.size() == 0) {
                    UserReviewPlaceholderBinding binding = DataBindingUtil.inflate(inflater, R.layout.user_review_placeholder, container, false);
                    return binding.getRoot();
                }

                EditReviewItemBinding editReviewBinding = DataBindingUtil.inflate(inflater, R.layout.edit_review_item, container, false);

                editReviewBinding.setReview(userReviews.get(position));
                Log.i(TAG, "Setting custom view in userSettings");

                editReviewBinding.btnDeleteReview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(getContext())
                                .setTitle("Delete Review?")
                                .setMessage("This action cannot be reversed!")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        deleteReview(userReviews.get(position));
                                    }})
                                .setNegativeButton(android.R.string.cancel, null).show();
                    }
                });

                return editReviewBinding.getRoot();
            }
        });

        return binding.getRoot();
    }

    private void editProfilePicture() {
        Toast.makeText(getActivity(), "Change Profile Picture?!", Toast.LENGTH_LONG).show();
        startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
    }

    private void setProfilePicture(byte[] imgBytes) {
        ParseFile imgFile = new ParseFile(imgBytes);
        ParseUser.getCurrentUser().put(User.KEY_PHOTO, imgFile);
        Glide.with(getContext()).load(imgBytes).circleCrop().into(binding.ivProfilePicture);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // If result of from editProfilePicture()
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
                byte[] imgBytes = getBytes(inputStream);
                setProfilePicture(imgBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int buffersize = 1024;
        byte[] buffer = new byte[buffersize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private void deleteReview(Review review) {
        try {
            review.delete();
            int currentReviewCount = ParseUser.getCurrentUser().getInt(User.KEY_REVIEW_COUNT);
            ParseUser.getCurrentUser().put(User.KEY_REVIEW_COUNT, currentReviewCount-1);
            deletePhotos(review);
            deleteComments(review);
            Toast.makeText(getContext(), "Review Deleted!", Toast.LENGTH_LONG).show();
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error deleting review.", Toast.LENGTH_LONG).show();
        }
        updateUserReviews(userReviews);
        binding.caroselEditReviews.setPageCount(userReviews.size());
    }

    private void deleteComments(Review review) {
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);

        query.include(Comment.KEY_USER);
        query.whereEqualTo(Comment.KEY_REVIEW, review);
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> comments, ParseException e) {
                if (e!=null){
                    Log.e(TAG, "Issue with getting comments", e);
                    return;
                }
                for (Comment comment: comments){
                    try {
                        comment.delete();
                    } catch (ParseException parseException) {
                        parseException.printStackTrace();
                    }
                }
            }
        });
    }

    private void deletePhotos(Review review) {
        ParseQuery<Images> query = ParseQuery.getQuery(Images.class);

        query.whereEqualTo(Images.KEY_REVIEW_ID, review);
        query.findInBackground(new FindCallback<Images>() {
            @Override
            public void done(List<Images> photos, ParseException e) {
                if (e!=null){
                    Log.e(TAG, "Issue with getting comments", e);
                    return;
                }
                for (Images image: photos){
                    try {
                        image.delete();
                    } catch (ParseException parseException) {
                        parseException.printStackTrace();
                    }
                }
            }
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void logOut() {
        new AlertDialog.Builder(getContext())
                .setTitle("Logout?")
                .setMessage("Are you sure you want to logout?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        ParseUser.logOut();
                        navController.navigate(R.id.splashFragment);
                    }})
                .setNegativeButton(android.R.string.cancel, null).show();
    }

    private void returnToUserFragment() {
        navController.navigate(R.id.userFragment);
    }

    private void saveSettings() {
        String userName = binding.etUserName.getText().toString().toUpperCase();
        String userBio = binding.etUserBio.getText().toString();

        ParseUser.getCurrentUser().put(User.KEY_NAME, userName);
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