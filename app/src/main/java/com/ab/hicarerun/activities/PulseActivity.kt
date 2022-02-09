package com.ab.hicarerun.activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.BaseActivity
import com.ab.hicarerun.R
import com.ab.hicarerun.adapter.pulse.PulseQuestionAdapter
import com.ab.hicarerun.databinding.ActivityPulseBinding
import com.ab.hicarerun.network.NetworkCallController
import com.ab.hicarerun.network.NetworkResponseListner
import com.ab.hicarerun.network.models.pulsemodel.PulseResponse
import com.ab.hicarerun.network.models.pulsemodel.QuestionList
import com.ab.hicarerun.utils.AppUtils
import com.ab.hicarerun.utils.LocaleHelper

class PulseActivity : BaseActivity() {

    lateinit var binding: ActivityPulseBinding
    lateinit var pulseQuestionAdapter: PulseQuestionAdapter
    val questionList = ArrayList<QuestionList>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPulseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        pulseQuestionAdapter = PulseQuestionAdapter(this)

        val questionsLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.questionListRecycler.layoutManager = questionsLayoutManager
        binding.questionListRecycler.setHasFixedSize(true)
        binding.questionListRecycler.isNestedScrollingEnabled = false
        binding.questionListRecycler.adapter = pulseQuestionAdapter

        binding.backIv.setOnClickListener {
            finish()
        }

        getPulseB2bInspectionQuestions(AppUtils.taskId, AppUtils.resourceId, LocaleHelper.getLanguage(this))
    }

    private fun getPulseB2bInspectionQuestions(taskId: String, resourceId: String, lan: String){
        val controller = NetworkCallController()
        controller.setListner(object: NetworkResponseListner<PulseResponse>{
            override fun onResponse(requestCode: Int, response: PulseResponse?) {
                if (response != null){
                    if (response.isSuccess == true){
                        if (response.data != null){
                            if (response.data.isTabList == false){
                                questionList.addAll(response.data.questionList!!)
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