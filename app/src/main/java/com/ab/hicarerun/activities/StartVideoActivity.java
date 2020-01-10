package com.ab.hicarerun.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.ab.hicarerun.BaseActivity;
import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.ActivityStartVideoBinding;
import com.ab.hicarerun.fragments.StartVideoFragment;
import com.ab.hicarerun.fragments.VideoPlayerFragment;

public class StartVideoActivity extends BaseActivity {
ActivityStartVideoBinding mActivityStartVideoBinding;
    public static final String ARG_USER = "ARG_USER";
    public static final String ARG_URL = "ARG_URL";
    private String username = "";
    private String URL = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityStartVideoBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_start_video);
        username = getIntent().getStringExtra(ARG_USER);
        URL = getIntent().getStringExtra(ARG_URL);
        addFragment(StartVideoFragment.newInstance(username, URL), "StartVideoActivity - StartVideoFragment");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
