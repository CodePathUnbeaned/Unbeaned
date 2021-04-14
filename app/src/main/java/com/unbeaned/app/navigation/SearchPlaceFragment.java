package com.unbeaned.app.navigation;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.android.material.snackbar.Snackbar;
import com.unbeaned.app.R;
import com.unbeaned.app.databinding.FragmentSearchPlaceBinding;

public class SearchPlaceFragment extends Fragment {

    FragmentSearchPlaceBinding binding;
    EditText etSearchPlace;
    Button btnSearch;
    Button btnCurrentLocation;
    RelativeLayout searchPlaceLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_place, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etSearchPlace = binding.etSearchPlace;
        btnSearch = binding.btnSearch;
        btnCurrentLocation = binding.btnCurrentLocation;
        searchPlaceLayout = binding.searchPlaceLayout;

        if (getArguments() != null) {
            String location = getArguments().getString("location");

            if (!location.equals("current")) {
                etSearchPlace.setText(location);
            }

        }

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchPlaceLayout.getWindowToken(), 0);

                SearchPlaceFragmentDirections.ActionSearchPlaceFragmentToFeedFragment action = SearchPlaceFragmentDirections.actionSearchPlaceFragmentToFeedFragment();

                if (!TextUtils.isEmpty(etSearchPlace.getText().toString())) {
                    action.setLocation(etSearchPlace.getText().toString());
                }

                NavHostFragment.findNavController(SearchPlaceFragment.this).navigate(action);

            }
        });

        btnCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchPlaceFragmentDirections.ActionSearchPlaceFragmentToFeedFragment action = SearchPlaceFragmentDirections.actionSearchPlaceFragmentToFeedFragment();
                NavHostFragment.findNavController(SearchPlaceFragment.this).navigate(action);
            }
        });

    }
}