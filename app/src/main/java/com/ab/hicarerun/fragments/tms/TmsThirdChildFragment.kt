package com.ab.hicarerun.fragments.tms

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Typeface
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.hicarerun.BaseApplication.getRealm
import com.ab.hicarerun.R
import com.ab.hicarerun.adapter.RecommendationsAdapter
import com.ab.hicarerun.databinding.FragmentTmsThirdChildBinding
import com.ab.hicarerun.network.NetworkCallController
import com.ab.hicarerun.network.NetworkResponseListner
import com.ab.hicarerun.network.models.ConsulationModel.Recommendations
import com.ab.hicarerun.network.models.GeneralModel.GeneralData
import com.ab.hicarerun.utils.AppUtils
import com.ab.hicarerun.utils.LocaleHelper
import io.realm.RealmResults
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class TmsThirdChildFragment(val type: String) : Fragment() {

    lateinit var binding: FragmentTmsThirdChildBinding
    lateinit var mAdapter: RecommendationsAdapter
    lateinit var mTaskDetailsData : RealmResults<GeneralData>
    lateinit var progressD: ProgressDialog
    lateinit var audios: ArrayList<String>
    private var isPLAYING = false
    var checked = false
    var playCompleted = 0
    var mp: MediaPlayer? = null
    val RECOMMENDATION_REQ = 1000


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentTmsThirdChildBinding.inflate(inflater, container, false)
        val view = binding.root
        progressD = ProgressDialog(activity, R.style.TransparentProgressDialog)
        progressD.setCancelable(false)
        audios = ArrayList()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mTaskDetailsData = getRealm().where(GeneralData::class.java).findAll()
        mAdapter = RecommendationsAdapter(activity, type)
        binding.recycleView.layoutManager = LinearLayoutManager(activity)
        binding.recycleView.setHasFixedSize(true)
        binding.recycleView.clipToPadding = false
        binding.recycleView.adapter = mAdapter
        binding.btnHome.isEnabled = false
        binding.btnHome.alpha = 0.6f
        binding.txtTitle.setTypeface(binding.txtTitle.typeface, Typeface.BOLD)
        binding.chkAgree.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checked = true
                binding.btnHome.isEnabled = true
                binding.btnHome.alpha = 1f
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
            AppUtils.isInspectionDone = true
            mAdapter.stopPlaying()
            val listener = parentFragment as ThirdChildListener
            listener.onConfirmClicked()
        }

        binding.speakerIv.setOnClickListener {
            if (!isPLAYING){
                playAudio(binding.speakerIv, audios[0])
            }else{
                stopPlaying(binding.speakerIv)
            }
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
                            if (type == "TMS") {
                                if (items[0].overallInfestationLevel != null && items[0].overallInfestationLevel != "") {
                                    binding.txtPart.text = "RECOMMENDATIONS " + "(" + items[0].overallInfestationLevel + ")"
                                    if (items[0].overallInfestationLevel.equals("High Infestation", ignoreCase = true)) {
                                        val animation = AnimationUtils.loadAnimation(activity, R.anim.blink)
                                        binding.imgAlert.startAnimation(animation)
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            v = activity?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                                            v.vibrate(3000)
                                        }
                                    } else {
                                        binding.imgAlert.visibility = View.GONE
                                    }
                                } else {
                                    binding.txtPart.text = "RECOMMENDATIONS"
                                }
                                AppUtils.infestationLevel = items[0].overallInfestationLevel
                            }
                            binding.recycleView.visibility = View.VISIBLE
                            binding.txtEmpty.visibility = View.GONE
                            items.forEach{
                                if (it.recommendationImageUrl != null && it.recommendationImageUrl != "") {
                                    /*imgCount++
                                    initImages.add(items[i].recommendationImageUrl)*/
                                }
                            }
                            items.forEach {
                                if (it.isAudioEnabled && (it.recommendationAudioUrl != null && it.recommendationAudioUrl != "")){
                                    audios.add(it.recommendationAudioUrl)
                                }
                            }
                            if (audios.size > 0){
                                binding.speakerIv.visibility = View.VISIBLE
                            }else{
                                binding.speakerIv.visibility = View.GONE
                            }
                            mAdapter.setData(items)
                            mAdapter.notifyDataSetChanged()
                        } else {
                            binding.chkAgree.visibility = View.GONE
                            binding.recycleView.visibility = View.GONE
                            binding.speakerIv.visibility = View.GONE
                            binding.txtEmpty.visibility = View.VISIBLE
                            binding.btnHome.isEnabled = true
                            binding.btnHome.alpha = 1f
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
    fun playAudio(speaker: ImageView, url: String) {
        if (mp == null) {
            mp = MediaPlayer()
        }
        isPLAYING = true
        try {
            speaker.setVisibility(View.GONE)
            binding.progressBar.setVisibility(View.VISIBLE)
            speaker.setImageDrawable(
                ContextCompat.getDrawable(requireActivity(), R.drawable.ic_music_stop)
            )
            mp?.setDataSource(url)
            mp?.prepareAsync()
            mp?.setOnPreparedListener { mediaPlayer ->
                binding.progressBar.setVisibility(View.GONE)
                speaker.setVisibility(View.VISIBLE)
                mediaPlayer.start()
            }
            mp?.setOnCompletionListener { mediaPlayer: MediaPlayer ->
                playCompleted++
                mediaPlayer.reset()
                Log.d("TAG", "Completed $playCompleted")
                if (playCompleted < audios.size) {
                    playAudio(speaker, audios[playCompleted])
                } else {
                    stopPlaying(speaker)
                }
            }
        } catch (e: IOException) {
            Log.d("TAG", "prepare() failed")
        }
    }

    fun stopPlaying(speaker: ImageView) {
        if (mp != null) {
            isPLAYING = false
            mp?.stop()
            mp?.release()
            mp = null
            speaker.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_speaker))
        }
    }

    interface ThirdChildListener{
        fun onConfirmClicked()
    }
}