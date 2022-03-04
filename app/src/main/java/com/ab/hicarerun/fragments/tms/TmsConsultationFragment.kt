package com.ab.hicarerun.fragments.tms

import android.app.Dialog
import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.Point
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.ab.hicarerun.BaseApplication
import com.ab.hicarerun.R
import com.ab.hicarerun.activities.NewTaskDetailsActivity
import com.ab.hicarerun.databinding.FragmentTmsConsultationBinding
import com.ab.hicarerun.network.NetworkCallController
import com.ab.hicarerun.network.NetworkResponseListner
import com.ab.hicarerun.network.models.consulationmodel.RecommendationResponse
import com.ab.hicarerun.network.models.generalmodel.GeneralData
import com.ab.hicarerun.network.models.tsscannermodel.BaseResponse
import com.ab.hicarerun.network.models.tmsmodel.QuestionList
import com.ab.hicarerun.utils.AppUtils
import com.ab.hicarerun.utils.LocaleHelper
import com.ab.hicarerun.utils.ProgressBarDrawable
import es.dmoral.toasty.Toasty
import io.realm.RealmResults


class TmsConsultationFragment : DialogFragment(), TmsFirstChildFragment.FirstChildListener, TmsSecondChildFragment.SecondChildListener, TmsThirdChildFragment.ThirdChildListener {

    lateinit var binding: FragmentTmsConsultationBinding
    private val SAVE_CON_REQ = 1000
    lateinit var mTaskDetailsData: RealmResults<GeneralData>
    lateinit var progressD: ProgressDialog


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentTmsConsultationBinding.inflate(inflater, container, false)
        val view = binding.root

        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        }
        progressD = ProgressDialog(requireContext(), R.style.TransparentProgressDialog)
        progressD.setCancelable(false)
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        isCancelable = false
        return super.onCreateDialog(savedInstanceState)
    }


    companion object {
        @JvmStatic
        fun newInstance(): TmsConsultationFragment {
            val args = Bundle()
            val fragment = TmsConsultationFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onResume() {
        val window = dialog?.window
        val size = Point()
        val display = window?.windowManager?.defaultDisplay
        display?.getSize(size)
        window?.setLayout((size.x * 0.9).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
        window?.setGravity(Gravity.CENTER)
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mTaskDetailsData = BaseApplication.getRealm().where(GeneralData::class.java).findAll()
        if (mTaskDetailsData != null && mTaskDetailsData.size > 0) {
            if (mTaskDetailsData[0]?.numberOfBhk == "0") {
                binding.txtTypeFlat.text = getString(R.string.termite)
            }else{
                binding.txtTypeFlat.text = mTaskDetailsData[0]?.numberOfBhk + " | " + getString(R.string.termite)
            }
        }

        val transaction = childFragmentManager.beginTransaction()
        val childFragment1 = TmsFirstChildFragment()
        transaction.replace(R.id.container_fragment, childFragment1)
        transaction.commit()
        binding.txtTypeFlat.setTypeface(binding.txtTypeFlat.typeface, Typeface.BOLD)


        val fillColor = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
        val emptyColor = ContextCompat.getColor(requireActivity(), R.color.tab_textColor)
        val separatorColor = ContextCompat.getColor(requireContext(), R.color.transparent)

        if (AppUtils.tmsInspectionList != null && AppUtils.tmsInspectionList.size > 0) {
            val progressDrawable = ProgressBarDrawable(2, fillColor, emptyColor, separatorColor)
            binding.progressBar.progressDrawable = progressDrawable
            if (AppUtils.isInspectionDone) {
                binding.progressBar.progress = 2
            } else {
                binding.progressBar.progress = 0
            }
            binding.progressBar.max = 2
        } else {
            val progressDrawable = ProgressBarDrawable(1, fillColor, emptyColor, separatorColor)
            binding.progressBar.progressDrawable = progressDrawable
            if (AppUtils.isInspectionDone) {
                binding.progressBar.progress = 1
            } else {
                binding.progressBar.progress = 0
            }
            binding.progressBar.max = 1
        }

        binding.cancelBtn.setOnClickListener {
            dialog?.cancel()
        }
    }

    private fun getRecommendations(){
        val controller = NetworkCallController()
        controller.setListner(object : NetworkResponseListner<RecommendationResponse>{
            override fun onResponse(requestCode: Int, response: RecommendationResponse?) {
                progressD.dismiss()
                if (response != null && response.isSuccess == true){
                    AppUtils.isInspectionDone = true
                    Toasty.success(requireContext(), getString(R.string.consultation_and_Inspection_submitted_successfully)).show()
                    onConfirmClicked()
                }else{
                    Toasty.error(requireContext(), "Something went wrong.").show()
                }
            }
            override fun onFailure(requestCode: Int) {
            }
        })
        controller.getTmsRecommendations(202206, mTaskDetailsData[0]?.resourceId, mTaskDetailsData[0]?.taskId, LocaleHelper.getLanguage(activity))
    }

    private fun saveConsultationData(type: String, questionTab: String, questionList: ArrayList<QuestionList>){
        val hashMap = HashMap<String, Any>()
        hashMap["TaskId"] = mTaskDetailsData[0]?.taskId.toString()
        hashMap["type"] = type
        hashMap["QuestionTabList"] = AppUtils.tmsConsultationList

        val controller = NetworkCallController()
        controller.setListner(object : NetworkResponseListner<BaseResponse>{
            override fun onResponse(requestCode: Int, response: BaseResponse?) {
                if (response?.isSuccess == false){
                    Toasty.error(requireContext(), "Failed").show()
                }
            }
            override fun onFailure(requestCode: Int) {
                Log.d("TAG", "$requestCode")
            }
        })
        controller.saveTmsQuestions(2211, arrayListOf(hashMap))
    }

    override fun onNextClicked(type: String, questionTab: String, questionList: ArrayList<QuestionList>) {
        if (AppUtils.tmsInspectionList.size > 0) {
            if (!AppUtils.isInspectionDone){
                saveConsultationData(type, questionTab, questionList)
            }
            val transaction = childFragmentManager.beginTransaction()
            val childFragment2 = TmsSecondChildFragment()
            transaction.replace(R.id.container_fragment, childFragment2)
            transaction.commit()
            requireActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
            binding.containerFragment.startAnimation(TmsUtils.inFromRightAnimation())
            if (AppUtils.isInspectionDone) {
                binding.progressBar.progress = 2
            } else {
                binding.progressBar.progress = 1
            }
        }
    }

    override fun onSaveClicked() {
        val transaction = childFragmentManager.beginTransaction()
        val childFragment3 = TmsThirdChildFragment("TMS")
        transaction.replace(R.id.container_fragment, childFragment3)
        transaction.commit()
        requireActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
        binding.containerFragment.startAnimation(TmsUtils.inFromLeftAnimation())
        //dialog?.dismiss()
    }

    override fun onSaveAndNextClicked(type: String) {
        progressD.show()
        val controller = NetworkCallController()
        controller.setListner(object : NetworkResponseListner<BaseResponse>{
            override fun onResponse(requestCode: Int, response: BaseResponse?) {
                if (response != null && response.isSuccess == true){
                    getRecommendations()
                }else{
                    progressD.dismiss()
                    Toasty.error(requireContext(), "Something went wrong.").show()
                }
            }
            override fun onFailure(requestCode: Int) {
                progressD.dismiss()
                Log.d("TAG", "$requestCode")
            }
        })
        if (!AppUtils.isInspectionDone){
            val hashMap = HashMap<String, Any>()
            hashMap["TaskId"] = mTaskDetailsData[0]?.taskId.toString()
            hashMap["type"] = type
            hashMap["QuestionTabList"] = AppUtils.tmsInspectionList
            controller.saveTmsQuestions(2211, arrayListOf(hashMap))
        }else{
            progressD.dismiss()
            onConfirmClicked()
        }
    }

    override fun onBackClicked() {
        val transaction = childFragmentManager.beginTransaction()
        val childFragment1 = TmsFirstChildFragment()
        transaction.replace(R.id.container_fragment, childFragment1)
        transaction.commit()
        requireActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
        binding.containerFragment.startAnimation(TmsUtils.inFromLeftAnimation())
    }

    override fun onConfirmClicked() {
        dialog?.dismiss()
        val mActivity = activity as NewTaskDetailsActivity
        mActivity.refreshMyData()
    }
}