package com.unbeaned.app.navigation.splash;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.unbeaned.app.R;
import com.unbeaned.app.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    FragmentLoginBinding binding;
    Button btnSplashBackLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnSplashBackLogin = binding.btnSplashLoginBack;

        btnSplashBackLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                NavDirections action = LoginFragmentDirections.actionFragmentLoginToSplashFragment();
                NavHostFragment.findNavController(LoginFragment.this).navigate(action);
            }
        });

    }
}