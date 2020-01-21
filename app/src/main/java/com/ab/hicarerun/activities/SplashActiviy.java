package com.ab.hicarerun.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.ActivitySplashActiviyBinding;
import com.ab.hicarerun.fragments.LoginFragment;
import com.ab.hicarerun.service.LocationManager;
import com.ab.hicarerun.service.ServiceLocationSend;
import com.ab.hicarerun.service.listner.LocationManagerListner;
import com.ab.hicarerun.utils.AppUtils;
import com.ab.hicarerun.utils.HandShakeReceiver;
import com.ab.hicarerun.utils.RuntimePermissionsActivity;
import com.ab.hicarerun.utils.SharedPreferencesUtility;
import com.splunk.mint.Mint;
import com.squareup.picasso.Picasso;

public class SplashActiviy extends AppCompatActivity implements LocationManagerListner {
    ActivitySplashActiviyBinding mActivitySplashBinding;
    private Location mLocation;
    private LocationManagerListner mListner;
    private android.location.LocationManager locationManager;
    private static int SPLASH_TIME_OUT = 3000;
    private PendingIntent pendingIntent = null;
    private AlarmManager mAlarmManager = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivitySplashBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_splash_activiy);
        mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        LocationManager.Builder builder = new LocationManager.Builder(this);
        builder.setLocationListner(this);
        builder.build();
        Mint.initAndStartSession(this.getApplication(), "5623ed44");
        Picasso.get().load(R.mipmap.splash).into(mActivitySplashBinding.imgSplash);
        splashScreen();
        PackageInfo pInfo = null;

        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String mobileVersion = pInfo.versionName;
            mActivitySplashBinding.txtVersion.setText("V " + mobileVersion);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash);
        mActivitySplashBinding.imgSplash.startAnimation(animation);
        SharedPreferencesUtility.savePrefBoolean(SplashActiviy.this, SharedPreferencesUtility.PREF_REFRESH, false);
    }


    void splashScreen() {
        try {
            new Handler().postDelayed(() -> {
                locationManager =
                        (android.location.LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
                        && locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)) {
                    if (SharedPreferencesUtility.getPrefBoolean(SplashActiviy.this, SharedPreferencesUtility.IS_USER_LOGIN)) {
                        if (SharedPreferencesUtility.getPrefString(SplashActiviy.this, SharedPreferencesUtility.PREF_LOGOUT) != null) {
                            String PreviousLoginDate = SharedPreferencesUtility.getPrefString(SplashActiviy.this, SharedPreferencesUtility.PREF_LOGOUT);
                            String ComparePreviousLogin = AppUtils.compareLoginDates(PreviousLoginDate, AppUtils.currentDate());
                            Log.i("LoginDates", AppUtils.compareLoginDates(PreviousLoginDate, AppUtils.currentDate()));
                            Log.i("CurrentDate", AppUtils.currentDate());
                            if (ComparePreviousLogin.equals("equal")) {
                                startActivity(new Intent(SplashActiviy.this, HomeActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
//                                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                                finish();
                            } else {
                                Intent myIntent = new Intent(SplashActiviy.this, HandShakeReceiver.class);
                                pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                                        0, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                                mAlarmManager.cancel(pendingIntent);
                                getApplicationContext().stopService(new Intent(getApplicationContext(), ServiceLocationSend.class));
                                startActivity(new Intent(SplashActiviy.this, LoginActivity.class));
//                                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                                finish();
                            }
                        } else {
                            Intent myIntent = new Intent(SplashActiviy.this, HandShakeReceiver.class);
                            pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                                    0, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                            pendingIntent.cancel();
                            mAlarmManager.cancel(pendingIntent);
                            getApplicationContext().stopService(new Intent(SplashActiviy.this, ServiceLocationSend.class));
                            startActivity(new Intent(SplashActiviy.this, LoginActivity.class));
//                                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                            finish();
                        }
                    } else {
                        getApplicationContext().stopService(new Intent(SplashActiviy.this, ServiceLocationSend.class));
                        startActivity(new Intent(SplashActiviy.this, LoginActivity.class));
//                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        finish();
                    }
                } else {
                    try {
                        AppUtils.statusCheck(SplashActiviy.this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, SPLASH_TIME_OUT);
        } catch (Exception e) {
            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
            AppUtils.sendErrorLogs(e.toString(), getClass().getSimpleName(), "splashScreen", lineNo, "", DeviceName);
        }
    }

    @Override
    public void locationFetched(Location mLocation, Location oldLocation, String time,
                                String locationProvider) {
        this.mLocation = mLocation;
        if (mListner != null) {
            mListner.locationFetched(mLocation, oldLocation, time, locationProvider);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
