package com.ab.hicarerun.activities;


import android.graphics.PorterDuff;
import android.os.Bundle;


import androidx.databinding.DataBindingUtil;

import com.ab.hicarerun.BaseActivity;
import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.ActivityVerifyOtpBinding;
import com.ab.hicarerun.fragments.FaceRecognizationFragment;
import com.ab.hicarerun.fragments.LoginFragment;
import com.ab.hicarerun.fragments.VerifyMobileOTPFragment;
import com.ab.hicarerun.fragments.VerifyOtpFragment;

import java.util.Objects;


public class VerifyOtpActivity extends BaseActivity {
    ActivityVerifyOtpBinding mActivityVerifyOtpBinding;
    public static final String ARGS_MOBILE = "ARGS_MOBILE";
    public static final String ARGS_USER = "ARGS_USER";
    public static final String ARGS_OTP = "ARGS_OTP";
    String mobile = "", otp = "", user = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityVerifyOtpBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_verify_otp);
        try {
            setSupportActionBar(mActivityVerifyOtpBinding.toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            Objects.requireNonNull(mActivityVerifyOtpBinding.toolbar.getNavigationIcon()).setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mobile = getIntent().getStringExtra(ARGS_MOBILE);
        otp = getIntent().getStringExtra(ARGS_OTP);
        user = getIntent().getStringExtra(ARGS_USER);
        addFragment(VerifyMobileOTPFragment.newInstance(mobile, otp, user), "VerifyOTPActivity - VerifyMobileOTPFragment");
    }


//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        if (getFragmentManager().getBackStackEntryCount() == 0) {
//            this.finish();
//        } else {
//            getFragmentManager().popBackStack();
//        }
//    }

    @Override
    public void onBackPressed() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount == 0) {
            this.finish();
        } else {
            super.onBackPressed();
        }
    }
}
