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
}
