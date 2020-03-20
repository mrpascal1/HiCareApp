package com.ab.hicarerun.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.ActivityHelpBinding;
import com.ab.hicarerun.handler.UserHelpClickHandler;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.utils.AppUtils;
import com.ab.hicarerun.utils.LocaleHelper;

import java.util.Objects;

import io.realm.RealmResults;

public class HelpActivity extends AppCompatActivity implements UserHelpClickHandler {
    ActivityHelpBinding mActivityHelpBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityHelpBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_help);
        mActivityHelpBinding.setHandler(this);
        setSupportActionBar(mActivityHelpBinding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Help");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onContactNoClicked(View view) {

        try{
            String number = mActivityHelpBinding.txtNumber.getText().toString();
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + number));
            startActivity(callIntent);

        }catch (Exception e){
            RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                assert mLoginRealmModels.get(0) != null;
                String userName = "TECHNICIAN NAME : "+mLoginRealmModels.get(0).getUserName();
                String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                String DeviceName = "DEVICE_NAME : "+ Build.DEVICE+", DEVICE_VERSION : "+ Build.VERSION.SDK_INT;
                AppUtils.sendErrorLogs(e.getMessage(), getClass().getSimpleName(), "onContactNoClicked", lineNo,userName,DeviceName);
            }
        }

    }


    @Override
    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(LocaleHelper.onAttach(base));
        super.attachBaseContext(LocaleHelper.onAttach(base, LocaleHelper.getLanguage(base)));

    }

    @Override
    public void onEmailClicked(View view) {
        try {
            String mail = mActivityHelpBinding.txtMail.getText().toString();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + mail));
            startActivity(intent);
        } catch (Exception e) {
            RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                assert mLoginRealmModels.get(0) != null;
                String userName = "TECHNICIAN NAME : "+mLoginRealmModels.get(0).getUserName();
                String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                String DeviceName = "DEVICE_NAME : "+ Build.DEVICE+", DEVICE_VERSION : "+ Build.VERSION.SDK_INT;
                AppUtils.sendErrorLogs(e.getMessage(), getClass().getSimpleName(), "onEmailClicked", lineNo,userName,DeviceName);
            }
        }
    }
}
