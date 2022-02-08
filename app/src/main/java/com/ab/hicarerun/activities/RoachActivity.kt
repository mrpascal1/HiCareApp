package com.ab.hicarerun.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ab.hicarerun.R
import com.ab.hicarerun.databinding.ActivityRoachBinding

class RoachActivity : AppCompatActivity() {

    lateinit var binding: ActivityRoachBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoachBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun getList(){

    }
}