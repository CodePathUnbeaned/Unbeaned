package com.unbeaned.app.navigation.review;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraInfoUnavailableException;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.extensions.HdrImageCaptureExtender;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.common.util.concurrent.ListenableFuture;
import com.unbeaned.app.R;
import com.unbeaned.app.databinding.FragmentCameraBinding;
import com.unbeaned.app.models.BitmapList;
import com.unbeaned.app.models.Place;
import com.unbeaned.app.models.Review;
import com.unbeaned.app.utils.ImageUtils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CameraFragment extends Fragment {

    FragmentCameraBinding binding;
    private static final String TAG = "CameraFragment";
    Button btnCameraCapture;
    PreviewView cameraPreviewView;
    TextView tvDoneCapture;
    private final int REQUEST_CODE_PERMISSIONS = 1001;
    private final String[] REQUIRED_PERMISSIONS = new String[] {"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    Snackbar snackbarPermissionDenied;
    private Executor executor;
    private BitmapList bitmapList;
    private Place place;
    private Review review;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_camera, container, false);

        CoordinatorLayout mainCoordinatorLayout = getActivity().findViewById(R.id.mainCoordinatorLayout);

        snackbarPermissionDenied = Snackbar.make(mainCoordinatorLayout, "Permissions not granted.", Snackbar.LENGTH_SHORT);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnCameraCapture = binding.btnCameraCapture;
        cameraPreviewView = binding.cameraPreviewView;
        tvDoneCapture = binding.tvDoneCapture;

        if (getArguments() != null) {
            bitmapList = getArguments().getParcelable("images");
            place = getArguments().getParcelable("place");
            review = getArguments().getParcelable("review");
        }

        executor = Executors.newSingleThreadExecutor();

        if (allPermissionsGranted()) {
            startCamera();
        }
        else {
            ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }

    private void startCamera() {
        final ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());

        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindPreview(cameraProvider);
                } catch (ExecutionException | InterruptedException e) {
                    // No errors to be handled for this Future
                    // This should never be reached
                }
            }
        }, ContextCompat.getMainExecutor(getContext()));
    }

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                snackbarPermissionDenied.show();
                NavHostFragment.findNavController(CameraFragment.this).navigateUp();
            }
        }
    }

    private void bindPreview(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();

        Preview preview = new Preview.Builder().build();

        int lensFacing;

        if (hasBackFacing(cameraProvider)) {
            lensFacing = CameraSelector.LENS_FACING_BACK;
        }
        else {
            lensFacing = CameraSelector.LENS_FACING_FRONT;
        }

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build();

        ImageCapture.Builder builder = new ImageCapture.Builder();

        //Vendor-Extensions (The CameraX extensions dependency in build.gradle)
        HdrImageCaptureExtender hdrImageCaptureExtender = HdrImageCaptureExtender.create(builder);

        // Query if extension is available (optional).
        if (hdrImageCaptureExtender.isExtensionAvailable(cameraSelector)) {
            // Enable the extension if available.
            hdrImageCaptureExtender.enableExtension(cameraSelector);
        }

        final ImageCapture imageCapture = builder
                .setTargetRotation(preview.getTargetRotation())
                .build();

        Preview.SurfaceProvider surfaceProvider = cameraPreviewView.createSurfaceProvider();

        Log.i(TAG, "Surface Provider: " + surfaceProvider);

        preview.setSurfaceProvider(surfaceProvider);

        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)getActivity(), cameraSelector, preview, imageCapture);

        Log.i(TAG, "BitmapList: " + bitmapList);

        btnCameraCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageCapture.takePicture(executor, new ImageCapture.OnImageCapturedCallback() {
                    @Override
                    public void onCaptureSuccess(@NonNull ImageProxy image) {
                        bitmapList.addImage(ImageUtils.getBitmap(image));
                        Log.i(TAG, "Capture Success: " + bitmapList.getImages());
                        image.close();

                        if (bitmapList.getImages().size() > 0) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvDoneCapture.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        super.onError(exception);
                    }
                });
            }
        });

        tvDoneCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraFragmentDirections.ActionCameraFragmentToComposeReviewFragment action = CameraFragmentDirections.actionCameraFragmentToComposeReviewFragment(place, review, bitmapList);
                NavHostFragment.findNavController(CameraFragment.this).navigate(action);
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                NavHostFragment.findNavController(CameraFragment.this).navigateUp();
            }
        };

        getActivity().getOnBackPressedDispatcher().addCallback(callback);

    }

    private boolean hasBackFacing(ProcessCameraProvider cameraProvider) {
        try {
            cameraProvider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA);
        } catch (CameraInfoUnavailableException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean hasFrontFacing(ProcessCameraProvider cameraProvider) {
        try {
            cameraProvider.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA);
        } catch (CameraInfoUnavailableException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}