package com.ab.hicarerun.fragments;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.activities.HomeActivity;
import com.ab.hicarerun.activities.StartVideoActivity;
import com.ab.hicarerun.activities.VerifyOtpActivity;
import com.ab.hicarerun.databinding.FragmentFaceRecognizationBinding;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.AttendanceModel.AttendanceDetail;
import com.ab.hicarerun.network.models.AttendanceModel.AttendanceRequest;
import com.ab.hicarerun.network.models.AttendanceModel.ProfilePicRequest;
import com.ab.hicarerun.network.models.HandShakeModel.ContinueHandShakeResponse;
import com.ab.hicarerun.network.models.HandShakeModel.HandShakeResponse;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.network.models.SelfAssessModel.SelfAssessmentResponse;
import com.ab.hicarerun.utils.AppUtils;
import com.ab.hicarerun.utils.SharedPreferencesUtility;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import io.realm.RealmResults;

import static android.content.Context.MODE_PRIVATE;

public class FaceRecognizationFragment extends BaseFragment implements SurfaceHolder.Callback, NetworkResponseListner {
    FragmentFaceRecognizationBinding mFragmentFaceRecognizationBinding;

    private SurfaceHolder surfaceHolder;
    SurfaceTexture surfceTexture;
    private Camera camera;
    private int cameraId;
    private static final String COVID_CHECK = "COVID_CHECK";
    private static final String ARG_ATTENDANCE = "ARG_ATTENDANCE";
    private static final String ARG_USER = "ARG_USER";
    private static final String ARG_URL = "ARG_URL";
    private boolean isAttendance = false;
    private int rotation;
    private String encodedImage = "";
    private static final int CAM_REQ = 1000;
    private static final int ATTENDANCE_REQ = 3000;
    private static final int HANDSHAKE_REQUEST = 2000;
    private String username = "";
    private int Days = 0;
    private int PresentDays = 0;
    private int AbsentDays = 0;
    private int LateDays = 0;
    private String uri = "";


    public FaceRecognizationFragment() {
        // Required empty public constructor
    }

//    @Override
//    public void onSaveInstanceState(Bundle oldInstanceState)
//    {
//        super.onSaveInstanceState(oldInstanceState);
//        oldInstanceState.clear();
//    }

    public static FaceRecognizationFragment newInstance(boolean isAttendance, String username, String videoUrl) {
        Bundle args = new Bundle();
        args.putBoolean(ARG_ATTENDANCE, isAttendance);
        args.putString(ARG_USER, username);
        args.putString(ARG_URL, videoUrl);
        FaceRecognizationFragment fragment = new FaceRecognizationFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isAttendance = getArguments().getBoolean(ARG_ATTENDANCE);
            username = getArguments().getString(ARG_USER);
            uri = getArguments().getString(ARG_URL);
        }

    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentFaceRecognizationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_face_recognization, container, false);
        surfaceHolder = mFragmentFaceRecognizationBinding.surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        if (isAttendance) {
            mFragmentFaceRecognizationBinding.txtReason.setText(getResources().getString(R.string.please_upload_your_photo_to_mark_attendance_face));
            LinearLayout toolbar = Objects.requireNonNull(getActivity()).findViewById(R.id.toolbar);
            LinearLayout tool = getActivity().findViewById(R.id.customToolbar);
            RelativeLayout relBottom = getActivity().findViewById(R.id.relBottom);
            RelativeLayout relCoin = getActivity().findViewById(R.id.relCoin);
            toolbar.setVisibility(View.GONE);
            tool.setVisibility(View.GONE);
            relBottom.setVisibility(View.GONE);
            relCoin.setVisibility(View.GONE);
            DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawer);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else {
            if ((VerifyOtpActivity) getActivity() != null) {
                mFragmentFaceRecognizationBinding.txtReason.setText(getResources().getString(R.string.please_upload_your_photo_to_complete_the_registration_face));
                Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
                toolbar.setVisibility(View.GONE);
            }
        }

        return mFragmentFaceRecognizationBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        mFragmentFaceRecognizationBinding.captureImage.setOnClickListener(v -> takeImage());
    }

    private void getAttendanceDetails() {
        try {
            RealmResults<LoginResponse> LoginRealmModels =
                    BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                assert LoginRealmModels.get(0) != null;
                String resourceId = LoginRealmModels.get(0).getUserID();
                NetworkCallController controller = new NetworkCallController(this);
                controller.setListner(new NetworkResponseListner() {
                    @Override
                    public void onResponse(int requestCode, Object response) {
                        AttendanceDetail data = (AttendanceDetail) response;
                        LateDays = data.getTotalDaysLateCome();
                        Days = data.getTotalNoOfDays();
                        PresentDays = data.getTotalNoOfDaysPresent();
                        AbsentDays = Days - PresentDays;
                        getAttendanceSheetDialog();
                    }

                    @Override
                    public void onFailure(int requestCode) {

                    }
                });
                controller.getAttendanceDetail(ATTENDANCE_REQ, resourceId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!openCamera(Camera.CameraInfo.CAMERA_FACING_FRONT)) {
            alertCameraDialog();
        }
    }

    private void alertCameraDialog() {
        AlertDialog.Builder dialog = createAlert(getActivity(),
                "Camera info", getResources().getString(R.string.unable_to_extract_camera));
        dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private AlertDialog.Builder createAlert(Activity activity, String title, String message) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(
                new ContextThemeWrapper(activity,
                        android.R.style.Theme_Holo_Light_Dialog));
        dialog.setIcon(R.drawable.ic_mapicon);
        if (title != null)
            dialog.setTitle(title);
        else
            dialog.setTitle("Information");
        dialog.setMessage(message);
        dialog.setCancelable(false);
        return dialog;
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
                camera.setErrorCallback((error, camera) -> {
                });
                surfceTexture = new SurfaceTexture(MODE_PRIVATE);
                camera.setPreviewTexture(surfceTexture);
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
            RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                assert mLoginRealmModels.get(0) != null;
                String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                AppUtils.sendErrorLogs(e.getMessage(), getClass().getSimpleName(), "releaseCamera", lineNo, userName, DeviceName);
            }
        }
    }

    private void setUpCamera(Camera c) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        rotation = Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getRotation();
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
        Camera.Parameters params = c.getParameters();

        List<String> focusModes = params.getSupportedFlashModes();
        if (focusModes != null) {
            if (focusModes
                    .contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                params.setFlashMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }
        }

        params.setRotation(rotation);
    }


    private void takeImage() {
        try {
            camera.takePicture(null, null, (data, camera) -> {
                try {
                    // convert byte array into bitmap
                    Bitmap loadedImage = BitmapFactory.decodeByteArray(data, 0,
                            data.length);
                    // rotate Image
                    Matrix rotateMatrix = new Matrix();
                    rotateMatrix.postRotate(270);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(loadedImage, 0,
                            0, loadedImage.getWidth(), loadedImage.getHeight(),
                            rotateMatrix, false);
                    Bitmap convertedImage = getResizedBitmap(rotatedBitmap, 500);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    convertedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] b = baos.toByteArray();
                    encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

                    if (isAttendance) {
                        if ((HomeActivity) getActivity() != null) {
                            RealmResults<LoginResponse> LoginRealmModels =
                                    BaseApplication.getRealm().where(LoginResponse.class).findAll();
                            if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                                String BatteryStatistics = String.valueOf(AppUtils.getMyBatteryLevel(getActivity()));
                                AttendanceRequest request = AppUtils.getDeviceInfo(getActivity(), encodedImage, BatteryStatistics, false);
                                NetworkCallController controller = new NetworkCallController(FaceRecognizationFragment.this);
                                controller.setListner(new NetworkResponseListner<SelfAssessmentResponse>() {
                                    @Override
                                    public void onResponse(int requestCode, SelfAssessmentResponse data) {
                                        SelfAssessmentResponse response = (SelfAssessmentResponse) data;
                                        if (response.getIsSuccess()) {
                                            getAttendanceDetails();
                                            Toasty.success((HomeActivity) getActivity(), getResources().getString(R.string.attendance_marked_successfully_face), Toast.LENGTH_SHORT).show();
                                            SharedPreferencesUtility.savePrefBoolean((HomeActivity) getActivity(), SharedPreferencesUtility.PREF_SHOW_NPS, true);
                                            startActivity(new Intent((HomeActivity) getActivity(), HomeActivity.class));
                                            Intent intent = new Intent(COVID_CHECK);
                                            intent.putExtra("message", "recieved");
                                            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                                            releaseCamera();
                                        } else {
                                            getErrorDialog("Attendance Failed", response.getErrorMessage());
                                        }
                                    }

                                    @Override
                                    public void onFailure(int requestCode) {
                                    }
                                });
                                controller.getTechAttendance(ATTENDANCE_REQ, request);
                            }
                        }

                    } else {
                        if ((VerifyOtpActivity) getActivity() != null) {
                            RealmResults<LoginResponse> LoginRealmModels =
                                    BaseApplication.getRealm().where(LoginResponse.class).findAll();
                            assert LoginRealmModels.get(0) != null;
                            String resourceId = LoginRealmModels.get(0).getUserID();
                            if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                                NetworkCallController controller = new NetworkCallController(FaceRecognizationFragment.this);
                                ProfilePicRequest request = new ProfilePicRequest();
                                request.setProfilePic(encodedImage);
                                request.setResourceId(resourceId);

                                controller.setListner(new NetworkResponseListner() {
                                    @Override
                                    public void onResponse(int requestCode, Object data) {
                                        HandShakeResponse response = (HandShakeResponse) data;
                                        if (response.getSuccess()) {
//                                                AppUtils.getHandShakeCall(username, getActivity());
                                            if (SharedPreferencesUtility.getPrefBoolean(Objects.requireNonNull(getActivity()), SharedPreferencesUtility.IS_SKIP_VIDEO)) {
                                                AppUtils.getHandShakeCall(username, getActivity());
                                            } else {
                                                if (uri.length() > 0) {
                                                    startActivity(new Intent(getActivity(), StartVideoActivity.class));
                                                    releaseCamera();
                                                } else {
                                                    AppUtils.getHandShakeCall(username, getActivity());
                                                }
                                            }
                                        } else {
                                            getErrorDialog("Error", getResources().getString(R.string.unable_to_capture_your_photo_please_try_again));
                                        }
                                    }


                                    @Override
                                    public void onFailure(int requestCode) {

                                    }
                                });
                                controller.postResourceProfilePic(CAM_REQ, request);
                            }
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void getAttendanceSheetDialog() {

        try {
            LayoutInflater li = LayoutInflater.from(getActivity());
            View promptsView = li.inflate(R.layout.layout_view_attendance, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
            TextView txtDays = promptsView.findViewById(R.id.txtDays);
            TextView txtPresent = promptsView.findViewById(R.id.txtPresent);
            TextView txtAbsent = promptsView.findViewById(R.id.txtAbsent);
            TextView txtLate = promptsView.findViewById(R.id.txtLate);
            txtDays.setText(String.valueOf(Days));
            txtPresent.setText(String.valueOf(PresentDays));
            txtAbsent.setText(String.valueOf(AbsentDays));
            txtLate.setText(String.valueOf(LateDays));
            alertDialogBuilder.setView(promptsView);
            final AlertDialog alertDialog = alertDialogBuilder.create();
            LinearLayout btnOk = promptsView.findViewById(R.id.lnrOk);
            btnOk.setOnClickListener(view -> alertDialog.dismiss());
            alertDialog.setCancelable(false);
            alertDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


    @Override
    public void onResponse(int requestCode, Object response) {

    }

    @Override
    public void onFailure(int requestCode) {

    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void getErrorDialog(String title, String message) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
            builder.setTitle(title);
            builder.setMessage(
                    message);
            builder.setPositiveButton("Try again", (dialogInterface, i) -> {
                openCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
                dialogInterface.dismiss();
            }).create().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
