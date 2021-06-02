package com.ab.hicarerun.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ab.hicarerun.BaseActivity;
import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.ActivityCamera2Binding;
import com.ab.hicarerun.fragments.Camera2Fragment;
import com.ab.hicarerun.handler.CameraListener;
import com.ab.hicarerun.utils.AppUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class Camera2Activity extends BaseActivity {
    ActivityCamera2Binding mActivityCamera2Binding;
    private static final String TAG = "Camera2Activity";
    private static final int REQUEST_CODE = 1234;
    public static String CAMERA_POSITION;

    //widgets

    //vars
    private boolean mPermissions;
    public String mCameraOrientation = "none"; // Front-facing or back-facing


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityCamera2Binding =
                DataBindingUtil.setContentView(this, R.layout.activity_camera2);
        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.transparentGrey));
        }
        CAMERA_POSITION = Objects.requireNonNull(getIntent().getExtras()).getString(AppUtils.CAMERA_ORIENTATION);
        init();
    }

    private void startCamera2(){
        addFragment(Camera2Fragment.newInstance(CAMERA_POSITION),"Camera2Activity - Camera2Fragment");
    }

    private void init(){
        if(mPermissions){
            if(checkCameraHardware(this)){
                // Open the Camera
                startCamera2();
            }
            else{
                showSnackBar("You need a camera to use this application", Snackbar.LENGTH_INDEFINITE);
            }
        }
        else{
            verifyPermissions();
        }
    }

    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        // this device has a camera
        // no camera on this device
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public void verifyPermissions(){
        Log.d(TAG, "verifyPermissions: asking user for permissions.");
        String[] permissions = {
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[0] ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[1] ) == PackageManager.PERMISSION_GRANTED) {
            mPermissions = true;
            init();
        } else {
            ActivityCompat.requestPermissions(
                    Camera2Activity.this,
                    permissions,
                    REQUEST_CODE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE){
            if(mPermissions){
                init();
            }
            else{
                verifyPermissions();
            }
        }
    }


    private void showSnackBar(final String text, final int length) {
        View view = this.findViewById(android.R.id.content).getRootView();
        Snackbar.make(view, text, length).show();
    }


}








