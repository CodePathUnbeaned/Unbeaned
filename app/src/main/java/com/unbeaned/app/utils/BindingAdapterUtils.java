package com.unbeaned.app.utils;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
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
}
