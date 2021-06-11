package com.ab.hicarerun.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.activities.StartVideoActivity;
import com.ab.hicarerun.databinding.FragmentVerifyMobileOtBinding;
import com.ab.hicarerun.handler.OtpReceivedInterface;
import com.ab.hicarerun.handler.UserVerifyOtpClickHandler;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.network.models.OtpModel.SendOtpResponse;
import com.ab.hicarerun.network.models.TrainingModel.Videos;
import com.ab.hicarerun.utils.AppSignatureHelper;
import com.ab.hicarerun.utils.AppUtils;
import com.ab.hicarerun.utils.SMSListener;
import com.ab.hicarerun.utils.SharedPreferencesUtility;
import com.ab.hicarerun.utils.notifications.OneSIgnalHelper;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class VerifyMobileOTPFragment extends BaseFragment implements UserVerifyOtpClickHandler, GoogleApiClient.ConnectionCallbacks,
        OtpReceivedInterface, GoogleApiClient.OnConnectionFailedListener {
    FragmentVerifyMobileOtBinding mFragmentVerifyMobileOtBinding;
    private static final String ARG_MOBILE = "ARG_MOBILE";
    private static final String ARG_OTP = "ARG_OTP";
    private static final String ARG_USER = "ARG_USER";
    private static final String ARG_USER_ID = "ARG_USER_ID";
    private static final int LOGIN_REQUEST = 2000;
    private String mobile = "", otp = "", user = "", userId = "";
    private static final int VIDEO_REQUEST = 2000;

    private static final int OTP_REQUEST = 1000;
    private int RESOLVE_HINT = 2;
    private Boolean isGetInside = false;
    private UserLoginTask mAuthTask = null;
    private String profilePic = "";
    GoogleApiClient mGoogleApiClient;
    //    private SMSListener reciever;
    SMSListener mSmsBroadcastReceiver;

    public VerifyMobileOTPFragment() {
        // Required empty public constructor
    }


    public static VerifyMobileOTPFragment newInstance(String mobile, String otp, String user, String userId) {
        Bundle args = new Bundle();
        args.putString(ARG_MOBILE, mobile);
        args.putString(ARG_OTP, otp);
        args.putString(ARG_USER, user);
        args.putString(ARG_USER_ID, userId);
        VerifyMobileOTPFragment fragment = new VerifyMobileOTPFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mobile = getArguments().getString(ARG_MOBILE);
            user = getArguments().getString(ARG_USER);
            userId = getArguments().getString(ARG_USER_ID);
            otp = getArguments().getString(ARG_OTP);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentVerifyMobileOtBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_verify_mobile_ot, container, false);
        mFragmentVerifyMobileOtBinding.setHandler(this);

        AppSignatureHelper appSignatureHelper = new AppSignatureHelper(getActivity());
        appSignatureHelper.getAppSignatures();
        mSmsBroadcastReceiver = new SMSListener();
        //set google api client for hint request
        mGoogleApiClient = new GoogleApiClient.Builder(Objects.requireNonNull(getContext()))
                .addConnectionCallbacks(this)
                .enableAutoManage(Objects.requireNonNull(getActivity()), this)
                .addApi(Auth.CREDENTIALS_API)
                .build();

        mSmsBroadcastReceiver.setOnOtpListeners(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
        getActivity().registerReceiver(mSmsBroadcastReceiver, intentFilter);
        getActivity().setTitle("");
        startSMSListener();
        return mFragmentVerifyMobileOtBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFragmentVerifyMobileOtBinding.txtNumber.setText("OTP has been sent to " + mobile);
        mFragmentVerifyMobileOtBinding.txtVerify.setTypeface(mFragmentVerifyMobileOtBinding.txtVerify.getTypeface(), Typeface.BOLD);
    }

    @Override
    public void onResendOtpClicked(View view) {
        try {
            NetworkCallController controller = new NetworkCallController(this);
            controller.setListner(new NetworkResponseListner<SendOtpResponse>() {
                @Override
                public void onResponse(int requestCode, SendOtpResponse response) {
                    if (response.getSuccess()) {
                        otp = response.getData().getLoginotp();
                        startSMSListener();
                        Log.i("RESEND", "true");
                    } else {
                        Toast.makeText(getActivity(), "Please resend again.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(int requestCode) {
                }

            });
            controller.sendOtp(OTP_REQUEST, mobile, "true");

        } catch (Exception e) {
            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
            AppUtils.sendErrorLogs(e.getMessage(), "", "onResendOtpClicked", lineNo, "", DeviceName);
        }
    }

    @Override
    public void onVerifyOtpClicked(View view) {
        if (mFragmentVerifyMobileOtBinding.otpView.length() == 6) {
            attemptLogin();
        } else {
            mFragmentVerifyMobileOtBinding.otpView.setError("Invalid OTP!");
        }
    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        String imei = "", device_info = "";

        TelephonyManager telephonyManager = (TelephonyManager) Objects.requireNonNull(getActivity()).getSystemService(Context.TELEPHONY_SERVICE);
        device_info = Build.MODEL;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        PackageInfo pinfo = null;
        try {
            pinfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        assert pinfo != null;
        String versionName = pinfo.versionName;
        OneSIgnalHelper oneSIgnalHelper = new OneSIgnalHelper(getActivity());
        String mStrPlayerId = oneSIgnalHelper.getmStrUserID();
        try {
            imei = Settings.Secure.getString(getActivity().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
//            if (mFragmentVerifyMobileOtBinding.otpView.getText().toString().equals(otp)) {
            String OTP = mFragmentVerifyMobileOtBinding.otpView.getText().toString();
            NetworkCallController controller = new NetworkCallController(VerifyMobileOTPFragment.this);
            controller.setListner(new NetworkResponseListner<LoginResponse>() {
                @Override
                public void onResponse(int requestCode, LoginResponse response) {
                    // delete all previous record
                    getRealm().beginTransaction();
                    getRealm().deleteAll();
                    getRealm().commitTransaction();
                    profilePic = response.getUserProfilePic();
                    userId = response.getUserID();
                    // add new record
                    getRealm().beginTransaction();
                    getRealm().copyToRealmOrUpdate(response);
                    getRealm().commitTransaction();
                    isGetInside = true;
                    new UserLoginTask(isGetInside).execute((Void) null);

                }

                @Override
                public void onFailure(int requestCode) {
                }

            });
            controller.login(LOGIN_REQUEST, mobile, OTP, versionName, imei, device_info, mStrPlayerId);
//            }
//        else {
//                startSMSListener();
//                mFragmentVerifyMobileOtBinding.otpView.setError("Invalid OTP!");
//            }


        } catch (Exception e) {
            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
            AppUtils.sendErrorLogs(e.getMessage(), "", "Login", lineNo, "", DeviceName);
        }
    }

    @Override
    public void onOtpReceived(String otp) {
        Log.e("Otp Received", otp);
        mFragmentVerifyMobileOtBinding.otpView.setText(otp);
        attemptLogin();
    }

    private void startSMSListener() {
        try {
            SmsRetrieverClient mClient = SmsRetriever.getClient(Objects.requireNonNull(getActivity()));
            Task<Void> mTask = mClient.startSmsRetriever();
            mTask.addOnSuccessListener(aVoid -> Log.e("TAG_OTP", "SMS Retriever starts"));
            mTask.addOnFailureListener(e -> Log.e("TAG_OTP", "Error"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESOLVE_HINT) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                    assert credential != null;
                    mFragmentVerifyMobileOtBinding.txtNumber.setText(credential.getId());
                }

            }
        }
    }

    @Override
    public void onOtpTimeout() {
        Log.e("TAG_OTP", "Time out, please resend");
    }

    @Override
    public void onConnected(@androidx.annotation.Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@androidx.annotation.NonNull ConnectionResult connectionResult) {

    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private Boolean isPass = false;

        UserLoginTask(Boolean isGetInside) {
            isPass = isGetInside;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                if (isPass) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            try {
                mAuthTask = null;
                if (success) {
                    getWelcomeVideo();
                    SharedPreferencesUtility.savePrefString(Objects.requireNonNull(getActivity()), SharedPreferencesUtility.PREF_LOGOUT, AppUtils.currentDate());
                } else {
                    mFragmentVerifyMobileOtBinding.otpView.setError("Invalid OTP!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        private void getWelcomeVideo() {
            try {
                NetworkCallController controller = new NetworkCallController(VerifyMobileOTPFragment.this);
                controller.setListner(new NetworkResponseListner() {
                    @Override
                    public void onResponse(int requestCode, Object response) {
                        Videos items = (Videos) response;
                        if (items != null) {
                            if (profilePic.trim().length() == 0) {
                                replaceFragment(FaceRecognizationFragment.newInstance(false, mobile, items.getVideoUrl()), "VerifyOtpFragment-FaceRecognizationFragment");
                            } else {
                                if (items.getVideoUrl().length() > 0) {
                                    if (SharedPreferencesUtility.getPrefBoolean(Objects.requireNonNull(getActivity()), SharedPreferencesUtility.IS_SKIP_VIDEO)) {
                                        AppUtils.getHandShakeCall(mobile, getActivity());
                                    } else {
                                        startActivity(new Intent(getActivity(), StartVideoActivity.class)
                                                .putExtra(StartVideoActivity.ARG_USER, mobile)
                                                .putExtra(StartVideoActivity.ARG_URL, items.getVideoUrl()));
                                    }
                                } else {
                                    AppUtils.getHandShakeCall(mobile, getActivity());
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(int requestCode) {

                    }
                });
                controller.getStartingVideos(VIDEO_REQUEST, userId);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private boolean getValidated() {
        if (mFragmentVerifyMobileOtBinding.otpView.getText().toString().length() == 0) {
            mFragmentVerifyMobileOtBinding.otpView.setError("Please enter OTP!");
            return false;
        } else if (mFragmentVerifyMobileOtBinding.otpView.getText().toString().length() < 6) {
            mFragmentVerifyMobileOtBinding.otpView.setError("Invalid OTP!");
            return false;
        } else if (!mFragmentVerifyMobileOtBinding.otpView.getText().toString().equals(otp)) {
            mFragmentVerifyMobileOtBinding.otpView.setError("Invalid OTP!");
            return false;
        } else {
            return true;
        }
    }
}
