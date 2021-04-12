package com.unbeaned.app.navigation.splash;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.unbeaned.app.R;
import com.unbeaned.app.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    FragmentLoginBinding binding;
    Button btnSplashBackLogin;
    Button btnSplashEnterLogin;
    EditText etUsernameLogin;
    EditText etPasswordLogin;
    LinearLayout linearLayoutLoginContainer;
    Snackbar snackbarLoginError;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);

        CoordinatorLayout mainCoordinatorLayout = getActivity().findViewById(R.id.mainCoordinatorLayout);

        snackbarLoginError = Snackbar.make(mainCoordinatorLayout, "Incorrect username and/or password", Snackbar.LENGTH_SHORT);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        btnSplashBackLogin = binding.btnSplashLoginBack;
        btnSplashEnterLogin = binding.btnSplashLoginEnter;

        etUsernameLogin = binding.etUsernameLogin;
        etPasswordLogin = binding.etPasswordLogin;

        linearLayoutLoginContainer = binding.linearLayoutLoginContainer;

        btnSplashEnterLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(linearLayoutLoginContainer.getWindowToken(), 0);

                String username = etUsernameLogin.getText().toString();
                String password = etPasswordLogin.getText().toString();

                ParseUser.logInInBackground(username, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (e != null) {
                            snackbarLoginError.show();
                        }
                        else {
                            NavDirections action = LoginFragmentDirections.actionFragmentLoginToFeedFragment();
                            NavHostFragment.findNavController(LoginFragment.this).navigate(action);
                        }
                    }
                });
            }
        });

        btnSplashBackLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                NavDirections action = LoginFragmentDirections.actionFragmentLoginToSplashFragment();
                NavHostFragment.findNavController(LoginFragment.this).navigate(action);
            }
        });

    }
}