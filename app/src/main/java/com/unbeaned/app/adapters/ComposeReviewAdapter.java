package com.unbeaned.app.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.unbeaned.app.R;
import com.unbeaned.app.databinding.ComposeReviewImageLayoutBinding;
import com.unbeaned.app.utils.ImageUtils;

import java.util.List;

public class ComposeReviewAdapter extends BaseAdapter {

    ComposeReviewImageLayoutBinding binding;
    Context context;
    List<Bitmap> images;

    public ComposeReviewAdapter(Context context, List<Bitmap> images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        ComposeReviewAdapter.ViewHolder viewHolder;

        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.compose_review_image_layout, parent, false);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.compose_review_image_layout, null);

//            final ImageView ivComposeReviewImage = binding.ivComposeReviewImage;
//            viewHolder = new ViewHolder(ivComposeReviewImage);
//
//            convertView.setTag(viewHolder);
        }
//        else {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }

        ImageView ivComposeReviewImage = convertView.findViewById(R.id.ivComposeReviewImage);
        ivComposeReviewImage.setImageBitmap(ImageUtils.transformBitmap(images.get(position), 3, -90));


//        viewHolder.ivComposeReviewImage.setImageBitmap(images.get(position));

//        Glide.with(context)
//                .asBitmap()
//                .load(images.get(position))
//                .into(new CustomTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                        viewHolder.ivComposeReviewImage.setImageBitmap(resource);
//                    }
//
//                    @Override
//                    public void onLoadCleared(@Nullable Drawable placeholder) {
//
//                    }
//                });
        Log.i("ComposeReviewAdapter", "Loading into ImageView: " + images);

        return convertView;
    }

    class ViewHolder {
        public ImageView ivComposeReviewImage;

        public ViewHolder(ImageView ivComposeReviewImage) {
            this.ivComposeReviewImage = ivComposeReviewImage;
        }

    }

}
