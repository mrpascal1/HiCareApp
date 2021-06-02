package com.ab.hicarerun.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.BuildConfig;
import com.ab.hicarerun.R;
import com.ab.hicarerun.activities.NewTaskDetailsActivity;
import com.ab.hicarerun.activities.VerifyOtpActivity;
import com.ab.hicarerun.databinding.FragmentNewLoginBinding;
import com.ab.hicarerun.handler.UserOtpLoginClickHandler;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.OtpModel.SendOtpResponse;
import com.ab.hicarerun.network.models.UpdateAppModel.UpdateData;
import com.ab.hicarerun.utils.AppUtils;
import com.ab.hicarerun.utils.DownloadApk;

//import com.ab.hicarerun.utils.LocaleHelper;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewLoginFragment extends BaseFragment implements UserOtpLoginClickHandler {
    FragmentNewLoginBinding mFragmentNewLoginBinding;
    private static final int OTP_REQUEST = 1000;
    private static final int UPDATE_REQ = 2000;
    private static final String ARG_VERSION = "ARG_VERSION";
    private static final String ARG_URL = "ARG_URL";
    private static final String ARG_TYPE = "ARG_TYPE";

    private String Version = "";
    private String Apk_URL = "";
    private String Apk_Type = "";
    private int mySessionID = 0;
    Configuration config;
    Locale locale;
    private Locale myLocale;

    public NewLoginFragment() {
        // Required empty public constructor
    }

    public static NewLoginFragment newInstance(String version, String apk_URL, String apk_Type) {
        Bundle args = new Bundle();
        args.putString(ARG_VERSION, version);
        args.putString(ARG_URL, apk_URL);
        args.putString(ARG_TYPE, apk_Type);
        NewLoginFragment fragment = new NewLoginFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Version = getArguments().getString(ARG_VERSION);
            Apk_URL = getArguments().getString(ARG_URL);
            Apk_Type = getArguments().getString(ARG_TYPE);
        }
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mFragmentNewLoginBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_new_login, container, false);
        mFragmentNewLoginBinding.setHandler(this);
//        getVersionFromApi();
        return mFragmentNewLoginBinding.getRoot();
    }

    private void showLanguageDialog() {
        try {

            LayoutInflater li = LayoutInflater.from(getActivity());
            View promptsView = li.inflate(R.layout.layout_language_dialog, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
            alertDialogBuilder.setView(promptsView);
            final AlertDialog alertDialog = alertDialogBuilder.create();
            final CardView lanEnglish =
                    promptsView.findViewById(R.id.lanEnglish);
            final CardView lanHindi =
                    promptsView.findViewById(R.id.lanHindi);
            final CardView lanMarathi =
                    promptsView.findViewById(R.id.lanMarathi);
            final CardView lanGujrati =
                    promptsView.findViewById(R.id.lanGujrati);
            final CardView lanTamil =
                    promptsView.findViewById(R.id.lanTamil);
            final CardView lanTelugu =
                    promptsView.findViewById(R.id.lanTelugu);
            final CardView lanKannad =
                    promptsView.findViewById(R.id.lanKannad);
            final Button btnContinue = promptsView.findViewById(R.id.btnOk);


            btnContinue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });

            lanEnglish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppUtils.updateViews("en", getActivity());
                    alertDialog.dismiss();
                    //It is required to recreate the activity to reflect the change in UI.
                    getActivity().recreate();
                }
            });

            lanHindi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppUtils.updateViews("hi", getActivity());
                    alertDialog.dismiss();
                    getActivity().recreate();

                }
            });


            lanMarathi.setOnClickListener(view -> {
                AppUtils.updateViews("mr", getActivity());
                alertDialog.dismiss();
                getActivity().recreate();
            });

            lanGujrati.setOnClickListener(view -> {
                AppUtils.updateViews("gu", getActivity());
                alertDialog.dismiss();
                getActivity().recreate();
            });

            lanTamil.setOnClickListener(view -> {
                AppUtils.updateViews("ta", getActivity());
                alertDialog.dismiss();
                getActivity().recreate();
            });

            lanTelugu.setOnClickListener(view -> {
                AppUtils.updateViews("te", getActivity());
                alertDialog.dismiss();
                getActivity().recreate();
            });

            lanKannad.setOnClickListener(view -> {
                AppUtils.updateViews("kn", getActivity());
                alertDialog.dismiss();
                getActivity().recreate();
            });

            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFragmentNewLoginBinding.txtPolicy.setOnClickListener(view15 -> replaceFragment(PrivacyPolicyFragment.newInstance(), "LoginFragment-PrivacyPolicyFragment"));
        mFragmentNewLoginBinding.txtTerms.setOnClickListener(view16 -> replaceFragment(TermsAndConditionsFragment.newInstance(), "LoginFragment-TermsAndConditionsFragment"));
        mFragmentNewLoginBinding.lnrLanguage.setOnClickListener(view1 -> showLanguageDialog());
    }


    @Override
    public void onEnterMobileClicked(View view) {
       Log.i("VERSION", BuildConfig.VERSION_NAME);
       float AppVersion = Float.parseFloat(BuildConfig.VERSION_NAME);
       float  VersionName = Float.parseFloat(Version);

        if(AppVersion >= VersionName){
            sendOTP();
        }else {
            UpdateAppBottomFragment bottomSheet = new UpdateAppBottomFragment(Apk_Type, Apk_URL, Version);
            bottomSheet.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), bottomSheet.getTag());
        }
    }

    private void sendOTP() {
        try {
            final String mobile = mFragmentNewLoginBinding.edtMobile.getText().toString();
            if (getValidated()) {
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
                                intent.putExtra(VerifyOtpActivity.ARGS_USER_ID, response.getData().getResourceId());
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
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSendOtpClicked(View view) {
    }

    private boolean getValidated() {
        if (mFragmentNewLoginBinding.edtMobile.getText().toString().length() == 0) {
            mFragmentNewLoginBinding.edtMobile.setError("Please enter mobile number!");
            return false;
        } else if (mFragmentNewLoginBinding.edtMobile.getText().toString().length() < 10) {
            mFragmentNewLoginBinding.edtMobile.setError("Invalid mobile number!");
            return false;
        } else {
            return true;
        }
    }



}
