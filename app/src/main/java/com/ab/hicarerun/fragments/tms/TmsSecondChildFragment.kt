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
import com.ab.hicarerun.databinding.FragmentTmsSecondChildBinding
import com.ab.hicarerun.utils.AppUtils


class TmsSecondChildFragment : Fragment() {

    lateinit var binding: FragmentTmsSecondChildBinding
    lateinit var chipsAdapter: TmsChipsAdapter
    lateinit var questionsParentAdapter: TmsQuestionsParentAdapter
    var currPos = 0
    lateinit var chipsArray: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTmsSecondChildBinding.inflate(inflater, container, false)
        val view = binding.root
        chipsArray = ArrayList()
        chipsArray.addAll(AppUtils.tmsInspectionChips)
        chipsAdapter = TmsChipsAdapter(requireContext(), chipsArray)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        binding.btnBack.setOnClickListener {
            val listener = parentFragment as SecondChildListener
            listener.onBackClicked()
        }
    }

    interface SecondChildListener{
        fun onSaveClicked()
        fun onBackClicked()
    }
}