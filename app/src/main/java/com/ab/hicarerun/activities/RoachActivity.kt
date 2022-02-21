package com.ab.hicarerun.activities

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatSpinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.BaseActivity
import com.ab.hicarerun.R
import com.ab.hicarerun.adapter.roach.RoachAdapter
import com.ab.hicarerun.databinding.ActivityRoachBinding
import com.ab.hicarerun.network.NetworkCallController
import com.ab.hicarerun.network.NetworkResponseListner
import com.ab.hicarerun.network.models.roachmodel.roachlistmodel.RoachBase
import com.ab.hicarerun.network.models.roachmodel.saveroachmodel.RoachSaveBase
import com.ab.hicarerun.utils.AppUtils
import es.dmoral.toasty.Toasty

class RoachActivity : BaseActivity() {

    lateinit var binding: ActivityRoachBinding
    lateinit var roachAdapter: RoachAdapter
    lateinit var locationList: ArrayList<String>
    lateinit var dialogView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoachBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //binding.addBtn.isEnabled = false
        binding.titleTv.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
        locationList = ArrayList()
        roachAdapter = RoachAdapter(this)
        binding.roachRecyclerView.configure(this).apply {
            adapter = roachAdapter
        }
        binding.addBtn.setOnClickListener {
            showAddDialog()
        }
        binding.backIv.setOnClickListener {
            onBackPressed()
        }
        getRoachList()
    }

    private fun getRoachList(){
        showProgressDialog()
        val controller = NetworkCallController()
        controller.setListner(object : NetworkResponseListner<RoachBase>{
            override fun onResponse(requestCode: Int, response: RoachBase?) {
                if (response != null && response.isSuccess == true){
                    val data = response.responseData
                    if (data != null){
                        if (!data.roachList.isNullOrEmpty()){
                            roachAdapter.addData(data.roachList)
                            binding.roachRecyclerView.visibility = View.VISIBLE
                            binding.errorTv.visibility = View.GONE
                            Log.d("Roach", "List should be visible")
                        }else{
                            binding.roachRecyclerView.visibility = View.GONE
                            binding.errorTv.visibility = View.VISIBLE
                        }
                        if (!data.locationList.isNullOrEmpty()){
                            locationList.clear()
                            locationList.add("Select Location")
                            locationList.addAll(data.locationList)
                        }
                    }
                }
                //binding.addBtn.isEnabled = true
                dismissProgressDialog()
            }

            override fun onFailure(requestCode: Int) {
                dismissProgressDialog()
                Log.d("TAG", "Roach API failed")
            }
        })
        controller.getAllDeviceByAccount(20222, AppUtils.accountId)
    }

    private fun addNewRoach(deviceDetails: HashMap<String, Any>){
        showProgressDialog()
        val controller = NetworkCallController()
        controller.setListner(object : NetworkResponseListner<RoachSaveBase>{
            override fun onResponse(requestCode: Int, response: RoachSaveBase?) {
                dismissProgressDialog()
                if (response != null){
                    if (response.isSuccess == true){
                        Toasty.success(applicationContext, "Successfully added").show()
                        getRoachList()
                    }else{
                        Toasty.error(applicationContext, "Invalid error").show()
                    }
                }else{
                    Toasty.error(applicationContext, "Invalid error").show()
                }
            }

            override fun onFailure(requestCode: Int) {
                dismissProgressDialog()
                Log.d("TAG", "Roach adding failed")
            }
        })
        controller.saveDeviceRegistrationForApp(20222, deviceDetails)
    }

    private fun showAddDialog(){
        var selectedLocation = ""
        val li = LayoutInflater.from(this)
        dialogView = li.inflate(R.layout.add_roach_dialog, null)
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(dialogView)
        val alertDialog = alertDialogBuilder.create()
        val spnLocation = dialogView.findViewById<AppCompatSpinner>(R.id.spnLocation)
        val okBtn = dialogView.findViewById(R.id.okBtn) as AppCompatButton
        val cancelBtn = dialogView.findViewById(R.id.btnCancel) as AppCompatButton
        okBtn.isEnabled = false
        okBtn.alpha = 0.6f
        val arrayAdapter = object : ArrayAdapter<String>(this, R.layout.spinner_layout_new, locationList){
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
        spnLocation.adapter = arrayAdapter
        spnLocation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedLocation = spnLocation.selectedItem.toString()
                if (selectedLocation != "Select Location"){
                    okBtn.isEnabled = true
                    okBtn.alpha = 1f
                }else{
                    okBtn.isEnabled = false
                    okBtn.alpha = 0.6f
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        okBtn.setOnClickListener {
            val deviceDetails = HashMap<String, Any>()
            deviceDetails["AccountNo"] = AppUtils.accountId
            deviceDetails["DeployedLocation"] = selectedLocation
            addNewRoach(deviceDetails)
            alertDialog.dismiss()
        }
        cancelBtn.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.setCancelable(false)
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.show()
    }

    fun RecyclerView.configure(context: Context, reverseLayout: Boolean = false): RecyclerView{
        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, reverseLayout)
        isNestedScrollingEnabled = false
        setHasFixedSize(true)
        return this
    }
}