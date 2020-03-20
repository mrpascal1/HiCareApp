package com.ab.hicarerun.activities;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.databinding.DataBindingUtil;

import com.ab.hicarerun.BaseActivity;
import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.ActivityTechnicianSeniorBinding;
import com.ab.hicarerun.fragments.TechnicianSeniorFragment;
import com.ab.hicarerun.utils.LocaleHelper;

import java.util.Objects;

public class TechnicianSeniorActivity extends BaseActivity {
    ActivityTechnicianSeniorBinding mTechnicianSeniorBinding;


    @Override
    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(LocaleHelper.onAttach(base));
        super.attachBaseContext(LocaleHelper.onAttach(base, LocaleHelper.getLanguage(base)));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTechnicianSeniorBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_technician_senior);
        addFragment(TechnicianSeniorFragment.newInstance(), "TechnicianSeniorActivity - TechnicianSeniorFragment");

        setSupportActionBar(mTechnicianSeniorBinding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        try {
            getGroomingBack();
            super.onBackPressed();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getGroomingBack() {
        int fragment = getSupportFragmentManager().getBackStackEntryCount();
        Log.e("fragments", String.valueOf(fragment));
        if (fragment < 1) {
            finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                getGroomingBack();
                break;
        }

        return true;
    }
}
