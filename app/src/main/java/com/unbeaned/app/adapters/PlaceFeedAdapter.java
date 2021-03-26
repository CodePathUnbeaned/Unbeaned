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
import com.unbeaned.app.models.Place;
import com.unbeaned.app.navigation.DetailsActivity;

import org.parceler.Parcels;

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

    //bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //get the data at position
        Place place = places.get(position);
        //bind the place with the viewholder
        holder.bind(place);
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    //Clear all elements of the recycler
    public void clear(){
        places.clear();
        notifyDataSetChanged();
    }

    //Add a List of items
    public void addAll(List<Place> placesList){
        places.addAll(placesList);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final PlaceItemBinding binding;
        ImageView ivPlace;
        TextView tvPlaceName;
        TextView tvRating;
        TextView tvAddress;
        TextView tvPrice;
        RelativeLayout placeItemLinearContainer;

        public ViewHolder(@NonNull PlaceItemBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
            ivPlace = binding.ivPlace;
            tvPlaceName = binding.tvPlaceName;
            tvRating = binding.tvRating;
            tvAddress = binding.tvAddress;
            tvPrice = binding.tvPrice;
            placeItemLinearContainer = binding.placeItemLinearContainer;

        }

        public void bind(final Place place) {
            binding.setPlace(place);
            binding.executePendingBindings();

            placeItemLinearContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, DetailsActivity.class);
                    i.putExtra("place", Parcels.wrap(place));
                    context.startActivity(i);
                }
            });
        }
    }
}
