package com.ab.hicarerun.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.R
import com.ab.hicarerun.adapter.roach.RoachAdapter
import com.ab.hicarerun.databinding.ActivityRoachBinding

class RoachActivity : AppCompatActivity() {

    lateinit var binding: ActivityRoachBinding
    lateinit var roachAdapter: RoachAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoachBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        roachAdapter = RoachAdapter(this)
        binding.roachRecyclerView.configure(this)
    }
    private fun getRoachList(){
        TODO("API call to populate list will be here")
    }

    fun RecyclerView.configure(context: Context){
        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        isNestedScrollingEnabled = false
        setHasFixedSize(true)
        adapter = roachAdapter
    }
}