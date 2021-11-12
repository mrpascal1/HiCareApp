package com.ab.hicarerun.fragments.tms;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ab.hicarerun.activities.Camera2Activity;
import com.ab.hicarerun.utils.AppUtils;
import com.google.android.material.snackbar.Snackbar;

public class TmsUtils {

    static boolean mPermissions;

    static void startCamera2(Context context) {
//        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.container, Camera2Fragment.newInstance(), getString(R.string.fragment_camera2));
//        transaction.commit();
        Intent intent = new Intent(context, Camera2Activity.class);
        intent.putExtra(AppUtils.CAMERA_ORIENTATION, "BACK");
        context.startActivity(intent);
    }

    private static void showSnackBar(Activity activity, final String text, final int length) {
        View view = activity.findViewById(android.R.id.content).getRootView();
        Snackbar.make(view, text, length).show();
    }

    public static void verifyPermissions(Context context, int REQUEST_CODE) {
        Log.d("TAG", "verifyPermissions: asking user for permissions.");
        String[] permissions = {
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};
        if (ContextCompat.checkSelfPermission(context.getApplicationContext(),
                permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context.getApplicationContext(),
                permissions[1]) == PackageManager.PERMISSION_GRANTED) {
            mPermissions = true;
            init(context, REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(
                    (Activity) context,
                    permissions,
                    REQUEST_CODE
            );
        }
    }


    private static boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    static void init(Context context, int REQUEST_CODE) {
        if (mPermissions) {
            if (checkCameraHardware(context)) {
                startCamera2(context);
            } else {
                showSnackBar((Activity) context, "You need a camera to use this application", Snackbar.LENGTH_INDEFINITE);
            }
        } else {
            verifyPermissions(context, REQUEST_CODE);
        }
    }
}
