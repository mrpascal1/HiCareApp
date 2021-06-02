package com.ab.hicarerun.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.ab.hicarerun.BaseActivity;
import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.ActivityKycBinding;
import com.ab.hicarerun.fragments.TechnicianRoutineFragment;
import com.ab.hicarerun.fragments.UserKycFragment;
import com.ab.hicarerun.utils.LocaleHelper;

import java.util.Objects;

public class KycActivity extends BaseActivity {
    ActivityKycBinding mActivityKycBinding;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, LocaleHelper.getLanguage(base)));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityKycBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_kyc);
        addFragment(UserKycFragment.newInstance(), "KycActivity - UserKycFragment");

        setSupportActionBar(mActivityKycBinding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        try {
            getBack();
            super.onBackPressed();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getBack() {
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
               onBackPressed();
                break;
        }

        return true;
    }

}
