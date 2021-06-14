package com.ab.hicarerun.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.adapter.BarcodeAdapter
import com.ab.hicarerun.databinding.ActivityTsverificationBinding
import com.ab.hicarerun.network.NetworkCallController
import com.ab.hicarerun.network.NetworkResponseListner
import com.ab.hicarerun.network.models.TSScannerModel.BarcodeList
import com.ab.hicarerun.network.models.TSScannerModel.Data
import com.ab.hicarerun.network.models.TSScannerModel.OrderDetails
import com.ab.hicarerun.utils.AppUtils
import com.ab.hicarerun.utils.LocaleHelper
import com.google.zxing.integration.android.IntentIntegrator

class TSVerificationActivity : AppCompatActivity() {

    lateinit var binding: ActivityTsverificationBinding
    lateinit var modelBarcodeList: ArrayList<BarcodeList>
    lateinit var barcodeAdapter: BarcodeAdapter

    var id: Int? = 0
    var account_No: String? = ""
    var order_No: String? = ""
    var account_Name: String? = ""
    var barcode_Data: String? = ""
    var last_Verified_On: String? = ""
    var last_Verified_By: Int? = null
    var created_On: String? = ""
    var created_By_Id_User: Int? = null
    var verified_By: String? = ""
    var created_By: String? = ""
    var isVerified: Boolean? = null
    var isFetched = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTsverificationBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)

        modelBarcodeList = ArrayList()
        barcodeAdapter = BarcodeAdapter(this, modelBarcodeList)
        val llManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        llManager.stackFromEnd = true
        llManager.reverseLayout = true
        binding.barcodeRecycler.layoutManager = llManager
        binding.barcodeRecycler.setHasFixedSize(true)
        binding.barcodeRecycler.isNestedScrollingEnabled = false
        binding.barcodeRecycler.adapter = barcodeAdapter

        binding.backIv.setOnClickListener {
            getBack()
        }

        binding.scanBtn.setOnClickListener {
            val integrator = IntentIntegrator(this)
            integrator.setBeepEnabled(false)
            if (isFetched == 1){
                integrator.initiateScan()
            }else{
                Toast.makeText(this, "Please Enter Order ID", Toast.LENGTH_SHORT).show()
            }
        }

        binding.searchBtn.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val orderNoInput = binding.searchEt.text.toString().trim()
            Log.d("TAG", orderNoInput)
            getOrderDetails("20031320692")
        }
    }

    private fun populateViews(accountName: String?, regionName: String?, servicePlan: String?){
        binding.dataCard.visibility = View.VISIBLE
        binding.errorTv.visibility = View.GONE
        binding.accountNameTv.text = accountName
        binding.regionNameTv.text = regionName
        binding.servicePlanTv.text = servicePlan
        if (accountName == ""){
            binding.accountNameTv.text = "N/A"
        }
        if (regionName == ""){
            binding.regionNameTv.text = "N/A"
        }
        if (servicePlan == ""){
            binding.servicePlanTv.text = "N/A"
        }
        binding.progressBar.visibility = View.GONE
    }

    private fun getOrderDetails(orderNoInput: String){
        binding.progressBar.visibility = View.VISIBLE
        val controller = NetworkCallController()
        controller.setListner(object : NetworkResponseListner<OrderDetails> {
            override fun onResponse(requestCode: Int, response: OrderDetails?) {
                val success = response?.isSuccess.toString()
                if (success == "true"){
                    isFetched = 1
                    modelBarcodeList.clear()
                    val accountNo = response?.data?.accountNo
                    val orderNo = response?.data?.orderNo
                    val accountName = response?.data?.accountName
                    val startDate = response?.data?.startDate
                    val endDate = response?.data?.endDate
                    val regionName = response?.data?.regionName
                    val serviceGroup = response?.data?.serviceGroup
                    val servicePlan = response?.data?.servicePlan
                    val barcodeList = response?.data?.barcodeList
                    if (response?.data?.barcodeList != null){
                        for (i in 0 until response.data.barcodeList.size) {
                            id = response.data.barcodeList[i].id
                            account_No = response.data.barcodeList[i].account_No
                            order_No = response.data.barcodeList[i].order_No
                            account_Name = response.data.barcodeList[i].account_Name
                            barcode_Data = response.data.barcodeList[i].barcode_Data
                            last_Verified_On = response.data.barcodeList[i].last_Verified_On
                            last_Verified_By = response.data.barcodeList[i].last_Verified_By
                            created_On = response.data.barcodeList[i].created_On
                            created_By_Id_User = response.data.barcodeList[i].created_By_Id_User
                            verified_By = response.data.barcodeList[i].verified_By
                            created_By = response.data.barcodeList[i].created_By
                            isVerified = response.data.barcodeList[i].isVerified
                            modelBarcodeList.add(BarcodeList(id, account_No, order_No, account_Name, barcode_Data, last_Verified_On, last_Verified_By, created_On, created_By_Id_User, verified_By, created_By, isVerified))
                        }
                        OrderDetails(response.isSuccess, Data(accountNo, orderNo, accountName, startDate, endDate, regionName, serviceGroup, servicePlan, modelBarcodeList), response.errorMessage, response.param1, response.responseMessage)
                    }
                    populateViews(accountName, regionName, servicePlan)
                    barcodeAdapter.notifyDataSetChanged()
                    binding.progressBar.visibility = View.GONE
                }else{
                    binding.progressBar.visibility = View.GONE
                    binding.errorTv.text = "Please Enter Valid Order Number."
                    binding.errorTv.visibility = View.VISIBLE
                }
            }

            override fun onFailure(requestCode: Int) {
                binding.progressBar.visibility = View.GONE
                binding.errorTv.text = "Error Occurred."
                Log.d("TAG-UAT-Error", requestCode.toString())
            }
        })
        controller.getOrderNoDetails(orderNoInput, "2056")
    }

    private fun modifyData(id: Int?, account_No: String?, order_No: String?, account_Name: String?,
                           barcode_Data: String?, last_Verified_On: String?, last_Verified_By: Int?,
                           created_On: String?, created_By_Id_User: Int?, verified_By: String?,
                           created_By: String?, isVerified: Boolean?){

        var found = 0
        for (i in 0 until modelBarcodeList.size){
            if(modelBarcodeList[i].barcode_Data == barcode_Data){
                modelBarcodeList[i] = BarcodeList(id, account_No, order_No, account_Name, barcode_Data,
                        last_Verified_On, last_Verified_By, created_On, created_By_Id_User, verified_By, created_By, isVerified)
                barcodeAdapter.notifyItemChanged(i)
                binding.barcodeRecycler.post {
                    binding.barcodeRecycler.smoothScrollToPosition(i)
                }
                found = 1
                Toast.makeText(this, "Barcode Updated", Toast.LENGTH_SHORT).show()
            }
        }
        if (found == 0){
            Toast.makeText(this, "No Barcode found", Toast.LENGTH_SHORT).show()
        }

        /*
        From VerifyBarcodeDetails
         * {
              "BarcodeId": 1,
              "ActivityName": "sample string 2",
              "Account_No": "sample string 3",
              "Order_No": "sample string 4",
              "Barcode_Data": "sample string 5",
              "Lat": "sample string 6",
              "Long": "sample string 7",
              "VerifiedOn": "2021-06-14T15:57:42.3432+05:30",
              "VerifiedBy": 9
            }
        */

        /*
        From GetOrderDetails
        * {
            "Id": 3,
            "Account_No": "809708/06.03.2020",
            "Order_No": "20031320692",
            "Account_Name": "Hotel Grand INN",
            "Barcode_Data": "1234567890",
            "Last_Verified_On": "2021-06-09T11:51:15",
            "Last_Verified_By": 9,
            "Created_On": "2021-06-09T11:51:15.710707",
            "Created_By_Id_User": 2,
            "Verified_By": null,
            "Created_By": "Optimizer",
            "IsVerified": false
          }*/

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        last_Verified_On = AppUtils.currentDateTimeWithTimeZone()
        if (result != null){
            if (result.contents != null){
                modifyData(id, account_No, order_No, account_Name, result.contents, created_On, last_Verified_By,
                    last_Verified_On, created_By_Id_User, verified_By, created_By, true)
                Log.d("TAG-QR", result.contents)
            }else{
                Log.d("TAG-QR", "Not found")
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}