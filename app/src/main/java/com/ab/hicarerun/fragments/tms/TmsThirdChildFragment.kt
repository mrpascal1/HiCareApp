package com.ab.hicarerun.fragments.tms

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicarerun.BaseApplication.getRealm
import com.ab.hicarerun.R
import com.ab.hicarerun.adapter.RecommendationsAdapter
import com.ab.hicarerun.databinding.FragmentTmsThirdChildBinding
import com.ab.hicarerun.fragments.ConsultationThirdFragment
import com.ab.hicarerun.network.NetworkCallController
import com.ab.hicarerun.network.NetworkResponseListner
import com.ab.hicarerun.network.models.ConsulationModel.Recommendations
import com.ab.hicarerun.network.models.GeneralModel.GeneralData
import com.ab.hicarerun.utils.AppUtils
import com.ab.hicarerun.utils.LocaleHelper
import io.realm.RealmResults
import java.util.*


class TmsThirdChildFragment(val type: String) : Fragment() {

    lateinit var binding: FragmentTmsThirdChildBinding
    val RECOMMENDATION_REQ = 1000;
    lateinit var mAdapter: RecommendationsAdapter
    lateinit var mTaskDetailsData : RealmResults<GeneralData>
    lateinit var progressD: ProgressDialog
    var checked = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentTmsThirdChildBinding.inflate(inflater, container, false)
        val view = binding.root
        progressD = ProgressDialog(activity, R.style.TransparentProgressDialog)
        progressD.setCancelable(false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mTaskDetailsData = getRealm().where(GeneralData::class.java).findAll()
        mAdapter = RecommendationsAdapter(activity, type)
        binding.recycleView.layoutManager = LinearLayoutManager(activity)
        binding.recycleView.setHasFixedSize(true)
        binding.recycleView.clipToPadding = false;
        binding.recycleView.adapter = mAdapter;
        binding.btnHome.isEnabled = false
        binding.btnHome.alpha = 0.6f
        binding.txtTitle.setTypeface(binding.txtTitle.typeface, Typeface.BOLD);
        binding.chkAgree.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                checked = true;
                binding.btnHome.isEnabled = true;
                binding.btnHome.alpha = 1f;
                /*if (imgCount > 0) {
                    if (imagesClicked.containsAll(initImages)) {
                        binding.btnHome.setEnabled(true);
                        binding.btnHome.setAlpha(1f);
                    }else {
                        binding.btnHome.setEnabled(false);
                        binding.btnHome.setAlpha(0.6f);
                    }
                }else {
                    binding.btnHome.setEnabled(true);
                    binding.btnHome.setAlpha(1f);
                }*/
            } else {
                checked = false
                binding.btnHome.isEnabled = false
                binding.btnHome.alpha = 0.6f
            }
        }
        if (AppUtils.isInspectionDone) {
            binding.chkAgree.visibility = View.GONE
            binding.btnHome.isEnabled = true
            binding.btnHome.alpha = 1f
        } else {
            binding.chkAgree.visibility = View.VISIBLE
        }

        binding.btnHome.setOnClickListener {
            AppUtils.isInspectionDone = true;
            mAdapter.stopPlaying()
            val listener = parentFragment as ThirdChildListener
            listener.onConfirmClicked()
        }

        getRecommendations()
    }

    private fun getRecommendations() {
        try {
            if (mTaskDetailsData != null && mTaskDetailsData.size > 0) {
                progressD.show()
                val controller = NetworkCallController()
                controller.setListner(object : NetworkResponseListner<List<Recommendations>> {
                    override fun onResponse(requestCode: Int, items: List<Recommendations>) {
                        /*initImages.clear()
                        imagesClicked.clear()*/
                        progressD.dismiss()
                        if (items != null && items.size > 0) {
                            var v: Vibrator? = null
                            if (type == "CMS") {
                                if (items[0].overallInfestationLevel != null && items[0].overallInfestationLevel != "") {
                                    binding.txtPart.setText("RECOMMENDATIONS " + "(" + items[0].overallInfestationLevel + ")")
                                    if (items[0].overallInfestationLevel.equals(
                                            "High Infestation",
                                            ignoreCase = true
                                        )
                                    ) {
                                        val animation = AnimationUtils.loadAnimation(
                                            activity, R.anim.blink
                                        )
                                        binding.imgAlert.startAnimation(
                                            animation
                                        )
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            v = Objects.requireNonNull(activity)!!.getSystemService(
                                                Context.VIBRATOR_SERVICE
                                            ) as Vibrator
                                            v.vibrate(3000)
                                        }
                                    } else {
                                        binding.imgAlert.setVisibility(
                                            View.GONE
                                        )
                                    }
                                } else {
                                    binding.txtPart.setText("RECOMMENDATIONS")
                                }
                                AppUtils.infestationLevel = items[0].overallInfestationLevel
                            }
                            binding.recycleView.setVisibility(View.VISIBLE)
                            binding.txtEmpty.setVisibility(View.GONE)
                            for (i in items.indices) {
                                if (items[i].recommendationImageUrl != null && items[i].recommendationImageUrl != "") {
                                    /*imgCount++
                                    initImages.add(items[i].recommendationImageUrl)*/
                                }
                            }
                            /*if (imgCount > 0){
                                if (AppUtils.isInspectionDone){
                                    binding.chkAgree.setVisibility(View.GONE);
                                    binding.btnHome.setEnabled(true);
                                    binding.btnHome.setAlpha(1f);
                                }else {
                                    binding.chkAgree.setVisibility(View.VISIBLE);
                                    binding.noteTv.setVisibility(View.VISIBLE);
                                    binding.btnHome.setEnabled(false);
                                    binding.btnHome.setAlpha(0.6f);
                                }
                            }else{
                                binding.noteTv.setVisibility(View.GONE);
                                if (AppUtils.isInspectionDone){
                                    binding.chkAgree.setVisibility(View.GONE);
                                    binding.btnHome.setEnabled(true);
                                    binding.btnHome.setAlpha(1f);
                                }else {
                                    binding.chkAgree.setVisibility(View.VISIBLE);
                                    binding.btnHome.setEnabled(false);
                                    binding.btnHome.setAlpha(0.6f);
                                }
                            }*/mAdapter.setData(items)
                            mAdapter.notifyDataSetChanged()
                        } else {
                            binding.chkAgree.setVisibility(View.GONE)
                            binding.recycleView.setVisibility(View.GONE)
                            binding.txtEmpty.setVisibility(View.VISIBLE)
                            binding.btnHome.setEnabled(true)
                            binding.btnHome.setAlpha(1f)
                        }
                    }

                    override fun onFailure(requestCode: Int) {}
                })
                controller.getRecommendations(RECOMMENDATION_REQ, mTaskDetailsData[0]?.resourceId, mTaskDetailsData[0]?.taskId, LocaleHelper.getLanguage(activity))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /*fun newInstance(): TmsConsultationFragment {
        val args = Bundle()
        val fragment = TmsConsultationFragment()
        fragment.arguments = args
        return fragment
    }*/

    interface ThirdChildListener{
        fun onConfirmClicked()
    }
}