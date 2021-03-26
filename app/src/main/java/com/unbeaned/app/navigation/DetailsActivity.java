package com.unbeaned.app.navigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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
    private Button btnCompose;


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
        btnCompose = binding.btnCompose;

        btnCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goComposeActivity();
            }
        });
    }
    private void goComposeActivity() {
        Intent i = new Intent(this, ComposeActivity.class);
        startActivity(i);
    }
    //TODO: when address is clicked navigate to google maps
    //TODO: btn on click listener to launch phone and call
    //TODO: populate recycler view with reviews for that place
}