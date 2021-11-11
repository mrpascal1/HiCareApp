package com.ab.hicarerun.fragments.tms

import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.adapter.tms.TmsChipsAdapter
import com.ab.hicarerun.adapter.tms.TmsQuestionsParentAdapter
import com.ab.hicarerun.databinding.FragmentTmsFirstChildBinding
import com.ab.hicarerun.network.models.TmsModel.QuestionsResponse
import com.ab.hicarerun.utils.AppUtils


class TmsFirstChildFragment : Fragment() {

    lateinit var binding: FragmentTmsFirstChildBinding
    lateinit var chipsAdapter: TmsChipsAdapter
    lateinit var questionsParentAdapter: TmsQuestionsParentAdapter
    lateinit var questionsResponse: ArrayList<QuestionsResponse>
    var currPos = 0
    lateinit var chipsArray: ArrayList<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTmsFirstChildBinding.inflate(inflater, container, false)
        val view = binding.root
        //chipsArray = arrayListOf("General", "Living Room", "Kitchen","Bedroom")
        chipsArray = ArrayList()
        chipsArray.addAll(AppUtils.tmsConsultationChips)

        val questionsArray = arrayListOf("What is android OS?", "What is android framework?",
            "What is android components","What is linux kernel?")
        questionsResponse = ArrayList()
        /*questionsResponse.add(QuestionsResponse("General",
            arrayListOf(
                Questions("What is android OS?",
                    arrayListOf(
                        Option(1, "1st Option -0", false),
                        Option(2, "2nd Option -0", false))
                    , false),
                Questions("Second Question",
                    arrayListOf(
                        Option(3, "1st Option 0", false),
                        Option(4, "2nd Option 0", false)), false)),

        ))
        questionsResponse.add(QuestionsResponse("Living Room",
            arrayListOf(
                Questions("What is android framework 0?",
                    arrayListOf(
                        Option(5, "1st Option -1", false),
                        Option(6, "2nd Option -1", false))
                    , false),
                Questions("Second Question",
                    arrayListOf(
                        Option(7, "1st Option 1", false),
                        Option(8, "2nd Option 1", false))
                    , false)),

        ))
        questionsResponse.add(QuestionsResponse("Kitchen",
            arrayListOf(
                Questions("What is android framework 1?",
                    arrayListOf(
                        Option(5, "1st Option -2", false),
                        Option(6, "2nd Option -2", false))
                    , false),
                Questions("Second Question",
                    arrayListOf(
                        Option(5, "1st Option 2", false),
                        Option(6, "2nd Option 2", false)),
                    false)),

        ))
        questionsResponse.add(QuestionsResponse("Bedroom",
            arrayListOf(
                Questions("What is android framework 2?",
                    arrayListOf(
                        Option(7, "1st Option 3", false),
                        Option(8, "2nd Option 3", false)),
                    false),
                Questions("Second Question",
                    arrayListOf(
                        Option(7, "1st Option 3", false),
                        Option(8, "2nd Option 3", false)),
                    false)),

        ))*/


        chipsAdapter = TmsChipsAdapter(requireContext(), chipsArray)
        questionsParentAdapter = TmsQuestionsParentAdapter(requireContext())
        return view
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
                currPos = position
                binding.chipsRecyclerView.post {
                    binding.chipsRecyclerView.smoothScrollToPosition(position)
                }
                AppUtils.tmsConsultationList.forEach {
                    if (it.questionTab.equals(category, true)){
                        questionsParentAdapter.addData(it.questionList)
                        questionsParentAdapter.notifyDataSetChanged()
                    }
                }
                if (!isVisible(binding.configLayout)){
                    val param = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f
                    )
                    binding.recycleView.layoutParams = param;
                }
                Log.d("TAG", "Size ${AppUtils.tmsConsultationChips.size}")
            }
        })

        binding.recycleView.layoutManager = questionsLayoutManager
        binding.recycleView.setHasFixedSize(true)
        binding.recycleView.isNestedScrollingEnabled = false
        binding.recycleView.adapter = questionsParentAdapter

        binding.btnNext.setOnClickListener {
            val listener = parentFragment as FirstChildListener
            listener.onNextClicked()
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

    interface FirstChildListener{
        fun onNextClicked()
    }
}