package com.ab.hicarerun.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.ab.hicarerun.BaseActivity;
import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.ActivityOfferBinding;
import com.ab.hicarerun.fragments.IncentiveFragment;
import com.ab.hicarerun.fragments.OffersFragment;
import com.ab.hicarerun.utils.LocaleHelper;
import com.google.android.material.appbar.AppBarLayout;

import java.util.Objects;

public class OfferActivity extends BaseActivity {
    ActivityOfferBinding mActivityOfferBinding;
    Menu menuHistory;
    private ImageView imgHistory;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, LocaleHelper.getLanguage(base)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityOfferBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_offer);
        setSupportActionBar(mActivityOfferBinding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        addFragment(OffersFragment.newInstance(), "OfferActivity - OfferFragment");
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        }
//        mActivityOfferBinding.collapsingToolbar.setTitle("RewardsData");
//        mActivityOfferBinding.appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            boolean isShow = false;
//            int scrollRange = -1;
//
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                if (scrollRange == -1) {
//                    scrollRange = appBarLayout.getTotalScrollRange();
//                }
//                if (scrollRange + verticalOffset == 0) {
//                    isShow = true;
//
//                    mActivityOfferBinding.collapsingToolbar.setTitle("");
//
//                } else if (isShow) {
//                    isShow = false;
//                    mActivityOfferBinding.collapsingToolbar.setTitle("RewardsData");
//                }
//            }
//        });
        mActivityOfferBinding.txtPoints.setTypeface(mActivityOfferBinding.txtPoints.getTypeface(), Typeface.BOLD);
        mActivityOfferBinding.txtStar.setTypeface(mActivityOfferBinding.txtStar.getTypeface(), Typeface.BOLD);
        mActivityOfferBinding.collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.white));

        mActivityOfferBinding.collapsingToolbar.setCollapsedTitleTypeface(mActivityOfferBinding.txtPoints.getTypeface());
        mActivityOfferBinding.collapsingToolbar.setTitle(" ");
//
        mActivityOfferBinding.appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    mActivityOfferBinding.collapsingToolbar.setTitle(getResources().getString(R.string.rewards_section));
                    mActivityOfferBinding.toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    //hideOption(R.id.actionHistory);
                    isShow = true;
                } else if (isShow) {
                    mActivityOfferBinding.toolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
                    mActivityOfferBinding.collapsingToolbar.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                    //hideOption(R.id.actionHistory);
                    isShow = true;
                }
            }
        });
        mActivityOfferBinding.historyBtn.setOnClickListener(v -> {
            startActivity(new Intent(OfferActivity.this, OffersHistoryActivity.class));
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_history, menu);
        this.menuHistory = menu;
        return true;
    }

    private void hideOption(int id) {
        MenuItem item = menuHistory.findItem(id);
        item.setVisible(false);
    }

    private void showOption(int id) {
        MenuItem item = menuHistory.findItem(id);
        item.setVisible(true);
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
        switch (item.getItemId()) {

            case R.id.actionHistory:
                startActivity(new Intent(OfferActivity.this, OffersHistoryActivity.class));
                return true;

            case android.R.id.home:
                getBack();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem alertMenuItem = menu.findItem(R.id.actionHistory);
        FrameLayout rootView = (FrameLayout) alertMenuItem.getActionView();
        imgHistory = (ImageView) rootView.findViewById(R.id.imgHistory);
        rootView.setOnClickListener(view -> onOptionsItemSelected(alertMenuItem));
        return super.onPrepareOptionsMenu(menu);
    }

}
