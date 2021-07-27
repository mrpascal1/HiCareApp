package com.ab.hicarerun.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ab.hicarerun.BaseActivity
import com.ab.hicarerun.R
import com.ab.hicarerun.databinding.ActivityQuizResultBinding

class QuizResultActivity : BaseActivity() {

    lateinit var binding: ActivityQuizResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizResultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val intent = intent
        binding.scoreTv.text = intent.getStringExtra("points").toString()
        binding.earnedCoins.text = intent.getStringExtra("earned").toString()
        binding.currLevelTv.text = intent.getStringExtra("levelName").toString()
        binding.messageTv.text = intent.getStringExtra("resMessage").toString()

        binding.restartBtn.setOnClickListener {
            /*val quizIntent = Intent(this, ActivityQuizCategory::class.java)
            quizIntent.putExtra("ARG_EVENT", false)
            startActivity(intent)*/
            finish()
        }
    }
}