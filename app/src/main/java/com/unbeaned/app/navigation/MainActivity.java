package com.unbeaned.app.navigation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;
import com.unbeaned.app.R;
import com.unbeaned.app.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    ActivityMainBinding binding;
    final FragmentManager fragmentManager = getSupportFragmentManager();
//    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        BottomNavigationView bottomNavigationView = binding.bottomNavigation;
        NavController navController = Navigation.findNavController(this, R.id.navHostContainer);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.splashFragment) {
                    bottomNavigationView.setVisibility(View.GONE);
                }
                else {
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }
            }
        });

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        ParseUser.logOut();

        if (ParseUser.getCurrentUser() != null) {
            navController.navigate(R.id.feedFragment);
        }
        else {
            navController.navigate(R.id.splashFragment);
        }

//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                Fragment fragment;
//                switch (item.getItemId()) {
//                    case R.id.action_search:
//                        // do something here
//                        fragment = new FeedFragment();
//                        break;
//                    case R.id.action_feed:
//                        // do something here
//                        fragment = new FeedFragment();
//                        break;
//                    case R.id.action_profile:
//                    default:
//                        // do something here
//                         fragment = new FeedFragment();
//                        break;
//                }
//                fragmentManager.beginTransaction().replace(R.id.mainFragmentContainerView, fragment).commit();
//                return true;
//            }
//        });
//        // Set default selection
//        bottomNavigationView.setSelectedItemId(R.id.action_feed);
    }
}