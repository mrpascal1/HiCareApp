package com.ab.hicarerun.fragments.tms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ab.hicarerun.R


class TmsThirdChildFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tms_third_child, container, false)
    }

    fun newInstance(): TmsConsultationFragment {
        val args = Bundle()
        val fragment = TmsConsultationFragment()
        fragment.arguments = args
        return fragment
    }

    interface ThirdChildListener{
        fun onConfirmClicked()
    }
}