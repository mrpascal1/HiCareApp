package com.ab.hicarerun.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.ab.hicarerun.BaseActivity;
import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.ActivityQuizCategoryBinding;
import com.ab.hicarerun.fragments.FragmentQuizCategory;
import com.ab.hicarerun.fragments.KarmaFragment;
import com.ab.hicarerun.fragments.KarmaVideoFragment;

import java.util.Objects;

public class ActivityQuizCategory extends BaseActivity {
    ActivityQuizCategoryBinding mActivityQuizCategoryBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityQuizCategoryBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_quiz_category);
        addFragment(FragmentQuizCategory.newInstance(), "ActivityQuizCategory - FragmentQuizCategory");

        setSupportActionBar(mActivityQuizCategoryBinding.toolbar);
        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPurple));
        }
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mActivityQuizCategoryBinding.leaderBoardIv.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), QuizLeaderBoard.class);
            startActivity(intent);
        });
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
        } else if(fragment == 1){
            if(KarmaVideoFragment.isBack){
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