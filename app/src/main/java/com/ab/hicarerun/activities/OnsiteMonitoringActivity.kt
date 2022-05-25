package com.ab.hicarerun.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.ab.hicarerun.BaseApplication
import com.ab.hicarerun.R
import com.ab.hicarerun.databinding.ActivityOnsiteMonitoringBinding
import com.ab.hicarerun.network.models.LoginResponse
import io.realm.RealmResults

class OnsiteMonitoringActivity : AppCompatActivity() {

    lateinit var binding: ActivityOnsiteMonitoringBinding
    private var resourceId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnsiteMonitoringBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loginResponse: RealmResults<LoginResponse> = BaseApplication.getRealm().where(
            LoginResponse::class.java).findAll()
        if (loginResponse != null && loginResponse.size > 0) {
            resourceId = loginResponse[0]?.userID.toString()
        }
        binding.cmtPerformBtn.setOnClickListener {
            val intent = Intent(this, RoachActivity::class.java)
            intent.putExtra("isFromTask", false)
            intent.putExtra("resourceId", resourceId)
            startActivity(intent)
        }

        binding.verifyEqBtn.setOnClickListener {

        }
    }
}