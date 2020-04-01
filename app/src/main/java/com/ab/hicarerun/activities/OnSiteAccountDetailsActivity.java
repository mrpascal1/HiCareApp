package com.ab.hicarerun.activities;

import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.ab.hicarerun.BaseActivity;
import com.ab.hicarerun.R;
import com.ab.hicarerun.adapter.TaskViewPagerAdapter;
import com.ab.hicarerun.databinding.ActivityOnSiteAccountDetailsBinding;
import com.ab.hicarerun.fragments.OnSiteTaskFragment;
import com.ab.hicarerun.fragments.RecentOnsiteTaskFragment;
import com.ab.hicarerun.network.models.OnSiteModel.Account;
import com.ab.hicarerun.utils.LocaleHelper;

public class OnSiteAccountDetailsActivity extends BaseActivity {
    ActivityOnSiteAccountDetailsBinding mActivityOnSiteAccountDetailsBinding;
    private TaskViewPagerAdapter mAdapter;
    Account model;
    public static final String ARG_ACCOUNT = "ARG_ACCOUNT";


    @Override
    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(LocaleHelper.onAttach(base));
        super.attachBaseContext(LocaleHelper.onAttach(base, LocaleHelper.getLanguage(base)));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityOnSiteAccountDetailsBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_on_site_account_details);
        setSupportActionBar(mActivityOnSiteAccountDetailsBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        model = getIntent().getParcelableExtra(ARG_ACCOUNT);
        setViewPagerView();
    }

    private void setViewPagerView() {
        mActivityOnSiteAccountDetailsBinding.viewpager.setOffscreenPageLimit(0);
        mAdapter = new TaskViewPagerAdapter(getSupportFragmentManager(), this);
        mAdapter.addFragment(OnSiteTaskFragment.newInstance(model), getResources().getString(R.string.onsite_tab));
        mAdapter.addFragment(RecentOnsiteTaskFragment.newInstance(model), getResources().getString(R.string.completed_tab));
//        mActivityOnSiteAccountDetailsBinding.viewpagertab.setDistributeEvenly(true);
        mActivityOnSiteAccountDetailsBinding.viewpager.setAdapter(mAdapter);
        mActivityOnSiteAccountDetailsBinding.tabs.setupWithViewPager(mActivityOnSiteAccountDetailsBinding.viewpager);
//        mActivityOnSiteAccountDetailsBinding.viewpagertab.setViewPager(mActivityOnSiteAccountDetailsBinding.pager);
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
