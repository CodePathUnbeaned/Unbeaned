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

import com.unbeaned.app.R;
import com.unbeaned.app.databinding.PlaceItemBinding;
import com.unbeaned.app.databinding.ReviewItemBinding;
import com.unbeaned.app.models.Place;
import com.unbeaned.app.models.Review;
import com.unbeaned.app.navigation.DetailsActivity;

import org.parceler.Parcels;

import java.util.List;

public class ReviewFeedAdapter extends RecyclerView.Adapter<ReviewFeedAdapter.ViewHolder> {
    Context context;
    List<Review> reviews;



    public ReviewFeedAdapter(Context context, List<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewFeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        ReviewItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.review_item, parent, false);

        return new ViewHolder(binding);
    }

    //bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ReviewFeedAdapter.ViewHolder holder, int position) {
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
        TextView tvPlaceName;
        TextView tvRating;
        RelativeLayout placeItemLinearContainer;

        public ViewHolder(@NonNull ReviewItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            //binding.setPlace();
            tvPlaceName = binding.tvPlaceName;
            tvRating = binding.tvRating;
            placeItemLinearContainer = binding.reviewItemLinearContainer;

        }

        public void bind(final Review review) {
            binding.setReview(review);
            binding.executePendingBindings();
        }
    }
}
