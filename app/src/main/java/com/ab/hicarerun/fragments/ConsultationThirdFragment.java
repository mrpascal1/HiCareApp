package com.ab.hicarerun.fragments;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.ab.hicarerun.R;
import com.ab.hicarerun.adapter.RecommendationsAdapter;
import com.ab.hicarerun.databinding.FragmentConsultationThirdBinding;
import com.ab.hicarerun.handler.UserRecommendationHandler;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.ConsulationModel.Recommendations;
import com.ab.hicarerun.network.models.GeneralModel.GeneralData;
import com.ab.hicarerun.utils.AppUtils;
import com.ab.hicarerun.utils.LocaleHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.realm.RealmResults;

import static com.ab.hicarerun.BaseApplication.getRealm;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConsultationThirdFragment extends Fragment implements UserRecommendationHandler {
    FragmentConsultationThirdBinding mFragmentConsultationThirdBinding;
    private static final int RECOMMENDATION_REQ = 1000;
    private RecommendationsAdapter mAdapter;
    private RealmResults<GeneralData> mTaskDetailsData = null;
    private ProgressDialog progressD;
    private String type = "";
    int imgCount = 0;
    List<String> initImages;
    List<String> imagesClicked;
    List<String> audios;
    MediaPlayer mp;
    private boolean isPLAYING = false;
    private boolean checked = false;
    int playCompleted = 0;

    public ConsultationThirdFragment(String type) {
        // Required empty public constructor
        this.type = type;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentConsultationThirdBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_consultation_third, container, false);
        mFragmentConsultationThirdBinding.setHandler(this);
        progressD = new ProgressDialog(getActivity(), R.style.TransparentProgressDialog);
        progressD.setCancelable(false);
        initImages = new ArrayList<>();
        imagesClicked = new ArrayList<>();
        audios = new ArrayList<>();
        return mFragmentConsultationThirdBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTaskDetailsData =
                getRealm().where(GeneralData.class).findAll();
        mAdapter = new RecommendationsAdapter(getActivity(), type);
        mFragmentConsultationThirdBinding.recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFragmentConsultationThirdBinding.recycleView.setHasFixedSize(true);
        mFragmentConsultationThirdBinding.recycleView.setClipToPadding(false);
        mFragmentConsultationThirdBinding.recycleView.setAdapter(mAdapter);
        mFragmentConsultationThirdBinding.btnHome.setEnabled(false);
        mFragmentConsultationThirdBinding.btnHome.setAlpha(0.6f);
        mFragmentConsultationThirdBinding.txtTitle.setTypeface(mFragmentConsultationThirdBinding.txtTitle.getTypeface(), Typeface.BOLD);
        mFragmentConsultationThirdBinding.chkAgree.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checked = true;
                mFragmentConsultationThirdBinding.btnHome.setEnabled(true);
                mFragmentConsultationThirdBinding.btnHome.setAlpha(1f);
                /*if (imgCount > 0) {
                    if (imagesClicked.containsAll(initImages)) {
                        mFragmentConsultationThirdBinding.btnHome.setEnabled(true);
                        mFragmentConsultationThirdBinding.btnHome.setAlpha(1f);
                    }else {
                        mFragmentConsultationThirdBinding.btnHome.setEnabled(false);
                        mFragmentConsultationThirdBinding.btnHome.setAlpha(0.6f);
                    }
                }else {
                    mFragmentConsultationThirdBinding.btnHome.setEnabled(true);
                    mFragmentConsultationThirdBinding.btnHome.setAlpha(1f);
                }*/
            } else {
                checked = false;
                mFragmentConsultationThirdBinding.btnHome.setEnabled(false);
                mFragmentConsultationThirdBinding.btnHome.setAlpha(0.6f);
            }
        });
        if (AppUtils.isInspectionDone) {
            mFragmentConsultationThirdBinding.chkAgree.setVisibility(View.GONE);
            mFragmentConsultationThirdBinding.btnHome.setEnabled(true);
            mFragmentConsultationThirdBinding.btnHome.setAlpha(1f);
        } else {
            mFragmentConsultationThirdBinding.chkAgree.setVisibility(View.VISIBLE);
        }

        mFragmentConsultationThirdBinding.speakerIv.setOnClickListener(v -> {
            if (!isPLAYING) {
                playAudio(mFragmentConsultationThirdBinding.speakerIv, audios.get(0), 0);
            }else {
                stopPlaying();
            }
        });

        getRecommendations();
    }

    /*@Override
    public void onResume() {
        mAdapter.setOnItemClickHandler((image, position) -> {
            if (!imagesClicked.contains(image)) {
                imagesClicked.add(image);
            }
            if (AppUtils.isInspectionDone) {
                    mFragmentConsultationThirdBinding.chkAgree.setVisibility(View.GONE);
                    mFragmentConsultationThirdBinding.btnHome.setEnabled(true);
                    mFragmentConsultationThirdBinding.btnHome.setAlpha(1f);
            } else {
                mFragmentConsultationThirdBinding.chkAgree.setVisibility(View.VISIBLE);
                if (imgCount > 0) {
                    if (imagesClicked.containsAll(initImages) && checked){
                        mFragmentConsultationThirdBinding.btnHome.setEnabled(true);
                        mFragmentConsultationThirdBinding.btnHome.setAlpha(1f);
                    }else{
                        mFragmentConsultationThirdBinding.btnHome.setEnabled(false);
                        mFragmentConsultationThirdBinding.btnHome.setAlpha(0.6f);
                    }
                }else {
                    if (checked){
                        mFragmentConsultationThirdBinding.btnHome.setEnabled(true);
                        mFragmentConsultationThirdBinding.btnHome.setAlpha(1f);
                    }else {
                        mFragmentConsultationThirdBinding.btnHome.setEnabled(false);
                        mFragmentConsultationThirdBinding.btnHome.setAlpha(0.6f);
                    }
                }
            }
        });
       super.onResume();
    }*/

    private void getRecommendations() {
        try {
            if (mTaskDetailsData != null && mTaskDetailsData.size() > 0) {
                progressD.show();
                NetworkCallController controller = new NetworkCallController();
                controller.setListner(new NetworkResponseListner<List<Recommendations>>() {
                    @Override
                    public void onResponse(int requestCode, List<Recommendations> items) {
                        initImages.clear();
                        imagesClicked.clear();
                        progressD.dismiss();
                        if (items != null && items.size() > 0) {
                            Vibrator v = null;


                            if (type.equals("CMS")) {
                                if (items.get(0).getOverallInfestationLevel() != null && !items.get(0).getOverallInfestationLevel().equals("")) {
                                    mFragmentConsultationThirdBinding.txtPart.setText("RECOMMENDATIONS " + "(" + items.get(0).getOverallInfestationLevel() + ")");
                                    if (items.get(0).getOverallInfestationLevel().equalsIgnoreCase("High Infestation")) {
                                        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.blink);
                                        mFragmentConsultationThirdBinding.imgAlert.startAnimation(animation);
                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                            v = (Vibrator) Objects.requireNonNull(getActivity()).getSystemService(getActivity().VIBRATOR_SERVICE);
                                            v.vibrate(3000);
                                        }
                                    }else {
                                        mFragmentConsultationThirdBinding.imgAlert.setVisibility(View.GONE);
                                    }
                                } else {
                                    mFragmentConsultationThirdBinding.txtPart.setText("RECOMMENDATIONS");
                                }
                                AppUtils.infestationLevel = items.get(0).getOverallInfestationLevel();
                            }
                            mFragmentConsultationThirdBinding.recycleView.setVisibility(View.VISIBLE);
                            mFragmentConsultationThirdBinding.txtEmpty.setVisibility(View.GONE);
                            for (int i=0; i < items.size(); i++){
                                if (items.get(i).getRecommendationImageUrl() != null && !items.get(i).getRecommendationImageUrl().equals("")){
                                    imgCount++;
                                    initImages.add(items.get(i).getRecommendationImageUrl());
                                }
                            }
                            for (int i = 0; i < items.size(); i++){
                                if (items.get(i).getRecommendationAudioUrl() != null && !items.get(i).getRecommendationAudioUrl().equals("")){
                                    audios.add(items.get(i).getRecommendationAudioUrl());
                                }/*else {
                                    audios.add("https://www.kozco.com/tech/piano2-CoolEdit.mp3");
                                }*/
                            }
                            if (audios.size() > 0){
                                mFragmentConsultationThirdBinding.speakerIv.setVisibility(View.VISIBLE);
                            }else {
                                mFragmentConsultationThirdBinding.speakerIv.setVisibility(View.GONE);
                            }
                            /*if (imgCount > 0){
                                if (AppUtils.isInspectionDone){
                                    mFragmentConsultationThirdBinding.chkAgree.setVisibility(View.GONE);
                                    mFragmentConsultationThirdBinding.btnHome.setEnabled(true);
                                    mFragmentConsultationThirdBinding.btnHome.setAlpha(1f);
                                }else {
                                    mFragmentConsultationThirdBinding.chkAgree.setVisibility(View.VISIBLE);
                                    mFragmentConsultationThirdBinding.noteTv.setVisibility(View.VISIBLE);
                                    mFragmentConsultationThirdBinding.btnHome.setEnabled(false);
                                    mFragmentConsultationThirdBinding.btnHome.setAlpha(0.6f);
                                }
                            }else{
                                mFragmentConsultationThirdBinding.noteTv.setVisibility(View.GONE);
                                if (AppUtils.isInspectionDone){
                                    mFragmentConsultationThirdBinding.chkAgree.setVisibility(View.GONE);
                                    mFragmentConsultationThirdBinding.btnHome.setEnabled(true);
                                    mFragmentConsultationThirdBinding.btnHome.setAlpha(1f);
                                }else {
                                    mFragmentConsultationThirdBinding.chkAgree.setVisibility(View.VISIBLE);
                                    mFragmentConsultationThirdBinding.btnHome.setEnabled(false);
                                    mFragmentConsultationThirdBinding.btnHome.setAlpha(0.6f);
                                }
                            }*/
                            mAdapter.setData(items);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mFragmentConsultationThirdBinding.chkAgree.setVisibility(View.GONE);
                            mFragmentConsultationThirdBinding.recycleView.setVisibility(View.GONE);
                            mFragmentConsultationThirdBinding.txtEmpty.setVisibility(View.VISIBLE);
                            mFragmentConsultationThirdBinding.btnHome.setEnabled(true);
                            mFragmentConsultationThirdBinding.btnHome.setAlpha(1f);
                        }
                    }

                    @Override
                    public void onFailure(int requestCode) {

                    }
                });
                controller.getRecommendations(RECOMMENDATION_REQ, mTaskDetailsData.get(0).getResourceId(), mTaskDetailsData.get(0).getTaskId(), LocaleHelper.getLanguage(getActivity()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playAudio(ImageView view, String url, int position) {
        if (mp == null) {
            mp = new MediaPlayer();
        }
        isPLAYING = true;
        try {
            mFragmentConsultationThirdBinding.speakerIv.setVisibility(View.GONE);
            mFragmentConsultationThirdBinding.progressBar.setVisibility(View.VISIBLE);
            mFragmentConsultationThirdBinding.speakerIv.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.ic_music_stop));
            mp.setDataSource(url);
            mp.prepareAsync();
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mFragmentConsultationThirdBinding.progressBar.setVisibility(View.GONE);
                    mFragmentConsultationThirdBinding.speakerIv.setVisibility(View.VISIBLE);
                    mediaPlayer.start();
                }
            });
            mp.setOnCompletionListener(mediaPlayer -> {
                playCompleted++;
                mediaPlayer.reset();
                Log.d("TAG", "Completed "+playCompleted);
                if (playCompleted < audios.size()) {
                    playAudio(mFragmentConsultationThirdBinding.speakerIv, audios.get(playCompleted), 0);
                } else {
                    stopPlaying();
                }
            });
        } catch (IOException e) {
            Log.d("TAG", "prepare() failed");
        }
    }

    public void stopPlaying() {
        if (mp != null) {
            isPLAYING = false;
            mp.stop();
            mp.release();
            mp = null;
            mFragmentConsultationThirdBinding.speakerIv.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_speaker));
        }
    }

    @Override
    public void onHomeButtonClicked(View view) {
        AppUtils.isInspectionDone = true;
        mAdapter.stopPlaying();
        ChildFragment3Listener listener = (ChildFragment3Listener) getParentFragment();
        listener.onHomeButtonClicked();
    }

    interface ChildFragment3Listener {
        void onHomeButtonClicked();
    }
}
