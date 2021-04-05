package com.unbeaned.app.utils;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.unbeaned.app.R;

public class BindingAdapterUtils {
    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String url) {
        Glide.with(view.getContext())
                .load(url)
                .placeholder(R.drawable.coffee_beans)
                .error(R.drawable.coffee_beans)
                .into(view);
    }

    @BindingAdapter({"bind:imageFile"})
    public static void loadImageFile(ImageView view, ParseFile file) {
        if (file == null) {
            Glide.with(view.getContext())
                    .load(R.drawable.coffee_beans)
                    .into(view);
        }
        else {
            Glide.with(view.getContext())
                    .load(file.getUrl())
                    .placeholder(R.drawable.coffee_beans)
                    .error(R.drawable.coffee_beans)
                    .into(view);
        }
    }

    @BindingAdapter({"bind:profileImage"})
    public static void loadProfileImage(ImageView view, ParseFile file) {
        if (file == null) {
            Glide.with(view.getContext())
                    .load(R.drawable.coffee_beans)
                    .circleCrop()
                    .into(view);
        }
        else {
            Glide.with(view.getContext())
                    .load(file.getUrl())
                    .circleCrop()
                    .placeholder(R.drawable.coffee_beans)
                    .error(R.drawable.coffee_beans)
                    .into(view);
        }
    }

    @BindingAdapter({"bind:beanBadge"})
    public static void loadProfileBadge(ImageView view, int reviewCount) {
        // 0 < b < 5
        // 5 < b < 10
        // 10 < b < 20
        // 20 < b < 30
        // 30 < b < 40
        // b > 40

        int beanID = R.drawable.ic_bean_start;

        if (reviewCount > 40) {
            beanID = R.drawable.ic_final_bean;
        }
        else if (reviewCount > 30) {
            beanID = R.drawable.ic_bean_master;
        }
        else if (reviewCount > 20) {
            beanID = R.drawable.ic_regular_bean;
        }
        else if (reviewCount > 10) {
            beanID = R.drawable.ic_bean_amateur;
        }
        else if (reviewCount > 5) {
            beanID = R.drawable.ic_just_beand;
        }

        Glide.with(view.getContext())
                .load(beanID)
                .into(view);

    }

    @BindingAdapter({"bind:averageRating"})
    public static void getAverageRating(TextView view, double rating) {
        double round = (double) Math.round(rating * 100) / 100;

        if (Math.round(round) == rating) {
            view.setText(String.valueOf(rating));
        }
        else {
            view.setText(String.valueOf(round));
        }

    }
}
