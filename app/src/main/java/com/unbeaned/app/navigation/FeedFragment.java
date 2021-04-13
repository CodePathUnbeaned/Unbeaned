package com.unbeaned.app.navigation;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.parse.ParseUser;
import com.unbeaned.app.R;
import com.unbeaned.app.adapters.PlaceFeedAdapter;
import com.unbeaned.app.databinding.FeedFragmentBinding;
import com.unbeaned.app.models.Place;
import com.unbeaned.app.utils.EndlessRecyclerViewScrollListener;
import com.unbeaned.app.utils.YelpClient;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.content.Context.LOCATION_SERVICE;

public class FeedFragment extends Fragment {
    public static final String TAG = "FeedFragment";
    FeedFragmentBinding binding;
    private double longitude, latitude;
    private RecyclerView rvPlaces;
    private EditText etSearch;
    private Button btnSearch;
    private PlaceFeedAdapter adapter;
    private List<Place> allPlaces;
    private LocationManager locationManager;
    private Snackbar snackbarEnableLocation;
    private RelativeLayout feedLayoutContainer;
    private String searchLocation = "current";
    private final int limit = 5;
    EndlessRecyclerViewScrollListener scrollListener;

    public FeedFragment() {
        //required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflate the layout for this view
        if (ParseUser.getCurrentUser() == null) {
            NavHostFragment.findNavController(this).navigate(R.id.splashFragment);
        }

        binding = DataBindingUtil.inflate(inflater, R.layout.feed_fragment, container, false);

        CoordinatorLayout mainCoordinatorLayout = getActivity().findViewById(R.id.mainCoordinatorLayout);

        snackbarEnableLocation = Snackbar.make(mainCoordinatorLayout, "Please enable GPS and Location Services", Snackbar.LENGTH_SHORT);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPlaces = binding.rvPlaces;
        etSearch = binding.etSearch;
        btnSearch = binding.btnSearch;
        feedLayoutContainer = binding.feedLayoutContainer;

        allPlaces = new ArrayList<>();
        adapter = new PlaceFeedAdapter(getContext(), allPlaces, this);

        if (TextUtils.isEmpty(etSearch.getText())) getLocation();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        rvPlaces.setAdapter(adapter);
        rvPlaces.setLayoutManager(layoutManager);

        scrollListener= new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.i(TAG, "onLoadMore "+page);
                searchBusinesses(searchLocation, page);
            }
        };
        rvPlaces.addOnScrollListener(scrollListener);

        //query API for places in yelp when search button is pressed
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(feedLayoutContainer.getWindowToken(), 0);
                searchLocation = etSearch.getText().toString();
                if (TextUtils.isEmpty(etSearch.getText())) {
                    searchLocation = "current";
                    getLocation();
//                    searchBusinesses("current");
                }
                searchBusinesses(searchLocation, 0);
            }
        });

        NavHostFragment.findNavController(this).addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                Log.i(TAG, "Destination ID: " + allPlaces);
            }
        });

    }

//    private void loadMoreData(String location, int page) {
//        Map<String, String> searchParameters = new HashMap<>();
//
//        if (location.equals("current")) {
//            searchParameters.put("longitude", String.valueOf(longitude));
//            searchParameters.put("latitude", String.valueOf(latitude));
//        } else {
//            searchParameters.put("location", location);
//        }
//
//        searchParameters.put("limit", String.valueOf(limit));
//        searchParameters.put("offset", String.valueOf(limit * page));
//
//        Request request = YelpClient.getBusinessBySearch(searchParameters);
//
//        OkHttpClient client = new OkHttpClient();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                Log.e(TAG, "Could not fetch data", e);
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                Log.i(TAG, "Success");
//                try (ResponseBody responseBody = response.body()) {
//                    if (!response.isSuccessful())
//                        throw new IOException("Unexpected code " + response + " " + response.message());
//
//                    String jsonData = responseBody.string();
//                    JSONObject jsonObject = new JSONObject(jsonData);
//                    JSONArray businessJsonArray = jsonObject.getJSONArray("businesses");
//                    Log.i(TAG, "JSONArray: " + businessJsonArray.toString());
//                    Log.i(TAG, "Size: " + businessJsonArray.length());
//
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                adapter.addAll(Place.fromJsonArray(businessJsonArray));
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//                } catch (JSONException e) {
//                    Log.e(TAG, "JSON exception", e);
//                }
//            }
//        });
//    }

    private void searchBusinesses(String location, int page) {
        Map<String, String> searchParameters = new HashMap<>();

        if (location.equals("current")) {
            searchParameters.put("longitude", String.valueOf(longitude));
            searchParameters.put("latitude", String.valueOf(latitude));
        } else {
            searchParameters.put("location", location);
        }

        searchParameters.put("limit", String.valueOf(limit));
        searchParameters.put("offset", String.valueOf(page * limit));

        Request request = YelpClient.getBusinessBySearch(searchParameters);

        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e(TAG, "Could not fetch data", e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.i(TAG, "Success");
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response + " " + response.message());

                    String jsonData = responseBody.string();

                    JSONObject jsonObject = new JSONObject(jsonData);
                    JSONArray businessJsonArray = jsonObject.getJSONArray("businesses");

                    Log.i(TAG, "JSONArray: " + businessJsonArray.toString());
                    Log.i(TAG, "Size: " + businessJsonArray.length());

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (page == 0) {
                                adapter.clear();
                            }
                            try {
                                adapter.addAll(Place.fromJsonArray(businessJsonArray));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    Log.e(TAG, "JSON exception", e);
                }
            }
        });
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            searchBusinesses("current", 0);
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            snackbarEnableLocation.show();
        }
    };

    private void getLocation() {
        LocationManager mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000,
                30, locationListener);

    }

}
