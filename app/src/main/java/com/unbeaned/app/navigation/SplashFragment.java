package com.unbeaned.app.navigation;

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.unbeaned.app.R;
import com.unbeaned.app.databinding.StartFragmentBinding;

public class SplashFragment extends Fragment {

    StartFragmentBinding binding;
    RelativeLayout rlContainer;
    Button btnLoginSplash;
    Button btnSignUpSplash;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.start_fragment, container, false);

        rlContainer = binding.rlContainer;
        btnLoginSplash = binding.btnLoginSplash;
        btnSignUpSplash = binding.btnSignUpSplash;

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);

        btnLoginSplash.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                NavDirections action = SplashFragmentDirections.splashFragmentToLoginFragment();
                NavHostFragment.findNavController(SplashFragment.this).navigate(action);
            }
        });

        btnSignUpSplash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = SplashFragmentDirections.splashFragmentToRegisterFragment();
                NavHostFragment.findNavController(SplashFragment.this).navigate(action);
            }
        });




    }
}
