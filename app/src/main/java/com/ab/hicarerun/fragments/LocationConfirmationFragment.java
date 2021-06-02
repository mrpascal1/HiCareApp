package com.ab.hicarerun.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.FragmentLocationConfirmationBinding;
import com.ab.hicarerun.handler.UserLocationClickHandler;

import java.util.Objects;

public class LocationConfirmationFragment extends BaseFragment implements UserLocationClickHandler {
    FragmentLocationConfirmationBinding mFragmentLocationConfirmationBinding;

    public static final int REQUEST_CODE_PERMISSIONS = 101;

    public LocationConfirmationFragment() {
        // Required empty public constructor
    }

    public static LocationConfirmationFragment newInstance() {
        LocationConfirmationFragment fragment = new LocationConfirmationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentLocationConfirmationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_location_confirmation, container, false);
        mFragmentLocationConfirmationBinding.setHandler(this);
        return mFragmentLocationConfirmationBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onTurnOnClicked(View view) {
        try {
            requestLocationPermission();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestLocationPermission() {

        boolean foreground = ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (foreground) {
            boolean background = ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;

            if (background) {
                handleLocationUpdates();
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, REQUEST_CODE_PERMISSIONS);
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION}, REQUEST_CODE_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {

            boolean foreground = false, background = false;

            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equalsIgnoreCase(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    //foreground permission allowed
                    if (grantResults[i] >= 0) {
                        foreground = true;
                        Objects.requireNonNull(getActivity()).getFragmentManager().popBackStack();
//                        Toast.makeText(getActivity(), "Foreground location permission allowed", Toast.LENGTH_SHORT).show();
                        continue;
                    } else {
//                        Toast.makeText(getActivity(), "Location Permission denied", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

                if (permissions[i].equalsIgnoreCase(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                    if (grantResults[i] >= 0) {
                        foreground = true;
                        background = true;
                    }

                }
            }

            if (foreground) {
                if (background) {
                    handleLocationUpdates();
                } else {
                    handleForegroundLocationUpdates();
                }
            }
        }
    }

    private void handleLocationUpdates() {
        //foreground and background
        Objects.requireNonNull(getActivity()).getFragmentManager().popBackStack();
    }

    private void handleForegroundLocationUpdates() {
        //handleForeground Location Updates
        Objects.requireNonNull(getActivity()).getFragmentManager().popBackStack();
    }


    @Override
    public void onNoThanksClicked(View view) {
        try {
            Objects.requireNonNull(getActivity()).finishAffinity();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}