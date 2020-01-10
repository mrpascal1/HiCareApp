package com.ab.hicarerun.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.ab.hicarerun.BaseActivity;
import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.ActivityIncentivesBinding;
import com.ab.hicarerun.fragments.IncentiveFragment;
import com.ab.hicarerun.fragments.TechIdFragment;

public class IncentivesActivity extends BaseActivity {
    ActivityIncentivesBinding mActivityIncentivesBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityIncentivesBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_incentives);
        addFragment(IncentiveFragment.newInstance(), "IncentivesActivity - IncentiveFragment");
        setSupportActionBar(mActivityIncentivesBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
