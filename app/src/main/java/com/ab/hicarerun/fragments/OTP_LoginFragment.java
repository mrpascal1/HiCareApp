package com.ab.hicarerun.fragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.activities.HomeActivity;
import com.ab.hicarerun.activities.VerifyOtpActivity;
import com.ab.hicarerun.adapter.MainSliderAdapter;
import com.ab.hicarerun.databinding.FragmentOtpLoginBinding;
import com.ab.hicarerun.handler.UserOtpLoginClickHandler;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.network.models.OtpModel.OtpData;
import com.ab.hicarerun.network.models.OtpModel.SendOtpResponse;
import com.ab.hicarerun.network.models.UpdateAppModel.UpdateData;
import com.ab.hicarerun.utils.AppUtils;
import com.ab.hicarerun.utils.DownloadApk;
import com.ab.hicarerun.viewmodel.UserLoginViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class OTP_LoginFragment extends BaseFragment implements UserOtpLoginClickHandler {
    FragmentOtpLoginBinding mFragmentOtpLoginBinding;
    private BottomSheetBehavior mBottomSheetBehaviour;
    private static final int OTP_REQUEST = 1000;
    private static final int UPDATE_REQ = 2000;
    private String Version = "";
    private String Apk_URL = "";
    private String Apk_Type = "";

    public OTP_LoginFragment() {
        // Required empty public constructor
    }

    public static OTP_LoginFragment newInstance() {
        Bundle args = new Bundle();
        OTP_LoginFragment fragment = new OTP_LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentOtpLoginBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_otp_login, container, false);
        mBottomSheetBehaviour = BottomSheetBehavior.from(mFragmentOtpLoginBinding.nestedScrollView);
        mFragmentOtpLoginBinding.setHandler(this);
        mFragmentOtpLoginBinding.bannerSlider.setAdapter(new MainSliderAdapter());
        mFragmentOtpLoginBinding.edtMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 10) {
                    mFragmentOtpLoginBinding.btnOtp.setAlpha(1);
                    mFragmentOtpLoginBinding.btnOtp.setText("CONTINUE");
                    mFragmentOtpLoginBinding.btnOtp.setEnabled(true);

                } else {
                    mFragmentOtpLoginBinding.btnOtp.setAlpha(0.5f);
                    mFragmentOtpLoginBinding.btnOtp.setText("ENTER PHONE NUMBER");
                    mFragmentOtpLoginBinding.btnOtp.setEnabled(false);
                }
            }
        });
        getVersionFromApi();
        return mFragmentOtpLoginBinding.getRoot();
    }

    private void getVersionFromApi() {
        NetworkCallController controller = new NetworkCallController(this);
        controller.setListner(new NetworkResponseListner() {
            @Override
            public void onResponse(int requestCode, Object response) {
                UpdateData data = (UpdateData) response;
                Version = data.getVersion();
                Apk_URL = data.getApkurl();
                Apk_Type = data.getApktype();
//                checkCurrentVersion(data.getApkurl(), data.getVersion(), data.getApktype());
            }

            @Override
            public void onFailure(int requestCode) {

            }
        });
        controller.getUpdateApp(UPDATE_REQ);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBottomSheetBehaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                String state = "";
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING: {
                        state = "DRAGGING";
                        mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);    // Will show the bottom sheet
                        break;
                    }
                    case BottomSheetBehavior.STATE_SETTLING: {
                        state = "SETTLING";
                        break;
                    }
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        state = "EXPANDED";
                        break;
                    }
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        state = "COLLAPSED";
                        break;
                    }
                    case BottomSheetBehavior.STATE_HIDDEN: {
                        state = "HIDDEN";
                        break;
                    }
                }

            }

            @Override
            public void onSlide(@NonNull View view, float v) {
            }
        });
    }

    @Override
    public void onEnterMobileClicked(View view) {
        try {
            PackageInfo pInfo = null;
            try {
                pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            String mobileVersion = pInfo.versionName;
            if (Float.parseFloat(mobileVersion) < Float.parseFloat(Version)) {
                String title = "New update available";
                String messageAlert = "<html><body><p>Please update your app to new version.<br><br>Current app version: " + mobileVersion + "<br><br>New version: " + Version + "</p></body></html>";
                AppUtils.showDownloadActionAlertBox(getActivity(), title, String.valueOf(Html.fromHtml(messageAlert)), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (AppUtils.checkConnection(getActivity())) {
                            ProgressDialog progress = new ProgressDialog(getActivity());
                            if (Apk_Type.equalsIgnoreCase("url")) {
                                final String appPackageName = getActivity().getPackageName(); // getPackageName() from Context or Activity object
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                } catch (android.content.ActivityNotFoundException anfe) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                }
                            } else {
                                DownloadApk downloadAndInstall = new DownloadApk();
                                progress.setCancelable(false);
                                progress.setMessage("Downloading...");
                                downloadAndInstall.setContext(getActivity(), progress);
                                downloadAndInstall.execute(Apk_URL);
                            }

                        } else {
                            AppUtils.showOkActionAlertBox(getActivity(), "No Internet Found.", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    getActivity().finish();
                                }
                            });
                        }
                    }
                });
            } else {
                mBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);    // Will show the bottom sheet
                mFragmentOtpLoginBinding.edtMobile.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                imm.showSoftInput(mFragmentOtpLoginBinding.edtMobile, InputMethodManager.SHOW_IMPLICIT);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onSendOtpClicked(View view) {

        final String mobile = mFragmentOtpLoginBinding.edtMobile.getText().toString();
        try {
            NetworkCallController controller = new NetworkCallController(this);
            controller.setListner(new NetworkResponseListner<SendOtpResponse>() {
                @Override
                public void onResponse(int requestCode, SendOtpResponse response) {
                    if (response.getSuccess()) {

                        Intent intent = new Intent(getActivity(), VerifyOtpActivity.class);
                        intent.putExtra(VerifyOtpActivity.ARGS_MOBILE, mobile);
                        intent.putExtra(VerifyOtpActivity.ARGS_USER, response.getData().getResourceName());
                        intent.putExtra(VerifyOtpActivity.ARGS_OTP, response.getData().getLoginotp());
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), response.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(int requestCode) {
                }

            });

            controller.sendOtp(OTP_REQUEST, mobile, "false");


        } catch (Exception e) {
            Log.i("LoginError", e.getMessage());
        }
    }
}
