package com.ab.hicarerun.fragments.tms

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.BaseApplication
import com.ab.hicarerun.adapter.tms.TmsChipsAdapter
import com.ab.hicarerun.adapter.tms.TmsQuestionsParentAdapter
import com.ab.hicarerun.databinding.FragmentTmsSecondChildBinding
import com.ab.hicarerun.network.NetworkCallController
import com.ab.hicarerun.network.NetworkResponseListner
import com.ab.hicarerun.network.models.CheckListModel.UploadCheckListData
import com.ab.hicarerun.network.models.CheckListModel.UploadCheckListRequest
import com.ab.hicarerun.network.models.GeneralModel.GeneralData
import com.ab.hicarerun.network.models.LoginResponse
import com.ab.hicarerun.network.models.TmsModel.QuestionList
import com.ab.hicarerun.network.models.TmsModel.QuestionTabList
import com.ab.hicarerun.utils.AppUtils
import es.dmoral.toasty.Toasty
import io.realm.RealmResults
import java.io.ByteArrayOutputStream
import java.io.File


class TmsSecondChildFragment : Fragment() {

    lateinit var binding: FragmentTmsSecondChildBinding
    lateinit var chipsAdapter: TmsChipsAdapter
    lateinit var questionsParentAdapter: TmsQuestionsParentAdapter
    var currPos = 0
    lateinit var chipsArray: ArrayList<String>
    var mPermissions = false
    var REQUEST_CODE = 1234
    val REQUEST_TAKE_PHOTO = 1
    private var selectedImagePath = ""
    private val mPhotoFile: File? = null
    private var bitmap: Bitmap? = null
    private val mTaskDetailsData: RealmResults<GeneralData>? = null
    private var checkPosition = 0
    private var qId = -1
    private var cBy = -1
    private var currChip = ""
    lateinit var currentList: ArrayList<QuestionList>
    lateinit var currentTabList: ArrayList<QuestionTabList>
    var isLast = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTmsSecondChildBinding.inflate(inflater, container, false)
        val view = binding.root
        chipsArray = ArrayList()
        currentList = ArrayList()
        currentTabList = ArrayList()
        chipsArray.addAll(AppUtils.tmsInspectionChips)
        chipsAdapter = TmsChipsAdapter(requireContext(), chipsArray)
        questionsParentAdapter = TmsQuestionsParentAdapter(requireContext(), false)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppUtils.CAMERA_SCREEN = "TmsInspection"
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
        super.onViewCreated(view, savedInstanceState)

        if (chipsArray.size == 1){
            binding.configLayout.visibility = View.GONE
        }else{
            binding.configLayout.visibility = View.VISIBLE
        }

        val questionsLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        questionsLayoutManager.stackFromEnd = false
        questionsLayoutManager.reverseLayout = false

        val chipsLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        chipsLayoutManager.stackFromEnd = false
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
                AppUtils.tmsInspectionList.forEach {
                    var found = false
                    if (it.questionDisplayTab.equals(category, true)){
                        currentList.clear()
                        currentList.addAll(it.questionList)
                        questionsParentAdapter.addData(it.questionList)
                        found = true
                        questionsParentAdapter.notifyDataSetChanged()
                    }
                }
                //validate()
                validate2()
                /*if (!isVisible(binding.configLayout)){
                    val param = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f
                    )
                    binding.recycleView.layoutParams = param;
                }*/
            }
        })
        questionsParentAdapter.setOnCameraClickHandler(object : TmsQuestionsParentAdapter.OnCameraClickListener{
            override fun onCameraClicked(position: Int, questionId: Int?, clickedBy: Int) {
                qId = questionId.toString().toInt()
                cBy = clickedBy
                checkPosition = position
                TmsUtils.init(requireContext(), 1)
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
                //validate()
                validate2()
            }
        })
        questionsParentAdapter.setOnItemClick(object : TmsQuestionsParentAdapter.OnItemClickListener{
            override fun onItemClicked(position: Int, questionId: Int?, answer: String) {
                //validate()
                validate2()
            }
        })

        /*if (!isVisible(binding.configLayout)){
            val param = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
            binding.recycleView.layoutParams = param;
        }*/

        binding.recycleView.layoutManager = questionsLayoutManager
        binding.recycleView.setHasFixedSize(true)
        binding.recycleView.isNestedScrollingEnabled = false
        binding.recycleView.adapter = questionsParentAdapter

        binding.btnBack.setOnClickListener {
            val listener = parentFragment as SecondChildListener
            listener.onBackClicked()
        }
        binding.btnSave.setOnClickListener {
            if (gotoTab()) {
                val listener = parentFragment as SecondChildListener
                if (!AppUtils.isInspectionDone) {
                    listener.onSaveAndNextClicked("Inspection")
                }
                listener.onSaveClicked()
                Log.d("TAG", "save ${AppUtils.tmsInspectionList}")
            }else{
                Toasty.error(requireContext(), "All fields are mandatory").show()
                Log.d("TAG", "Missing fields")
            }
        }
        binding.backChipBtn.setOnClickListener {
            if (currPos > 0){
                currPos -= 1
                binding.recycleView.startAnimation(TmsUtils.inFromLeftAnimation())
                chipsAdapter.backChip(currPos)
            }else{
                Log.d("TAG", "Last")
            }
        }

        binding.nextChipBtn.setOnClickListener {
            if (currPos < chipsArray.size-1){
                currPos += 1
                /*if (!AppUtils.isInspectionDone) {
                    val listener = parentFragment as SecondChildListener
                    listener.onSaveAndNextClicked("Inspection", currChip, currentTabList)
                }*/
                binding.recycleView.startAnimation(TmsUtils.inFromRightAnimation())
                chipsAdapter.nextChip(currPos)
            }else{
                Log.d("TAG", "Last")
            }
        }
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
                            AppUtils.tmsInspectionList.forEach {
                                if (it.questionDisplayTab == currChip) {
                                    tmsCList.addAll(it.questionList)
                                }
                            }
                            val url = response.fileUrl
                            tmsCList.forEach {
                                if (it.questionId == qId){
                                    /*if (it.qPictureURL == null){
                                    }else{
                                        it.qPictureURL?.set(cBy, QuestionImageUrl(cBy, url))
                                    }*/

                                    //val q = QuestionImageUrl(cBy, url)
                                    //Log.d("TAG", "$q")
                                    if (it.pictureURL == null) {
                                        it.pictureURL = arrayListOf(url)
                                    }else{
                                        it.pictureURL?.add(url)
                                    }

                                    /*it.qPictureURL?.forEach { image ->
                                        image.id = cBy
                                        image.url = url
                                    }*/

                                    /*it.qPictureURL!![cBy].id = cBy
                                    it.qPictureURL!![cBy].url = url*/
                                }
                            }
                            Log.d("TAG", "by $cBy")
                            Log.d("TAG", "Before Modified ${AppUtils.tmsInspectionList}")
                            questionsParentAdapter.notifyDataSetChanged()
                            Log.d("TAG", "Modified ${AppUtils.tmsInspectionList}")
                            //validate()
                            validate2()
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

    private fun gotoTab(): Boolean{
        val tabName = validate2()
        if (tabName != ""){
            for (i in 0 until AppUtils.tmsInspectionChips.size) {
                if (tabName == i.toString()){
                    if (i > currPos){
                        binding.recycleView.startAnimation(TmsUtils.inFromRightAnimation())
                    }else{
                        binding.recycleView.startAnimation(TmsUtils.inFromLeftAnimation())
                    }
                    chipsAdapter.nextChip(i)
                    return false
                }
                /*if (AppUtils.tmsInspectionChips[i].equals(tabName, true)){
                    if (i > currPos){
                        binding.recycleView.startAnimation(TmsUtils.inFromRightAnimation())
                    }else{
                        binding.recycleView.startAnimation(TmsUtils.inFromLeftAnimation())
                    }
                    chipsAdapter.nextChip(i)
                    return false
                }*/
            }
        }
        return true
    }

    private fun validate2(): String{
        var tabName = "";
        AppUtils.tmsInspectionList.forEach {
            tabName = TmsUtils.isListChecked2(it.questionList)
            if (tabName == ""){
                tabName = TmsUtils.isImgChecked2(it.questionList)
                if (tabName == ""){
                    binding.btnSave.isEnabled = true
                    binding.btnSave.alpha = 1.0f
                } else {
                    binding.btnSave.isEnabled = true
                    binding.btnSave.alpha = 0.6f
                    return tabName
                }
            } else {
                binding.btnSave.isEnabled = true
                binding.btnSave.alpha = 0.6f
                return tabName
            }
        }
        return tabName
    }

    private fun validate(){
        if (AppUtils.isInspectionDone){
            binding.btnSave.isEnabled = true
            binding.btnSave.alpha = 1.0f
            binding.nextChipBtn.isEnabled = true
            binding.nextChipBtn.alpha = 1.0f
            return
        }
        if (currentList.size == questionsParentAdapter.itemCount) {
            Log.d("Validate", "true")
            if (TmsUtils.isImgChecked(currentList)) {
                if (TmsUtils.isListChecked(currentList)) {
                    if (isLast) {
                        binding.btnSave.isEnabled = true
                        binding.btnSave.alpha = 1.0f
                    }else{
                        binding.btnSave.isEnabled = false
                        binding.btnSave.alpha = 0.6f
                    }
                    binding.nextChipBtn.isEnabled = true
                    binding.nextChipBtn.alpha = 1.0f
                } else {
                    binding.btnSave.isEnabled = false
                    binding.btnSave.alpha = 0.6f

                    binding.nextChipBtn.isEnabled = false
                    binding.nextChipBtn.alpha = 0.6f
                }
            } else {
                binding.btnSave.isEnabled = false
                binding.btnSave.alpha = 0.6f

                binding.nextChipBtn.isEnabled = false
                binding.nextChipBtn.alpha = 0.6f
            }
        }else{
            Log.d("Validate", "false")
            binding.btnSave.isEnabled = false
            binding.btnSave.alpha = 0.6f

            binding.nextChipBtn.isEnabled = false
            binding.nextChipBtn.alpha = 0.6f
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                try {
                    selectedImagePath = mPhotoFile?.path.toString()
                    if (selectedImagePath != null || selectedImagePath != "") {
                        val bit: Bitmap = BitmapDrawable(resources, selectedImagePath).bitmap
                        val i = (bit.height * (1024.0 / bit.width)).toInt()
                        bitmap = Bitmap.createScaledBitmap(bit, 1024, i, true)
                    }
                    val baos = ByteArrayOutputStream()
                    bitmap?.compress(Bitmap.CompressFormat.JPEG, 50, baos)
                    val b = baos.toByteArray()
                    val encodedImage = Base64.encodeToString(b, Base64.DEFAULT)
                    //uploadOnsiteImage(encodedImage);
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
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

    interface SecondChildListener{
        fun onSaveClicked()
        fun onSaveAndNextClicked(type: String)
        fun onBackClicked()
    }
}