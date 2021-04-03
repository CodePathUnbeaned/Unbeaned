package com.unbeaned.app.navigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.OutputConfiguration;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Parcelable;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.slider.Slider;
import com.parse.ParseUser;
import com.unbeaned.app.R;
import com.unbeaned.app.databinding.ActivityComposeBinding;
import com.unbeaned.app.models.Place;

import org.parceler.Parcels;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ComposeActivity extends AppCompatActivity {
    private static final String TAG = "ComposeActivity";
    ActivityComposeBinding binding;
    private Place place;
    private Slider slider;
    private EditText etReview;
    private Button btnCamera;
    private Button btnSubmit;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_compose);
        place = Parcels.unwrap(getIntent().getParcelableExtra("place"));
        binding.setPlace(place);
        slider = binding.slider;
        etReview = binding.etReview;
        btnCamera = binding.btnCamera;
        btnSubmit = binding.btnSubmit;

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCamera();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String review = etReview.getText().toString();
                if(review.isEmpty()){
                    return;
                }
                double rating = slider.getValue();
                ParseUser currentUser = ParseUser.getCurrentUser();
                String placeName = place.getName();
                String placeId = place.getPlaceId();

                saveReview(review, rating, currentUser, placeName,placeId);
            }
        });

    }

    private void saveReview(String review, double rating, ParseUser currentUser, String placeName, String placeId) {
    }

    private void goToCamera() {
        Intent i = new Intent(this, CameraActivity.class);
        startActivity(i);
    }
}