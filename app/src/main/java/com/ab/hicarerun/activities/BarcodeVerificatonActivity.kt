package com.ab.hicarerun.activities

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.hardware.Camera
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.BaseActivity
import com.ab.hicarerun.BaseApplication
import com.ab.hicarerun.BuildConfig
import com.ab.hicarerun.R
import com.ab.hicarerun.adapter.BarcodeAdapter2
import com.ab.hicarerun.adapter.PestTypeAdapter
import com.ab.hicarerun.databinding.ActivityBarcodeVerificatonBinding
import com.ab.hicarerun.network.NetworkCallController
import com.ab.hicarerun.network.NetworkResponseListner
import com.ab.hicarerun.network.models.ConsulationModel.Data
import com.ab.hicarerun.network.models.LoginResponse
import com.ab.hicarerun.network.models.TSScannerModel.BarcodeDDPestType
import com.ab.hicarerun.network.models.TSScannerModel.BarcodeDetailsData
import com.ab.hicarerun.network.models.TSScannerModel.BarcodeDetailsResponse
import com.ab.hicarerun.network.models.TSScannerModel.BaseResponse
import com.ab.hicarerun.service.listner.LocationManagerListner
import com.ab.hicarerun.utils.AppUtils
import com.ab.hicarerun.utils.LocaleHelper
import com.google.zxing.integration.android.IntentIntegrator
import io.realm.RealmResults
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class BarcodeVerificatonActivity : BaseActivity(), LocationManagerListner {

    lateinit var binding: ActivityBarcodeVerificatonBinding
    lateinit var modelBarcodeList: ArrayList<BarcodeDetailsData>
    lateinit var modelBarcodeDDPestType: ArrayList<BarcodeDDPestType>
    lateinit var barcodeAdapter: BarcodeAdapter2
    lateinit var pestTypeAdapter: PestTypeAdapter
    private var ARGS_COMBINE_ORDER = "ARGS_COMBINE_ORDER"
    private var ARGS_ORDER = "ARGS_ORDER"
    private var ARGS_COMBINED_TASKS = "ARGS_IS_COMBINE"
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
    var barcode_Type: String? = ""
    lateinit var promptsView: View
    var resourceId = ""
    var taskId = ""
    var mPermissions = false
    val REQUEST_CODE = 1234
    val CAMERA_REQUEST = 100
    var selectedImagePath = ""
    var mPhotoFile: File? = null
    var bitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBarcodeVerificatonBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.toolbar.setTitle(getString(R.string.check_bait_stations))
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.progressBar.visibility = View.VISIBLE

        val intent = intent
        combineOrder = intent.getStringExtra(ARGS_COMBINE_ORDER).toString()
        order = intent.getStringExtra(ARGS_ORDER).toString()
        isCombinedTask = intent.getBooleanExtra(ARGS_COMBINED_TASKS, false)

        Log.d("isCombine", combineOrder)
        Log.d("isCombine", isCombinedTask.toString())

        val loginResponse: RealmResults<LoginResponse> = BaseApplication.getRealm().where(
                LoginResponse::class.java).findAll()
        if (loginResponse != null && loginResponse.size > 0) {
            empCode = loginResponse[0]?.id?.toInt()
            resourceId = loginResponse[0]?.userID.toString()
            Log.d("TAG-Login", empCode.toString())
        }

//        val generalResponse: RealmResults<GeneralData> = BaseApplication.getRealm().where(GeneralData::class.java).findAll()
//        if (generalResponse != null && generalResponse.size > 0) {
//            orNo = generalResponse[0]?.orderNumber.toString()
//        }

        modelBarcodeList = ArrayList()
        modelBarcodeDDPestType = ArrayList()
        pestTypeAdapter = PestTypeAdapter(this, modelBarcodeDDPestType)
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

    private fun getOrderDetails(uId: String) {
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
                            barcode_Type = response.data[i].barcode_Type
                            val pestType = response.data[i].pest_Type
                            if (!pestType.isNullOrEmpty()){
                                for (j in 0 until pestType.size){
                                    modelBarcodeDDPestType.add(BarcodeDDPestType(
                                        pestType[j].id,
                                        pestType[j].barcode_Type,
                                        pestType[j].sub_Type,
                                        pestType[j].show_Count,
                                        pestType[j].capture_Image,
                                    ))
                                }
                            }
                            modelBarcodeList.add(BarcodeDetailsData(id, account_No, order_No, account_Name, barcode_Data, last_Verified_On, last_Verified_By, created_On, created_By_Id_User, verified_By, created_By, isVerified, barcode_Type, modelBarcodeDDPestType))
                        }
                        BarcodeDetailsResponse(response.isSuccess, modelBarcodeList, response.errorMessage, response.param1, response.responseMessage)
                        if (modelBarcodeList.size > 0) {
                            binding.errorTv.visibility = View.GONE
                            AppUtils.IS_QRCODE_THERE = isVerified(modelBarcodeList)

                        } else {
                            binding.errorTv.visibility = View.VISIBLE
                            AppUtils.IS_QRCODE_THERE = false
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


    private fun isVerified(listData: List<BarcodeDetailsData>): Boolean {
        for (data in listData) {
            if (data.isVerified == true) {
                return false
            }
        }
        return true
    }

    private fun modifyData(id: Int?, account_No: String?, order_No: String?, account_Name: String?,
                           barcode_Data: String?, last_Verified_On: String?, last_Verified_By: Int?,
                           created_On: String?, created_By_Id_User: Int?, verified_By: String?,
                           created_By: String?, isVerified: Boolean?, barcode_Type: String?, modelBarcodeDDPestType: ArrayList<BarcodeDDPestType>) {

        var found = 0
        for (i in 0 until modelBarcodeList.size) {
            if (modelBarcodeList[i].barcode_Data == barcode_Data) {
                if (modelBarcodeList[i].isVerified == false) {
                    modelBarcodeList[i] = BarcodeDetailsData(modelBarcodeList[i].id, account_No, order_No, account_Name, barcode_Data,
                            last_Verified_On, last_Verified_By, created_On, created_By_Id_User, verified_By, created_By, isVerified, barcode_Type, modelBarcodeDDPestType)
                    Log.d("TAG-Veri", id.toString())
                    Log.d("TAG-VerifiedOn-start", last_Verified_On.toString())
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
        Log.d("TAG-VerifiedOn-top", verifiedOn.toString())
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
        Log.d("TAG-VerifiedOn", verifiedOn.toString())

        val controller = NetworkCallController()
        controller.setListner(object : NetworkResponseListner<BaseResponse> {
            override fun onResponse(requestCode: Int, response: BaseResponse?) {
                if (response != null) {
                    if (response.isSuccess == true) {
                        if (response.data == "Verified") {
                            Toast.makeText(applicationContext, "Verified successfully", Toast.LENGTH_SHORT).show()
                            getOrderDetails(empCode.toString())
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

    private fun showPestDialog(){
        val li = LayoutInflater.from(this)
        promptsView = li.inflate(R.layout.add_pest_info_dialog, null)
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(promptsView)
        val alertDialog = alertDialogBuilder.create()
        val lnrCheck = promptsView.findViewById(R.id.lnrCheque) as LinearLayout
        val clickedImageLayout = promptsView.findViewById(R.id.clickedImageLayout) as RelativeLayout
        val clickedIv = promptsView.findViewById(R.id.clickedIv) as ImageView
        val pestRecyclerView = promptsView.findViewById(R.id.pestRecyclerView) as RecyclerView
        val cancelBtn = promptsView.findViewById(R.id.btn_cancel) as AppCompatButton
        val radioGrp = promptsView.findViewById(R.id.radioGrp) as RadioGroup
        val yesBtn = promptsView.findViewById(R.id.yesBtn) as RadioButton
        val noBtn = promptsView.findViewById(R.id.noBtn) as RadioButton
        val addImgBtn = promptsView.findViewById(R.id.addImgBtn) as AppCompatButton
        pestRecyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        pestRecyclerView.layoutManager = layoutManager
        pestRecyclerView.adapter = pestTypeAdapter

        pestTypeAdapter.setOnEditTextChangedListener(object : PestTypeAdapter.OnEditTextChanged{
            override fun onTextChanged(position: Int, charSeq: String) {
                Log.d("TAG-Count", charSeq)
            }

        })

        /*addImgBtn.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (intent.resolveActivity(this.packageManager) != null) {
                // Create the File where the photo should go
                val photoFile = createImageFile()
                val photoURI: Uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", photoFile)
                mPhotoFile = photoFile
                intent.putExtra("android.intent.extras.CAMERA_FACING", Camera.CameraInfo.CAMERA_FACING_BACK)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(intent, CAMERA_REQUEST)
            }
        }*/
        /*if (bitmap != null){
            clickedImageLayout.visibility = View.VISIBLE
            clickedIv.setImageBitmap(bitmap)
        }*/
        cancelBtn.setOnClickListener {
            alertDialog.cancel()
        }

        alertDialog.show()
        alertDialog.setCancelable(false)
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.setCanceledOnTouchOutside(false)
    }

    private fun uploadBoxImage(resourceId: String, taskId: String, file: String){
        val hashMap = HashMap<String, Any>()
        hashMap["ResourceId"] = resourceId
        hashMap["TaskId"] = taskId
        hashMap["File"] = file

        val controller = NetworkCallController()
        controller.setListner(object : NetworkResponseListner<BaseResponse>{
            override fun onResponse(requestCode: Int, response: BaseResponse?) {
                Log.d("TAG", response.toString())
            }

            override fun onFailure(requestCode: Int) {
            }
        })
        controller.uploadBoxImage(20210109, hashMap)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp =
            SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        val mFileName = "JPEG_" + timeStamp + "_"
        val storageDir: File? = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(mFileName, ".jpg", storageDir)
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
        val last_Verified_On2 = AppUtils.currentDateTimeWithTimeZone()
        Log.d("TAG-Act", last_Verified_On2)
        if (result != null) {
            if (result.contents != null) {
                showPestDialog()
                /*modifyData(id, account_No, order_No, account_Name, result.contents, created_On, empCode,
                        last_Verified_On2, created_By_Id_User, verified_By, created_By, true, barcode_Type, modelBarcodeDDPestType)*/
                Log.d("TAG-VerifiedOn-beyond", last_Verified_On2.toString())
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