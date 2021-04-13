package com.unbeaned.app.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseUser;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;
import com.unbeaned.app.R;
import com.unbeaned.app.databinding.PlaceDetailReviewItemBinding;
import com.unbeaned.app.models.Place;
import com.unbeaned.app.models.Review;
import com.unbeaned.app.navigation.place.PlaceDetailFragmentDirections;

import java.util.List;

public class ReviewFeedAdapter extends RecyclerView.Adapter<ReviewFeedAdapter.ViewHolder> {

    Context context;
    List<Review> reviews;
    Place place;
    Fragment fragmentContext;

    public ReviewFeedAdapter(Context context, List<Review> reviews, Place place, Fragment fragmentContext) {
        this.context = context;
        this.reviews = reviews;
        this.place = place;
        this.fragmentContext = fragmentContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        PlaceDetailReviewItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.place_detail_review_item, parent, false);
//        binding.setPlace(place);
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
        LinearLayout placeDetailFirstReviewContainer;
        TextView tvFirstReviewPlaceholder;

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
            placeDetailFirstReviewContainer = binding.placeDetailFirstReviewContainer;
            tvFirstReviewPlaceholder = binding.tvFirstReviewPlaceholder;
        }

        public void bind(final Review review) {
            binding.setReview(review);
            binding.executePendingBindings();

            if (!review.getReviewState()) {  // false if no reviews
                placeDetailReviewContainer.setVisibility(View.GONE);
                placeDetailFirstReviewContainer.setVisibility(View.VISIBLE);
            }
            else {
                placeDetailReviewContainer.setVisibility(View.VISIBLE);
                placeDetailFirstReviewContainer.setVisibility(View.GONE);
            }

            Log.i("ReviewFeedAdapter", "Binding Review ID: "+review.getObjectId());

//            Log.i("Review", "Review images: "+review.images);

            if(review.images.size() != 0){
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

            ivPlaceDetailReviewItemProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ParseUser user = review.getUser();
                    PlaceDetailFragmentDirections.ActionPlaceDetailFragmentToOtherUserFragment action = PlaceDetailFragmentDirections.actionPlaceDetailFragmentToOtherUserFragment(user);
                    action.setBackPath("placeDetails");
                    NavHostFragment.findNavController(fragmentContext).navigate(action);

                }
            });



            tvReadMorePlaceDetailReviewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PlaceDetailFragmentDirections.ActionPlaceDetailFragmentToReviewDetailFragment action = PlaceDetailFragmentDirections.actionPlaceDetailFragmentToReviewDetailFragment(review);
                    action.setBackPath("placeDetails");
                    NavHostFragment.findNavController(fragmentContext).navigate(action);

                }
            });
        }
    }



}
