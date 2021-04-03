package com.unbeaned.app.navigation.splash;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.SignUpCallback;
import com.unbeaned.app.R;
import com.unbeaned.app.databinding.FragmentRegisterBinding;
import com.unbeaned.app.models.User;
import com.unbeaned.app.staticActivity.LoginActivity;

public class RegisterFragment extends Fragment {

    FragmentRegisterBinding binding;
    Button btnSplashRegisterEnter;
    Button btnSplashRegisterBack;
    EditText etNameRegister;
    EditText etEmailRegister;
    EditText etUsernameRegister;
    EditText etPasswordRegister;
    LinearLayout linearLayoutRegisterContainer;
    private static final String TAG = "LoginActivity";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnSplashRegisterEnter = binding.btnSplashRegisterEnter;
        btnSplashRegisterBack = binding.btnSplashRegisterBack;

        etNameRegister = binding.etNameRegister;
        etEmailRegister = binding.etEmailRegister;
        etUsernameRegister = binding.etUsernameRegister;
        etPasswordRegister = binding.etPasswordRegister;

        linearLayoutRegisterContainer = binding.linearLayoutRegisterContainer;


        btnSplashRegisterEnter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(linearLayoutRegisterContainer.getWindowToken(), 0);

                User user = new User();
                user.setName(etNameRegister.getText().toString());
                user.setEmail(etEmailRegister.getText().toString());
                user.setUsername(etUsernameRegister.getText().toString());
                //need another edit text field to get email for sign up
                user.setKeyReviewCount(0);
                //user.setUsername("random");
                user.setPassword(etPasswordRegister.getText().toString());
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e!=null){
                            //better error handling with Toasts (username/email already exists)
                            Log.e(TAG, "Issue with signup", e);
                            return;
                        }
                        NavDirections action = RegisterFragmentDirections.actionFragmentRegisterToFeedFragment();
                        NavHostFragment.findNavController(RegisterFragment.this).navigate(action);
                    }
                });
            }
        });

    }
}