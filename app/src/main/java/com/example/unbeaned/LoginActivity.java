package com.example.unbeaned;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import com.example.unbeaned.User;

import com.example.unbeaned.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    public static final String CLIENT_ID = "f9eQg5gZ7Z7PFMKktwA9lA";
    public static final String API_KEY = "HjXXdhlitIyTyevAVFPG1l9Qj6x-ICTdlKn640JqqAW53vxHypjUgAShNvBC5ztdxYYwU78PDWjRnky8VwoGwM_f5_TPOwfE1ue4_w7rDfsHwDkF47h_ooQfOSdUYHYx";

    ActivityLoginBinding binding;
    TextView tvText;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        User user = new User("Claudia");

        tvText = binding.textView;
        btn = binding.button;

        binding.setUser(user);
    }
}