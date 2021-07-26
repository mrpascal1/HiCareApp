package com.ab.hicarerun.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.ab.hicarerun.BaseActivity;
import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.ActivityQuizBinding;

public class QuizActivity extends BaseActivity {
    ActivityQuizBinding mActivityQuizBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        /*mActivityQuizBinding = DataBindingUtil.setContentView(this, R.layout.activity_quiz);
        addFragment();*/
    }
}