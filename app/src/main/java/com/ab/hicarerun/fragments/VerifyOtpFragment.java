package com.ab.hicarerun.fragments;


import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.activities.HomeActivity;
import com.ab.hicarerun.activities.StartVideoActivity;
import com.ab.hicarerun.activities.VerifyOtpActivity;
import com.ab.hicarerun.databinding.FragmentOtpLoginBinding;
import com.ab.hicarerun.databinding.FragmentVerifyOtpBinding;
import com.ab.hicarerun.handler.Common;
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
import com.ab.hicarerun.viewmodel.UserLoginViewModel;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;


public class VerifyOtpFragment extends BaseFragment implements UserVerifyOtpClickHandler, GoogleApiClient.ConnectionCallbacks,
        OtpReceivedInterface, GoogleApiClient.OnConnectionFailedListener {
    FragmentVerifyOtpBinding mFragmentVerifyOtpBinding;
    private static final String ARG_MOBILE = "ARG_MOBILE";
    private static final String ARG_OTP = "ARG_OTP";
    private static final String ARG_USER = "ARG_USER";
    private static final int LOGIN_REQUEST = 2000;
    private String mobile = "", otp = "", user = "";
    private static final int VIDEO_REQUEST = 2000;

    private static final int OTP_REQUEST = 1000;
    private int RESOLVE_HINT = 2;
    private Boolean isGetInside = false;
    private LoginFragment.UserLoginTask mAuthTask = null;
    private String profilePic = "";
    GoogleApiClient mGoogleApiClient;
    //    private SMSListener reciever;
    SMSListener mSmsBroadcastReceiver;

    public VerifyOtpFragment() {
        // Required empty public constructor
    }

    public static VerifyOtpFragment newInstance(String mobile, String otp, String user) {
        Bundle args = new Bundle();
        args.putString(ARG_MOBILE, mobile);
        args.putString(ARG_OTP, otp);
        args.putString(ARG_USER, user);
        VerifyOtpFragment fragment = new VerifyOtpFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mobile = getArguments().getString(ARG_MOBILE);
            user = getArguments().getString(ARG_USER);
            otp = getArguments().getString(ARG_OTP);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mFragmentVerifyOtpBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_verify_otp, container, false);
        mFragmentVerifyOtpBinding.setHandler(this);

        AppSignatureHelper appSignatureHelper = new AppSignatureHelper(getActivity());
        appSignatureHelper.getAppSignatures();
        mSmsBroadcastReceiver = new SMSListener();
        //set google api client for hint request
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.CREDENTIALS_API)
                .build();

        mSmsBroadcastReceiver.setOnOtpListeners(VerifyOtpFragment.this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
        getActivity().registerReceiver(mSmsBroadcastReceiver, intentFilter);
        getActivity().setTitle("");
        startSMSListener();
        return mFragmentVerifyOtpBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String text = "<font color=#000000>Didn't get the OTP?</font> <font color=#2bb77a> RESEND</font>";
        mFragmentVerifyOtpBinding.txtResend.setText(Html.fromHtml(text));
        mFragmentVerifyOtpBinding.txtNumber.setText("OTP sent to " + mobile);
        mFragmentVerifyOtpBinding.otpView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 6) {
                    mFragmentVerifyOtpBinding.btnVerify.setEnabled(true);
                    mFragmentVerifyOtpBinding.btnVerify.setAlpha(1);
                    mFragmentVerifyOtpBinding.btnVerify.setText("VERIFY AND PROCEED");
                } else {
                    mFragmentVerifyOtpBinding.btnVerify.setEnabled(false);
                    mFragmentVerifyOtpBinding.btnVerify.setAlpha(0.5f);
                    mFragmentVerifyOtpBinding.btnVerify.setText("ENTER OTP");
                }

            }
        });


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
            Log.i("LoginError", e.getMessage());
            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
            AppUtils.sendErrorLogs(e.getMessage(), "", "onResendOtpClicked", lineNo, "", DeviceName);
        }
    }

    @Override
    public void onVerifyOtpClicked(View view) {
        if (mFragmentVerifyOtpBinding.otpView.length() == 6) {
            attemptLogin();
        }
    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        String imei = "", device_info = "";

        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
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
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            try {
////                imei = telephonyManager.getImei();
//                imei = UUID.randomUUID().toString();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            try {
////                imei = telephonyManager.getDeviceId();
//                imei = UUID.randomUUID().toString();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        PackageInfo pinfo = null;
        try {
            pinfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String versionName = pinfo.versionName;
        OneSIgnalHelper oneSIgnalHelper = new OneSIgnalHelper(getActivity());
        String mStrPlayerId = oneSIgnalHelper.getmStrUserID();
//        imei = oneSIgnalHelper.getmStrUserID();
        imei = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);


        try {
            if (mFragmentVerifyOtpBinding.otpView.getText().toString().equals(otp)) {

                String OTP = mFragmentVerifyOtpBinding.otpView.getText().toString();
                NetworkCallController controller = new NetworkCallController(this);
                controller.setListner(new NetworkResponseListner<LoginResponse>() {
                    @Override
                    public void onResponse(int requestCode, LoginResponse response) {
                        // delete all previous record
                        getRealm().beginTransaction();
                        getRealm().deleteAll();
                        getRealm().commitTransaction();
                        profilePic = response.getUserProfilePic();
                        // add new record
                        getRealm().beginTransaction();
                        getRealm().copyToRealmOrUpdate(response);
                        getRealm().commitTransaction();
                        if (isVisible()) {
                            isGetInside = true;
                            new UserLoginTask(isGetInside).execute((Void) null);
                        }
                    }

                    @Override
                    public void onFailure(int requestCode) {
                    }

                });


                controller.login(LOGIN_REQUEST, mobile, OTP, versionName, imei, device_info, mStrPlayerId);
            } else {
                Toast.makeText(getActivity(), "Invalid OTP!", Toast.LENGTH_SHORT).show();
                startSMSListener();
            }

        } catch (Exception e) {
            Log.i("LoginError", e.getMessage());
            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
            AppUtils.sendErrorLogs(e.getMessage(), "", "Login", lineNo, "", DeviceName);
        }
    }

    @Override
    public void onOtpReceived(String otp) {
        Log.e("Otp Received", otp);
        mFragmentVerifyOtpBinding.otpView.setText(otp);
        if (mFragmentVerifyOtpBinding.otpView.length() == 6) {
            attemptLogin();
        }
    }

    public void startSMSListener() {
        SmsRetrieverClient mClient = SmsRetriever.getClient(getActivity());
        Task<Void> mTask = mClient.startSmsRetriever();
        mTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Log.e("TAG_OTP", "SMS Retriever starts");
            }
        });
        mTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TAG_OTP", "Error");
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESOLVE_HINT) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                    // credential.getId();  <-- will need to process phone number string
                    mFragmentVerifyOtpBinding.txtNumber.setText(credential.getId());
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
//        private UserLoginViewModel model = mFragmentLoginBinding.getModel();

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
                Log.i("Background failed", e.getMessage());
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            if (success) {
//                    if (profilePic.trim().length() == 0) {
//                        replaceFragment(FaceRecognizationFragment.newInstance(false, mobile), "VerifyOtpFragment-FaceRecognizationFragment");
//                    } else {
//                        if(SharedPreferencesUtility.getPrefBoolean(getActivity(),SharedPreferencesUtility.IS_SKIP_VIDEO)){
//                            AppUtils.getHandShakeCall(mobile, getActivity());
//                        }else {
//                            startActivity(new Intent(getActivity(), StartVideoActivity.class).putExtra(StartVideoActivity.ARG_USER,mobile));
//                            getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//                        }
//                    }
                getWelcomeVideo();
                SharedPreferencesUtility.savePrefString(getActivity(), SharedPreferencesUtility.PREF_LOGOUT, AppUtils.currentDate());

            } else {
                mFragmentVerifyOtpBinding.otpView.setError("Invalid OTP!");
            }
        }


        private void getWelcomeVideo() {
            NetworkCallController controller = new NetworkCallController(VerifyOtpFragment.this);
            controller.setListner(new NetworkResponseListner() {
                @Override
                public void onResponse(int requestCode, Object response) {
                    Videos items = (Videos) response;
                    if (items != null) {

                        if (profilePic.trim().length() == 0) {
                            replaceFragment(FaceRecognizationFragment.newInstance(false, mobile, items.getVideoUrl()), "VerifyOtpFragment-FaceRecognizationFragment");
                        } else {
                            if (items.getVideoUrl().length() > 0) {
                                if (SharedPreferencesUtility.getPrefBoolean(getActivity(), SharedPreferencesUtility.IS_SKIP_VIDEO)) {
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
            controller.getStartingVideos(VIDEO_REQUEST);
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
}
