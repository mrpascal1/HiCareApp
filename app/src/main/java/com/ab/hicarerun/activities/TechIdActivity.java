package com.ab.hicarerun.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.databinding.DataBindingUtil;

import com.ab.hicarerun.BaseActivity;
import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.ActivityTechIdBinding;
import com.ab.hicarerun.fragments.TechIdFragment;
import com.ab.hicarerun.fragments.TechnicianSeniorFragment;

import java.util.Objects;

public class TechIdActivity extends BaseActivity {
    ActivityTechIdBinding mActivityTechIdBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityTechIdBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_tech_id);
        addFragment(TechIdFragment.newInstance(), "TechIdActivity - TechIdFragment");
        setSupportActionBar(mActivityTechIdBinding.toolbar);
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
