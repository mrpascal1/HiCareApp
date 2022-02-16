package com.ab.hicarerun.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.hardware.Camera
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.BaseActivity
import com.ab.hicarerun.BaseApplication
import com.ab.hicarerun.BuildConfig
import com.ab.hicarerun.R
import com.ab.hicarerun.adapter.pulse.PulseQuestionAdapter
import com.ab.hicarerun.databinding.ActivityPulseBinding
import com.ab.hicarerun.network.NetworkCallController
import com.ab.hicarerun.network.NetworkResponseListner
import com.ab.hicarerun.network.models.LoginResponse
import com.ab.hicarerun.network.models.checklistmodel.UploadCheckListData
import com.ab.hicarerun.network.models.checklistmodel.UploadCheckListRequest
import com.ab.hicarerun.network.models.pulsemodel.*
import com.ab.hicarerun.network.models.tsscannermodel.BaseResponse
import com.ab.hicarerun.utils.AppUtils
import com.ab.hicarerun.utils.LocaleHelper
import com.google.android.material.chip.Chip
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PulseActivity : BaseActivity() {

    lateinit var binding: ActivityPulseBinding
    lateinit var pulseQuestionAdapter: PulseQuestionAdapter
    val questionList = ArrayList<QuestionList>()
    val questionOptions = ArrayList<QuestionOption>()
    val subQuestionList = ArrayList<SubQuestionList>()
    var pulseData: PulseData? = null
    var mPermissions = false
    var REQUEST_CODE = 1234
    private var selectedImagePath = ""
    private var mPhotoFile: File? = null
    private var bitmap: Bitmap? = null
    private var checkPosition = 0
    private var qId = -1
    private var cBy = -1
    private var isFromSubList = false
    val CAMERA_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPulseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
        }

        val genres = arrayOf("Thriller", "Comedy", "Adventure", "Thriller", "Comedy", "Adventure", "Thriller", "Comedy", "Adventure")
        genres.forEach {
            val states = arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(-android.R.attr.state_checked)
            )
            val colors = intArrayOf(
                Color.parseColor("#2BB77A"),
                // chip unchecked color
                Color.parseColor("#E0E0E0")
            )
            val colorList = ColorStateList(states, colors)
            val chip = Chip(view.context)
            chip.text = it
            chip.isClickable = true
            chip.isCheckable = true
            chip.isCheckedIconVisible = false
            chip.chipBackgroundColor = colorList
            //binding.chipGroup.addView(chip)
        }

        binding.titleTv.setTypeface(Typeface.DEFAULT, Typeface.BOLD)

        pulseQuestionAdapter = PulseQuestionAdapter(this)

        val questionsLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.questionListRecycler.layoutManager = questionsLayoutManager
        binding.questionListRecycler.setHasFixedSize(true)
        binding.questionListRecycler.isNestedScrollingEnabled = false
        binding.questionListRecycler.adapter = pulseQuestionAdapter

        binding.backIv.setOnClickListener {
            finish()
        }

        binding.saveBtn.setOnClickListener {
            updateB2BInspection()
        }

        getPulseB2bInspectionQuestions(AppUtils.taskId, AppUtils.resourceId, LocaleHelper.getLanguage(this))

        pulseQuestionAdapter.setOnCameraClickHandler(object : PulseQuestionAdapter.OnCameraClickListener{
            override fun onCameraClicked(position: Int, questionId: Int?, clickedBy: Int, sublistClick: Boolean) {
                qId = questionId.toString().toInt()
                cBy = clickedBy
                checkPosition = position
                isFromSubList = sublistClick
                requestStoragePermission(true)
            }

            override fun onCancelClicked(position: Int, questionId: Int?, clickedBy: Int, sublistClick: Boolean) {
                qId = questionId.toString().toInt()
                cBy = clickedBy
                checkPosition = position
                isFromSubList = sublistClick
                val pulseList = ArrayList<QuestionList>()
                pulseList.addAll(questionList)
                val pulseSubList = ArrayList<SubQuestionList>()
                pulseSubList.addAll(subQuestionList)
                if (isFromSubList){
                    pulseSubList.forEach {
                        if (it.questionId == qId){
                            if (it.pictureURL?.isEmpty() == true) {
                                it.pictureURL = null
                            }
                        }
                    }
                }else {
                    pulseList.forEach {
                        if (it.questionId == qId) {
                            if (it.pictureURL?.isEmpty() == true) {
                                it.pictureURL = null
                            }
                        }
                    }
                }
                pulseQuestionAdapter.notifyDataSetChanged()
                //validate2()
            }
        })
    }

    private fun getPulseB2bInspectionQuestions(taskId: String, resourceId: String, lan: String){
        val controller = NetworkCallController()
        controller.setListner(object: NetworkResponseListner<PulseResponse>{
            override fun onResponse(requestCode: Int, response: PulseResponse?) {
                if (response != null){
                    if (response.isSuccess == true){
                        pulseData = response.data
                        if (response.data != null){
                            if (response.data.isTabList == false){
                                questionList.clear()
                                questionOptions.clear()
                                subQuestionList.clear()
                                questionList.addAll(response.data.questionList!!)
                                questionList.forEach {
                                    if (!it.questionOption.isNullOrEmpty()) {
                                        questionOptions.addAll(it.questionOption)
                                    }
                                }
                                questionOptions.forEach {
                                    if (!it.subQuestionList.isNullOrEmpty()) {
                                        subQuestionList.addAll(it.subQuestionList)
                                    }
                                }
                                pulseQuestionAdapter.addData(questionList)
                            }
                        }
                    }
                }
            }
            override fun onFailure(requestCode: Int) {
                Log.d("TAG", "Failed $requestCode")
            }
        })
        controller.getPulseB2bInspectionQuestions(202202, taskId, resourceId, lan)
    }

    private fun updateB2BInspection(){
        val controller = NetworkCallController()
        controller.setListner(object : NetworkResponseListner<BaseResponse>{
            override fun onResponse(requestCode: Int, response: BaseResponse?) {
                if (response != null){
                    if (response.isSuccess == true){
                        if (!response.data.isNullOrEmpty()){
                            Toast.makeText(applicationContext, "Success", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            override fun onFailure(requestCode: Int) {
                Log.d("TAG", "$requestCode")
            }
        })
        Log.d("Update", "$pulseData")
        //controller.updateB2BInspection(202215, pulseData)
    }

    private fun isImgChecked(questionList: List<QuestionList>): Boolean {
        questionList.forEach {
            if (it.isPictureRequired == true) {
                if (it.pictureURL != null && it.pictureURL!!.isNotEmpty()) {
                    return false
                }
            }
        }
        return true
    }

    private fun isSubListChecked(listData: List<SubQuestionList>): Boolean{
        listData.forEach {
            if (it.answer == null || it.answer?.length == 0) {
                return false
            }
        }
        return false
    }

    private fun isListChecked(listData: List<QuestionList>): Boolean{
        listData.forEach {
            if (it.answer == null || it.answer?.length == 0) {
                return false
            }
        }
        return true
    }

    private fun uploadOnsiteImage(base64: String) {
        try {
            val LoginRealmModels = BaseApplication.getRealm().where(LoginResponse::class.java).findAll()
            if (LoginRealmModels != null && LoginRealmModels.size > 0) {
                val UserId = LoginRealmModels[0]?.userID
                val request = UploadCheckListRequest()
                request.resourceId = UserId
                request.fileUrl = ""
                request.fileName = ""
                request.taskId = AppUtils.taskId
                request.fileContent = base64
                val controller = NetworkCallController()
                controller.setListner(object : NetworkResponseListner<UploadCheckListData> {
                    override fun onResponse(requestCode: Int, response: UploadCheckListData) {
                        try {
                            val pulseList = ArrayList<QuestionList>()
                            pulseList.addAll(questionList)
                            val pulseSubList = ArrayList<SubQuestionList>()
                            pulseSubList.addAll(subQuestionList)
                            val url = response.fileUrl
                            if (isFromSubList){
                                pulseSubList.forEach {
                                    if (it.questionId == qId) {
                                        if (it.pictureURL == null) {
                                            it.pictureURL = arrayListOf(url)
                                        } else {
                                            it.pictureURL?.add(url)
                                        }
                                    }
                                }
                            }else {
                                pulseList.forEach {
                                    if (it.questionId == qId) {
                                        if (it.pictureURL == null) {
                                            it.pictureURL = arrayListOf(url)
                                        } else {
                                            it.pictureURL?.add(url)
                                        }
                                    }
                                }
                            }
                            Log.d("TAG", "by $cBy")
                            Log.d("TAG", "Before Modified $questionList")
                            pulseQuestionAdapter.notifyDataSetChanged()
                            Log.d("TAG", "Modified $questionList")
                            //validate()
                            //validate2()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun onFailure(requestCode: Int) {}
                })
                controller.uploadCheckListAttachment(1211, request)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
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

    private fun getRealPathFromUri(contentUri: Uri?): String? {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = this.contentResolver.query(contentUri!!, proj, null, null, null)
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
            Log.d("TAG-Image", encodedImage)
            uploadOnsiteImage(encodedImage)
        }
    }

    private fun showExitAlert() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Data Alert")
        dialog.setMessage("You will lose your data\nAre you sure you want to exit?")
        dialog.setPositiveButton(getString(R.string.yes)) { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
            finish()
        }
        dialog.setNegativeButton(getString(R.string.no)) { dialogInterface: DialogInterface, _: Int -> dialogInterface.dismiss() }
        dialog.show()
    }
}