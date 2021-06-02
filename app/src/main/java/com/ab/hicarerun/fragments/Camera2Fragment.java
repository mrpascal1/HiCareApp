package com.ab.hicarerun.fragments;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.sql.Timestamp;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.ErrorCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Base64;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.FragmentCamera2Binding;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.utils.AppUtils;

import io.realm.RealmResults;

/**
 * Created by User on 5/29/2018.
 */

public class Camera2Fragment extends BaseFragment implements Callback,
        OnClickListener {
    FragmentCamera2Binding mFragmentCamera2Binding;
    public static final String ARGS_CAMERA = "ARGS_CAMERA";
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private int cameraId;
    private boolean flashmode = false;
    private int rotation;
    private String CameraPosition = "";


    public static Camera2Fragment newInstance() {
        return new Camera2Fragment();
    }

    public static Camera2Fragment newInstance(String cameraPosition) {
        Bundle args = new Bundle();
        args.putString(ARGS_CAMERA, cameraPosition);
        Camera2Fragment fragment = new Camera2Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            CameraPosition = getArguments().getString(ARGS_CAMERA, "BACK");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentCamera2Binding = DataBindingUtil.inflate(inflater, R.layout.fragment_camera2, container, false);
        return mFragmentCamera2Binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // camera surface view created
        if (CameraPosition.equals("FRONT"))
            cameraId = CameraInfo.CAMERA_FACING_FRONT;
        else
            cameraId = CameraInfo.CAMERA_FACING_BACK;

        surfaceHolder = mFragmentCamera2Binding.surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        mFragmentCamera2Binding.captureImage.setOnClickListener(this);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (CameraPosition.equals("FRONT")) {
            if (!openCamera(CameraInfo.CAMERA_FACING_FRONT)) {
                alertCameraDialog();
            }
        } else {
            if (!openCamera(CameraInfo.CAMERA_FACING_BACK)) {
                alertCameraDialog();
            }
        }

    }

    private boolean openCamera(int id) {
        boolean result = false;
        cameraId = id;
        releaseCamera();
        try {
            camera = Camera.open(cameraId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (camera != null) {
            try {
                setUpCamera(camera);
                camera.setErrorCallback(new ErrorCallback() {

                    @Override
                    public void onError(int error, Camera camera) {

                    }
                });
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
                result = true;
            } catch (IOException e) {
                e.printStackTrace();
                result = false;
                releaseCamera();
            }
        }
        return result;
    }

    private void setUpCamera(Camera c) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        int degree = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 0;
                break;
            case Surface.ROTATION_90:
                degree = 90;
                break;
            case Surface.ROTATION_180:
                degree = 180;
                break;
            case Surface.ROTATION_270:
                degree = 270;
                break;

            default:
                break;
        }

        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            // frontFacing
            rotation = (info.orientation + degree) % 330;
            rotation = (360 - rotation) % 360;
        } else {
            // Back-facing
            rotation = (info.orientation - degree + 360) % 360;
        }
        c.setDisplayOrientation(rotation);
        Parameters params = c.getParameters();

        List<String> focusModes = params.getSupportedFlashModes();
        if (focusModes != null) {
            if (focusModes
                    .contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                params.setFlashMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }
        }

        params.setRotation(rotation);
    }


    private void releaseCamera() {
        try {
            if (camera != null) {
                camera.setPreviewCallback(null);
                camera.setErrorCallback(null);
                camera.stopPreview();
                camera.release();
                camera = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error", e.toString());
            camera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.captureImage:
                takeImage();
                break;

            default:
                break;
        }
    }

    private void takeImage() {
        camera.takePicture(null, null, new PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                try {
                    // convert byte array into bitmap
                    Bitmap loadedImage = null;
                    Bitmap rotatedBitmap = null;
                    loadedImage = BitmapFactory.decodeByteArray(data, 0,
                            data.length);

                    // rotate Image
                    Matrix rotateMatrix = new Matrix();
                    rotateMatrix.postRotate(270);
                    rotatedBitmap = Bitmap.createBitmap(loadedImage, 0, 0,
                            loadedImage.getWidth(), loadedImage.getHeight(),
                            rotateMatrix, false);
                    ByteArrayOutputStream ostream = new ByteArrayOutputStream();
                    rotatedBitmap.compress(CompressFormat.JPEG, 30, ostream);
                    byte[] b = ostream.toByteArray();
                    String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

                    if(CameraPosition.equals("FRONT")){
                        Intent intent = new Intent("Onsite-Image");
                        intent.putExtra("base64", encodedImage);
                        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                    }else {
                        Intent intent = new Intent(AppUtils.CAMERA_SCREEN);
                        intent.putExtra("base64", encodedImage);
                        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                    }
                    releaseCamera();
                    getActivity().finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }


    private void alertCameraDialog() {
        AlertDialog.Builder dialog = createAlert(getActivity(),
                "Camera info", "error to open camera");
        dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });

        dialog.show();
    }

    private Builder createAlert(Context context, String title, String message) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(
                new ContextThemeWrapper(context,
                        android.R.style.Theme_Holo_Light_Dialog));
        dialog.setIcon(R.mipmap.logo);
        if (title != null)
            dialog.setTitle(title);
        else
            dialog.setTitle("Information");
        dialog.setMessage(message);
        dialog.setCancelable(false);
        return dialog;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseCamera();
    }
}