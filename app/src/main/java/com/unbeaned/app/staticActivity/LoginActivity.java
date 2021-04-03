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
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
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
    TextInputEditText etUsernameLogin;
    TextInputEditText etLoginPass;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        tvAppName = binding.tvAppName;
        btnLogin = binding.btnLogin;
        btnRegister = binding.btnRegister;
        etUsernameLogin = binding.etUsernameLogin;
        etLoginPass = binding.etLoginPass;

        btnRegister.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick login button");
                String username = etUsernameLogin.getText().toString();
                String password = etLoginPass.getText().toString();
                registerUser(username,password);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick login button");
                String username = etUsernameLogin.getText().toString();
                String password = etLoginPass.getText().toString();
                loginUser(username,password);
            }
        });

    }
    //TODO: SignUp should probably launch to its own activity to allow user to add more info
    private void registerUser(String username, String password) {
        Log.i(TAG, "Attempting to register user "+username);
        User user = new User();
        user.setUsername(etUsernameLogin.getText().toString());
        //need another edit text field to get email for sign up
        user.setKeyReviewCount(0);
        //user.setUsername("random");
        user.setPassword(etLoginPass.getText().toString());
        user.getCurrentUser().signUpInBackground(new SignUpCallback() {
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

    //TODO: not sure how to make it use email
    private void loginUser(String username, String password) {
        Log.i(TAG, "Attempting to login user "+username);
        ParseUser.logInInBackground(username, password, new LogInCallback() {
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