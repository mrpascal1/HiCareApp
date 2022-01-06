package com.ab.hicarerun.activities

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.BaseActivity
import com.ab.hicarerun.R
import com.ab.hicarerun.adapter.inventory.InventoryAdapter
import com.ab.hicarerun.databinding.ActivityInventoryBinding
import com.ab.hicarerun.network.NetworkCallController
import com.ab.hicarerun.network.NetworkResponseListner
import com.ab.hicarerun.network.models.InventoryModel.ActionList
import com.ab.hicarerun.network.models.InventoryModel.AddInventoryResult
import com.ab.hicarerun.network.models.InventoryModel.InventoryListModel.InventoryListResult
import com.ab.hicarerun.network.models.InventoryModel.TechnicianList
import com.ab.hicarerun.utils.AppUtils
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class InventoryActivity : BaseActivity() {
    val BARCODE_CAMERA_REQUEST = 3000
    var selectedAction = ""
    var selectedTechnician = ""
    val la = ArrayList<String>()
    val lt = ArrayList<String>()
    lateinit var binding: ActivityInventoryBinding
    lateinit var actionList: ArrayList<ActionList>
    lateinit var technicianList: ArrayList<TechnicianList>
    lateinit var inventoryAdapter: InventoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInventoryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        actionList = ArrayList()
        technicianList = ArrayList()
        inventoryAdapter = InventoryAdapter(this)

        val inventoryLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        inventoryLayoutManager.stackFromEnd = false
        inventoryLayoutManager.reverseLayout = false

        binding.inventoryRecyclerView.layoutManager = inventoryLayoutManager
        binding.inventoryRecyclerView.setHasFixedSize(true)
        binding.inventoryRecyclerView.isNestedScrollingEnabled = false
        binding.inventoryRecyclerView.adapter = inventoryAdapter

        binding.addBtn.setOnClickListener {
            val intent = Intent(this, NewBarcodeActivity::class.java)
            requestCameraPermission(intent)
        }

        binding.backIv.setOnClickListener {
            getBack()
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
        val li = LayoutInflater.from(this)
        val promptsView = li.inflate(R.layout.add_inventory_action_dialog, null)
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(promptsView)
        val alertDialog = alertDialogBuilder.create()
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
                        technicianLayout.visibility = View.GONE
                        okBtn.isEnabled = true
                        okBtn.alpha = 1f
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
                    }else{
                        okBtn.isEnabled = true
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

        cancelBtn.setOnClickListener {
            dismissProgressDialog()
            alertDialog.cancel()
        }
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
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
                        dismissProgressDialog()
                    }
                }else{
                    dismissProgressDialog()
                }
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
                        inventoryAdapter.addData(response.data)
                    }
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
        val date = str.substring(0, 6)
        var formatted = ""
        var count = 0
        for (i in 0 until date.length){
            if (count == 2){
                formatted += "-"
                count = 0
            }
            formatted += date[i]
            count++
        }
        formatted = AppUtils.getFormatted(formatted, "dd-MM-yyyy", "dd-MM-yy")
        return formatted
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
        if (requestCode == BARCODE_CAMERA_REQUEST){
            val barcode = data?.getStringExtra("content").toString()
            addInventory(AppUtils.resourceId, getItemSerialNo(barcode), getDate(barcode), barcode)
        }
    }
}