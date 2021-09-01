package com.ab.hicarerun.activities

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.hardware.Camera
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.BaseActivity
import com.ab.hicarerun.BaseApplication
import com.ab.hicarerun.BuildConfig
import com.ab.hicarerun.R
import com.ab.hicarerun.adapter.BarcodeAdapter
import com.ab.hicarerun.adapter.PestTypeAdapter
import com.ab.hicarerun.databinding.ActivityTsverificationBinding
import com.ab.hicarerun.network.NetworkCallController
import com.ab.hicarerun.network.NetworkResponseListner
import com.ab.hicarerun.network.models.LoginResponse
import com.ab.hicarerun.network.models.TSScannerModel.*
import com.ab.hicarerun.service.listner.LocationManagerListner
import com.ab.hicarerun.utils.AppUtils
import com.ab.hicarerun.utils.LocaleHelper
import com.google.zxing.integration.android.IntentIntegrator
import io.realm.RealmResults
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class TSVerificationActivity : BaseActivity(), LocationManagerListner {

    lateinit var binding: ActivityTsverificationBinding
    lateinit var modelBarcodeList: ArrayList<BarcodeList>
    lateinit var pestList: ArrayList<Pest_Type>
    lateinit var barcodeAdapter: BarcodeAdapter
    lateinit var pestTypeAdapter: PestTypeAdapter
    lateinit var progressDialog: ProgressDialog

    var empCode: Int? = null
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
    var lat = ""
    var long = ""
    var mPermissions = false
    val REQUEST_CODE = 1234
    val CAMERA_REQUEST = 100
    var selectedImagePath = ""
    var mPhotoFile: File? = null
    var bitmap: Bitmap? = null
    lateinit var promptsView: View
    var resourceId = ""
    var taskId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTsverificationBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)

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
            resourceId = loginResponse[0]?.userID.toString()
            Log.d("TAG-Login", empCode.toString())
        }
//        getEmpCode()

        if (empCode == null){
            progressDialog.show()
        }

        modelBarcodeList = ArrayList()
        pestList = ArrayList()
        barcodeAdapter = BarcodeAdapter(this, modelBarcodeList, "TSVerification")
        pestTypeAdapter = PestTypeAdapter(this, pestList)
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
            integrator.setCaptureActivity(CaptureActivityPortrait::class.java)
            integrator.setBeepEnabled(false)
            integrator.setPrompt("Scan a barcode")
            if (isFetched == 1){
                if (modelBarcodeList.isNotEmpty()){
                    integrator.initiateScan()
                }else{
                    Toast.makeText(this, "Rodent station not found", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Please Enter Order No", Toast.LENGTH_SHORT).show()
            }
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

/*    private fun getEmpCode(){
        val userId = SharedPreferencesUtility.getPrefString(this, SharedPreferencesUtility.PREF_USERID)
        val controller = NetworkCallController()
        controller.setListner(object : NetworkResponseListner<Any>{
            override fun onResponse(requestCode: Int, response: Any) {
                progressDialog.dismiss()
                val responseProfile: Profile = response as Profile
                empCode = responseProfile.employeeCode.toString().toInt()
                Log.d("TAG-profile", empCode.toString())
            }
            override fun onFailure(requestCode: Int) {
                progressDialog.dismiss()
                Log.d("TAG", requestCode.toString())
            }
        })
        controller.getTechnicianProfile(1000, userId)
    }*/

    private fun getOrderDetails(orderNoInput: String){
        binding.errorTv.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        val controller = NetworkCallController()
        controller.setListner(object : NetworkResponseListner<OrderDetails> {
            override fun onResponse(requestCode: Int, response: OrderDetails?) {
                val success = response?.isSuccess.toString()
                if (success == "true"){
                    isFetched = 1
                    modelBarcodeList.clear()
                    pestList.clear()
                    val accountNo = response?.data?.accountNo
                    val orderNo = response?.data?.orderNo
                    val accountName = response?.data?.accountName
                    val startDate = response?.data?.startDate
                    val endDate = response?.data?.endDate
                    val regionName = response?.data?.regionName
                    val serviceGroup = response?.data?.serviceGroup
                    val servicePlan = response?.data?.servicePlan
                    val barcodeType = response?.data?.barcodeType
                    val barcodeList = response?.data?.barcodeList
                    if (response?.data?.barcodeList != null) {
                        var itemsCount = 0
                        for (i in 0 until response.data.barcodeList.size) {
                            itemsCount++
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
                            val pestResp = response.data.barcodeList[i].pest_Type
                            if (!pestResp.isNullOrEmpty()){
                                for (j in 0 until pestResp.size){
                                    pestList.add(Pest_Type(pestResp[j].id, pestResp[j].barcode_Type, pestResp[j].sub_Type, pestResp[j].show_Count, pestResp[j].capture_Image))
                                }
                            }
                            modelBarcodeList.add(
                                BarcodeList(
                                    id,
                                    account_No,
                                    order_No,
                                    account_Name,
                                    barcode_Data,
                                    last_Verified_On,
                                    last_Verified_By,
                                    created_On,
                                    created_By_Id_User,
                                    verified_By,
                                    created_By,
                                    isVerified,
                                    pestList,
                                    "no"
                                )
                            )
                        }
                        OrderDetails(
                            response.isSuccess,
                            Data(
                                accountNo,
                                orderNo,
                                accountName,
                                startDate,
                                endDate,
                                regionName,
                                serviceGroup,
                                servicePlan,
                                barcodeType,
                                modelBarcodeList
                            ),
                            response.errorMessage,
                            response.param1,
                            response.responseMessage
                        )
                        if (itemsCount > 0) {
                            binding.barcodeErrorTv.visibility = View.GONE
                        } else {
                            binding.barcodeErrorTv.visibility = View.VISIBLE
                        }
                        binding.boxesTitleTv.text = "Bait Stations: (${modelBarcodeList.size})"
                    }
                    populateViews(accountName, regionName, servicePlan)
                    barcodeAdapter.notifyDataSetChanged()
                    binding.progressBar.visibility = View.GONE
                }else{
                    modelBarcodeList.clear()
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

    private fun verifyBarcode(barcodeId: Int?, activityName: String?, account_No: String?, order_No: String?, barcode_Data: String?,
                              lat: String?, long: String?, verifiedOn: String?, verifiedBy: Int?){
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
        controller.setListner(object : NetworkResponseListner<BaseResponse>{
            override fun onResponse(requestCode: Int, response: BaseResponse?) {
                if (response != null){
                    if (response.isSuccess == true){
                        if (response.data == "Verified"){
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(applicationContext, "Verified successfully", Toast.LENGTH_SHORT).show()
                            getOrderDetails(order_No.toString())
                        }
                    }else{
                        Log.d("TAG-VERIFIER", "Something wrong ${response.data}")
                    }
                }
            }

            override fun onFailure(requestCode: Int) {
                binding.progressBar.visibility = View.GONE
                Log.d("TAG-VERIFIER", requestCode.toString())
            }
        })
        controller.verifyBarcodeDetails(20212, verifyMap)
    }

    private fun modifyData(id: Int?, account_No: String?, order_No: String?, account_Name: String?,
                           barcode_Data: String?, last_Verified_On: String?, last_Verified_By: Int?,
                           created_On: String?, created_By_Id_User: Int?, verified_By: String?,
                           created_By: String?, pestList: ArrayList<Pest_Type>?, isVerified: Boolean?){

        binding.progressBar.visibility = View.VISIBLE
        var found = 0
        for (i in 0 until modelBarcodeList.size){
            if(modelBarcodeList[i].barcode_Data == barcode_Data){
                if (modelBarcodeList[i].isVerified == false){
                    modelBarcodeList[i] = BarcodeList(modelBarcodeList[i].id, account_No, order_No, account_Name, barcode_Data,
                        last_Verified_On, last_Verified_By, created_On, created_By_Id_User, verified_By, created_By, isVerified, pestList, "no")
                    Log.d("TAG-Veri", id.toString())
                    verifyBarcode(modelBarcodeList[i].id, "TSVerification", account_No, order_No, barcode_Data, lat, long, last_Verified_On, last_Verified_By)
                    barcodeAdapter.notifyItemChanged(i)
                    binding.barcodeRecycler.post {
                        binding.barcodeRecycler.smoothScrollToPosition(i)
                    }
                }else{
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Already verified", Toast.LENGTH_SHORT).show()
                }
                found = 1
            }
        }
        if (found == 0){
            binding.progressBar.visibility = View.GONE
            Toast.makeText(this, "Rodent station not found", Toast.LENGTH_SHORT).show()
        }
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
        val layoutManager = GridLayoutManager(this, 2)
        pestRecyclerView.layoutManager = layoutManager
        pestRecyclerView.adapter = pestTypeAdapter

        radioGrp.setOnCheckedChangeListener { _, _ ->
            if (yesBtn.isChecked){
                lnrCheck.visibility = View.VISIBLE
                Log.d("TAG", "yes")
            }else{
                lnrCheck.visibility = View.GONE
                Log.d("TAG", "no")
            }
        }
        addImgBtn.setOnClickListener {
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
        }
        if (bitmap != null){
            clickedImageLayout.visibility = View.VISIBLE
            clickedIv.setImageBitmap(bitmap)
        }
        cancelBtn.setOnClickListener {
            alertDialog.cancel()
        }

        alertDialog.show()
        alertDialog.setCancelable(false)
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.setCanceledOnTouchOutside(false)
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

    private fun checkCameraHardware(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
    }

    private fun startCamera2() {
//        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.container, Camera2Fragment.newInstance(), getString(R.string.fragment_camera2));
//        transaction.commit();
        val intent = Intent(this, Camera2Activity::class.java)
        intent.putExtra(AppUtils.CAMERA_ORIENTATION, "BACK")
        startActivity(intent)
    }
    private fun init() {
        if (mPermissions) {
            if (checkCameraHardware(this)) {
                // Open the Camera
                startCamera2()
            } else {
                Toast.makeText(this, "You need a camera to use this application", Toast.LENGTH_SHORT).show()
            }
        } else {
            verifyPermissions()
        }
    }
    private fun verifyPermissions(){
        Log.d("TAG", "verifyPermissions: asking user for permissions.")
        val permissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
        if (ContextCompat.checkSelfPermission(applicationContext,
                permissions[0]) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(applicationContext,
                permissions[1]) == PackageManager.PERMISSION_GRANTED) {
            mPermissions = true
            init()
        }else{
            ActivityCompat.requestPermissions(
                this,
                permissions,
                REQUEST_CODE
            )
        }
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
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE){
            if (mPermissions){
                init()
            }else{
                verifyPermissions()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        last_Verified_On = AppUtils.currentDateTimeWithTimeZone()
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Log.d("TAG", "Camera")
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
            val civ = promptsView.findViewById(R.id.clickedIv) as ImageView
            val clickedLayout = promptsView.findViewById(R.id.clickedImageLayout) as RelativeLayout
            if (bitmap != null) {
                clickedLayout.visibility = View.VISIBLE
                civ.setImageBitmap(bitmap)
            }
            uploadBoxImage(resourceId, taskId, encodedImage)
            Log.d("TAG", "selectedImagePath")
        }
        if (result != null){
            if (result.contents != null){
                showPestDialog()
                /*modifyData(id, account_No, order_No, account_Name, result.contents, created_On, empCode,
                    last_Verified_On, created_By_Id_User, verified_By, created_By, pestList, true)*/
                Log.d("TAG-QR", result.contents)
            }else{
                Log.d("TAG-QR", "Not found")
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun locationFetched(mLocation: Location?, oldLocation: Location?, time: String?, locationProvider: String?) {
        lat = mLocation?.latitude.toString()
        long = mLocation?.longitude.toString()
    }
}