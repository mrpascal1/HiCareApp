package com.ab.hicarerun.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.ab.hicarerun.R;
import com.ab.hicarerun.adapter.RoutineCheckUpParentAdapter;
import com.ab.hicarerun.adapter.RoutineSubmittedAdapter;
import com.ab.hicarerun.databinding.FragmentRoutineCheckBinding;
import com.ab.hicarerun.databinding.FragmentRoutineSubmittedBinding;
import com.ab.hicarerun.handler.UserRoutineCheckClickHandler;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.RoutineModel.RoutineQuestion;
import com.ab.hicarerun.network.models.RoutineModel.SaveRoutineResponse;
import com.ab.hicarerun.network.models.RoutineModel.TechRoutineData;
import com.ab.hicarerun.utils.LocaleHelper;

import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoutineSubmittedFragment extends DialogFragment implements UserRoutineCheckClickHandler {
    FragmentRoutineSubmittedBinding mFragmentRoutineSubmittedBinding;
    public static final String ARGS_RES = "ARGS_RES";
    private static final int ROUTINE_REQ = 1000;
    private String resourceId = "";
    private RoutineSubmittedAdapter mAdapter;
    private TechRoutineData routineData;
    private ProgressDialog progressD;

    public RoutineSubmittedFragment() {
        // Required empty public constructor
    }

    public static RoutineSubmittedFragment newInstance(String technicianId) {
        Bundle args = new Bundle();
        args.putString(ARGS_RES, technicianId);
        RoutineSubmittedFragment fragment = new RoutineSubmittedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            resourceId = getArguments().getString(ARGS_RES);
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
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
        mFragmentRoutineSubmittedBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_routine_submitted, container, false);
        mFragmentRoutineSubmittedBinding.setHandler(this);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        return mFragmentRoutineSubmittedBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressD = new ProgressDialog(getActivity(), R.style.TransparentProgressDialog);
        progressD.setCancelable(false);
        mAdapter = new RoutineSubmittedAdapter(getActivity());
        mFragmentRoutineSubmittedBinding.recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFragmentRoutineSubmittedBinding.recycleView.setHasFixedSize(true);
        mFragmentRoutineSubmittedBinding.recycleView.setClipToPadding(false);
        mFragmentRoutineSubmittedBinding.recycleView.setAdapter(mAdapter);
        progressD.show();
        getRoutine();
    }

    private void getRoutine() {
        try {
            NetworkCallController controller = new NetworkCallController();
            controller.setListner(new NetworkResponseListner<TechRoutineData>() {
                @Override
                public void onResponse(int requestCode, TechRoutineData response) {
                    routineData = new TechRoutineData();
                    routineData.setResourceId(response.getResourceId());
                    routineData.setRoutineQuestions(response.getRoutineQuestions());
                    progressD.dismiss();
                    if (response != null) {
                        if (response.getRoutineQuestions() != null && response.getRoutineQuestions().size() > 0) {
                            mAdapter.addData(response.getRoutineQuestions());
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onFailure(int requestCode) {
                    progressD.dismiss();
                }
            });
            controller.getRoutineResponse(ROUTINE_REQ, resourceId, LocaleHelper.getLanguage(getActivity()));
        } catch (Exception e) {
            progressD.dismiss();
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveButtonClicked(View view) {
        dismiss();
    }

}