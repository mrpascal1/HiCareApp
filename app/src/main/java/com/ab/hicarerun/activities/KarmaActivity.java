package com.ab.hicarerun.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.ab.hicarerun.BaseActivity;
import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.ActivityKarmaBinding;
import com.ab.hicarerun.fragments.KarmaFragment;
import com.ab.hicarerun.fragments.KarmaVideoFragment;
import com.ab.hicarerun.fragments.UserKycFragment;

import java.util.Objects;

public class KarmaActivity extends BaseActivity {
    ActivityKarmaBinding mActivityKarmaBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityKarmaBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_karma);
        addFragment(KarmaFragment.newInstance(), "KarmaActivity - KarmaFragment");

        setSupportActionBar(mActivityKarmaBinding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        try {
            getBack();
//            super.onBackPressed();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getBack() {
        int fragment = getSupportFragmentManager().getBackStackEntryCount();
        Log.e("fragments", String.valueOf(fragment));
        if (fragment < 1) {
            finish();
        } else if (fragment == 1) {
            if (KarmaVideoFragment.isBack) {
                super.onBackPressed();
                KarmaVideoFragment.isBack = false;
            }
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