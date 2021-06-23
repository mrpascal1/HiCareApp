package com.ab.hicarerun.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.BaseActivity
import com.ab.hicarerun.BaseApplication
import com.ab.hicarerun.adapter.BarcodeAdapter
import com.ab.hicarerun.databinding.ActivityTsscannerBinding
import com.ab.hicarerun.handler.OnBarcodeCountListener
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
    var last_Verified_By: Int? = 0
    var created_On: String? = ""
    var verified_By: String? = ""
    var created_By: String? = "Optimizer"
    var isVerified: Boolean? = false
    var isFetched = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTsscannerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.accountNameTv.setTypeface(null, Typeface.BOLD)
        binding.regionNameTv.setTypeface(null, Typeface.NORMAL)
        binding.servicePlanTv.setTypeface(null, Typeface.NORMAL)
        binding.boxesTitleTv.setTypeface(null, Typeface.BOLD)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("HiCare")
        progressDialog.setMessage("Getting details")

        val loginResponse: RealmResults<LoginResponse> = BaseApplication.getRealm().where(LoginResponse::class.java).findAll()
        if (loginResponse != null && loginResponse.size > 0){
            empCode = loginResponse[0]?.id?.toInt()
            Log.d("TAG-Login", empCode.toString())
        }

        //getEmpCode()

        if (empCode == null){
            progressDialog.show()
        }

        modelBarcodeList = ArrayList()
        barcodeAdapter = BarcodeAdapter(this, modelBarcodeList, "TSScanner")
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
            if (orderNoInput != ""){
                getOrderDetails(orderNoInput)
            }else{
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Please Enter Order No", Toast.LENGTH_SHORT).show()
            }
        }
        binding.fab.setOnClickListener {
            val integrator = IntentIntegrator(this)
            integrator.setCaptureActivity(CaptureActivityPortrait::class.java)
            integrator.setBeepEnabled(false)
            integrator.setPrompt("Scan a barcode")
            if (isFetched == 1){
                integrator.initiateScan()
            }else{
                Toast.makeText(this, "Please Enter Order No", Toast.LENGTH_SHORT).show()
            }
        }
        binding.saveBtn.setOnClickListener {
            if (modelBarcodeList.isNotEmpty()){
                if (modelBarcodeList.any { it.id == 0 }){
                    saveBarcodeDetails()
                    Log.d("TAG-Save", modelBarcodeList.toString())
                }else{
                    Toast.makeText(applicationContext, "Please add new barcodes", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(applicationContext, "No barcode found", Toast.LENGTH_SHORT).show()
            }
        }

        barcodeAdapter.setOnBarcodeCountListener(object : OnBarcodeCountListener{
            override fun onBarcodeCountListener(count: Int) {
                if (count == 0){
                    binding.barcodeErrorTv.visibility = View.VISIBLE
                }else{
                    binding.barcodeErrorTv.visibility = View.GONE
                }
            }
        })
    }

    private fun saveBarcodeDetails(){
        binding.progressBar.visibility = View.VISIBLE
        binding.searchBtn.isEnabled = false
        val controller = NetworkCallController()
        controller.setListner(object : NetworkResponseListner<BaseResponse>{
            override fun onResponse(requestCode: Int, response: BaseResponse?) {
                if (response != null){
                    val isSuccess = response.isSuccess
                    val data = response.data.toString()
                    if (isSuccess == true){
                        if (data == "Success"){
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(applicationContext, "Barcode is already saved", Toast.LENGTH_SHORT).show()
                            getOrderDetails(order_No.toString())
                        }else{
                            binding.searchBtn.isEnabled = true
                        }
                    }else{
                        binding.searchBtn.isEnabled = true
                    }
                }
                barcodeAdapter.notifyDataSetChanged()
            }
            override fun onFailure(requestCode: Int) {
                binding.progressBar.visibility = View.GONE
                binding.searchBtn.isEnabled = true
            }

        })
        controller.saveBarcodeList(20211, modelBarcodeList)
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
        binding.errorTv.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        val controller = NetworkCallController()
        controller.setListner(object : NetworkResponseListner<OrderDetails>{
            override fun onResponse(requestCode: Int, response: OrderDetails?) {
                modelBarcodeList.clear()
                binding.searchBtn.isEnabled = true
                val success = response?.isSuccess.toString()
                if (success == "true"){
                    isFetched = 1
                    account_No = response?.data?.accountNo
                    order_No = response?.data?.orderNo.toString()
                    val orderNo = response?.data?.orderNo
                    account_Name = response?.data?.accountName
                    val startDate = response?.data?.startDate
                    val endDate = response?.data?.endDate
                    val regionName = response?.data?.regionName
                    val serviceGroup = response?.data?.serviceGroup
                    val servicePlan = response?.data?.servicePlan
                    val barcodeList = response?.data?.barcodeList
                    if (response?.data?.barcodeList != null){
                        var itemsCount = 0
                        for (i in 0 until response.data.barcodeList.size) {
                            itemsCount++
                            val id = response.data.barcodeList[i].id
                            account_No = response.data.barcodeList[i].account_No
                            order_No = response.data.barcodeList[i].order_No
                            val account_Name = response.data.barcodeList[i].account_Name
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
                        OrderDetails(response.isSuccess, Data(account_No, orderNo, account_Name, startDate, endDate, regionName, serviceGroup, servicePlan, modelBarcodeList), response.errorMessage, response.param1, response.responseMessage)
                        if (itemsCount > 0){
                            binding.barcodeErrorTv.visibility = View.GONE
                        }else{
                            binding.barcodeErrorTv.visibility = View.VISIBLE
                        }
                    }
                    populateViews(account_Name, regionName, servicePlan)
                    barcodeAdapter.notifyDataSetChanged()
                    binding.progressBar.visibility = View.GONE
                }else{
                    modelBarcodeList.clear()
                    binding.searchBtn.isEnabled = true
                    binding.progressBar.visibility = View.GONE
                    binding.errorTv.text = "Please Enter Valid Order Number."
                    binding.errorTv.visibility = View.VISIBLE
                    binding.dataCard.visibility = View.GONE
                }
            }

            override fun onFailure(requestCode: Int) {
                modelBarcodeList.clear()
                binding.progressBar.visibility = View.GONE
                binding.dataCard.visibility = View.GONE
                binding.errorTv.text = "Error Occurred."
                binding.errorTv.visibility = View.VISIBLE
                Log.d("TAG-UAT-Error", requestCode.toString())
            }
        })
        controller.getOrderNoDetails(orderNoInput, empCode.toString())
    }

    /*private fun getEmpCode(){
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
    }*/

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
                Toast.makeText(this, "Barcode is already added", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(this, "Barcode is added", Toast.LENGTH_SHORT).show()
        }
        if (modelBarcodeList.isEmpty()){
            binding.barcodeErrorTv.visibility = View.VISIBLE
        }else{
            binding.barcodeErrorTv.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        val currentDateTime = AppUtils.currentDateTimeWithTimeZone()
        if (result != null){
            if (result.contents != null){
                //Toast.makeText(this, ""+SharedPreferencesUtility.getPrefString(this, SharedPreferencesUtility.PREF_USERID), Toast.LENGTH_SHORT).show()
                addNewData(
                    account_No = account_No,
                    order_No = order_No,
                    account_Name = account_Name,
                    barcode_Data = result.contents,
                    last_Verified_On = currentDateTime,
                    last_Verified_By = last_Verified_By,
                    created_On = currentDateTime,
                    created_By_Id_User = empCode,
                    verified_By = empCode.toString(),
                    created_By = created_By,
                    isVerified = isVerified)
                Log.d("TAG-QR", result.contents)
            }else{
                Log.d("TAG-QR", "Not found")
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}