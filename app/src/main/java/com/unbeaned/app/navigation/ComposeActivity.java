package com.unbeaned.app.navigation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.os.SystemClock;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.slider.Slider;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.unbeaned.app.R;
import com.unbeaned.app.databinding.ActivityComposeBinding;
import com.unbeaned.app.models.Place;
import com.unbeaned.app.models.Review;

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
    private EditText etReviewTitle;
    private Button btnCamera;
    private Button btnSubmit;
    private Review reviewItem;
    private ParseUser currentUser;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_compose);
        place = Parcels.unwrap(getIntent().getParcelableExtra("place"));
        binding.setPlace(place);
        slider = binding.slider;
        etReview = binding.etReview;
        btnCamera = binding.btnCamera;
        btnSubmit = binding.btnSubmit;
        etReviewTitle = binding.etReviewTitle;
        reviewItem = new Review();
        String placeName = place.getName();
        String placeId = place.getPlaceId();
        currentUser = ParseUser.getCurrentUser();
        reviewItem.setPlaceId(placeId);
        reviewItem.setPlaceName(placeName);
        reviewItem.setUser(currentUser);
        reviewItem.setTitle("title");
        reviewItem.setReview("review");
        reviewItem.setRating(0.0);

        reviewItem.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null){
                    Log.e(TAG, "Error while saving", e);
                    return;
                }
                Log.i(TAG, "Post was saved successfully");

            }
        });

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
                String reviewTitle = etReviewTitle.getText().toString();
                if(reviewTitle.isEmpty()){
                    return;
                }
                double rating = slider.getValue();

                saveReview(review, reviewTitle, rating);

            }
        });

    }

    private void saveReview(String review, String reviewTitle, double rating) {

        reviewItem.setTitle(reviewTitle);
        reviewItem.setReview(review);
        reviewItem.setRating(rating);


        ProgressBar progressBar = binding.progressBar;
        progressBar.setVisibility(ProgressBar.VISIBLE);
        Runnable prgRun = new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    waitPrg();
                    final int prg = i;
                    progressBar.post(new Runnable() {

                        @Override
                        public void run() {
                            progressBar.setProgress(prg);
                        }
                    });
                }
            }
        };
        Thread prgThread = new Thread(prgRun);
        prgThread.start();

        reviewItem.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null){
                    Log.e(TAG, "Error while saving", e);
                    return;
                }
                Log.i(TAG, "Post was saved successfully");

            }
        });
        int reviewCount = currentUser.getInt("reviewCount");
        currentUser.put("reviewCount", reviewCount+1);
        if(reviewCount+1 == 5 || reviewCount+1 == 10 || reviewCount+1 ==20 || reviewCount+1 == 30 || reviewCount+1 == 40){
            displayDialog(reviewCount+1);
        }
        currentUser.saveInBackground();
        prgThread.interrupt();
        progressBar.setProgress(progressBar.getMax());
        progressBar.setVisibility(View.INVISIBLE);
        Log.i(TAG, "Success!");
        etReview.setText("");
    }

    private void displayDialog(int reviewCount){
        int beanID = R.drawable.ic_bean_start;
        String title="";
        String message="";
        if(reviewCount==40){
           beanID = R.drawable.ic_final_bean;
           title= "Bean Master";
           message= "Congrats on 40 reviews!";
        }
        else if (reviewCount==30){
            beanID = R.drawable.ic_bean_master;
            title="Bean Machine";
            message="Congrats on 30 reviews!";
        }
        else if(reviewCount==20){
            beanID = R.drawable.ic_regular_bean;
            title="Coffee Grinder";
            message= "Congrats on 20 reviews!";
        }else if(reviewCount==10){
            beanID= R.drawable.ic_bean_amateur;
            title="Cool Bean";
            message = "Congrats on 10 reviews!";
        }else if (reviewCount==5){
            beanID =R.drawable.ic_just_beand;
            title = "Just Beand";
            message = "Congrats on 5 reviews!";
        }
        //TODO: replace this with getContext once converted to fragement
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setIcon(beanID);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Yay!",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which){
                dialog.dismiss();
            }
        });

        AlertDialog alert=builder.create();
        alert.show();
    }



    private void goToCamera() {
        Intent i = new Intent(this, CameraActivity.class);
        i.putExtra("review", reviewItem.getObjectId());
        startActivityForResult(i,200);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //queryPhotos();
    }

    private void queryPhotos() {
        //query photos with review id
    }

    private void waitPrg() {
        SystemClock.sleep(10);
    }
}