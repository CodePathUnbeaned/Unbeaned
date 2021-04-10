package com.unbeaned.app.navigation;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.unbeaned.app.R;
import com.unbeaned.app.adapters.PlaceFeedAdapter;
import com.unbeaned.app.databinding.SearchFragmentBinding;
import com.unbeaned.app.models.Place;
import com.unbeaned.app.models.PlaceReg;
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

public class SearchFragment extends Fragment {
    public static final String TAG = "SearchFragment";
    SearchFragmentBinding binding;
    private RecyclerView rvPlaces;
    private EditText etSearch;
    private Button btnSearch;
    private PlaceFeedAdapter adapter;
    private List<Place> allPlaces;
    private double longitude, latitude;

    //Grab from twitter app
    //EndlessRecyclerViewScrollListener scrollListener;

    public SearchFragment() {
        //required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflate the layout for this view
        binding = DataBindingUtil.inflate(inflater, R.layout.search_fragment, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        rvPlaces = binding.rvPlaces;
        etSearch = binding.etSearch;
        btnSearch = binding.btnSearch;
        allPlaces = new ArrayList<>();
        adapter = new PlaceFeedAdapter(getContext(), allPlaces, this);

        final LocationListener mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                Log.i(TAG, "Location: " + location);
                longitude = location.getLongitude();
                latitude = location.getLatitude();
            }
        };

        rvPlaces.setAdapter(adapter);
        rvPlaces.setLayoutManager(new LinearLayoutManager(getContext()));
        //query API for places in yelp when search button is pressed
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBusinesses(etSearch.getText().toString());
            }
        });

        NavHostFragment.findNavController(this).addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                Log.i(TAG, "Destination ID: " + allPlaces);
            }
        });

    }

    private void searchBusinesses(String placeName) {
        Map<String, String> searchParameters = new HashMap<>();
        searchParameters.put("term", placeName);
        searchParameters.put("longitude", String.valueOf(longitude));
        searchParameters.put("latitude", String.valueOf(latitude));
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
}
