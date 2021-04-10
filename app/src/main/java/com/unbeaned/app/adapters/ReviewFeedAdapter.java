package com.unbeaned.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;
import com.unbeaned.app.R;
import com.unbeaned.app.databinding.PlaceDetailReviewItemBinding;
import com.unbeaned.app.databinding.ReviewItemBinding;
import com.unbeaned.app.models.Images;
import com.unbeaned.app.models.Place;
import com.unbeaned.app.models.Review;
import com.unbeaned.app.navigation.DetailsActivity;
import com.unbeaned.app.navigation.ReviewDetailsActivity;
import com.unbeaned.app.navigation.place.PlaceDetailFragmentDirections;


import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class ReviewFeedAdapter extends RecyclerView.Adapter<ReviewFeedAdapter.ViewHolder> {

    Context context;
    List<Review> reviews;
    Place place;

    public ReviewFeedAdapter(Context context, List<Review> reviews, Place place) {
        this.context = context;
        this.reviews = reviews;
        this.place = place;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        PlaceDetailReviewItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.place_detail_review_item, parent, false);
        binding.setPlace(place);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //get the data at position
        Review review = reviews.get(position);
        //bind the place with the viewholder
        holder.bind(review);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }
    //Clear all elements of the recycler
    public void clear(){
        reviews.clear();
        notifyDataSetChanged();
    }

    //Add a List of items
    public void addAll(List<Review> reviewsList){
        reviews.addAll(reviewsList);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final PlaceDetailReviewItemBinding binding;
        ImageView ivPlaceDetailReviewItemProfile;
        TextView tvReadMorePlaceDetailReviewItem;
        TextView tvPlaceDetailReviewProfileName;
        TextView tvPlaceDetailReviewRating;
//        TextView tvReviewTitle;
        TextView tvPlaceDetailReviewItemContent;
        CarouselView carouselPlaceDetailReviewItem;
        RelativeLayout placeDetailReviewContainer;

        public ViewHolder(@NonNull PlaceDetailReviewItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
            ivPlaceDetailReviewItemProfile = binding.ivPlaceDetailReviewItemProfile;
            tvPlaceDetailReviewProfileName = binding.tvPlaceDetailReviewProfileName;
            tvPlaceDetailReviewRating = binding.tvPlaceDetailReviewRating;
            carouselPlaceDetailReviewItem = binding.carouselPlaceDetailReviewItem;
            placeDetailReviewContainer = binding.placeDetailReviewContainer;
            tvPlaceDetailReviewItemContent = binding.tvPlaceDetailReviewItemContent;
            tvReadMorePlaceDetailReviewItem = binding.tvReadMorePlaceDetailReviewItem;
        }

        public void bind(final Review review) {
            binding.setReview(review);
            binding.executePendingBindings();

            Log.i("Review", "Review ID: "+review.getObjectId());

            Log.i("Review", "Review images: "+review.images);

            if(review.images.size()!=0){
                carouselPlaceDetailReviewItem.setPageCount(review.images.size());

                carouselPlaceDetailReviewItem.setImageListener(new ImageListener() {
                    @Override
                    public void setImageForPosition(int position, ImageView imageView) {
                        Glide.with(context)
                                .load(review.images.get(position).getImage().getUrl())
                                .into(imageView);
                    }
                });
            }
            else{
                //hide carousel view if no images
                placeDetailReviewContainer.removeView(carouselPlaceDetailReviewItem);
            }
//            //TODO: Onclick listener for the container
//            placeDetailReviewContainer.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent i = new Intent(context, ReviewDetailsActivity.class);
//                    i.putExtra("review", review.getObjectId());
//                    context.startActivity(i);
//                }
//            });

            tvReadMorePlaceDetailReviewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }



}
