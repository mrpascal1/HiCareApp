package com.ab.hicarerun.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.ab.hicarerun.BaseActivity;
import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.ActivityLoginBinding;
import com.ab.hicarerun.fragments.NewLoginFragment;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.UpdateAppModel.UpdateData;
import com.ab.hicarerun.utils.AppUtils;

public class LoginActivity extends BaseActivity {
    ActivityLoginBinding mActivityLoginBinding;
    private static final int PERMISSION_REQUEST_CODE = 1000;
    AlertDialog alert;
    private String Version = "";
    private String Apk_URL = "";
    private String Apk_Type = "";
    private static final int UPDATE_REQ = 2000;
    private ProgressDialog progress;

    @Override
    protected void onResume() {
        super.onResume();
        try {
            AppUtils.statusCheck(LoginActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityLoginBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_login);
        progress = new ProgressDialog(this, R.style.TransparentProgressDialog);
        progress.setCancelable(false);
        progress.show();
        askPermissions();
        getVersionFromApi();
//        addFragment(NewLoginFragment.newInstance(Version, Apk_URL, Apk_Type), "LoginTrealActivity-CreateRealFragment");
    }


    private void getVersionFromApi() {
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
//        if (fragment < 1) {
//            finish();
//        } else {
//            getFragmentManager().popBackStack();
//        }
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

}
