package com.unbeaned.app.adapters;

import android.content.Context;
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
import com.unbeaned.app.databinding.ReviewItemBinding;
import com.unbeaned.app.models.Images;
import com.unbeaned.app.models.Place;
import com.unbeaned.app.models.Review;


import java.util.ArrayList;
import java.util.List;

public class ReviewFeedAdapter extends RecyclerView.Adapter<ReviewFeedAdapter.ViewHolder> {

    Context context;
    List<Review> reviews;
    Place place;



    public ReviewFeedAdapter(Context context, List<Review> reviews, Place place) {
        this.context = context;
        this.reviews = reviews;
        this.place=place;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        ReviewItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.review_item, parent, false);
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

        private final ReviewItemBinding binding;
        ImageView ivProfileImage;
        TextView tvUsername;
        TextView tvName;
        TextView tvRating;
        TextView tvPlaceName;
        TextView tvReview;
        CarouselView carouselView;
        RelativeLayout reviewItemLinearContainer;
        int[] sampleImages = {R.drawable.coffee_beans,R.drawable.coffee_beans, R.drawable.coffee_beans};


        public ViewHolder(@NonNull ReviewItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
            ivProfileImage = binding.ivProfileImage;
            tvUsername = binding.tvUsername;
            tvName = binding.tvName;
            tvPlaceName = binding.tvPlaceName;
            tvRating = binding.tvRating;
            tvReview = binding.tvReview;
            carouselView = binding.carouselView;
            reviewItemLinearContainer = binding.reviewItemLinearContainer;

        }

        public void bind(final Review review) {
            binding.setReview(review);
            binding.executePendingBindings();
            Log.i("Review", "Review ID: "+review.getObjectId());
            List<Images> images=queryImages(review);
            Log.i("Review", "Review images: "+images);

            if(images.size()!=0){
                carouselView.setPageCount(images.size());

                carouselView.setImageListener(new ImageListener() {
                    @Override
                    public void setImageForPosition(int position, ImageView imageView) {
                        Glide.with(context)
                                .load(images.get(position).getImage().getUrl())
                                .into(imageView);
                    }
                });
            }
            else{
                //hide carousel view if no images
                reviewItemLinearContainer.removeView(carouselView);
            }





            //TODO: Onclick listener for the container
        }
        public List<Images> queryImages(Review review){
            ParseQuery<Images> query = ParseQuery.getQuery(Images.class);
            List<Images> images =new ArrayList<>();
            query.whereEqualTo(Images.KEY_REVIEW_ID, review);
            try {
                images.clear();
                images.addAll(query.find());
                Log.i("Review", "Images: "+images);

            }catch (ParseException e) {
                e.printStackTrace();
            }
            return images;
        }
    }



}
