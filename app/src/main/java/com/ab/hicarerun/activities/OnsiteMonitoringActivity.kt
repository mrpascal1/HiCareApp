package com.ab.hicarerun.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.ab.hicarerun.BaseActivity
import com.ab.hicarerun.BaseApplication
import com.ab.hicarerun.R
import com.ab.hicarerun.databinding.ActivityOnsiteMonitoringBinding
import com.ab.hicarerun.network.NetworkCallController
import com.ab.hicarerun.network.NetworkResponseListner
import com.ab.hicarerun.network.models.LoginResponse
import com.ab.hicarerun.network.models.tsscannermodel.BaseResponse
import io.realm.RealmResults

class OnsiteMonitoringActivity : BaseActivity() {

    lateinit var binding: ActivityOnsiteMonitoringBinding
    private var resourceId = ""
    private var accountNo = ""

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
            getAccountNoByResourceIdForOnsite(resourceId)
        }
    }

    private fun getAccountNoByResourceIdForOnsite(resourceId: String){
        showProgressDialog()
        val controller = NetworkCallController()
        controller.setListner(object : NetworkResponseListner<BaseResponse>{
            override fun onResponse(requestCode: Int, response: BaseResponse?) {
                dismissProgressDialog()
                if (response?.isSuccess == true){
                    val data = response.data
                    if (data != ""){
                        accountNo = data.toString()
                        val intent = Intent(applicationContext, BarcodeVerificatonActivity::class.java)
                        intent.putExtra("isFromTask", false)
                        intent.putExtra("accountNo", accountNo)
                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(requestCode: Int) {
                dismissProgressDialog()
                Log.d("TAG", "onFailure: Failed getting account no")
            }
        })
        controller.getAccountNoByResourceIdForOnsite(2705, resourceId)
    }
}