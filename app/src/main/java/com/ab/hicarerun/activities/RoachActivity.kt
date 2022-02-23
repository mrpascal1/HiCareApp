package com.ab.hicarerun.activities

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.hardware.Camera
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
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
import com.ab.hicarerun.BuildConfig
import com.ab.hicarerun.R
import com.ab.hicarerun.adapter.roach.RoachAdapter
import com.ab.hicarerun.databinding.ActivityRoachBinding
import com.ab.hicarerun.network.NetworkCallController
import com.ab.hicarerun.network.NetworkResponseListner
import com.ab.hicarerun.network.models.LoginResponse
import com.ab.hicarerun.network.models.checklistmodel.UploadCheckListData
import com.ab.hicarerun.network.models.checklistmodel.UploadCheckListRequest
import com.ab.hicarerun.network.models.roachmodel.roachlistmodel.RoachBase
import com.ab.hicarerun.network.models.roachmodel.roachlistmodel.RoachList
import com.ab.hicarerun.network.models.roachmodel.saveroachmodel.RoachSaveBase
import com.ab.hicarerun.utils.AppUtils
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.squareup.picasso.Picasso
import es.dmoral.toasty.Toasty
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class RoachActivity : BaseActivity() {

    lateinit var binding: ActivityRoachBinding
    lateinit var roachAdapter: RoachAdapter
    lateinit var locationList: ArrayList<String>
    lateinit var roachList: ArrayList<RoachList>
    //lateinit var dialogView: View
    //lateinit var updateDialogView: View
    private var selectedImagePath = ""
    private var mPhotoFile: File? = null
    private var bitmap: Bitmap? = null
    var imageUpdateListener: ImageUpdateListener? = null

    var mPermissions = false
    val CAMERA_REQUEST = 1
    var dName = ""
    var dId = -1
    var aNo = ""
    var p = -1
    var imageCountMap = HashMap<String, Any>()
    var isImageUploaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoachBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //binding.addBtn.isEnabled = false
        binding.titleTv.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
        locationList = ArrayList()
        roachList = ArrayList()
        roachAdapter = RoachAdapter(this)
        roachAdapter.setHasStableIds(true)
        binding.roachRecyclerView.configure(this).apply {
            adapter = roachAdapter
        }
        binding.addBtn.setOnClickListener {
            showAddDialog()
        }
        binding.backIv.setOnClickListener {
            onBackPressed()
        }
        roachAdapter.roachClickListener = object : RoachAdapter.RoachClickListener{
            override fun uploadClick(position: Int, deviceName: String, accountNo: String) {
                dName = deviceName
                aNo = accountNo
                p = position
                imageCountMap.clear()
                imageCountMap["TaskId"] = AppUtils.taskId
                imageCountMap["AccountNo"] = accountNo
                imageCountMap["DeviceName"] = deviceName
                showUpdateDialog()
                //roachAdapter.notifyItemChanged(position)
            }
            override fun deleteClick(position: Int, deviceId: Int) {
                p = position
                dId = deviceId
                isImageUploaded = false
                deleteDevice(deviceId)
            }
        }
        getRoachList()
    }

    private fun getRoachList(){
        roachList.clear()
        showProgressDialog()
        val controller = NetworkCallController()
        controller.setListner(object : NetworkResponseListner<RoachBase>{
            override fun onResponse(requestCode: Int, response: RoachBase?) {
                if (response != null && response.isSuccess == true){
                    val data = response.responseData
                    if (data != null){
                        if (!data.roachList.isNullOrEmpty()){
                            roachList.addAll(data.roachList)
                            roachAdapter.addData(roachList)
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
        controller.getAllDeviceByAccount(20222, AppUtils.accountId, AppUtils.taskId)
    }

    private fun deleteDevice(deviceId: Int){
        showProgressDialog()
        val controller = NetworkCallController()
        controller.setListner(object : NetworkResponseListner<RoachSaveBase>{
            override fun onResponse(requestCode: Int, response: RoachSaveBase?) {
                if (response != null && response.isSuccess == true){
                    Toasty.success(applicationContext, "Device deleted successfully").show()
                    getRoachList()
                }else{
                    Toasty.success(applicationContext, "Update error").show()
                }
                dismissProgressDialog()
            }
            override fun onFailure(requestCode: Int) {
                dismissProgressDialog()
                Log.d("TAG", "Delete device API failed")
            }
        })
        controller.deleteDevice(20222, deviceId)
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

    private fun uploadOnsiteImage(base64: String){
        try {
            val loginRealmModels = BaseApplication.getRealm().where(LoginResponse::class.java).findAll()
            if (loginRealmModels != null && loginRealmModels.size > 0) {
                val userId = loginRealmModels[0]?.userID
                val request = UploadCheckListRequest()
                request.resourceId = userId
                request.fileUrl = ""
                request.fileName = ""
                request.taskId = AppUtils.taskId
                request.fileContent = base64
                val controller = NetworkCallController()
                controller.setListner(object : NetworkResponseListner<UploadCheckListData> {
                    override fun onResponse(requestCode: Int, response: UploadCheckListData?) {
                        dismissProgressDialog()
                        imageCountMap["ImgUrl"] = response?.fileUrl.toString()
                        isImageUploaded = true
                        imageUpdateListener?.onUpdateImage(response?.fileUrl.toString())
                        Log.d("TAG", "Image uploaded")
                    }

                    override fun onFailure(requestCode: Int) {
                        dismissProgressDialog()
                        Log.d("TAG", "Image upload error in Roach")
                    }
                })
                controller.uploadCheckListAttachment(1211, request)
            }
        }catch (e: Exception){
            dismissProgressDialog()
            Log.d("TAG", "Image upload error in Roach")
        }
    }

    private fun updateImageAndCount(){
        showProgressDialog()
        val controller = NetworkCallController()
        controller.setListner(object : NetworkResponseListner<RoachSaveBase>{
            override fun onResponse(requestCode: Int, response: RoachSaveBase?) {
                if (response != null && response.isSuccess == true){
                    Toasty.success(applicationContext, "Updated successfully").show()
                    roachList.forEach {
                        if (it.deviceName == dName){
                            it.isDeviceUpdateDone = true
                            return@forEach
                        }
                    }
                    roachAdapter.notifyItemChanged(p)
                    imageCountMap.clear()
                    //getRoachList()
                }else{
                    Toasty.error(applicationContext, "Update error").show()
                }
                dismissProgressDialog()
            }
            override fun onFailure(requestCode: Int) {
                dismissProgressDialog()
                Log.d("TAG", "Update roach data failed")
            }
        })
        controller.updateImageForApp(20221, imageCountMap)
    }

    private fun showUpdateDialog(){
        showProgressDialog()
        isImageUploaded = false
        /*val li = LayoutInflater.from(this)
        updateDialogView = li.inflate(R.layout.update_roach_dialog, null)
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(updateDialogView)*/
        val alertDialog = Dialog(this).apply {
            setCancelable(false)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
            setCanceledOnTouchOutside(false)
            setContentView(R.layout.update_roach_dialog)
        }
        val countEt = alertDialog.findViewById<EditText>(R.id.countEt)
        val cancelBtn = alertDialog.findViewById<AppCompatButton>(R.id.btnCancel)
        val okBtn = alertDialog.findViewById<AppCompatButton>(R.id.okBtn)
        val imgCapture = alertDialog.findViewById<ImageView>(R.id.imgCapture)
        val imgCaptured = alertDialog.findViewById<ImageView>(R.id.imgCaptured)
        val imgCancel = alertDialog.findViewById<RelativeLayout>(R.id.cancelLayout)

        imgCapture.setOnClickListener {
            requestStoragePermission(true)
        }
        setOnImageUpdateListener {
            Picasso.get().load(it).fit().into(imgCaptured)
            imgCapture.visibility = View.GONE
            imgCaptured.visibility = View.VISIBLE
            imgCancel.visibility = View.VISIBLE
            Log.d("TAG", "Image Loaded")
            //dismissProgressDialog()
        }
        imgCancel.setOnClickListener {
            imgCapture.visibility = View.VISIBLE
            imgCaptured.visibility = View.GONE
            imgCancel.visibility = View.GONE
        }
        okBtn.setOnClickListener {
            if (isImageUploaded){
                val insectCount = countEt.text.toString().trim()
                if (insectCount != ""){
                    imageCountMap["InsectCount"] = insectCount.toInt()
                    updateImageAndCount()
                    alertDialog.dismiss()
                }else{
                    Toasty.error(this, "Please enter count").show()
                }
            }else{
                Toasty.error(this, "Please upload image").show()
            }
        }
        cancelBtn.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
        dismissProgressDialog()
    }

    private fun showAddDialog(){
        var selectedLocation = ""
        val dialogView = Dialog(this).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
            setCanceledOnTouchOutside(false)
            setContentView(R.layout.add_roach_dialog)
        }
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
            dialogView.dismiss()
        }
        cancelBtn.setOnClickListener {
            dialogView.dismiss()
        }
        dialogView.show()
    }

    private fun dispatchTakePictureIntent() {
        try {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (intent.resolveActivity(this.packageManager) != null) {
                // Create the File where the photo should go
                val photoFile = createImageFile()
                val photoURI: Uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", photoFile)
                mPhotoFile = photoFile
                intent.putExtra("android.intent.extras.CAMERA_FACING", Camera.CameraInfo.CAMERA_FACING_BACK)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                val TimeStamp = AppUtils.getCurrentTimeStamp()
                startActivityForResult(intent, CAMERA_REQUEST)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun requestStoragePermission(isCamera: Boolean) {
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
                                dispatchTakePictureIntent()
                            } else {
                                //dispatchGalleryIntent()
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
                .withErrorListener {
                    Toast.makeText(this, "Error occurred! ", Toast.LENGTH_SHORT).show()
                }
                .onSameThread()
                .check()
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
            val builder = androidx.appcompat.app.AlertDialog.Builder(this)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            try {
                showProgressDialog()
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
                Log.d("TAG-Image", encodedImage)
                uploadOnsiteImage(encodedImage)
            }catch (e: Exception){
                dismissProgressDialog()
            }
        }
    }

    fun RecyclerView.configure(context: Context, reverseLayout: Boolean = false): RecyclerView{
        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, reverseLayout)
        isNestedScrollingEnabled = false
        setHasFixedSize(true)
        return this
    }


    fun setOnImageUpdateListener(imageUpdateListener: ImageUpdateListener){
        this.imageUpdateListener = imageUpdateListener
    }

    fun interface ImageUpdateListener{
        fun onUpdateImage(url: String)
    }
}