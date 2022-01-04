package com.ab.hicarerun.fragments.tms

import android.Manifest
import android.R
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.hardware.Camera
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.BaseApplication
import com.ab.hicarerun.BuildConfig
import com.ab.hicarerun.activities.Camera2Activity
import com.ab.hicarerun.adapter.tms.TmsChipsAdapter
import com.ab.hicarerun.adapter.tms.TmsQuestionsParentAdapter
import com.ab.hicarerun.databinding.FragmentTmsFirstChildBinding
import com.ab.hicarerun.network.NetworkCallController
import com.ab.hicarerun.network.NetworkResponseListner
import com.ab.hicarerun.network.models.CheckListModel.UploadCheckListData
import com.ab.hicarerun.network.models.CheckListModel.UploadCheckListRequest
import com.ab.hicarerun.network.models.ConsulationModel.Data
import com.ab.hicarerun.network.models.GeneralModel.GeneralData
import com.ab.hicarerun.network.models.LoginResponse
import com.ab.hicarerun.network.models.TmsModel.QuestionList
import com.ab.hicarerun.network.models.TmsModel.QuestionsResponse
import com.ab.hicarerun.utils.AppUtils
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import io.realm.RealmResults
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class TmsFirstChildFragment : Fragment() {

    lateinit var binding: FragmentTmsFirstChildBinding
    lateinit var chipsAdapter: TmsChipsAdapter
    lateinit var questionsParentAdapter: TmsQuestionsParentAdapter
    lateinit var questionsResponse: ArrayList<QuestionsResponse>
    var currPos = 0
    lateinit var chipsArray: ArrayList<String>
    var mPermissions = false
    var REQUEST_CODE = 1234
    val REQUEST_TAKE_PHOTO = 1
    private var selectedImagePath = ""
    private var mPhotoFile: File? = null
    private var bitmap: Bitmap? = null
    private val mTaskDetailsData: RealmResults<GeneralData>? = null
    private var checkPosition = 0
    private var qId = -1
    private var cBy = -1
    private var currChip = ""
    lateinit var currentList: ArrayList<QuestionList>
    var isLast = false

    val CAMERA_REQUEST = 1
    val REQUEST_GALLERY_PHOTO = 2


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTmsFirstChildBinding.inflate(inflater, container, false)
        val view = binding.root
        //chipsArray = arrayListOf("General", "Living Room", "Kitchen","Bedroom")
        Log.d("TAG", "Chip first child ${AppUtils.tmsConsultationChips}")
        chipsArray = ArrayList()
        currentList = ArrayList()
        chipsArray.addAll(AppUtils.tmsConsultationChips)

        questionsResponse = ArrayList()

        chipsAdapter = TmsChipsAdapter(requireContext(), chipsArray)
        questionsParentAdapter = TmsQuestionsParentAdapter(requireContext(), false)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppUtils.CAMERA_SCREEN = "TmsConsultation"
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(mMessageReceiver, IntentFilter(AppUtils.CAMERA_SCREEN))
    }

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Get extra data included in the Intent
            val base64 = intent.getStringExtra("base64")
            uploadOnsiteImage(base64.toString())
            Log.d("receiver", "Got message: $base64")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        if (chipsArray.size == 1){
            binding.configLayout.visibility = View.GONE
        }else{
            binding.configLayout.visibility = View.VISIBLE
        }

        val questionsLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        questionsLayoutManager.stackFromEnd = false
        questionsLayoutManager.reverseLayout = false

        val chipsLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        chipsLayoutManager.stackFromEnd = true
        chipsLayoutManager.reverseLayout = false

        binding.chipsRecyclerView.layoutManager = chipsLayoutManager
        binding.chipsRecyclerView.setHasFixedSize(true)
        binding.chipsRecyclerView.isNestedScrollingEnabled = false
        binding.chipsRecyclerView.adapter = chipsAdapter
        binding.chipsRecyclerView.post {
            binding.chipsRecyclerView.smoothScrollToPosition(0)
        }
        chipsAdapter.setOnListItemClickHandler(object : TmsChipsAdapter.OnListItemClickHandler{
            override fun onItemClick(position: Int, category: String) {
                Log.d("TAG", "$position")
                currChip = category
                currPos = position
                /**
                 * Controlling visibility of back/next
                 * according to the first and last chip (Tab)
                * */
                if (currPos == chipsArray.size-1){
                    isLast = true
                    binding.nextChipBtn.visibility = View.GONE
                }else{
                    isLast = false
                    binding.nextChipBtn.visibility = View.VISIBLE
                }
                if (currPos == 0){
                    binding.backChipBtn.visibility = View.GONE
                }else{
                    binding.backChipBtn.visibility = View.VISIBLE
                }
                binding.chipsRecyclerView.post {
                    binding.chipsRecyclerView.smoothScrollToPosition(position)
                }
                binding.recycleView.post {
                    binding.recycleView.smoothScrollToPosition(0)
                }
                /**
                 * This forEach loop Adding/updating questions
                 * in the consultation page
                 * according to the chips (Tabs).
                * */
                AppUtils.tmsConsultationList.forEach {
                    if (it.questionDisplayTab.equals(category, true)){
                        currentList.clear()
                        currentList.addAll(it.questionList)
                        questionsParentAdapter.addData(it.questionList)
                        questionsParentAdapter.notifyDataSetChanged()
                    }
                }

                /**
                 * Controlling the visibility of next and back button
                 * according to the visibility because of the long list
                * */
                /*if (!isVisible(binding.configLayout)){
                    val param = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f
                    )
                    binding.recycleView.layoutParams = param;
                }*/

                validate()

                Log.d("TAG", "Size ${AppUtils.tmsConsultationChips.size}")
            }
        })

        questionsParentAdapter.setOnCameraClickHandler(object : TmsQuestionsParentAdapter.OnCameraClickListener{
            override fun onCameraClicked(position: Int, questionId: Int?, clickedBy: Int) {
                qId = questionId.toString().toInt()
                cBy = clickedBy
                checkPosition = position
                requestStoragePermission(true)
                //TmsUtils.init(requireContext(), 1)
            }

            override fun onCancelClicked(position: Int, questionId: Int?, clickedBy: Int) {
                qId = questionId.toString().toInt()
                cBy = clickedBy
                checkPosition = position
                val tmsCList = ArrayList<QuestionList>()
                AppUtils.tmsInspectionList.forEach {
                    if (it.questionDisplayTab == currChip) {
                        tmsCList.addAll(it.questionList)
                    }
                }
                tmsCList.forEach {
                    if (it.questionId == qId){
                        if (it.pictureURL?.isEmpty() == true) {
                            it.pictureURL = null
                        }
                    }
                }
                questionsParentAdapter.notifyDataSetChanged()
                validate()
            }
        })

        questionsParentAdapter.setOnItemClick(object : TmsQuestionsParentAdapter.OnItemClickListener{
            override fun onItemClicked(position: Int, questionId: Int?, answer: String) {
                validate()
            }
        })

        binding.recycleView.layoutManager = questionsLayoutManager
        binding.recycleView.setHasFixedSize(true)
        binding.recycleView.isNestedScrollingEnabled = false
        binding.recycleView.adapter = questionsParentAdapter

        binding.btnNext.setOnClickListener {
            val listener = parentFragment as FirstChildListener
            listener.onNextClicked("Consultation", currChip, currentList)
        }

        binding.backChipBtn.setOnClickListener {
            if (currPos > 0){
                currPos -= 1
                chipsAdapter.backChip(currPos)
            }else{
                Log.d("TAG", "Last")
            }
        }

        binding.nextChipBtn.setOnClickListener {
            if (currPos < chipsArray.size-1){
                currPos += 1
                chipsAdapter.nextChip(currPos)
            }else{
                Log.d("TAG", "Last")
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    fun isVisible(view: View?): Boolean {
        if (view == null) {
            return false
        }
        if (!view.isShown) {
            return false
        }
        val actualPosition = Rect()
        view.getGlobalVisibleRect(actualPosition)
        val screen = Rect(0, 0, Resources.getSystem().displayMetrics.widthPixels, Resources.getSystem().displayMetrics.heightPixels)
        return actualPosition.intersect(screen)
    }

    private fun checkCameraHardware(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
    }

    private fun startCamera2() {
        val intent = Intent(activity, Camera2Activity::class.java)
        intent.putExtra(AppUtils.CAMERA_ORIENTATION, "BACK")
        startActivity(intent)
    }

    private fun showSnackBar(text: String, length: Int) {
        val view = activity?.findViewById<View>(R.id.content)?.rootView
        if (view != null) {
            Snackbar.make(view, text, length).show()
        }
    }

    fun verifyPermissions() {
        Log.d("TAG", "verifyPermissions: asking user for permissions.")
        val permissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
        if (ContextCompat.checkSelfPermission(
                requireContext().applicationContext,
                permissions[0]
            ) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                requireContext().applicationContext,
                permissions[1]
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mPermissions = true
            init()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                permissions,
                REQUEST_CODE
            )
        }
    }

    private fun init() {
        if (mPermissions) {
            if (checkCameraHardware(requireContext())) {
                // Open the Camera
                startCamera2()
            } else {
                showSnackBar(
                    "You need a camera to use this application",
                    Snackbar.LENGTH_INDEFINITE
                )
            }
        } else {
            verifyPermissions()
        }
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
                request.taskId = mTaskDetailsData?.get(0)?.taskId
                request.fileContent = base64
                val controller = NetworkCallController()
                controller.setListner(object : NetworkResponseListner<UploadCheckListData> {
                    override fun onResponse(requestCode: Int, response: UploadCheckListData) {
                        try {
                            val tmsCList = ArrayList<QuestionList>()
                            AppUtils.tmsConsultationList.forEach {
                                if (it.questionDisplayTab == currChip) {
                                    tmsCList.addAll(it.questionList)
                                }
                            }
                            val url = response.fileUrl
                            tmsCList.forEach {
                                if (it.questionId == qId){
                                    var found1 = false
                                    var found2 = false
                                    var found3 = false
                                    /*it.pictureURL?.forEach { image ->
                                        if (cBy == 1 && image.id == cBy){
                                            found1 = true
                                        }
                                        if (cBy == 2 && image.id == cBy){
                                            found2 = true
                                        }
                                        if (cBy == 3 && image.id == cBy){
                                            found3 = true
                                        }
                                    }*/
                                    //val q = QuestionImageUrl(cBy, url)
                                    //Log.d("TAG", "$q")
                                    if (it.pictureURL == null) {
                                        it.pictureURL = arrayListOf(url)
                                    }else{
                                        it.pictureURL?.add(url)
                                    }

                                    /*it.qPictureURL!![cBy].id = cBy
                                    it.qPictureURL!![cBy].url = url*/
                                }
                            }
                            validate()
                            Log.d("TAG", "Modified ${AppUtils.tmsConsultationList}")
                            questionsParentAdapter.notifyDataSetChanged()
                            Log.d("TAG", "Modified ${AppUtils.tmsConsultationList}")
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

    private fun validate(){
        if (currentList.size == questionsParentAdapter.itemCount) {
            Log.d("Validate", "true")
            if (TmsUtils.isImgChecked(currentList)) {
                if (TmsUtils.isListChecked(currentList)) {
                    if (isLast) {
                        binding.btnNext.isEnabled = true
                        binding.btnNext.alpha = 1.0f
                    }else{
                        binding.btnNext.isEnabled = false
                        binding.btnNext.alpha = 0.6f
                    }
                    binding.nextChipBtn.isEnabled = true
                    binding.nextChipBtn.alpha = 1.0f
                } else {
                    binding.btnNext.isEnabled = false
                    binding.btnNext.alpha = 0.6f

                    binding.nextChipBtn.isEnabled = false
                    binding.nextChipBtn.alpha = 0.6f
                }
            } else {
                binding.btnNext.isEnabled = false
                binding.btnNext.alpha = 0.6f

                binding.nextChipBtn.isEnabled = false
                binding.nextChipBtn.alpha = 0.6f
            }
        }else{
            Log.d("Validate", "false")
            binding.btnNext.isEnabled = false
            binding.btnNext.alpha = 0.6f

            binding.nextChipBtn.isEnabled = false
            binding.nextChipBtn.alpha = 0.6f
        }
    }

    private fun dispatchTakePictureIntent() {
        try {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                // Create the File where the photo should go
                val photoFile = createImageFile()
                val photoURI: Uri = FileProvider.getUriForFile(requireContext(), BuildConfig.APPLICATION_ID + ".provider", photoFile)
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
    private fun requestStoragePermission(isCamera: Boolean) {
        try {
            Dexter.withActivity(requireActivity())
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
                .withErrorListener {
                    Toast.makeText(requireContext(), "Error occurred! ", Toast.LENGTH_SHORT).show()
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
            val uri = Uri.fromParts("package", requireActivity().packageName, null)
            intent.data = uri
            startActivityForResult(intent, 101)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
    private fun showSettingsDialog() {
        try {
            val builder = AlertDialog.Builder(requireContext())
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
        val storageDir: File? = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(mFileName, ".jpg", storageDir)
    }

    private fun getRealPathFromUri(contentUri: Uri?): String? {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = requireActivity().contentResolver.query(contentUri!!, proj, null, null, null)
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
            uploadOnsiteImage(encodedImage)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (TmsUtils.mPermissions) {
                TmsUtils.init(requireContext(), REQUEST_CODE)
            } else {
                TmsUtils.verifyPermissions(requireContext(), REQUEST_CODE)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(mMessageReceiver)
    }

    interface FirstChildListener{
        fun onNextClicked(type: String, questionTab: String, questionList: ArrayList<QuestionList>)
    }
}