package com.unbeaned.app.staticActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.unbeaned.app.R;
import com.unbeaned.app.databinding.ActivityLoginBinding;
import com.unbeaned.app.models.User;
import com.unbeaned.app.navigation.MainActivity;
import com.unbeaned.app.utils.YelpClient;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    TextView tvAppName;
    Button btnLogin;
    Button btnRegister;
    TextInputEditText etEmailLogin;
    TextInputEditText etLoginPass;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        tvAppName = binding.tvAppName;
        btnLogin = binding.btnLogin;
        btnRegister = binding.btnRegister;
        etEmailLogin = binding.etEmailLogin;
        etLoginPass = binding.etLoginPass;

        btnRegister.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick login button");
                String email = etEmailLogin.getText().toString();
                String password = etLoginPass.getText().toString();
                registerUser(email,password);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick login button");
                String email = etEmailLogin.getText().toString();
                String password = etLoginPass.getText().toString();
                loginUser(email,password);
            }
        });

    }

    private void registerUser(String email, String password) {
        Log.i(TAG, "Attempting to register user "+email);
        User user = new User();
        user.setEmail(etEmailLogin.getText().toString());
        //need another edit text field to get Username for sign up
        user.setUsername("random");
        user.setPassword(etLoginPass.getText().toString());
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null){
                    //better error handling with Toasts (username/email already exists)
                    Log.e(TAG, "Issue with signup", e);
                    Toast.makeText(LoginActivity.this, "Failed to Register", Toast.LENGTH_SHORT);
                    return;
                }
                //navigate to main activity if the user has signed up properly
                goMainActivity();
                Toast.makeText(LoginActivity.this,"Success!", Toast.LENGTH_SHORT);
            }
        });
    }

    //not sure how to make it use email
    private void loginUser(String email, String password) {
        Log.i(TAG, "Attempting to login user "+email);
        ParseUser.logInInBackground(email, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e!=null){
                    //better error handling with Toasts
                    Log.e(TAG, "Issue with login", e);
                    Toast.makeText(LoginActivity.this, "Failed to login", Toast.LENGTH_SHORT);
                    return;
                }
                //navigate to main activity if the user has signed in properly
                goMainActivity();
                Toast.makeText(LoginActivity.this,"Success!", Toast.LENGTH_SHORT);
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}