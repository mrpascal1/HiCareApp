package com.ab.hicarerun.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.ab.hicarerun.BaseActivity;
import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.ActivityWelcomeVideoBinding;
import com.ab.hicarerun.fragments.IncentiveFragment;
import com.ab.hicarerun.fragments.VideoPlayerFragment;

public class WelcomeVideoActivity extends BaseActivity {
    ActivityWelcomeVideoBinding mActivityWelcomeVideoBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityWelcomeVideoBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_welcome_video);
        addFragment(VideoPlayerFragment.newInstance(), "IncentivesActivity - IncentiveFragment");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
