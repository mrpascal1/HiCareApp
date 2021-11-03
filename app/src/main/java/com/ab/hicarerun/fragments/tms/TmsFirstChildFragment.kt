package com.ab.hicarerun.fragments.tms

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.hicarerun.R
import com.ab.hicarerun.adapter.tms.TmsChipsAdapter
import com.ab.hicarerun.adapter.tms.TmsQuestionsParentAdapter
import com.ab.hicarerun.databinding.FragmentTmsFirstChildBinding
import com.ab.hicarerun.network.models.TmsModel.QuestionsResponse


class TmsFirstChildFragment : Fragment() {

    lateinit var binding: FragmentTmsFirstChildBinding
    lateinit var chipsAdapter: TmsChipsAdapter
    lateinit var questionsParentAdapter: TmsQuestionsParentAdapter
    lateinit var questionsResponse: ArrayList<QuestionsResponse>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTmsFirstChildBinding.inflate(inflater, container, false)
        val view = binding.root
        val chipsArray = arrayListOf("General", "Living Room", "Kitchen","Bedroom")
        val questionsArray = arrayListOf("What is android OS?", "What is android framework?",
            "What is android components","What is linux kernel?")
        questionsResponse = ArrayList()
        questionsResponse.add(QuestionsResponse("What is android OS?", "General"))
        questionsResponse.add(QuestionsResponse("What is android framework?", "Living Room"))
        questionsResponse.add(QuestionsResponse("What is android components?", "Kitchen"))
        questionsResponse.add(QuestionsResponse("What is linux kernel?", "Bedroom"))

        chipsAdapter = TmsChipsAdapter(requireContext(), chipsArray)
        questionsParentAdapter = TmsQuestionsParentAdapter(requireContext(), questionsArray)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val questionsLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        questionsLayoutManager.stackFromEnd = true
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
                questionsResponse.forEach {
                    if (it.category == category){
                        questionsParentAdapter.addData(it.question.toString())
                        questionsParentAdapter.notifyDataSetChanged()
                    }
                }
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
        super.onViewCreated(view, savedInstanceState)
    }

    interface FirstChildListener{
        fun onNextClicked()
    }
}