package com.ab.hicarerun.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ab.hicarerun.R
import com.ab.hicarerun.databinding.ActivityOnsiteMonitoringBinding

class OnsiteMonitoringActivity : AppCompatActivity() {

    lateinit var binding: ActivityOnsiteMonitoringBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnsiteMonitoringBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnFinish.setOnClickListener {
            val intent = Intent(this, RoachActivity::class.java)
            intent.putExtra("isFromTask", false)
            startActivity(intent)
        }
    }
}