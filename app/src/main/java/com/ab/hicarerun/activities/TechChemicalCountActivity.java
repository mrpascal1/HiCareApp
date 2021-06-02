package com.ab.hicarerun.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.ab.hicarerun.BaseActivity;
import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.ActivityTechChemicalCountBinding;
import com.ab.hicarerun.fragments.AttendanceViewFragment;
import com.ab.hicarerun.fragments.TechChemicalCountFragment;
import com.ab.hicarerun.utils.LocaleHelper;

import java.util.Objects;

public class TechChemicalCountActivity extends BaseActivity {
ActivityTechChemicalCountBinding mActivityTechChemicalCountBinding;

    @Override
    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(LocaleHelper.onAttach(base));
        super.attachBaseContext(LocaleHelper.onAttach(base, LocaleHelper.getLanguage(base)));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityTechChemicalCountBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_tech_chemical_count);
        addFragment(TechChemicalCountFragment.newInstance(), "TechChemicalCountActivity - TechChemicalCountFragment");
        setSupportActionBar(mActivityTechChemicalCountBinding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        try {
            overridePendingTransition(R.anim.stay, R.anim.slide_out_right);  //close animation
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
