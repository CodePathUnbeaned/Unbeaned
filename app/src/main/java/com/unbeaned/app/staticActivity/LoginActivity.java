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
import com.unbeaned.app.R;
import com.unbeaned.app.databinding.ActivityLoginBinding;
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



        Map<String, String> searchParameters = new HashMap<String, String>();
        searchParameters.put("location", "NYC");
        YelpClient client = new YelpClient();
        client.getBusinessBySearch(searchParameters).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e(TAG, "Could not fetch data", e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.i(TAG, "Success");
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);
                    //what do i do here
                    String jsonData = responseBody.string();
                    JSONObject jsonObject = new JSONObject(jsonData);
                    JSONArray jsonArray = jsonObject.getJSONArray("businesses");
                    Log.i(TAG, "JSONArray: "+jsonArray.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void registerUser(String email, String password) {
        Log.i(TAG, "Attempting to register user "+email);
        //ParseUser.signUp(email, password);
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
        finish();

    }
}