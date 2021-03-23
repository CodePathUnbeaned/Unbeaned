package com.unbeaned.app.staticActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.unbeaned.app.R;
import com.unbeaned.app.databinding.ActivityLoginBinding;
import com.unbeaned.app.utils.YelpClient;

import org.jetbrains.annotations.NotNull;

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
    Button btn;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        tvAppName = binding.tvAppName;

        Map<String, String> map = new HashMap<>();
        map.put("location", "NYC");

        Call business = YelpClient.getBusinessBySearch(map);

        business.enqueue(new Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())  throw new IOException("Response not found");
                    Log.i(TAG, "onSuccess: " + responseBody.string());
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i(TAG, "onFailure", e);
            }
        });

    }
}