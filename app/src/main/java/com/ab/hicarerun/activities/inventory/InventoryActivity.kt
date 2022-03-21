package com.ab.hicarerun.activities.inventory

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatSpinner
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.BaseActivity
import com.ab.hicarerun.R
import com.ab.hicarerun.activities.NewBarcodeActivity
import com.ab.hicarerun.adapter.inventory.InventoryAdapter
import com.ab.hicarerun.adapter.inventory.InventoryHistoryAdapter
import com.ab.hicarerun.databinding.ActivityInventoryBinding
import com.ab.hicarerun.databinding.LayoutInventoryHistoryBinding
import com.ab.hicarerun.network.NetworkCallController
import com.ab.hicarerun.network.NetworkResponseListner
import com.ab.hicarerun.network.models.inventorymodel.ActionList
import com.ab.hicarerun.network.models.inventorymodel.AddInventoryResult
import com.ab.hicarerun.network.models.inventorymodel.inventorylistmodel.InventoryListResult
import com.ab.hicarerun.network.models.inventorymodel.TechnicianList
import com.ab.hicarerun.network.models.inventorymodel.historymodel.InventoryHistoryBase
import com.ab.hicarerun.network.models.inventorymodel.historymodel.InventoryHistoryData
import com.ab.hicarerun.network.models.tsscannermodel.BaseResponse
import com.ab.hicarerun.utils.AppUtils
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import es.dmoral.toasty.Toasty

class InventoryActivity : BaseActivity() {
    val BARCODE_CAMERA_REQUEST = 3000
    var selectedAction = ""
    var selectedTechnician = ""
    var bucketId = 0
    var reasons = ""
    var itemSerialNo = ""
    var referenceId = ""
    val la = ArrayList<String>()
    val lt = ArrayList<String>()
    val historyData = ArrayList<InventoryHistoryData>()
    lateinit var binding: ActivityInventoryBinding
    lateinit var actionList: ArrayList<ActionList>
    lateinit var technicianList: ArrayList<TechnicianList>
    lateinit var inventoryAdapter: InventoryAdapter
    lateinit var historyAdapter: InventoryHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInventoryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        actionList = ArrayList()
        technicianList = ArrayList()
        inventoryAdapter = InventoryAdapter(this)
        historyAdapter = InventoryHistoryAdapter(this)

        val inventoryLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        inventoryLayoutManager.stackFromEnd = false
        inventoryLayoutManager.reverseLayout = false

        binding.inventoryRecyclerView.configure(this).apply {
            adapter = inventoryAdapter
        }
        //binding.inventoryRecyclerView.adapter = inventoryAdapter

        binding.addBtn.setOnClickListener {
            val intent = Intent(this, NewBarcodeActivity::class.java)
            requestCameraPermission(intent)
        }

        binding.returnBtn.setOnClickListener {
            val intent = Intent(this, NewBarcodeActivity::class.java)
            requestCameraPermission(intent)
        }

        binding.backIv.setOnClickListener {
            getBack()
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            getInventoryList()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        inventoryAdapter.historyClickListener = InventoryAdapter.HistoryClickListener {
            Log.d("TAG", it)
            getInventoryHistory(it)
        }

        getInventoryList()
    }

    private fun requestCameraPermission(intent: Intent) {
        try {
            Dexter.withActivity(this)
                .withPermission(
                    Manifest.permission.CAMERA
                )
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                        startActivityForResult(intent, BARCODE_CAMERA_REQUEST)
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                        Toast.makeText(applicationContext, "Camera permission is required by scanner", Toast.LENGTH_SHORT).show()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest?,
                        token: PermissionToken?
                    ) {
                        token?.continuePermissionRequest()
                    }

                })
                .withErrorListener { error: DexterError? ->
                    Toast.makeText(this, "Error occurred! ", Toast.LENGTH_SHORT).show()
                }
                .onSameThread()
                .check()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showDialog(){
        selectedAction = ""
        selectedTechnician = ""
        bucketId = 0
        reasons = ""
        val promptsView = Dialog(this).apply {
            setCancelable(false)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
            setCanceledOnTouchOutside(false)
            setContentView(R.layout.add_inventory_action_dialog)
        }
        val technicianLayout = promptsView.findViewById(R.id.technicianLayout) as ConstraintLayout
        val spnAction = promptsView.findViewById(R.id.spnAction) as AppCompatSpinner
        val spnTechnician = promptsView.findViewById(R.id.spnTechnician) as AppCompatSpinner
        val cancelBtn = promptsView.findViewById(R.id.btnCancel) as AppCompatButton
        val okBtn = promptsView.findViewById(R.id.okBtn) as AppCompatButton
        okBtn.isEnabled = false
        okBtn.alpha = 0.6f
        technicianLayout.visibility = View.GONE

        val actionAdapter = object : ArrayAdapter<String>(this, R.layout.spinner_layout_new, la){
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
        actionAdapter.setDropDownViewResource(R.layout.spinner_popup)
        spnAction.adapter = actionAdapter

        spnAction.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedAction = spnAction.selectedItem.toString()
                if (selectedAction != "Select Action"){
                    if (selectedAction.equals("Assign To Technician", true)){
                        technicianLayout.visibility = View.VISIBLE
                        if (selectedTechnician != "Select Technician"){
                            okBtn.isEnabled = true
                            okBtn.alpha = 1f
                        }else{
                            okBtn.isEnabled = false
                            okBtn.alpha = 0.6f
                        }
                    }else{
                        selectedTechnician = ""
                        technicianLayout.visibility = View.GONE
                        okBtn.isEnabled = true
                        okBtn.alpha = 1f
                    }
                    actionList.forEach {
                        if (it.reasons == selectedAction){
                            reasons = it.reasons
                            bucketId = it.allocate_Bucket_Id.toString().toInt()
                        }
                    }
                    if (bucketId == 2){
                        if (selectedTechnician != "" && selectedTechnician != "Select Technician"){
                            technicianList.forEach {
                                if (it.technicianName == selectedTechnician){
                                    referenceId = it.technicianId.toString()
                                }
                            }
                        }else{
                            referenceId = AppUtils.resourceId
                        }
                    }else if (bucketId == 1){
                        referenceId = AppUtils.resourceId
                    }else{
                        referenceId = ""
                    }
                }else{
                    technicianLayout.visibility = View.GONE
                    okBtn.isEnabled = false
                    okBtn.alpha = 0.6f
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        spnTechnician.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedTechnician = spnTechnician.selectedItem.toString()
                if (selectedAction.equals("Assign To Technician", true)){
                    if (selectedTechnician != "Select Technician"){
                        okBtn.isEnabled = true
                        okBtn.alpha = 1f
                        if (bucketId == 2){
                            if (selectedTechnician != "" && selectedTechnician != "Select Technician"){
                                technicianList.forEach {
                                    if (it.technicianName == selectedTechnician){
                                        referenceId = it.technicianId.toString()
                                    }
                                }
                            }else{
                                referenceId = AppUtils.resourceId
                            }
                        }else if(bucketId == 1){
                            referenceId = AppUtils.resourceId
                        }else{
                            referenceId = ""
                        }
                    }else{
                        if (bucketId == 3){
                            referenceId = ""
                        }else if(bucketId == 1){
                            referenceId = AppUtils.resourceId
                        }
                        okBtn.isEnabled = false
                        okBtn.alpha = 0.6f
                    }
                }else{
                    if (selectedAction != "Select Action"){
                        okBtn.isEnabled = true
                        okBtn.alpha = 1f
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        val technicianAdapter = object : ArrayAdapter<String>(this, R.layout.spinner_layout_new, lt){
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
        technicianAdapter.setDropDownViewResource(R.layout.spinner_popup)
        spnTechnician.adapter = technicianAdapter

        okBtn.setOnClickListener {
            updateInventory()
            promptsView.cancel()
        }

        cancelBtn.setOnClickListener {
            promptsView.cancel()
        }
        promptsView.show()
    }

    private fun updateInventory(){
        showProgressDialog()
        val hashMap = HashMap<String, Any>()
        hashMap["Reasons"] = reasons
        hashMap["Bucket_Id"] = bucketId
        hashMap["ItemCode"] = itemSerialNo
        hashMap["User_Id"] = AppUtils.resourceId
        hashMap["Reference_Id"] = referenceId

        val controller = NetworkCallController()
        controller.setListner(object : NetworkResponseListner<BaseResponse>{
            override fun onResponse(requestCode: Int, response: BaseResponse?) {
                if (response != null){
                    if (response.isSuccess == true){
                        Toasty.success(applicationContext, "Inventory updated successfully").show()
                        getInventoryList()
                    }else{
                        Toasty.error(applicationContext, "Unable to update").show()
                    }
                }else{
                    Toasty.error(applicationContext, "Unable to update").show()
                }
                dismissProgressDialog()
            }

            override fun onFailure(requestCode: Int) {
                dismissProgressDialog()
                Log.d("TAG", "Error")
            }
        })
        controller.updateInventory(2022, hashMap)
    }

    private fun showInventoryHistoryDialog(){
        val historyView = Dialog(this).apply {
            setCancelable(false)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
            setCanceledOnTouchOutside(false)
            setContentView(R.layout.layout_inventory_history)
        }
        //val nestedScrollView = historyView.findViewById<NestedScrollView>(R.id.nestedScrollView)
        val closeIv = historyView.findViewById<ImageView>(R.id.closeIv)
        val historyRecyclerView = historyView.findViewById<RecyclerView>(R.id.historyRecyclerView)
        val txtTitle = historyView.findViewById<TextView>(R.id.txtTitle)
        historyAdapter.addData(historyData)
        historyRecyclerView.configure(this).apply {
            adapter = historyAdapter
        }
        /*nestedScrollView.post {
            nestedScrollView.scrollTo(0,0)
        }*/
        txtTitle.setOnClickListener {
            historyView.dismiss()
        }
        historyView.show()
    }

    private fun getInventoryHistory(itemCode: String){
        showProgressDialog()
        val controller = NetworkCallController()
        controller.setListner(object : NetworkResponseListner<InventoryHistoryBase>{
            override fun onResponse(requestCode: Int, response: InventoryHistoryBase?) {
                if (response != null && response.isSuccess == true){
                    if (!response.data.isNullOrEmpty()){
                        historyData.clear()
                        historyData.addAll(response.data)
                        showInventoryHistoryDialog()
                    }else{
                        Toasty.error(applicationContext, "No history found").show()
                    }
                }else{
                    Toasty.error(applicationContext, "No history found").show()
                }
                dismissProgressDialog()
            }

            override fun onFailure(requestCode: Int) {
                dismissProgressDialog()
                Log.d("TAG", "Inventory history Failed")
            }
        })
        controller.getInventoryHistory(20222, itemCode)
    }

    private fun addInventory(userId: String, itemSerialNo: String, date: String, barcodeData: String){
        showProgressDialog()
        val hashMap = HashMap<String, Any>()
        hashMap["UserId"] = userId
        hashMap["ItemSerial_No"] = itemSerialNo
        hashMap["Manufacturing_Date"] = date
        hashMap["BarcodeData"] = barcodeData

        val controller = NetworkCallController()
        controller.setListner(object : NetworkResponseListner<AddInventoryResult>{
            override fun onResponse(requestCode: Int, response: AddInventoryResult?) {
                if (response != null){
                    if (response.isSuccess == true){
                        if (response.data != null){
                            actionList.clear()
                            technicianList.clear()
                            if (response.data.actionList != null && response.data.technicianList != null){
                                actionList.addAll(response.data.actionList)
                                technicianList.addAll(response.data.technicianList)
                                la.clear()
                                lt.clear()
                                la.add("Select Action")
                                lt.add("Select Technician")
                                actionList.forEach {
                                    if (it.is_Active != null && it.is_Active == true) {
                                        la.add(it.reasons.toString())
                                    }
                                }
                                technicianList.forEach {
                                    lt.add(it.technicianName.toString())
                                }
                                showDialog()
                            }
                        }
                    }else{
                        Toasty.error(applicationContext, "Error").show()
                    }
                }else{
                    Toasty.error(applicationContext, "Error").show()
                }
                dismissProgressDialog()
            }

            override fun onFailure(requestCode: Int) {
                dismissProgressDialog()
                Log.d("TAG", "Error")
            }
        })
        controller.addInventory(2022, hashMap)
    }

    private fun getInventoryList(){
        showProgressDialog()
        val controller = NetworkCallController()
        controller.setListner(object : NetworkResponseListner<InventoryListResult>{
            override fun onResponse(requestCode: Int, response: InventoryListResult?) {
                if (response != null && response.isSuccess == true){
                    if (response.data != null && response.data.isNotEmpty()){
                        inventoryAdapter.addData(response.data, false)
                        binding.errorTv.visibility = View.GONE
                        binding.inventoryRecyclerView.visibility = View.VISIBLE
                    }else{
                        binding.inventoryRecyclerView.visibility = View.GONE
                        binding.errorTv.visibility = View.VISIBLE
                    }
                }else{
                    binding.inventoryRecyclerView.visibility = View.GONE
                    binding.errorTv.visibility = View.VISIBLE
                }
                inventoryAdapter.notifyDataSetChanged()
                dismissProgressDialog()
            }
            override fun onFailure(requestCode: Int) {
                dismissProgressDialog()
            }
        })
        controller.getInventoryList(202206, AppUtils.resourceId)
    }

    private fun getDate(str: String): String{
        try {
            val date = str.substring(0, 6)
            var formatted = ""
            var count = 0
            for (i in 0 until date.length) {
                if (count == 2) {
                    formatted += "-"
                    count = 0
                }
                formatted += date[i]
                count++
            }
            formatted = AppUtils.getFormatted(formatted, "dd-MM-yyyy", "dd-MM-yy")
            return formatted
        }catch (e: Exception) {
            Toasty.error(this, "Invalid QR code").show()
        }
        return ""
    }

    private fun getItemSerialNo(str: String): String{
        return str.substring(6)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == BARCODE_CAMERA_REQUEST) {
            if (data != null) {
                val barcode = data.getStringExtra("content").toString()
                if (barcode.length < 7) {
                    Toasty.error(this, "Invalid QR code").show()
                    return
                }
                itemSerialNo = getItemSerialNo(barcode)
                val date = getDate(barcode)
                if (date != "") {
                    addInventory(AppUtils.resourceId, itemSerialNo, date, barcode)
                }
            }
        }
    }

    fun RecyclerView.configure(context: Context, reverseLayout: Boolean = false): RecyclerView{
        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, reverseLayout)
        isNestedScrollingEnabled = false
        setHasFixedSize(true)
        return this
    }
}