package com.ab.hicarerun.activities;

import android.os.Build
import android.os.Bundle;
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.ab.hicarerun.BaseActivity;
import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.ActivityQuizBinding;
import com.ab.hicarerun.fragments.QuizFragment

class QuizActivity : BaseActivity() {

    lateinit var binding: ActivityQuizBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorPurple)
        }

        val intent = intent
        val puzzleId = intent.getIntExtra("puzzleId", -1)
        replaceFragment(QuizFragment.newInstance(puzzleId), "QuizActivity - QuizFragment")
    }
}

