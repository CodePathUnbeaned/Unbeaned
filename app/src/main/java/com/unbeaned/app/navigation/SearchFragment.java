package com.unbeaned.app.navigation;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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
import com.unbeaned.app.databinding.SearchFragmentBinding;
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

public class SearchFragment extends Fragment {
    public static final String TAG = "SearchFragment";
    SearchFragmentBinding binding;
    private RecyclerView rvPlaces;
    private EditText etSearch;
    private Button btnSearch;
    private PlaceFeedAdapter adapter;
    private Snackbar snackbarEnableLocation;
    private List<Place> allPlaces;
    private RelativeLayout searchLayoutContainer;
    private double longitude, latitude;
    private final int limit = 5;
    EndlessRecyclerViewScrollListener scrollListener;
    String lookup;

    public SearchFragment() {
        //required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflate the layout for this view
        if (ParseUser.getCurrentUser() == null) {
            NavHostFragment.findNavController(this).navigate(R.id.splashFragment);
        }

        binding = DataBindingUtil.inflate(inflater, R.layout.search_fragment, container, false);

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
        allPlaces = new ArrayList<>();
        searchLayoutContainer = binding.searchLayoutContainer;
        adapter = new PlaceFeedAdapter(getContext(), allPlaces, this);



        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());



        rvPlaces.setAdapter(adapter);
        rvPlaces.setLayoutManager(layoutManager);

        scrollListener= new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.i(TAG, "onLoadMore "+page);
                loadMoreData(lookup,page);
            }
        };
        rvPlaces.addOnScrollListener(scrollListener);
        //query API for places in yelp when search button is pressed
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchLayoutContainer.getWindowToken(), 0);
                lookup = etSearch.getText().toString();
                getLocation();
                searchBusinesses(lookup);
            }
        });

        NavHostFragment.findNavController(this).addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                Log.i(TAG, "Destination ID: " + allPlaces);
            }
        });

    }

    private void loadMoreData(String lookup, int page) {
        Map<String, String> searchParameters = new HashMap<>();
        searchParameters.put("term", lookup);
        searchParameters.put("longitude", String.valueOf(longitude));
        searchParameters.put("latitude", String.valueOf(latitude));
        searchParameters.put("limit", String.valueOf(limit));
        searchParameters.put("offset", String.valueOf(limit * page));

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

    private void searchBusinesses(String lookup) {
        Map<String, String> searchParameters = new HashMap<>();
        searchParameters.put("term", lookup);
        searchParameters.put("longitude", String.valueOf(longitude));
        searchParameters.put("latitude", String.valueOf(latitude));
        searchParameters.put("limit", String.valueOf(limit));
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
                        throw new IOException("Unexpected code " + response);

                    String jsonData = responseBody.string();
                    JSONObject jsonObject = new JSONObject(jsonData);
                    JSONArray businessJsonArray = jsonObject.getJSONArray("businesses");
                    Log.i(TAG, "JSONArray: "+businessJsonArray.toString());

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.clear();
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
            loadMoreData(lookup, 0);
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
