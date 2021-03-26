package com.unbeaned.app.navigation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.unbeaned.app.R;
import com.unbeaned.app.databinding.ActivityDetailsBinding;
import com.unbeaned.app.models.Place;

import org.parceler.Parcels;

public class DetailsActivity extends AppCompatActivity {
    public static final String TAG = "DetailsActivity";
    ActivityDetailsBinding binding;
    private Place place;
    private ImageView ivPlace;
    private TextView tvPlaceName;
    private TextView tvAddress;
    private TextView tvRating;
    private TextView tvPrice;
    private Button btnCall;

   //TODO: need to display action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.compose_review_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details);
        place = Parcels.unwrap(getIntent().getParcelableExtra("place"));
        binding.setPlace(place);
        ivPlace = binding.ivPlace;
        tvPlaceName = binding.tvPlaceName;
        tvAddress = binding.tvAddress;
        tvRating = binding.tvRating;
        tvPrice = binding.tvPrice;
        btnCall = binding.btnCall;
    }
    //TODO: when address is clicked navigate to google maps
    //TODO: btn on click listener to launch phone and call
    //TODO: populate recycler view with reviews for that place
    //TODO: add intent to move to compose activity
}