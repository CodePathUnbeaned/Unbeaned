package com.unbeaned.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.unbeaned.app.R;
import com.unbeaned.app.databinding.ReviewItemBinding;
import com.unbeaned.app.models.Place;
import com.unbeaned.app.models.Review;
import com.unbeaned.app.models.User;


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


        public ViewHolder(@NonNull ReviewItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
            ivProfileImage = binding.ivProfileImage;
            tvUsername = binding.tvUsername;
            tvName = binding.tvName;
            tvPlaceName = binding.tvPlaceName;
            tvRating = binding.tvRating;
            tvReview = binding.tvReview;

        }

        public void bind(final Review review) {
            binding.setReview(review);
            //binding.setUser(user);
            //binding.setUser((User) review.getUser());
            binding.executePendingBindings();

            ParseQuery<User> query = ParseQuery.getQuery(User.class);

            query.whereEqualTo(User.KEY_ID, review.getUser().getObjectId());
            query.findInBackground(new FindCallback<User>() {

                @Override
                public void done(List<User> objects, ParseException e) {

                }
            });


        }
    }
}
