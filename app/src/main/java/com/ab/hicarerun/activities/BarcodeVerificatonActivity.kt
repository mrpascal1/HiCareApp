package com.ab.hicarerun.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.TypedArray
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.hardware.Camera
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.BaseActivity
import com.ab.hicarerun.BaseApplication
import com.ab.hicarerun.BaseApplication.getRealm
import com.ab.hicarerun.BuildConfig
import com.ab.hicarerun.R
import com.ab.hicarerun.adapter.BarcodeAdapter2
import com.ab.hicarerun.adapter.PestTypeAdapter
import com.ab.hicarerun.databinding.ActivityBarcodeVerificatonBinding
import com.ab.hicarerun.network.NetworkCallController
import com.ab.hicarerun.network.NetworkResponseListner
import com.ab.hicarerun.network.models.GeneralModel.GeneralData
import com.ab.hicarerun.network.models.Item
import com.ab.hicarerun.network.models.LoginResponse
import com.ab.hicarerun.network.models.TSScannerModel.*
import com.ab.hicarerun.service.listner.LocationManagerListner
import com.ab.hicarerun.utils.AppUtils
import com.ab.hicarerun.utils.LocaleHelper
import com.bumptech.glide.Glide
import com.google.zxing.integration.android.IntentIntegrator
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.squareup.picasso.Picasso
import io.realm.RealmResults
import java.io.ByteArrayOutputStream
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
    lateinit var pestType: ArrayList<BarcodeDDPestType>
    lateinit var notAccessibleList: ArrayList<BarcodeType>
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
    val REQUEST_GALLERY_PHOTO = 200
    var selectedImagePath = ""
    var mPhotoFile: File? = null
    var bitmap: Bitmap? = null
    var barcodeIdFromAdapter = -1
    var pestTypeIdFromAdapter = -1
    var IMAGE_FILE_PATH = ""
    var tempImageName = ""
    var pos = 0
    var IMAGE_DIRECTORY = "/hicare";
    var FILE_ENTENSION = ".jpg";
    var imageCount = 0;
    var TimeStamp = "";
    var currentItemCount = ""
    var prevImageLink = "";
    var naReason = ""
    var imageUpdateListener: ImageUpdateListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBarcodeVerificatonBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //binding.toolbar.setTitle(getString(R.string.check_bait_stations))
        binding.toolbar.setTitle("Verify Equipment")
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
        notAccessibleList = ArrayList()
        pestType = ArrayList()
        barcodeAdapter = BarcodeAdapter2(this, modelBarcodeList, "TSVerification")
        val llManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        llManager.stackFromEnd = true
        llManager.reverseLayout = true
        binding.barcodeRecycler.layoutManager = llManager
        binding.barcodeRecycler.setHasFixedSize(true)
        binding.barcodeRecycler.isNestedScrollingEnabled = false
        binding.barcodeRecycler.adapter = barcodeAdapter

        binding.scanBtn.setOnClickListener {
            val intent = Intent(this, NewBarcodeActivity::class.java)
            val integrator = IntentIntegrator(this)
            integrator.setCaptureActivity(CaptureActivityPortrait::class.java)
            integrator.setBeepEnabled(false)
            integrator.setPrompt("Scan a barcode")
            if (isFetched == 1) {
                if (modelBarcodeList.isNotEmpty()) {
                    requestCameraPermission(intent)
                    //startActivityForResult(intent, 3000)
                    //integrator.initiateScan()
                } else {
                    Toast.makeText(this, "Equipment not found", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please Enter Order No", Toast.LENGTH_SHORT).show()
            }
        }

        barcodeAdapter.setOnNAClickListener(object : BarcodeAdapter2.NotAccessibleListener{
            override fun onNAClicked(barcodeData: String) {
                showNADialog(barcodeData)
            }
        })

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
                    modelBarcodeDDPestType.clear()
                    if (response?.data != null && response.data.isNotEmpty()) {
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
                            val reasons = response.data[i].not_Accessible_Reasons_List
                            val su = response.data[i].service_Unit
                            val additional_Info = response.data[i].additional_Info
                            val nar = response.data[i].not_Accessible_Reason
                            if (!reasons.isNullOrEmpty()){
                                notAccessibleList.addAll(reasons)
                            }
                            if (!pestType.isNullOrEmpty()){
                                for (j in 0 until pestType.size){
                                    modelBarcodeDDPestType.add(BarcodeDDPestType(
                                        pestType[j].id,
                                        pestType[j].barcode_Type,
                                        pestType[j].sub_Type,
                                        pestType[j].show_Count,
                                        pestType[j].capture_Image,
                                        pestType[j].show_Option,
                                        pestType[j].option_Value,
                                        pestType[j].option_List,
                                        pestType[j].pest_Count,
                                        pestType[j].image_Url,
                                        id,
                                    ))
                                }
                            }
                            //modelBarcodeList.add(BarcodeDetailsData(id, account_No, order_No, account_Name, barcode_Data, last_Verified_On, last_Verified_By, created_On, created_By_Id_User, verified_By, created_By, isVerified, barcode_Type, su, additional_Info, notAccessibleList, nar, modelBarcodeDDPestType))
                        }
                        modelBarcodeList.addAll(response.data)
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

    private fun showNADialog(barcode_Data: String?){
        val li = LayoutInflater.from(this)
        promptsView = li.inflate(R.layout.not_accessible_reason_dialog, null)
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(promptsView)
        val alertDialog = alertDialogBuilder.create()
        val spnType = promptsView.findViewById(R.id.spnType) as Spinner
        val cancelBtn = promptsView.findViewById(R.id.btnCancel) as AppCompatButton
        val okBtn = promptsView.findViewById(R.id.okBtn) as AppCompatButton
        okBtn.isEnabled = false
        okBtn.alpha = 0.6f

        val reasons = ArrayList<String>()
        reasons.clear()
        reasons.add("Select Reason")
        for (i in modelBarcodeList.indices){
            if (modelBarcodeList[i].barcode_Data == barcode_Data){
                modelBarcodeList[i].not_Accessible_Reasons_List?.forEach {
                    reasons.add(it.value.toString())
                }
            }
        }
        val arrayAdapter = object : ArrayAdapter<String>(this, R.layout.spinner_layout_new, reasons){
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
                naReason = spnType.selectedItem.toString()
                if (naReason != "Select Reason"){
                    okBtn.isEnabled = true
                    okBtn.alpha = 1f
                }else{
                    okBtn.isEnabled = false
                    okBtn.alpha = 0.6f
                }
                //Log.d("TAG", selectedType)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        okBtn.setOnClickListener {
            //additional_Info = remarksEt.text.toString().trim()
            //Log.d("TAG", selectedType)
            val last_Verified_On2 = AppUtils.currentDateTimeWithTimeZone()
            if (naReason != "" && naReason != "Select Reason"){
                /*addNewData(
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
                    pestList = pestList)*/
                modifyData(
                    id,
                    account_No,
                    order_No,
                    account_Name,
                    barcode_Data,
                    created_On,
                    empCode,
                    last_Verified_On2,
                    created_By_Id_User,
                    verified_By,
                    created_By,
                    true,
                    barcode_Type,
                    "",
                    "",
                    notAccessibleList,
                    naReason,
                    pestType
                )
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

    private fun isVerified(listData: List<BarcodeDetailsData>): Boolean {
        for (data in listData) {
            if (data.isVerified == true) {
                return false
            }
        }
        return true
    }

    private fun checkVerification(barcode_Data: String?, last_Verified_On2: String){
        var found = false
        for (i in 0 until modelBarcodeList.size){
            if (modelBarcodeList[i].barcode_Data == barcode_Data){
                if (modelBarcodeList[i].isVerified == true){
                    Toast.makeText(this, "Already Verified", Toast.LENGTH_SHORT).show()
                }else{
                    bitmap = null
                    prevImageLink = ""
                    showPestDialog(barcode_Data.toString(), last_Verified_On2, modelBarcodeList[i].id)
                }
                found = true
            }
        }
        if (!found){
            Toast.makeText(this, "Equipment not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun modifyData(id: Int?, account_No: String?, order_No: String?, account_Name: String?,
                           barcode_Data: String?, last_Verified_On: String?, last_Verified_By: Int?,
                           created_On: String?, created_By_Id_User: Int?, verified_By: String?,
                           created_By: String?, isVerified: Boolean?, barcode_Type: String?,
                           service_Unit: String?, additional_Info: String?,
                           not_Accessible_Reason_List: ArrayList<BarcodeType>,
                           not_Accessible_Reason: String, modelBarcodeDDPestType: ArrayList<BarcodeDDPestType>) {

        var found = 0
        for (i in 0 until modelBarcodeList.size) {
            if (modelBarcodeList[i].barcode_Data == barcode_Data) {
                if (modelBarcodeList[i].isVerified == false) {
                    modelBarcodeList[i] = BarcodeDetailsData(modelBarcodeList[i].id, account_No, order_No, account_Name, barcode_Data,
                            last_Verified_On, last_Verified_By, created_On, created_By_Id_User, verified_By, created_By, isVerified, barcode_Type, service_Unit, additional_Info, not_Accessible_Reason_List, not_Accessible_Reason, modelBarcodeDDPestType)
                    Log.d("TAG-Veri", id.toString())
                    Log.d("TAG-VerifiedOn-start", last_Verified_On.toString())
                    verifyBarcode(modelBarcodeList[i].id, "Technician Scanner", account_No, order_No, barcode_Data, lat, long, last_Verified_On, last_Verified_By, modelBarcodeDDPestType, not_Accessible_Reason)
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
            Toast.makeText(this, "Equipment not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun verifyBarcode(barcodeId: Int?, activityName: String?, account_No: String?, order_No: String?, barcode_Data: String?,
                              lat: String?, long: String?, verifiedOn: String?, verifiedBy: Int?, pest_Type: ArrayList<BarcodeDDPestType>?, Not_Accessible_Reason: String) {
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
        verifyMap["Pest_Type"] = pest_Type
        verifyMap["Not_Accessible_Reason"] = naReason

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

    private fun showPestDialog(resultBarcode: String, last_Verified_On2: String, barcodeId: Int?){
        val li = LayoutInflater.from(this)
        promptsView = li.inflate(R.layout.add_pest_info_dialog, null)
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(promptsView)
        val alertDialog = alertDialogBuilder.create()
        val barcodeTypeTv = promptsView.findViewById(R.id.barcodeTypeTv) as TextView
        val subTypeTitleTv = promptsView.findViewById(R.id.subTypeTitleTv) as TextView
        val pestRecyclerView = promptsView.findViewById(R.id.pestRecyclerView) as RecyclerView
        val cancelBtn = promptsView.findViewById(R.id.btn_cancel) as AppCompatButton
        val saveBtn = promptsView.findViewById(R.id.saveBtn) as AppCompatButton
        val imgCapture = promptsView.findViewById(R.id.imgCapture) as ImageView
        val imgCaptured = promptsView.findViewById(R.id.imgCaptured) as ImageView
        val imgCancel = promptsView.findViewById(R.id.cancelLayout) as RelativeLayout
        val radioGrp = promptsView.findViewById(R.id.radioGrp) as RadioGroup
        val yesBtn = promptsView.findViewById(R.id.yesBtn) as RadioButton
        val noBtn = promptsView.findViewById(R.id.noBtn) as RadioButton
        val imageLinearLayout = promptsView.findViewById(R.id.imageLinearLayout) as LinearLayout
        pestType = ArrayList()
        val optionType = ArrayList<String>()
        var isChecked = false

        pestType.clear()
        optionType.clear()
        optionType.add("Select Option")
        modelBarcodeList.forEach {
            if (it.barcode_Data == resultBarcode){
                /*if ("mosquito dispenser".equals(it.barcode_Type, true)){
                    subTypeTitleTv.text = "Feature"
                }
                if ("rodent station".equals(it.barcode_Type, true) || "fly catcher machine".equals(it.barcode_Type, true) ){
                    subTypeTitleTv.text = "Types of Pest"
                }*/
                modelBarcodeDDPestType.forEach { pest ->
                    if (pest.barcode_Type == it.barcode_Type && pest.barcodeId == it.id) {
                        Log.d("TAG", "ID ${pest.id}")
                        barcodeTypeTv.text = it.barcode_Type
                        pestType.add(
                            BarcodeDDPestType(
                            pest.id,
                            pest.barcode_Type,
                            pest.sub_Type,
                            pest.show_Count,
                            pest.capture_Image,
                            pest.show_Option,
                            pest.option_Value,
                            pest.option_List,
                            "",
                            pest.image_Url,
                            pest.barcodeId,
                        ))
                        pest.option_List?.forEach { options ->
                            optionType.add(options.text.toString())
                        }
                    }
                }
            }
        }

        pestType.forEach {
            if (it.capture_Image == true){
                imageLinearLayout.visibility = View.VISIBLE
                return@forEach
            }
        }
        val arrayAdapter = object : ArrayAdapter<String>(this, R.layout.spinner_layout_new, optionType){
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
        pestTypeAdapter = PestTypeAdapter(this, pestType, arrayAdapter)
        pestRecyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        pestRecyclerView.layoutManager = layoutManager

        pestRecyclerView.adapter = pestTypeAdapter

        pestTypeAdapter.setOnEditTextChangedListener(object : PestTypeAdapter.OnEditTextChanged{
            override fun onTextChanged(position: Int, charSeq: String?, barcodeId: Int?, pestTypeId: Int?) {
                //Log.d("TAG-Count", charSeq)
                var count = charSeq
                currentItemCount = count.toString()
                pestType.forEach {
                    if (it.barcodeId == barcodeId && it.id == pestTypeId){
                        it.pest_Count = count
                    }
                }
                Log.d("TAG", "onText item position $position")
            }

            override fun onOptionTypeChanged(position: Int, charSeq: String?, barcodeId: Int?, pestTypeId: Int?) {
                var count = charSeq
                currentItemCount = count.toString()
                pestType.forEach {
                    if (it.barcodeId == barcodeId && it.id == pestTypeId){
                        it.pest_Count = count
                    }
                }
            }

            override fun onPictureIconClicked(position: Int, charSeq: String?, barcodeId: Int?, pestTypeId: Int?) {
                Log.d("TAG", "Picture")
                pos = position
                currentItemCount = charSeq.toString()
                barcodeIdFromAdapter = barcodeId.toString().toInt()
                pestTypeIdFromAdapter = pestTypeId.toString().toInt()
                getImageDialog(barcodeId, pestTypeId)
                Log.d("TAG", "onPicture item position $position")
                /*val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (intent.resolveActivity(applicationContext.packageManager) != null) {
                    barcodeIdFromAdapter = barcodeId.toString().toInt()
                    pestTypeIdFromAdapter = pestTypeId.toString().toInt()
                    // Create the File where the photo should go
                    val photoFile = createImageFile()
                    val photoURI: Uri = FileProvider.getUriForFile(applicationContext, BuildConfig.APPLICATION_ID + ".provider", photoFile)
                    mPhotoFile = photoFile
                    intent.putExtra("android.intent.extras.CAMERA_FACING", Camera.CameraInfo.CAMERA_FACING_BACK)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    TimeStamp = AppUtils.getCurrentTimeStamp()
                    startActivityForResult(intent, CAMERA_REQUEST)
                }*/
            }

            override fun onCancelIconClicked(position: Int, charSeq: String?, barcodeId: Int?, pestTypeId: Int?) {
                pestType.forEach {
                    if (it.barcodeId == barcodeId && it.id == pestTypeId){
                        //it.pest_Count = charSeq
                        it.image_Url = null
                    }
                }
                Log.d("TAG", "onCancel item position $position")
                //pestTypeAdapter.notifyItemChanged(position)
                pestTypeAdapter.notifyDataSetChanged()
            }
        })
        setOnImageUpdateListener(object : ImageUpdateListener{
            override fun onUpdateImage(url: String) {
                Picasso.get().load(url).fit().into(imgCaptured)
                imgCapture.visibility = View.GONE
                imgCaptured.visibility = View.VISIBLE
                imgCancel.visibility = View.VISIBLE
                pestType.forEach {
                    it.image_Url = url
                }
            }
        })
        imgCapture.setOnClickListener {
            getImageDialog(barcodeId, -1)
        }
        imgCancel.setOnClickListener {
            imgCapture.visibility = View.VISIBLE
            imgCaptured.visibility = View.GONE
            imgCancel.visibility = View.GONE
            pestType.forEach {
                it.image_Url = null
            }
        }
        cancelBtn.setOnClickListener {
            pestType.forEach {
                it.image_Url = null
                it.pest_Count = ""
            }
            alertDialog.cancel()
        }

        radioGrp.setOnCheckedChangeListener { _, _ ->
            isChecked = true
        }

        saveBtn.setOnClickListener {
            pestType.forEach {
                Log.d("TAG", "Saving: ${it.sub_Type} ${it.pest_Count} ${it.image_Url}")
            }
            var foundAllEmpty = true

            if (imageLinearLayout.visibility == View.VISIBLE) {
                pestType.forEach {
                    //Check for each empty
                    if (it.image_Url != null && it.pest_Count != "") {
                        foundAllEmpty = false
                    }
                }
            }else{
                pestType.forEach {
                    //Check for each empty
                    if (it.pest_Count != "") {
                        foundAllEmpty = false
                    }
                }
            }
            var partialEmpty = false
            pestType.forEach {
                //Check for partial Empty
                if (it.pest_Count != ""){
                    //foundAllEmpty = false
                    if (it.pest_Count.equals("yes", true)){
                        yesBtn.isChecked = true
                        isChecked = true
                    }
                    else if (it.pest_Count.equals("no", true)){ }
                    else if (it.pest_Count.toString().toInt() > 0){
                        yesBtn.isChecked = true
                        isChecked = true
                    }
                }else{
                    partialEmpty = true
                }
                /*if (it.image_Url == null && it.pest_Count != ""){
                    if (it.pest_Count.equals("no", true)){
                        foundAllEmpty = false
                        partialEmpty = false
                    }else{
                        if (it.pest_Count.equals("yes", true)) {
                            partialEmpty = true
                            foundAllEmpty = true
                            yesBtn.isChecked = true
                            Toast.makeText(this, "Please enter the data correctly", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }else if (it.pest_Count.toString().toInt() == 0){
                            foundAllEmpty = false
                            partialEmpty = false
                        }else if (it.pest_Count.toString().toInt() > 0){
                            partialEmpty = true
                            foundAllEmpty = true
                            Toast.makeText(this, "Please enter the data correctly", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }
                    }
                }
                if ((it.pest_Count == "" || it.pest_Count.equals("Select Option", true)) && it.image_Url != null){
                    partialEmpty = true
                    foundAllEmpty = true
                    Toast.makeText(this, "Please enter the data correctly", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if ((it.pest_Count.equals("no", true) || it.pest_Count.equals("yes", true)) && it.image_Url != null){
                    if (it.pest_Count.equals("yes", true)){
                        yesBtn.isChecked = true
                    }
                    foundAllEmpty = false
                    partialEmpty = false
                }
                if (it.pest_Count != ""){
                    if (!it.pest_Count.equals("no", true) && !it.pest_Count.equals("yes", true)){
                        if (it.pest_Count.toString().toInt() > 0){
                            yesBtn.isChecked = true
                        }
                    }
                }*/
            }

            Log.d("TAG", "Final $foundAllEmpty and $partialEmpty")
            if (!foundAllEmpty && !partialEmpty && isChecked) {
                modifyData(
                    id,
                    account_No,
                    order_No,
                    account_Name,
                    resultBarcode,
                    created_On,
                    empCode,
                    last_Verified_On2,
                    created_By_Id_User,
                    verified_By,
                    created_By,
                    true,
                    barcode_Type,
                    "",
                    "",
                    notAccessibleList,
                    naReason,
                    pestType
                )
                optionType.clear()
                pestType.clear()
                alertDialog.cancel()
            }else{
                Toast.makeText(this, "Please enter the data correctly", Toast.LENGTH_SHORT).show()
            }
        }

        alertDialog.show()
        alertDialog.setCancelable(false)
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        alertDialog.setCanceledOnTouchOutside(false)
    }

    private fun selectOldImage(){
        if (prevImageLink != ""){
            pestType.forEach {
                if (pestTypeIdFromAdapter == -1){
                    imageUpdateListener?.onUpdateImage(prevImageLink)
                }else {
                    if (it.barcodeId == barcodeIdFromAdapter && it.id == pestTypeIdFromAdapter) {
                        //it.pest_Count = currentItemCount
                        it.image_Url = prevImageLink
                    }
                }
            }
            pestTypeAdapter.notifyDataSetChanged()
        }
    }
    private fun uploadBoxImage(resourceId: String, taskId: String, file: String){
        val hashMap = HashMap<String, String>()
        hashMap["ResourceId"] = resourceId
        hashMap["TaskId"] = taskId
        hashMap["File"] = file

        val controller = NetworkCallController()
        controller.setListner(object : NetworkResponseListner<BaseResponse>{
            override fun onResponse(requestCode: Int, response: BaseResponse?) {
                Log.d("TAG", response.toString())
                if (response?.isSuccess == true) {
                    if (pestTypeIdFromAdapter == -1) {
                        imageUpdateListener?.onUpdateImage(response.data.toString())
                    } else {
                        pestType.forEach {
                            if (it.barcodeId == barcodeIdFromAdapter && it.id == pestTypeIdFromAdapter) {
                                //it.pest_Count = currentItemCount
                                it.image_Url = response.data
                                prevImageLink = response.data.toString()
                            }
                        }
                        //pestTypeAdapter.notifyItemChanged(pos)
                        pestTypeAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(requestCode: Int) {
            }
        })
        controller.uploadBoxImage(20210109, hashMap)
    }

    private fun getImageDialog(barcodeId: Int?, pestTypeId: Int?) {
        try {
            val mGeneralRealmData: RealmResults<GeneralData> = getRealm().where<GeneralData>(
                GeneralData::class.java
            ).findAll()
            if (mGeneralRealmData != null && mGeneralRealmData.size > 0) {
                val li = LayoutInflater.from(this)
                val promptsView = li.inflate(R.layout.layout_image_pest_dialog, null)
                val alertDialogBuilder = AlertDialog.Builder(this)
                alertDialogBuilder.setView(promptsView)
                val alertDialog = alertDialogBuilder.create()
                val selectedImg = promptsView.findViewById<ImageView>(R.id.selectedImg)
                val lnrCamera = promptsView.findViewById<LinearLayout>(R.id.lnrCamera)
                val lnrGallery = promptsView.findViewById<LinearLayout>(R.id.lnrGallery)
                val btn_cancel = promptsView.findViewById<TextView>(R.id.btnCancel)
                val cardSelected = promptsView.findViewById<LinearLayout>(R.id.cardSelected)
                val attrs = intArrayOf(R.attr.selectableItemBackground)
                val typedArray: TypedArray = this.obtainStyledAttributes(attrs)
                val backgroundResource = typedArray.getResourceId(0, 0)
                lnrCamera.setBackgroundResource(backgroundResource)
                lnrGallery.setBackgroundResource(backgroundResource)
                if (bitmap != null) {
                    cardSelected.visibility = View.VISIBLE
                    Glide.with(this)
                        .load(bitmap)
                        .error(android.R.drawable.stat_notify_error)
                        .into(selectedImg)
                } else {
                    cardSelected.visibility = View.GONE
                }
                cardSelected.setOnClickListener { view: View? ->
                    if (bitmap != null) {
                        alertDialog.dismiss()
                        val baos = ByteArrayOutputStream()
                        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        val b = baos.toByteArray()
                        val encodedImage =
                            Base64.encodeToString(b, Base64.DEFAULT)
                        selectOldImage()
                        //uploadBoxImage(resourceId, taskId, encodedImage)
                    }
                }
                lnrCamera.setOnClickListener { view: View? ->
                    requestStoragePermission(true, barcodeId, pestTypeId)
                    alertDialog.dismiss()
                }
                lnrGallery.setOnClickListener { view: View? ->
                    requestStoragePermission(false, barcodeId, pestTypeId)
                    alertDialog.dismiss()
                }
                btn_cancel.setOnClickListener { v: View? -> alertDialog.dismiss() }
                alertDialog.setCanceledOnTouchOutside(false)
                alertDialog.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun dispatchGalleryIntent() {
        try {
            val pickPhoto = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivityForResult(pickPhoto, REQUEST_GALLERY_PHOTO)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun requestCameraPermission(intent: Intent) {
        try {
            Dexter.withActivity(this)
                .withPermission(
                    Manifest.permission.CAMERA
                )
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                        startActivityForResult(intent, 3000)
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

    private fun requestStoragePermission(isCamera: Boolean, barcodeId: Int?, pestTypeId: Int?) {
        try {
            Dexter.withActivity(this)
                .withPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            if (isCamera) {
                                dispatchTakePictureIntent(barcodeId, pestTypeId)
                            } else {
                                dispatchGalleryIntent()
                            }
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest>,
                        token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
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

    private fun dispatchTakePictureIntent(barcodeId: Int?, pestTypeId: Int?) {
        try {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (intent.resolveActivity(applicationContext.packageManager) != null) {
                barcodeIdFromAdapter = barcodeId.toString().toInt()
                pestTypeIdFromAdapter = pestTypeId.toString().toInt()
                // Create the File where the photo should go
                val photoFile = createImageFile()
                val photoURI: Uri = FileProvider.getUriForFile(applicationContext, BuildConfig.APPLICATION_ID + ".provider", photoFile)
                mPhotoFile = photoFile
                intent.putExtra("android.intent.extras.CAMERA_FACING", Camera.CameraInfo.CAMERA_FACING_BACK)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                TimeStamp = AppUtils.getCurrentTimeStamp()
                startActivityForResult(intent, CAMERA_REQUEST)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun openSettings() {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", this.packageName, null)
            intent.data = uri
            startActivityForResult(intent, 101)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
    private fun showSettingsDialog() {
        try {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Need Permissions")
            builder.setMessage(
                "This app needs permission to use this feature. You can grant them in app settings."
            )
            builder.setPositiveButton("GOTO SETTINGS") { dialog: DialogInterface, which: Int ->
                dialog.cancel()
                openSettings()
            }
            builder.setNegativeButton(
                "Cancel"
            ) { dialog: DialogInterface, which: Int -> dialog.cancel() }
            builder.show()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
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

    private fun getRealPathFromUri(contentUri: Uri?): String? {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = this.getContentResolver().query(contentUri!!, proj, null, null, null)
            assert(cursor != null)
            val column_index = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor?.moveToFirst()
            cursor?.getString(column_index!!)
        } finally {
            cursor?.close()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        val last_Verified_On2 = AppUtils.currentDateTimeWithTimeZone()
        Log.d("TAG-Act", last_Verified_On2)
        if (requestCode == 3000){
            val qrData = data?.getStringExtra("content")
            checkVerification(qrData.toString(), last_Verified_On2)
            return
        }
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Log.d("TAG", "Cam Request")

            selectedImagePath = mPhotoFile?.path ?: ""
            if (selectedImagePath != null || selectedImagePath != ""){
                val bit = BitmapDrawable(resources, selectedImagePath).bitmap
                Log.d("TAG", bit.toString())
                val i = (bit.height * (1024.0 / bit.width)).toInt()
                bitmap = Bitmap.createScaledBitmap(bit, 1024, i, true)
            }
            val baos = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 50, baos)
            val b = baos.toByteArray()
            val encodedImage = Base64.encodeToString(b, Base64.DEFAULT)
            /*val imageAsBytes = Base64.decode(encodedImage, Base64.DEFAULT)
            val decodedImage = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size);*/
            Log.d("TAG-Image", encodedImage)
            uploadBoxImage(resourceId, taskId, encodedImage)
        }else if (requestCode == REQUEST_GALLERY_PHOTO && resultCode == Activity.RESULT_OK){
            val selectedImage = data?.data
            mPhotoFile = File(getRealPathFromUri(selectedImage).toString())
            selectedImagePath = mPhotoFile!!.path
            if (selectedImagePath != null) {
                val bit = BitmapDrawable(this.resources, selectedImagePath).bitmap
                val i = (bit.height * (1024.0 / bit.width)).toInt()
                bitmap = Bitmap.createScaledBitmap(bit, 1024, i, true)
            }
            val baos = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 50, baos)
            val b = baos.toByteArray()
            val encodedImage = Base64.encodeToString(b, Base64.DEFAULT)
            uploadBoxImage(resourceId, taskId, encodedImage)
        }
        if (result != null) {
            if (result.contents != null) {
                checkVerification(result.contents, last_Verified_On2.toString())
                Log.d("TAG-VerifiedOn-beyond", last_Verified_On2.toString())
                Log.d("TAG-QR", result.contents)
            } else {
                Log.d("TAG-QR", "Not found")
            }
        }
    }

    private fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap? {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }
    private fun getCapturedImage(requestCode: Int): Item {
        var myClass: Item? = null
        var picturePath: String
        if (requestCode == CAMERA_REQUEST) {
            myClass = Item()
            val f: File = File("$IMAGE_FILE_PATH/$tempImageName")
            picturePath = f.absolutePath
            if (!f.exists()) {
                picturePath = ""
            }
            myClass.setPath(picturePath)
        }
        return myClass!!
    }

    interface ImageUpdateListener{
        fun onUpdateImage(url: String)
    }

    fun setOnImageUpdateListener(imageUpdateListener: ImageUpdateListener){
        this.imageUpdateListener = imageUpdateListener
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