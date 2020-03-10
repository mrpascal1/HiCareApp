package com.ab.hicarerun.activities;

import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.ab.hicarerun.BaseActivity;
import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.ActivityOnsiteTaskBinding;
import com.ab.hicarerun.fragments.OnSiteAccountFragment;
import com.ab.hicarerun.service.LocationManager;
import com.ab.hicarerun.service.listner.LocationManagerListner;
import com.ab.hicarerun.utils.GPSUtils;

import java.util.Objects;

public class OnSiteTaskActivity extends BaseActivity implements LocationManagerListner {
    ActivityOnsiteTaskBinding mActivityOnSiteTaskBinding;
    private Location mLocation;
    private LocationManagerListner mListner;
    private boolean isGPS = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityOnSiteTaskBinding = DataBindingUtil.setContentView(this, R.layout.activity_onsite_task);
        LocationManager.Builder builder = new LocationManager.Builder(this);
        builder.setLocationListner(this);
        builder.build();
        new GPSUtils(this).turnGPSOn(isGPSEnable -> {
            // turn on GPS
            if (isGPSEnable) {
                addFragment(OnSiteAccountFragment.newInstance(), "OnSiteTaskActivity - OnSiteAccountFragment");
            } else {
                isGPS = isGPSEnable;
            }
        });
        setSupportActionBar(mActivityOnSiteTaskBinding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GPSUtils.GPS_REQUEST) {
                isGPS = true; // flag maintain before get location
                addFragment(OnSiteAccountFragment.newInstance(), "OnSiteTaskActivity - OnSiteAccountFragment");
            }
        }
    }

    @Override
    public void onBackPressed() {
        try {
            getBack();
            super.onBackPressed();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getBack() {
        int fragment = getSupportFragmentManager().getBackStackEntryCount();
        Log.e("fragments", String.valueOf(fragment));
        if (fragment < 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                getBack();
                break;
        }
        return true;
    }

    @Override
    public void locationFetched(Location mLocation, Location oldLocation, String time, String locationProvider) {
        this.mLocation = mLocation;
        if (mListner != null) {
            mListner.locationFetched(mLocation, oldLocation, time, locationProvider);
        }
    }

    public Location getmLocation() {
        return mLocation;
    }
}