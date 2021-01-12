package com.ab.hicarerun.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.ab.hicarerun.BaseActivity;
import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.ActivityLoginBinding;
import com.ab.hicarerun.fragments.LocationConfirmationFragment;
import com.ab.hicarerun.fragments.LoginFragment;
import com.ab.hicarerun.fragments.NewLoginFragment;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.UpdateAppModel.UpdateData;
import com.ab.hicarerun.utils.AppUtils;
import com.ab.hicarerun.utils.GPSUtils;
//import com.ab.hicarerun.utils.LocaleHelper;
import com.ab.hicarerun.utils.LocaleHelper;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.testing.FakeAppUpdateManager;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class LoginActivity extends BaseActivity implements InstallStateUpdatedListener {
    ActivityLoginBinding mActivityLoginBinding;
    private static final int PERMISSION_REQUEST_CODE = 1000;
    AlertDialog alert;
    private String Version = "";
    private String Apk_URL = "";
    private String Apk_Type = "";
    private static final int UPDATE_REQ = 2000;
    private static final int HIGH_PRIORITY_UPDATE = 5;
    private static final int UPDATE_REQUEST_CODE = 11;
    private ProgressDialog progress;
    private boolean isGPS = false;
    private AppUpdateManager mAppUpdateManager;
    public static final int REQUEST_CODE_PERMISSIONS = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityLoginBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_login);

        progress = new ProgressDialog(this, R.style.TransparentProgressDialog);
        progress.setCancelable(false);
        new GPSUtils(this).turnGPSOn(isGPSEnable -> {
            // turn on GPS
            if (isGPSEnable) {
                requestPermission();
            } else {
                isGPS = isGPSEnable;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAppUpdateManager = AppUpdateManagerFactory.create(this);

        mAppUpdateManager.registerListener(this);

        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE /*AppUpdateType.IMMEDIATE*/)) {
                try {
                    mAppUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo, AppUpdateType.IMMEDIATE /*AppUpdateType.IMMEDIATE*/, this, UPDATE_REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }

            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
                popupSnackbarForCompleteUpdate();
            } else {
                Log.e("LoginActivity", "checkForAppUpdateAvailability: something else");
            }
        });
    }


    /* Displays the snackbar notification and call to action. */
    private void popupSnackbarForCompleteUpdate() {
        Snackbar snackbar =
                Snackbar.make(
                        findViewById(R.id.container),
                        "An update has just been downloaded.",
                        Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("RESTART", view -> mAppUpdateManager.completeUpdate());
        snackbar.setActionTextColor(
                getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, LocaleHelper.getLanguage(base)));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GPSUtils.GPS_REQUEST) {
                isGPS = true; // flag maintain before get location
                requestPermission();
            }
        } else if (resultCode == UPDATE_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Log.i("LoginActivity", "Update flow failed! Result code: " + resultCode);
                // If the update is cancelled or fails,
                // you can request to start the update again.
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        mAppUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(appUpdateInfo -> {
                    // If the update is downloaded but not installed,
                    // notify the user to complete the update.
                    if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                        popupSnackbarForCompleteUpdate();
                    }
                });
    }

    private void requestLocationPermission() {
        boolean foreground = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (foreground) {
            boolean background = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;
            if (background) {
                handleLocationUpdates();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, REQUEST_CODE_PERMISSIONS);
            }
        } else {
            ActivityCompat.requestPermissions(this,
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
//                        Toast.makeText(getApplicationContext(), "Foreground location permission allowed", Toast.LENGTH_SHORT).show();
                        continue;
                    } else {
                        Toast.makeText(getApplicationContext(), "Location Permission denied", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

                if (permissions[i].equalsIgnoreCase(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                    if (grantResults[i] >= 0) {
                        foreground = true;
                        background = true;
                    } else {
//                        Toast.makeText(getApplicationContext(), "Background location location permission denied", Toast.LENGTH_SHORT).show();
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
        addFragment(NewLoginFragment.newInstance(Version, Apk_URL, Apk_Type), "LoginTrealActivity-CreateRealFragment");

    }

    private void handleForegroundLocationUpdates() {
        addFragment(NewLoginFragment.newInstance(Version, Apk_URL, Apk_Type), "LoginTrealActivity-CreateRealFragment");
        //handleForeground Location Updates
//        Toast.makeText(getApplicationContext(), "Start foreground location updates", Toast.LENGTH_SHORT).show();
    }

    private void requestPermission() {
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                Dexter.withActivity(this)
                        .withPermissions(/*Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,*/
                                Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE/*, Manifest.permission.SYSTEM_ALERT_WINDOW*/)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                // check if all permissions are granted
                                if (report.areAllPermissionsGranted()) {
                                    getVersionFromApi();
//                                addFragment(LocationConfirmationFragment.newInstance(), "LoginTrealActivity-LocationConfirmationFragment");
//                                requestLocationPermission();
                                }
                                // check for permanent denial of any permission
                                if (report.isAnyPermissionPermanentlyDenied()) {
                                    // show alert dialog navigating to Settings
                                    showSettingsDialog();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions,
                                                                           PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        })
                        .withErrorListener(
                                error -> Toast.makeText(this, "Error occurred! ", Toast.LENGTH_SHORT)
                                        .show())
                        .onSameThread()
                        .check();
            } else {
                Dexter.withActivity(this)
                        .withPermissions(/*Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,*/
                                Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                // check if all permissions are granted
                                if (report.areAllPermissionsGranted()) {
                                    getVersionFromApi();
//                                addFragment(LocationConfirmationFragment.newInstance(), "LoginTrealActivity-LocationConfirmationFragment");
//                                requestLocationPermission();
                                }
                                // check for permanent denial of any permission
                                if (report.isAnyPermissionPermanentlyDenied()) {
                                    // show alert dialog navigating to Settings
                                    showSettingsDialog();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions,
                                                                           PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        })
                        .withErrorListener(
                                error -> Toast.makeText(this, "Error occurred! ", Toast.LENGTH_SHORT)
                                        .show())
                        .onSameThread()
                        .check();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showSettingsDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Need Permissions");
            builder.setMessage(
                    "HiCare Run needs permission to use these features. You can grant them in app settings.");
            builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
                dialog.cancel();
                openSettings();
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // navigating user to app settings
    private void openSettings() {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", this.getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, 101);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getVersionFromApi() {
        progress.show();
//        requestPermission();
        try {
            NetworkCallController controller = new NetworkCallController();
            controller.setListner(new NetworkResponseListner() {
                @Override
                public void onResponse(int requestCode, Object response) {
                    progress.dismiss();
                    UpdateData data = (UpdateData) response;
                    Version = data.getVersion();
                    Apk_URL = data.getApkurl();
                    Apk_Type = data.getApktype();
                    addFragment(NewLoginFragment.newInstance(Version, Apk_URL, Apk_Type), "LoginTrealActivity-CreateRealFragment");
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                        boolean foreground = ActivityCompat.checkSelfPermission(LoginActivity.this,
                                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
                        if (foreground) {
                            boolean background = ActivityCompat.checkSelfPermission(LoginActivity.this,
                                    Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;
                            if (background) {
                                handleLocationUpdates();
                            } else {
                                addFragment(LocationConfirmationFragment.newInstance(), "LoginTrealActivity-LocationConfirmationFragment");
                            }
                        } else {
                            addFragment(LocationConfirmationFragment.newInstance(), "LoginTrealActivity-LocationConfirmationFragment");
                        }
                    } else {

                    }

                }

                @Override
                public void onFailure(int requestCode) {
                    progress.dismiss();
                }
            });
            controller.getUpdateApp(UPDATE_REQ);
        } catch (Exception e) {
            e.printStackTrace();
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
        finishAffinity();
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

    public void askPermissions() {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                        != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                        != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED

                ) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            || ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION)
                            || ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                            || ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.READ_PHONE_STATE)
                            || ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            || ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            || ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.CAMERA)
                            || ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.RECEIVE_SMS)
                            || ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.CALL_PHONE)

                    ) {

                        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage(
                                "Please allow all permissions in App Settings for additional functionality.")
                                .setCancelable(false)
                                .setPositiveButton("Allow", (dialog, id) -> Toast.makeText(LoginActivity.this, "Welcome", Toast.LENGTH_SHORT).show())
                                .setNegativeButton("Deny", (dialog, id) -> {
                                    // Permission denied
                                });
                        alert = builder.create();
                        alert.show();
                    } else {
                        ActivityCompat.requestPermissions(this, new String[]{
                                Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.RECEIVE_SMS, Manifest.permission.INTERNET,
                                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION, Manifest.permission.CALL_PHONE
                        }, PERMISSION_REQUEST_CODE);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStateUpdate(InstallState state) {
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
            popupSnackbarForCompleteUpdate();
        } else if (state.installStatus() == InstallStatus.INSTALLED) {
            if (mAppUpdateManager != null) {
                mAppUpdateManager.unregisterListener(this);
            }

        } else {
            Log.i("LoginActivity", "InstallStateUpdatedListener: state: " + state.installStatus());
        }
    }
}
