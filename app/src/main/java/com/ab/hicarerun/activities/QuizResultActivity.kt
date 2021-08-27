package com.ab.hicarerun.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ab.hicarerun.BaseActivity
import com.ab.hicarerun.R
import com.ab.hicarerun.databinding.ActivityQuizResultBinding
import com.squareup.picasso.Picasso
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size

class QuizResultActivity : BaseActivity() {

    lateinit var binding: ActivityQuizResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizResultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val intent = intent
        val currLID = intent.getStringExtra("currLID").toString()
        val upgrdLID = intent.getStringExtra("upgrdLID").toString()
        val currLIC = intent.getStringExtra("currLIC").toString()
        val upgrdLIC = intent.getStringExtra("upgrdLIC").toString()
        val upgrdLN = intent.getStringExtra("upGrdLevelName").toString()
        binding.scoreTv.text = intent.getStringExtra("points").toString()
        binding.earnedCoins.text = intent.getStringExtra("earned").toString()
        //binding.currLevelTv.text = intent.getStringExtra("levelName").toString()
        binding.currLevelTv.text = upgrdLN
        binding.messageTv.text = intent.getStringExtra("resMessage").toString()

        if (currLID != upgrdLID){
            binding.levelUpgrdTv.visibility = View.VISIBLE
            Picasso.get().load(upgrdLIC).placeholder(R.drawable.ic_level_common).into(binding.imgBadge)
        }else{
            binding.levelUpgrdTv.visibility = View.GONE
            Picasso.get().load(currLIC).placeholder(R.drawable.ic_level_common).into(binding.imgBadge)
        }

        if (intent.getStringExtra("earned").toString() != "0") {
            binding.viewKonfetti.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(5f, 10f)
                .setFadeOutEnabled(true)
                .setTimeToLive(1000L)
                .addShapes(Shape.Square, Shape.Circle)
                .addSizes(Size(12))
                .setPosition(-50f, binding.viewKonfetti.width + 50f, -50f, -50f)
                .streamFor(300, 5000L)
        }

        binding.restartBtn.setOnClickListener {
            /*val quizIntent = Intent(this, ActivityQuizCategory::class.java)
            quizIntent.putExtra("ARG_EVENT", false)
            startActivity(intent)*/
            finish()
        }
    }
}