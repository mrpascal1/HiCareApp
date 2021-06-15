package com.ab.hicarerun.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.BaseActivity
import com.ab.hicarerun.BaseApplication
import com.ab.hicarerun.adapter.BarcodeAdapter
import com.ab.hicarerun.databinding.ActivityTsscannerBinding
import com.ab.hicarerun.network.NetworkCallController
import com.ab.hicarerun.network.NetworkResponseListner
import com.ab.hicarerun.network.models.LoginResponse
import com.ab.hicarerun.network.models.ProfileModel.Profile
import com.ab.hicarerun.network.models.TSScannerModel.BarcodeList
import com.ab.hicarerun.network.models.TSScannerModel.BaseResponse
import com.ab.hicarerun.network.models.TSScannerModel.Data
import com.ab.hicarerun.network.models.TSScannerModel.OrderDetails
import com.ab.hicarerun.utils.AppUtils
import com.ab.hicarerun.utils.LocaleHelper
import com.ab.hicarerun.utils.SharedPreferencesUtility
import com.google.zxing.integration.android.IntentIntegrator
import io.realm.RealmResults

class TSScannerActivity : BaseActivity() {

    lateinit var binding: ActivityTsscannerBinding
    lateinit var modelBarcodeList: ArrayList<BarcodeList>
    lateinit var barcodeAdapter: BarcodeAdapter
    lateinit var progressDialog: ProgressDialog

    var empCode: Int? = null
    var account_No: String? = ""
    var order_No: String? = ""
    var account_Name: String? = ""
    var barcode_Data: String? = ""
    var last_Verified_On: String? = ""
    var last_Verified_By: Int? = null
    var created_On: String? = ""
    var verified_By: String? = ""
    var created_By: String? = ""
    var isVerified: Boolean? = null
    var isFetched = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTsscannerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("HiCare")
        progressDialog.setMessage("Getting details")

        getEmpCode()

        if (empCode == null){
            progressDialog.show()
        }

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
        binding.dataCard.setOnClickListener {
            Log.d("TAG-MODEL", modelBarcodeList.toString())
            Log.d("TAG-TIME", AppUtils.currentDateTimeWithTimeZone())
        }
        binding.searchBtn.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val orderNoInput = binding.searchEt.text.toString().trim()
            Log.d("TAG", orderNoInput)
            getOrderDetails("20031320692")
        }
        binding.fab.setOnClickListener {
            val integrator = IntentIntegrator(this)
            integrator.setBeepEnabled(false)
            if (isFetched == 1){
                integrator.initiateScan()
            }else{
                Toast.makeText(this, "Please Enter Order ID", Toast.LENGTH_SHORT).show()
            }
        }
        binding.saveBtn.setOnClickListener {
            if (modelBarcodeList.isNotEmpty()){
                if (modelBarcodeList.any { it.id == 0 }){
                    saveBarcodeDetails()
                }else{
                    Toast.makeText(applicationContext, "Please add new barcodes", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(applicationContext, "No barcode found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveBarcodeDetails(){
        binding.progressBar.visibility = View.VISIBLE
        val controller = NetworkCallController()
        controller.setListner(object : NetworkResponseListner<BaseResponse>{
            override fun onResponse(requestCode: Int, response: BaseResponse?) {
                if (response != null){
                    val isSuccess = response.isSuccess
                    val data = response.data.toString()
                    if (isSuccess == true){
                        if (data == "Success"){
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(applicationContext, "Data Saved Successfully", Toast.LENGTH_SHORT).show()
                            modelBarcodeList.clear()
                            getOrderDetails(order_No.toString())
                        }
                    }
                }
                barcodeAdapter.notifyDataSetChanged()
            }
            override fun onFailure(requestCode: Int) {
                binding.progressBar.visibility = View.GONE
            }

        })
        controller.saveBarcodeList(20211, modelBarcodeList)
    }

    private fun populateViews(accountNo: String?, orderNo: Long?, accountName: String?,
                              startDate: String?, endDate: String?, regionName: String?,
                              serviceGroup: String?, servicePlan: String?){
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
        controller.setListner(object : NetworkResponseListner<OrderDetails>{
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
                            val id = response.data.barcodeList[i].id
                            account_No = response.data.barcodeList[i].account_No
                            order_No = response.data.barcodeList[i].order_No
                            account_Name = response.data.barcodeList[i].account_Name
                            barcode_Data = response.data.barcodeList[i].barcode_Data
                            last_Verified_On = response.data.barcodeList[i].last_Verified_On
                            last_Verified_By = response.data.barcodeList[i].last_Verified_By
                            created_On = response.data.barcodeList[i].created_On
                            val created_By_Id_User = response.data.barcodeList[i].created_By_Id_User
                            verified_By = response.data.barcodeList[i].verified_By
                            created_By = response.data.barcodeList[i].created_By
                            isVerified = response.data.barcodeList[i].isVerified
                            modelBarcodeList.add(BarcodeList(id, account_No, order_No, account_Name, barcode_Data, last_Verified_On, last_Verified_By, created_On, created_By_Id_User, verified_By, created_By, isVerified))
                        }
                        OrderDetails(response.isSuccess, Data(accountNo, orderNo, accountName, startDate, endDate, regionName, serviceGroup, servicePlan, modelBarcodeList), response.errorMessage, response.param1, response.responseMessage)
                    }
                    populateViews(accountNo, orderNo, accountName, startDate, endDate, regionName, serviceGroup, servicePlan)
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
        controller.getOrderNoDetails(orderNoInput, empCode.toString())
    }

    private fun getEmpCode(){
        val userId = SharedPreferencesUtility.getPrefString(this, SharedPreferencesUtility.PREF_USERID)
        val controller = NetworkCallController()
        controller.setListner(object : NetworkResponseListner<Any>{
            override fun onResponse(requestCode: Int, response: Any) {
                progressDialog.dismiss()
                val responseProfile: Profile = response as Profile
                empCode = responseProfile.employeeCode.toInt()
                Log.d("TAG-profile", empCode.toString())
            }
            override fun onFailure(requestCode: Int) {
                progressDialog.dismiss()
                Log.d("TAG", requestCode.toString())
            }
        })
        controller.getTechnicianProfile(1000, userId)
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

    private fun addNewData(account_No: String?, order_No: String?, account_Name: String?,
                           barcode_Data: String?, last_Verified_On: String?, last_Verified_By: Int?,
                           created_On: String?, created_By_Id_User: Int?, verified_By: String?,
                           created_By: String?, isVerified: Boolean?){

        var found = 0
        for (i in 0 until modelBarcodeList.size){
            if(modelBarcodeList[i].barcode_Data == barcode_Data){
                found = 1
                Toast.makeText(this, "Barcode Already Inserted", Toast.LENGTH_SHORT).show()
            }
        }
        if (found == 0){
            modelBarcodeList.add(BarcodeList(0, account_No, order_No, account_Name, barcode_Data,
                last_Verified_On, last_Verified_By, created_On, created_By_Id_User, verified_By,
                created_By, isVerified))
            barcodeAdapter.notifyItemInserted(modelBarcodeList.lastIndex)
            binding.barcodeRecycler.post {
                binding.barcodeRecycler.smoothScrollToPosition(barcodeAdapter.itemCount - 1)
            }
            Toast.makeText(this, "Barcode Added", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        val currentDateTime = AppUtils.currentDateTimeWithTimeZone()
        if (result != null){
            if (result.contents != null){
                Toast.makeText(this, ""+SharedPreferencesUtility.getPrefString(this, SharedPreferencesUtility.PREF_USERID), Toast.LENGTH_SHORT).show()
                addNewData(account_No, order_No, account_Name, result.contents, currentDateTime,
                    last_Verified_By, currentDateTime, empCode,
                    verified_By, "Optimizer", false)
                Log.d("TAG-QR", result.contents)
            }else{
                Log.d("TAG-QR", "Not found")
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}