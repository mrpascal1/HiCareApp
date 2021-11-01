package com.ab.hicarerun.fragments.tms

import android.app.ProgressDialog
import android.graphics.Point
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.ab.hicarerun.R
import com.ab.hicarerun.network.models.GeneralModel.GeneralData
import io.realm.RealmResults


class TmsConsultationFragment : DialogFragment() {

    private val SAVE_CON_REQ = 1000
    private val mTaskDetailsData: RealmResults<GeneralData>? = null
    private val progressD: ProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tms_consultation, container, false)
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
}