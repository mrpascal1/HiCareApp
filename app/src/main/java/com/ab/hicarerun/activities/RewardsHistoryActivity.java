package com.ab.hicarerun.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.ab.hicarerun.BaseActivity;
import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.ActivityRewardsHistoryBinding;
import com.ab.hicarerun.fragments.IncentiveFragment;
import com.ab.hicarerun.fragments.RewardsHistoryFragment;
import com.ab.hicarerun.utils.LocaleHelper;

import java.util.Objects;

public class RewardsHistoryActivity extends BaseActivity {
    ActivityRewardsHistoryBinding mActivityRewardsHistoryBinding;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, LocaleHelper.getLanguage(base)));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityRewardsHistoryBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_rewards_history);
        addFragment(RewardsHistoryFragment.newInstance(), "RewardsHistoryActivity - RewardsHistoryFragment");
        setSupportActionBar(mActivityRewardsHistoryBinding.toolbar);
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
                getBack();
                break;
        }

        return true;
    }
}
