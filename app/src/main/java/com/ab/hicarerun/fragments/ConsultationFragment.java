package com.ab.hicarerun.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.ab.hicarerun.R;
import com.ab.hicarerun.activities.NewTaskDetailsActivity;
import com.ab.hicarerun.databinding.FragmentConsultationBinding;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.consulationmodel.SaveConsulationResponse;
import com.ab.hicarerun.network.models.generalmodel.GeneralData;
import com.ab.hicarerun.utils.AppUtils;
import com.ab.hicarerun.utils.ProgressBarDrawable;

import java.util.Objects;

import io.realm.RealmResults;

import static com.ab.hicarerun.BaseApplication.getRealm;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConsultationFragment extends DialogFragment implements ConsultaionFirstChildFragmentt.ChildFragment1Listener, ConsultaionSecondChildFragment.ChildFragment2Listener, ConsultationThirdFragment.ChildFragment3Listener {
    FragmentConsultationBinding mFragmentConsultationBinding;
    private static final int SAVE_CON_REQ = 1000;
    private RealmResults<GeneralData> mTaskDetailsData = null;
    private ProgressDialog progressD;

    public ConsultationFragment() {
        // Required empty public constructor
    }

    public static ConsultationFragment newInstance() {
        Bundle args = new Bundle();
        ConsultationFragment fragment = new ConsultationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void onResume() {
        Window window = getDialog().getWindow();
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 0.9), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentConsultationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_consultation, container, false);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        progressD = new ProgressDialog(getActivity(), R.style.TransparentProgressDialog);
        progressD.setCancelable(false);
        return mFragmentConsultationBinding.getRoot();

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        setCancelable(false);
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTaskDetailsData = getRealm().where(GeneralData.class).findAll();
        if (mTaskDetailsData != null && mTaskDetailsData.size() > 0) {
            if (mTaskDetailsData.get(0).getNumberOfBhk().equals("0"))
                mFragmentConsultationBinding.txtTypeFlat.setText(mTaskDetailsData.get(0).getServiceType());
            else
                mFragmentConsultationBinding.txtTypeFlat.setText(mTaskDetailsData.get(0).getNumberOfBhk() + " | " + mTaskDetailsData.get(0).getServiceType());
        }

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        ConsultaionFirstChildFragmentt childFragment1 = new ConsultaionFirstChildFragmentt();
        transaction.replace(R.id.container_fragment, childFragment1);
        transaction.commit();
        mFragmentConsultationBinding.txtTypeFlat.setTypeface(mFragmentConsultationBinding.txtTypeFlat.getTypeface(), Typeface.BOLD);


        int fillColor = ContextCompat.getColor(getActivity(), R.color.colorPrimary);
        int emptyColor = ContextCompat.getColor(getActivity(), R.color.tab_textColor);
        int separatorColor = ContextCompat.getColor(getActivity(), R.color.transparent);

        if (AppUtils.inspectionList != null && AppUtils.inspectionList.size() > 0) {
            ProgressBarDrawable progressDrawable = new ProgressBarDrawable(2, fillColor, emptyColor, separatorColor);
            mFragmentConsultationBinding.progressBar.setProgressDrawable(progressDrawable);
            if (AppUtils.isInspectionDone) {
                mFragmentConsultationBinding.progressBar.setProgress(2);
            } else {
                mFragmentConsultationBinding.progressBar.setProgress(0);
            }
            mFragmentConsultationBinding.progressBar.setMax(2);
        } else {
            ProgressBarDrawable progressDrawable = new ProgressBarDrawable(1, fillColor, emptyColor, separatorColor);
            mFragmentConsultationBinding.progressBar.setProgressDrawable(progressDrawable);
            if (AppUtils.isInspectionDone) {
                mFragmentConsultationBinding.progressBar.setProgress(1);
            } else {
                mFragmentConsultationBinding.progressBar.setProgress(0);
            }
            mFragmentConsultationBinding.progressBar.setMax(1);
        }

    }

    @Override
    public void onNextClicked() {
        if (AppUtils.inspectionList.size() > 0) {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            ConsultaionSecondChildFragment childFragment2 = new ConsultaionSecondChildFragment();
            transaction.replace(R.id.container_fragment, childFragment2);
            transaction.commit();
            getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            if (AppUtils.isInspectionDone) {
                mFragmentConsultationBinding.progressBar.setProgress(2);
            } else {
                mFragmentConsultationBinding.progressBar.setProgress(1);
            }
        } else {
            saveConsInsData(1);
        }

    }

    @Override
    public void onBackClicked() {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        ConsultaionFirstChildFragmentt childFragment1 = new ConsultaionFirstChildFragmentt();
        transaction.replace(R.id.container_fragment, childFragment1);
        transaction.commit();
        getActivity().overridePendingTransition(R.anim.push_left_out, R.anim.push_left_in);
    }

    @Override

    public void onSaveClicked() {
        saveConsInsData(2);
    }

    private void saveConsInsData(int progress) {
        try {
            if (AppUtils.isInspectionDone) {
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                ConsultationThirdFragment childFragment3 = new ConsultationThirdFragment("CMS");
                transaction.replace(R.id.container_fragment, childFragment3);
                transaction.commit();
                Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.push_left_out, R.anim.push_left_in);
            } else {
                mFragmentConsultationBinding.progressBar.setProgress(progress);
                if (AppUtils.dataList != null && AppUtils.dataList.size() > 0) {
                    progressD.show();
                    NetworkCallController controller = new NetworkCallController();
                    controller.setListner(new NetworkResponseListner<SaveConsulationResponse>() {
                        @Override
                        public void onResponse(int requestCode, SaveConsulationResponse response) {
                            if (response.getIssuccess()) {
                                Log.d("cons_", "enter");
                                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                                ConsultationThirdFragment childFragment3 = new ConsultationThirdFragment("CMS");
                                transaction.replace(R.id.container_fragment, childFragment3);
                                transaction.commit();
                                getActivity().overridePendingTransition(R.anim.push_left_out, R.anim.push_left_in);
                                AppUtils.dataList.clear();
                                progressD.dismiss();
                            }
                        }

                        @Override
                        public void onFailure(int requestCode) {

                        }
                    });
                    controller.saveConsultationNdInspection(SAVE_CON_REQ, AppUtils.dataList);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onHomeButtonClicked() {
        getDialog().dismiss();
        NewTaskDetailsActivity mActivity = (NewTaskDetailsActivity) getActivity();
        if (mActivity != null) {
            mActivity.refreshMyData();
        }
    }


}
