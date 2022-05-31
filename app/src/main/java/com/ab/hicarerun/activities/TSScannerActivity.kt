package com.ab.hicarerun.activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatSpinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.BaseActivity
import com.ab.hicarerun.BaseApplication
import com.ab.hicarerun.R
import com.ab.hicarerun.adapter.BarcodeAdapter
import com.ab.hicarerun.databinding.ActivityTsscannerBinding
import com.ab.hicarerun.handler.OnBarcodeCountListener
import com.ab.hicarerun.network.NetworkCallController
import com.ab.hicarerun.network.NetworkResponseListner
import com.ab.hicarerun.network.models.LoginResponse
import com.ab.hicarerun.network.models.tsscannermodel.*
import com.ab.hicarerun.utils.AppUtils
import com.ab.hicarerun.utils.LocaleHelper
import com.google.zxing.integration.android.IntentIntegrator
import io.realm.RealmResults

class TSScannerActivity : BaseActivity() {

    lateinit var binding: ActivityTsscannerBinding
    lateinit var modelBarcodeList: ArrayList<BarcodeList>
    lateinit var pestList: ArrayList<Pest_Type>
    lateinit var serviceUnit: ArrayList<BarcodeType>
    lateinit var barcodeType: List<BarcodeType>
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
    var barcode_Type: String? = ""
    var isVerified: Boolean? = false
    var selectedType = ""
    var selectedUnit = ""
    var additional_Info = ""
    var isFetched = 0
    var requestFrom = 0
    lateinit var promptsView: View

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
            created_By = loginResponse[0]?.userName.toString()
            Log.d("TAG-Login", empCode.toString())
        }

        //getEmpCode()

        if (empCode == null){
            progressDialog.show()
        }

        modelBarcodeList = ArrayList()
        pestList = ArrayList()
        barcodeType = ArrayList()
        serviceUnit = ArrayList()
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
                getOrderDetailsByAccountNo(orderNoInput)
            }else{
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Please Enter Customer ID", Toast.LENGTH_SHORT).show()
            }
        }
        binding.fab.setOnClickListener {
            val integrator = IntentIntegrator(this)
            integrator.setCaptureActivity(CaptureActivityPortrait::class.java)
            integrator.setBeepEnabled(false)
            integrator.setPrompt("Scan a barcode")
            if (isFetched == 1){
                integrator.initiateScan()
                requestFrom = 1
            }else{
                Toast.makeText(this, "Please Enter Customer ID", Toast.LENGTH_SHORT).show()
            }
        }
        binding.saveBtn.setOnClickListener {
            binding.saveBtn.isEnabled = false
            if (modelBarcodeList.isNotEmpty()){
                if (modelBarcodeList.any { it.id == 0 }){
                    saveBarcodeDetails()
                    Log.d("TAG-Save", modelBarcodeList.toString())
                }else{
                    binding.saveBtn.isEnabled = true
                    Toast.makeText(applicationContext, "Please add new equipment", Toast.LENGTH_SHORT).show()
                }
            }else{
                binding.saveBtn.isEnabled = true
                binding.saveBtn.visibility = View.GONE
                Toast.makeText(applicationContext, "Equipment not found", Toast.LENGTH_SHORT).show()
            }
        }

        binding.deleteFab.setOnClickListener {
            val integrator = IntentIntegrator(this)
            integrator.setCaptureActivity(CaptureActivityPortrait::class.java)
            integrator.setBeepEnabled(false)
            integrator.setPrompt("Scan a barcode")
            if (isFetched == 1){
                integrator.initiateScan()
                requestFrom = 2
            }else{
                Toast.makeText(this, "Please Enter Account No", Toast.LENGTH_SHORT).show()
            }
        }
        barcodeAdapter.setOnBarcodeCountListener(object : OnBarcodeCountListener{
            override fun onBarcodeCountListener(count: Int) {
                binding.boxesTitleTv.text = "Equipments: (${modelBarcodeList.size})"
                if (count == 0){
                    binding.barcodeErrorTv.visibility = View.VISIBLE
                    binding.saveBtn.visibility = View.GONE
                }else{
                    binding.barcodeErrorTv.visibility = View.GONE
                    binding.saveBtn.visibility = View.VISIBLE
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
                            Toast.makeText(applicationContext, "Saved successfully", Toast.LENGTH_SHORT).show()
                            getOrderDetailsByAccountNo(account_No.toString())
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
        binding.accountNameTv.text = "$accountName - $account_No"
        binding.regionNameTv.text = regionName
        binding.servicePlanTv.text = servicePlan
        binding.regionNameLayout.visibility = View.GONE
        binding.servicePlanLayout.visibility = View.GONE

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
                pestList.clear()
                serviceUnit.clear()
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
                    val su = response?.data?.service_Units
                    if (!su.isNullOrEmpty()){
                        serviceUnit.addAll(su)
                    }
                    if (!response?.data?.barcodeType.isNullOrEmpty()) {
                        barcodeType = response?.data?.barcodeType!!
                    }

                    val barcodeList = response?.data?.barcodeList
                    if (response?.data?.barcodeList != null){
                        var itemsCount = 0
                        if (barcodeList != null) {
                            modelBarcodeList.addAll(barcodeList)
                        }
                        for (i in 0 until response.data.barcodeList.size) {
                            itemsCount++
                        }
                        OrderDetails(response.isSuccess, response.data, response.errorMessage, response.param1, response.responseMessage)
                        if (itemsCount > 0){
                            binding.barcodeErrorTv.visibility = View.GONE
                            binding.saveBtn.visibility = View.VISIBLE
                        }else{
                            binding.barcodeErrorTv.visibility = View.VISIBLE
                            binding.saveBtn.visibility = View.GONE
                        }
                        binding.boxesTitleTv.text = "Equipments: (${modelBarcodeList.size})"
                    }
                    populateViews(account_Name, regionName, servicePlan)
                    barcodeAdapter.notifyDataSetChanged()
                    binding.progressBar.visibility = View.GONE
                    binding.saveBtn.isEnabled = true
                }else{
                    modelBarcodeList.clear()
                    binding.searchBtn.isEnabled = true
                    binding.saveBtn.isEnabled = true
                    binding.progressBar.visibility = View.GONE
                    binding.errorTv.text = "Please Enter Valid Order Number."
                    binding.errorTv.visibility = View.VISIBLE
                    binding.saveBtn.visibility = View.GONE
                    binding.dataCard.visibility = View.GONE
                }
            }

            override fun onFailure(requestCode: Int) {
                modelBarcodeList.clear()
                binding.saveBtn.isEnabled = true
                binding.progressBar.visibility = View.GONE
                binding.dataCard.visibility = View.GONE
                binding.saveBtn.visibility = View.GONE
                binding.errorTv.text = "Error Occurred."
                binding.errorTv.visibility = View.VISIBLE
                Log.d("TAG-UAT-Error", requestCode.toString())
            }
        })
        controller.getOrderNoDetails(orderNoInput, empCode.toString())
    }

    private fun getOrderDetailsByAccountNo(orderNoInput: String){
        binding.errorTv.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        val controller = NetworkCallController()
        controller.setListner(object : NetworkResponseListner<OrderDetails>{
            override fun onResponse(requestCode: Int, response: OrderDetails?) {
                modelBarcodeList.clear()
                pestList.clear()
                serviceUnit.clear()
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
                    val su = response?.data?.service_Units
                    if (!su.isNullOrEmpty()){
                        serviceUnit.addAll(su)
                    }
                    if (!response?.data?.barcodeType.isNullOrEmpty()) {
                        barcodeType = response?.data?.barcodeType!!
                    }

                    val barcodeList = response?.data?.barcodeList
                    if (response?.data?.barcodeList != null){
                        var itemsCount = 0
                        if (barcodeList != null) {
                            modelBarcodeList.addAll(barcodeList)
                        }
                        for (i in 0 until response.data.barcodeList.size) {
                            itemsCount++
                        }
                        OrderDetails(response.isSuccess, response.data, response.errorMessage, response.param1, response.responseMessage)
                        if (itemsCount > 0){
                            binding.barcodeErrorTv.visibility = View.GONE
                            binding.saveBtn.visibility = View.VISIBLE
                        }else{
                            binding.barcodeErrorTv.visibility = View.VISIBLE
                            binding.saveBtn.visibility = View.GONE
                        }
                        binding.boxesTitleTv.text = "Equipments: (${modelBarcodeList.size})"
                    }
                    populateViews(account_Name, regionName, servicePlan)
                    barcodeAdapter.notifyDataSetChanged()
                    binding.progressBar.visibility = View.GONE
                    binding.saveBtn.isEnabled = true
                }else{
                    modelBarcodeList.clear()
                    binding.searchBtn.isEnabled = true
                    binding.saveBtn.isEnabled = true
                    binding.progressBar.visibility = View.GONE
                    binding.errorTv.text = "Please Enter Valid Customer ID."
                    binding.errorTv.visibility = View.VISIBLE
                    binding.saveBtn.visibility = View.GONE
                    binding.dataCard.visibility = View.GONE
                }
            }

            override fun onFailure(requestCode: Int) {
                modelBarcodeList.clear()
                binding.saveBtn.isEnabled = true
                binding.progressBar.visibility = View.GONE
                binding.dataCard.visibility = View.GONE
                binding.saveBtn.visibility = View.GONE
                binding.errorTv.text = "Error Occurred."
                binding.errorTv.visibility = View.VISIBLE
                Log.d("TAG-UAT-Error", requestCode.toString())
            }
        })
        controller.getBarcodeDetailsByAccount(orderNoInput, empCode.toString())
    }

    private fun getScannedQR(barcode_Data: String){
        var found = 0
        for (i in 0 until modelBarcodeList.size){
            if (modelBarcodeList[i].barcode_Data == barcode_Data){
                found = 1
                binding.barcodeRecycler.post {
                    binding.barcodeRecycler.smoothScrollToPosition(i)
                }
                modelBarcodeList[i].callForDelete = "yes"
            }else{
                modelBarcodeList[i].callForDelete = "no"
            }
        }
        if (found == 0){
            Toast.makeText(this, "Equipment not found", Toast.LENGTH_SHORT).show()
        }
        barcodeAdapter.notifyDataSetChanged()
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


    private fun checkBarcode(barcode_Data: String, currentDateTime: String){
        var found = false
        for (i in 0 until modelBarcodeList.size){
            if(modelBarcodeList[i].barcode_Data == barcode_Data){
                found = true
                Toast.makeText(this, "Already exists", Toast.LENGTH_SHORT).show()
                break
            }
        }
        if (!found){
            showPestTypeDialog(barcode_Data, currentDateTime)
        }
    }
    private fun addNewData(account_No: String?, order_No: String?, account_Name: String?,
                           barcode_Data: String?, last_Verified_On: String?, last_Verified_By: Int?,
                           created_On: String?, created_By_Id_User: Int?, verified_By: String?,
                           created_By: String?, service_Unit: String, additional_Info: String, pestList: ArrayList<Pest_Type>?, isVerified: Boolean?, barcode_Type: String?){

        var found = 0
        for (i in 0 until modelBarcodeList.size){
            if(modelBarcodeList[i].barcode_Data == barcode_Data){
                found = 1
                Toast.makeText(this, "Already exists", Toast.LENGTH_SHORT).show()
            }
        }
        if (found == 0){
            modelBarcodeList.add(BarcodeList(0, account_No, order_No, account_Name, barcode_Data,
                last_Verified_On, last_Verified_By, created_On, created_By_Id_User, verified_By,
                created_By, isVerified, barcode_Type, service_Unit, additional_Info, pestList, "no"))
            barcodeAdapter.notifyItemInserted(modelBarcodeList.lastIndex)
            binding.barcodeRecycler.post {
                binding.barcodeRecycler.smoothScrollToPosition(barcodeAdapter.itemCount - 1)
            }
            Toast.makeText(this, "Scanned successfully", Toast.LENGTH_SHORT).show()
        }
        if (modelBarcodeList.isEmpty()){
            binding.barcodeErrorTv.visibility = View.VISIBLE
            binding.saveBtn.visibility = View.GONE
        }else{
            binding.barcodeErrorTv.visibility = View.GONE
            binding.saveBtn.visibility = View.VISIBLE
        }
        binding.boxesTitleTv.text = "Equipments: (${modelBarcodeList.size})"
    }

    private fun showPestTypeDialog(contents: String, currentDateTime: String){
        val li = LayoutInflater.from(this)
        promptsView = li.inflate(R.layout.add_barcode_type_dialog, null)
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(promptsView)
        val alertDialog = alertDialogBuilder.create()
        val spnType = promptsView.findViewById(R.id.spnType) as AppCompatSpinner
        val suSpnType = promptsView.findViewById(R.id.suSpnType) as AppCompatSpinner
        val cancelBtn = promptsView.findViewById(R.id.btnCancel) as AppCompatButton
        val okBtn = promptsView.findViewById(R.id.okBtn) as AppCompatButton
        val remarksEt = promptsView.findViewById(R.id.remarksEt) as EditText
        okBtn.isEnabled = false
        okBtn.alpha = 0.6f

        val bType = ArrayList<String>()
        val su = ArrayList<String>()
        bType.clear()
        su.clear()
        bType.add("Select Type")
        su.add("Select Unit")
        barcodeType.forEach {
            bType.add(it.value.toString())
        }
        serviceUnit.forEach {
            su.add(it.value.toString())
        }
        val arrayAdapter = object : ArrayAdapter<String>(this, R.layout.spinner_layout_new, bType){
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                val tv = view as TextView
                if (position == 0){
                    tv.setTextColor(Color.GRAY)
                }else{
                    tv.setTextColor(Color.BLACK)
                }
                return view
            }
        }
        arrayAdapter.setDropDownViewResource(R.layout.spinner_popup)
        spnType.adapter = arrayAdapter

        spnType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedType = spnType.selectedItem.toString()
                if (selectedType != "Select Type"){
                    if (selectedUnit != "Select Unit" && selectedUnit != "") {
                        okBtn.isEnabled = true
                        okBtn.alpha = 1f
                    }else{
                        okBtn.isEnabled = false
                        okBtn.alpha = 0.6f
                    }
                }
                //Log.d("TAG", selectedType)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        /**
         * Adapter for Service Unit
        * */
        val suArrayAdapter = object : ArrayAdapter<String>(this, R.layout.spinner_layout_new, su){
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                val tv = view as TextView
                if (position == 0){
                    tv.setTextColor(Color.GRAY)
                }else{
                    tv.setTextColor(Color.BLACK)
                }
                return view
            }
        }
        suArrayAdapter.setDropDownViewResource(R.layout.spinner_popup)
        suSpnType.adapter = suArrayAdapter
        suSpnType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedUnit = suSpnType.selectedItem.toString()
                if (selectedUnit != "Select Unit"){
                    if (selectedType != "Select Type" && selectedType != "") {
                        okBtn.isEnabled = true
                        okBtn.alpha = 1f
                    }else{
                        okBtn.isEnabled = false
                        okBtn.alpha = 0.6f
                    }
                }
                //Log.d("TAG", selectedType)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        okBtn.setOnClickListener {
            additional_Info = remarksEt.text.toString().trim()
            Log.d("TAG", selectedType)
            if (selectedType != "Select Type"){
                addNewData(
                    account_No = account_No,
                    order_No = order_No,
                    account_Name = account_Name,
                    barcode_Data = contents,
                    last_Verified_On = currentDateTime,
                    last_Verified_By = last_Verified_By,
                    created_On = currentDateTime,
                    created_By_Id_User = empCode,
                    verified_By = empCode.toString(),
                    created_By = created_By,
                    isVerified = isVerified,
                    barcode_Type = selectedType,
                    service_Unit = selectedUnit,
                    additional_Info = additional_Info,
                    pestList = pestList)
                alertDialog.cancel()
            }
        }
        cancelBtn.setOnClickListener {
            alertDialog.cancel()
        }
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        val currentDateTime = AppUtils.currentDateTimeWithTimeZone()
        if (result != null){
            if (result.contents != null){
                //Toast.makeText(this, ""+SharedPreferencesUtility.getPrefString(this, SharedPreferencesUtility.PREF_USERID), Toast.LENGTH_SHORT).show()
                if (requestFrom == 1){
                    checkBarcode(result.contents, currentDateTime)
                Log.d("TAG-QR", result.contents)
                }
                if (requestFrom == 2){
                    getScannedQR(result.contents)
                }
            }else{
                Log.d("TAG-QR", "Not found")
            }
        }
        requestFrom = 0
    }
}