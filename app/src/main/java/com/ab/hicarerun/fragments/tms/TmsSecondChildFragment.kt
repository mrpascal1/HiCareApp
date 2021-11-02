package com.ab.hicarerun.fragments.tms

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ab.hicarerun.R
import com.ab.hicarerun.databinding.FragmentTmsSecondChildBinding


class TmsSecondChildFragment : Fragment() {

    lateinit var binding: FragmentTmsSecondChildBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTmsSecondChildBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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