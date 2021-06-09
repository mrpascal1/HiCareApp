package com.ab.hicarerun.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.ab.hicarerun.BaseActivity
import com.ab.hicarerun.R
import com.ab.hicarerun.databinding.ActivityTsscannerBinding
import com.ab.hicarerun.network.NetworkCallController
import com.ab.hicarerun.network.NetworkResponseListner
import com.ab.hicarerun.network.models.TSScannerModel.OrderDetails
import com.ab.hicarerun.utils.LocaleHelper

class TSScannerActivity : BaseActivity() {
    lateinit var binding: ActivityTsscannerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTsscannerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.backIv.setOnClickListener {
            getBack()
        }
        binding.dataCard.setOnClickListener {
        }
        binding.searchBtn.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val orderNoInput = binding.searchEt.text.toString().trim()
            Log.d("TAG", orderNoInput)
            getOrderDetails(orderNoInput)
        }
    }

    private fun populateViews(accountNo: String, orderNo: String, accountName: String,
                              startDate: String, endDate: String, regionName: String,
                              serviceGroup: String, servicePlan: String){
        val data = "Account No  : $accountNo" +
                "\nOrder No     : $orderNo" +
                "\nAccount Name : $accountName" +
                "\nStart Date   : $startDate" +
                "\nEnd Date     : $endDate" +
                "\nRegion Name  : $regionName" +
                "\nService Group: $serviceGroup" +
                "\nService Plan : $servicePlan"
        binding.dataTv.text = data
        binding.progressBar.visibility = View.GONE
    }
    private fun getOrderDetails(orderNoInput: String){
        val controller = NetworkCallController()
        controller.setListner(object : NetworkResponseListner<OrderDetails>{
            override fun onResponse(requestCode: Int, response: OrderDetails?) {
                val success = response?.isSuccess.toString()
                if (success == "true"){
                    val accountNo = response?.data?.accountNo.toString()
                    val orderNo = response?.data?.orderNo.toString()
                    val accountName = response?.data?.accountName.toString()
                    val startDate = response?.data?.startDate.toString()
                    val endDate = response?.data?.endDate.toString()
                    val regionName = response?.data?.regionName.toString()
                    val serviceGroup = response?.data?.serviceGroup.toString()
                    val servicePlan = response?.data?.servicePlan.toString()
                    val barcodeList = response?.data?.barcodeList
                    populateViews(accountNo, orderNo, accountName, startDate, endDate, regionName, serviceGroup, servicePlan)
                }else{
                    binding.progressBar.visibility = View.GONE
                    binding.dataTv.text = "Please Enter Valid Order Number."
                }
            }

            override fun onFailure(requestCode: Int) {
                binding.progressBar.visibility = View.GONE
                binding.dataTv.text = "Error Occurred."
                Log.d("TAG-UAT-Error", requestCode.toString())
            }
        })
        controller.getOrderNoDetails(orderNoInput, "9")
    }
    private fun getBack(){
        val fragment = supportFragmentManager.backStackEntryCount
        if (fragment < 1){
            finish()
        }else{
            fragmentManager.popBackStack()
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                getBack()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun attachBaseContext(context: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(context, LocaleHelper.getLanguage(context)))
    }
}