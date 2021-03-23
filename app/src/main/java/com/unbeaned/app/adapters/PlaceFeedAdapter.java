package com.unbeaned.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.unbeaned.app.R;
import com.unbeaned.app.databinding.PlaceItemBinding;
import com.unbeaned.app.models.Place;

import java.util.List;

public class PlaceFeedAdapter extends RecyclerView.Adapter<PlaceFeedAdapter.ViewHolder> {

    Context context;
    List<Place> places;

    public PlaceFeedAdapter(Context context, List<Place> places) {
        this.context = context;
        this.places = places;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        PlaceItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.place_item, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final PlaceItemBinding binding;
        ImageView ivPlace;
        TextView tvPlaceName;
        TextView tvRating;
        TextView tvAddress;
        TextView tvPrice;

        public ViewHolder(@NonNull PlaceItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
            ivPlace = binding.ivPlace;
            tvPlaceName = binding.tvPlaceName;
            tvRating = binding.tvRating;
            tvAddress = binding.tvAddress;
            tvPrice = binding.tvPrice;

        }

        public void bind(final Place place) {
            binding.setPlace(place);
            binding.executePendingBindings();
        }
    }
}
