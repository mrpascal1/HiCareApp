package com.ab.hicarerun.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.ab.hicarerun.BaseActivity;
import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.ActivityIncentivesBinding;
import com.ab.hicarerun.databinding.ActivityVoucherBinding;
import com.ab.hicarerun.fragments.IncentiveFragment;
import com.ab.hicarerun.fragments.VoucherFragment;

public class VoucherActivity extends BaseActivity {
    ActivityVoucherBinding mActivityVoucherBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityVoucherBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_voucher);
        addFragment(VoucherFragment.newInstance(), "VoucherActivity - VoucherFragment");
        setSupportActionBar(mActivityVoucherBinding.toolbar);
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
