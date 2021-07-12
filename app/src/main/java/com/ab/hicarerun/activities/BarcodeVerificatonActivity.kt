package com.ab.hicarerun.activities

import android.content.Context
import android.content.Intent
import android.location.Location
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
import com.ab.hicarerun.adapter.BarcodeAdapter2
import com.ab.hicarerun.databinding.ActivityBarcodeVerificatonBinding
import com.ab.hicarerun.network.NetworkCallController
import com.ab.hicarerun.network.NetworkResponseListner
import com.ab.hicarerun.network.models.GeneralModel.GeneralData
import com.ab.hicarerun.network.models.LoginResponse
import com.ab.hicarerun.network.models.TSScannerModel.*
import com.ab.hicarerun.service.listner.LocationManagerListner
import com.ab.hicarerun.utils.AppUtils
import com.ab.hicarerun.utils.LocaleHelper
import com.google.zxing.integration.android.IntentIntegrator
import io.realm.RealmResults

class BarcodeVerificatonActivity : BaseActivity(), LocationManagerListner {

    lateinit var binding: ActivityBarcodeVerificatonBinding
    lateinit var modelBarcodeList: ArrayList<BarcodeDetailsData>
    lateinit var barcodeAdapter: BarcodeAdapter2
    private var ARGS_COMBINE_ORDER = "ARGS_COMBINE_ORDER"
    private var ARGS_ORDER = "ARGS_ORDER"
    private var ARGS_COMBINED_TASKS = "ARGS_COMBINED_TASKS"
    var empCode: Int? = null
    var orNo = ""
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
    var isCombinedTask: Boolean? = null
    var isFetched = 0
    var lat = ""
    var long = ""
    var combineOrder = ""
    var order = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBarcodeVerificatonBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.toolbar.setTitle("Check Bait Stations")
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.progressBar.visibility = View.VISIBLE

        val intent = intent
        combineOrder = intent.getStringExtra(ARGS_COMBINE_ORDER).toString()
        order = intent.getStringExtra(ARGS_ORDER).toString()
        isCombinedTask = intent.getBooleanExtra(ARGS_COMBINED_TASKS, false)

        val loginResponse: RealmResults<LoginResponse> = BaseApplication.getRealm().where(
                LoginResponse::class.java).findAll()
        if (loginResponse != null && loginResponse.size > 0) {
            empCode = loginResponse[0]?.id?.toInt()
            Log.d("TAG-Login", empCode.toString())
        }

//        val generalResponse: RealmResults<GeneralData> = BaseApplication.getRealm().where(GeneralData::class.java).findAll()
//        if (generalResponse != null && generalResponse.size > 0) {
//            orNo = generalResponse[0]?.orderNumber.toString()
//        }

        modelBarcodeList = ArrayList()
        barcodeAdapter = BarcodeAdapter2(this, modelBarcodeList, "TSVerification")
        val llManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        llManager.stackFromEnd = true
        llManager.reverseLayout = true
        binding.barcodeRecycler.layoutManager = llManager
        binding.barcodeRecycler.setHasFixedSize(true)
        binding.barcodeRecycler.isNestedScrollingEnabled = false
        binding.barcodeRecycler.adapter = barcodeAdapter

        binding.scanBtn.setOnClickListener {
            val integrator = IntentIntegrator(this)
            integrator.setCaptureActivity(CaptureActivityPortrait::class.java)
            integrator.setBeepEnabled(false)
            integrator.setPrompt("Scan a barcode")
            if (isFetched == 1) {
                if (modelBarcodeList.isNotEmpty()) {
                    integrator.initiateScan()
                } else {
                    Toast.makeText(this, "Rodent station not found", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please Enter Order No", Toast.LENGTH_SHORT).show()
            }
        }

        getOrderDetails(empCode.toString())
    }

    private fun getOrderDetails( uId: String) {
        val controller = NetworkCallController()
        controller.setListner(object : NetworkResponseListner<BarcodeDetailsResponse> {
            override fun onResponse(requestCode: Int, response: BarcodeDetailsResponse?) {
                binding.progressBar.visibility = View.GONE
                val success = response?.isSuccess.toString()
                if (success == "true") {
                    isFetched = 1
                    modelBarcodeList.clear()
                    if (response?.data != null) {
                        var itemsCount = 0
                        for (i in 0 until response.data.size) {
                            itemsCount++
                            id = response.data[i].id
                            account_No = response.data[i].account_No
                            order_No = response.data[i].order_No
                            account_Name = response.data[i].account_Name
                            barcode_Data = response.data[i].barcode_Data
                            last_Verified_On = response.data[i].last_Verified_On
                            last_Verified_By = response.data[i].last_Verified_By
                            created_On = response.data[i].created_On
                            created_By_Id_User = response.data[i].created_By_Id_User
                            verified_By = response.data[i].verified_By
                            created_By = response.data[i].created_By
                            isVerified = response.data[i].isVerified
                            modelBarcodeList.add(BarcodeDetailsData(id, account_No, order_No, account_Name, barcode_Data, last_Verified_On, last_Verified_By, created_On, created_By_Id_User, verified_By, created_By, isVerified))
                        }
                        BarcodeDetailsResponse(response.isSuccess, modelBarcodeList, response.errorMessage, response.param1, response.responseMessage)
                        if (modelBarcodeList.size > 0) {
                            binding.errorTv.visibility = View.GONE
                        } else {
                            binding.errorTv.visibility = View.VISIBLE
                        }
                    }
                    barcodeAdapter.notifyDataSetChanged()
                } else {
                    modelBarcodeList.clear()
                }
            }

            override fun onFailure(requestCode: Int) {
                modelBarcodeList.clear()
                binding.progressBar.visibility = View.GONE
                Log.d("TAG-UAT-Error", requestCode.toString())
            }
        })
        if (isCombinedTask == true) {
            controller.getBarcodeOrderDetails(combineOrder, uId)
        } else {
            controller.getBarcodeOrderDetails(order, uId)
        }

    }

    private fun modifyData(id: Int?, account_No: String?, order_No: String?, account_Name: String?,
                           barcode_Data: String?, last_Verified_On: String?, last_Verified_By: Int?,
                           created_On: String?, created_By_Id_User: Int?, verified_By: String?,
                           created_By: String?, isVerified: Boolean?) {

        var found = 0
        for (i in 0 until modelBarcodeList.size) {
            if (modelBarcodeList[i].barcode_Data == barcode_Data) {
                if (modelBarcodeList[i].isVerified == false) {
                    modelBarcodeList[i] = BarcodeDetailsData(modelBarcodeList[i].id, account_No, order_No, account_Name, barcode_Data,
                            last_Verified_On, last_Verified_By, created_On, created_By_Id_User, verified_By, created_By, isVerified)
                    Log.d("TAG-Veri", id.toString())
                    verifyBarcode(modelBarcodeList[i].id, "Technician Scanner", account_No, order_No, barcode_Data, lat, long, last_Verified_On, last_Verified_By)
                    barcodeAdapter.notifyItemChanged(i)
                    binding.barcodeRecycler.post {
                        binding.barcodeRecycler.smoothScrollToPosition(i)
                    }
                } else {
                    Toast.makeText(this, "Already verified", Toast.LENGTH_SHORT).show()
                }
                found = 1
            }
        }
        if (found == 0) {
            Toast.makeText(this, "Rodent station not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun verifyBarcode(barcodeId: Int?, activityName: String?, account_No: String?, order_No: String?, barcode_Data: String?,
                              lat: String?, long: String?, verifiedOn: String?, verifiedBy: Int?) {
        val verifyMap = HashMap<String, Any?>()
        verifyMap["BarcodeId"] = barcodeId
        verifyMap["ActivityName"] = activityName
        verifyMap["Account_No"] = account_No
        verifyMap["Order_No"] = order_No
        verifyMap["Barcode_Data"] = barcode_Data
        verifyMap["Lat"] = lat
        verifyMap["Long"] = long
        verifyMap["VerifiedOn"] = verifiedOn
        verifyMap["VerifiedBy"] = verifiedBy

        Log.d("TAG-Verifier", verifyMap.toString())

        val controller = NetworkCallController()
        controller.setListner(object : NetworkResponseListner<BaseResponse> {
            override fun onResponse(requestCode: Int, response: BaseResponse?) {
                if (response != null) {
                    if (response.isSuccess == true) {
                        if (response.data == "Verified") {
                            Toast.makeText(applicationContext, "Verified successfully", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.d("TAG-VERIFIER", "Something wrong ${response.data}")
                    }
                }
            }

            override fun onFailure(requestCode: Int) {
                Log.d("TAG-VERIFIER", requestCode.toString())
            }
        })
        controller.verifyBarcodeDetails(20212, verifyMap)
    }

    override fun onBackPressed() {
        getBack()
        super.onBackPressed()
    }

    private fun getBack() {
        val fragment = supportFragmentManager.backStackEntryCount
        if (fragment < 1) {
            finish()
        } else {
            fragmentManager.popBackStack()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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
        if (result != null) {
            if (result.contents != null) {
                modifyData(id, account_No, order_No, account_Name, result.contents, created_On, empCode,
                        last_Verified_On, created_By_Id_User, verified_By, created_By, true)
                Log.d("TAG-QR", result.contents)
            } else {
                Log.d("TAG-QR", "Not found")
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun locationFetched(
            mLocation: Location?,
            oldLocation: Location?,
            time: String?,
            locationProvider: String?
    ) {
        lat = mLocation?.latitude.toString()
        long = mLocation?.longitude.toString()
    }
}