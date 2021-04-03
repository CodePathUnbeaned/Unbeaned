package com.unbeaned.app.navigation.splash;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.unbeaned.app.R;
import com.unbeaned.app.databinding.FragmentSplashOptionsBinding;
import com.unbeaned.app.databinding.StartFragmentBinding;
import com.unbeaned.app.navigation.MainActivity;
import com.unbeaned.app.navigation.SplashFragment;

public class SplashOptionsFragment extends Fragment {

    FragmentSplashOptionsBinding binding;
    Button btnLoginSplash;
    Button btnSignUpSplash;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash_options, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnLoginSplash = binding.btnLoginSplash;
        btnSignUpSplash = binding.btnSignUpSplash;

        NavController navController = Navigation.findNavController(view);

        btnLoginSplash.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                navController.navigate(R.id.splashOptionsToLoginFragment);
            }
        });

        btnSignUpSplash.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                navController.navigate(R.id.splashOptionsToRegisterFragment);
            }
        });

    }
}