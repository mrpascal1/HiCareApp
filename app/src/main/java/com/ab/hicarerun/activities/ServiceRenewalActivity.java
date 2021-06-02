package com.ab.hicarerun.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.ab.hicarerun.BaseActivity;
import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.ActivityServiceRenewalBinding;
import com.ab.hicarerun.fragments.RewardsHistoryFragment;
import com.ab.hicarerun.fragments.ServiceRenewalFragment;
import com.ab.hicarerun.utils.LocaleHelper;

import java.util.Objects;

public class ServiceRenewalActivity extends BaseActivity {
    ActivityServiceRenewalBinding mActivityServiceRenewalBinding;
    public static final String ARGS_TASKS = "ARGS_TASKS";
    private String taskId;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, LocaleHelper.getLanguage(base)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityServiceRenewalBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_service_renewal);
        taskId = getIntent().getStringExtra(ARGS_TASKS);
        addFragment(ServiceRenewalFragment.newInstance(taskId), "ServiceRenewalActivity - ServiceRenewalFragment");
        setSupportActionBar(mActivityServiceRenewalBinding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

    }

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
                getBack();
                break;
        }

        return true;
    }
}
