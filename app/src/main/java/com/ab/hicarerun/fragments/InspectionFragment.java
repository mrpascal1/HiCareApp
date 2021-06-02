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
import com.ab.hicarerun.databinding.FragmentInspectionBinding;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.ConsulationModel.SaveConsulationResponse;
import com.ab.hicarerun.network.models.GeneralModel.GeneralData;
import com.ab.hicarerun.utils.AppUtils;
import com.ab.hicarerun.utils.ProgressBarDrawable;

import java.util.Objects;

import io.realm.RealmResults;

import static com.ab.hicarerun.BaseApplication.getRealm;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InspectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InspectionFragment extends DialogFragment implements InspectionFirstFragment.TermiteFirstListener, InspectionSecondFragment.TermiteSecondListener, ConsultationThirdFragment.ChildFragment3Listener {
    FragmentInspectionBinding mFragmentInspectionBinding;
    private static final int SAVE_CON_REQ = 1000;
    private RealmResults<GeneralData> mTaskDetailsData = null;
    private ProgressDialog progressD;

    public InspectionFragment() {
        // Required empty public constructor
    }

    public static InspectionFragment newInstance() {
        Bundle args = new Bundle();
        InspectionFragment fragment = new InspectionFragment();
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
        mFragmentInspectionBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_inspection, container, false);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        progressD = new ProgressDialog(getActivity(), R.style.TransparentProgressDialog);
        progressD.setCancelable(false);
        return mFragmentInspectionBinding.getRoot();

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTaskDetailsData = getRealm().where(GeneralData.class).findAll();
        if (mTaskDetailsData != null && mTaskDetailsData.size() > 0) {
            if (mTaskDetailsData.get(0).getNumberOfBhk().equals("0"))
                mFragmentInspectionBinding.txtTypeFlat.setText(mTaskDetailsData.get(0).getServiceType());
            else
                mFragmentInspectionBinding.txtTypeFlat.setText(mTaskDetailsData.get(0).getNumberOfBhk() + " | " + mTaskDetailsData.get(0).getServiceType());
        }

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        InspectionFirstFragment childFragment1 = new InspectionFirstFragment();
        transaction.replace(R.id.container_fragment, childFragment1);
        transaction.commit();
        mFragmentInspectionBinding.txtTypeFlat.setTypeface(mFragmentInspectionBinding.txtTypeFlat.getTypeface(), Typeface.BOLD);


        int fillColor = ContextCompat.getColor(getActivity(), R.color.colorPrimary);
        int emptyColor = ContextCompat.getColor(getActivity(), R.color.tab_textColor);
        int separatorColor = ContextCompat.getColor(getActivity(), R.color.transparent);

        if (AppUtils.inspectionList.size() > 0) {
            ProgressBarDrawable progressDrawable = new ProgressBarDrawable(2, fillColor, emptyColor, separatorColor);
            mFragmentInspectionBinding.progressBar.setProgressDrawable(progressDrawable);
            if (AppUtils.isInspectionDone) {
                mFragmentInspectionBinding.progressBar.setProgress(2);
            } else {
                mFragmentInspectionBinding.progressBar.setProgress(0);
            }
            mFragmentInspectionBinding.progressBar.setMax(2);
        } else {
            ProgressBarDrawable progressDrawable = new ProgressBarDrawable(1, fillColor, emptyColor, separatorColor);
            mFragmentInspectionBinding.progressBar.setProgressDrawable(progressDrawable);
            if (AppUtils.isInspectionDone) {
                mFragmentInspectionBinding.progressBar.setProgress(1);
            } else {
                mFragmentInspectionBinding.progressBar.setProgress(0);
            }
            mFragmentInspectionBinding.progressBar.setMax(1);
        }

    }

    @Override
    public void onNextClicked() {
        try {
            if (AppUtils.inspectionList.size() > 0) {
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                InspectionSecondFragment childFragment2 = new InspectionSecondFragment();
                transaction.replace(R.id.container_fragment, childFragment2);
                transaction.commit();
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                if (AppUtils.isInspectionDone) {
                    mFragmentInspectionBinding.progressBar.setProgress(2);
                } else {
                    mFragmentInspectionBinding.progressBar.setProgress(1);
                }
            } else {
                saveConsInsData(1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onBackClicked() {
        try {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            InspectionFirstFragment childFragment1 = new InspectionFirstFragment();
            transaction.replace(R.id.container_fragment, childFragment1);
            transaction.commit();
            getActivity().overridePendingTransition(R.anim.push_left_out, R.anim.push_left_in);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override

    public void onSaveClicked() {
        saveConsInsData(2);
    }

    private void saveConsInsData(int progress) {
        try {
            if (AppUtils.isInspectionDone) {
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                ConsultationThirdFragment childFragment3 = new ConsultationThirdFragment("TMS");
                transaction.replace(R.id.container_fragment, childFragment3);
                transaction.commit();
                Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.push_left_out, R.anim.push_left_in);
            } else {
                mFragmentInspectionBinding.progressBar.setProgress(progress);
                if (AppUtils.dataList != null && AppUtils.dataList.size() > 0) {
                    progressD.show();
                    NetworkCallController controller = new NetworkCallController();
                    controller.setListner(new NetworkResponseListner<SaveConsulationResponse>() {
                        @Override
                        public void onResponse(int requestCode, SaveConsulationResponse response) {
                            if (response.getIssuccess()) {
                                Log.d("cons_", "enter");
                                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                                ConsultationThirdFragment childFragment3 = new ConsultationThirdFragment("TMS");
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
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onHomeButtonClicked() {
        getDialog().dismiss();
    }
}
