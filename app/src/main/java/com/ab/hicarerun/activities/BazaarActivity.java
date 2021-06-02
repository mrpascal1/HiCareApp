package com.ab.hicarerun.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ab.hicarerun.BaseActivity;
import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.ActivityBazaarBinding;
import com.ab.hicarerun.fragments.BazaarFragment;
import com.ab.hicarerun.fragments.OffersFragment;
import com.ab.hicarerun.utils.LocaleHelper;
import com.google.android.material.appbar.AppBarLayout;

import java.util.Objects;

public class BazaarActivity extends BaseActivity {
    ActivityBazaarBinding mActivityBazaarBinding;
    Menu menuHistory;
    private ImageView imgHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityBazaarBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_bazaar);
        addFragment(BazaarFragment.newInstance(), "BazaarActivity - BazaarFragment");
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        }
        setSupportActionBar(mActivityBazaarBinding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mActivityBazaarBinding.txtPoints.setTypeface(mActivityBazaarBinding.txtPoints.getTypeface(), Typeface.BOLD);
        mActivityBazaarBinding.txtStar.setTypeface(mActivityBazaarBinding.txtStar.getTypeface(), Typeface.BOLD);
        mActivityBazaarBinding.collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.white));

        mActivityBazaarBinding.collapsingToolbar.setCollapsedTitleTypeface(mActivityBazaarBinding.txtPoints.getTypeface());
        mActivityBazaarBinding.collapsingToolbar.setTitle(" ");

        mActivityBazaarBinding.appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    mActivityBazaarBinding.collapsingToolbar.setTitle(getResources().getString(R.string.rewards_section));
                    mActivityBazaarBinding.toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    showOption(R.id.actionHistory);
                    isShow = true;
                } else if (isShow) {
                    mActivityBazaarBinding.toolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
                    mActivityBazaarBinding.collapsingToolbar.setTitle(" ");//careful there should a space between double quote otherwise it wont work
//                    hideOption(R.id.actionHistory);
                    isShow = true;
                }
            }
        });
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
    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(LocaleHelper.onAttach(base));
        super.attachBaseContext(LocaleHelper.onAttach(base, LocaleHelper.getLanguage(base)));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_history, menu);
        this.menuHistory = menu;
//        hideOption(R.id.actionHistory);
        return true;
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
                startActivity(new Intent(BazaarActivity.this, RewardsHistoryActivity.class));
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
