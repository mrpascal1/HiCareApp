package com.ab.hicarerun.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
//import com.ab.hicarerun.activities.BarcodeVerificatonActivity;
import com.ab.hicarerun.activities.BarcodeVerificatonActivity;
import com.ab.hicarerun.activities.ServiceRenewalActivity;
import com.ab.hicarerun.adapter.ChemicalRecycleAdapter;
import com.ab.hicarerun.adapter.TaskViewPagerAdapter;
import com.ab.hicarerun.databinding.FragmentChemicalInfoBinding;
import com.ab.hicarerun.handler.OnSaveEventHandler;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.ChemicalModel.Chemicals;
import com.ab.hicarerun.network.models.GeneralModel.GeneralData;
import com.ab.hicarerun.network.models.TaskModel.TaskChemicalList;
import com.ab.hicarerun.network.models.TaskModel.Tasks;
import com.ab.hicarerun.utils.AppUtils;
import com.ab.hicarerun.utils.GPSUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChemicalInfoFragment extends BaseFragment {
    FragmentChemicalInfoBinding mFragmentChemicalInfoBinding;
    public static final String ARGS_TASKS = "ARGS_TASKS";
    public static final String ARGS_COMBINED_TASKS = "ARGS_COMBINED_TASKS";
    public static final String ARGS_COMBINED_ID = "ARGS_COMBINED_ID";
    public static final String ARGS_COMBINE_ORDER = "ARGS_COMBINE_ORDER";
    public static final String ARGS_ORDER = "ARGS_ORDER";

    private static final int CHEMICAL_REQ = 1000;
    private Boolean isVerified = false;

    TaskViewPagerAdapter mAdapter;
    private OnSaveEventHandler mCallback;
    //    private Tasks model;
    private String taskId = "";
    private String combinedTaskId = "";
    private boolean isCombinedTask = false;
    private String combineOrder = "";
    private String orderId = "";

    public ChemicalInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle oldInstanceState) {
        super.onSaveInstanceState(oldInstanceState);
        oldInstanceState.clear();
    }

    public static ChemicalInfoFragment newInstance(String taskId, String combinedTaskId, boolean isCombinedTasks, String combinedOrderId, String orderId) {
        Bundle args = new Bundle();
        args.putString(ARGS_TASKS, taskId);
        args.putString(ARGS_COMBINED_ID, combinedTaskId);
        args.putString(ARGS_COMBINE_ORDER, combinedOrderId);
        args.putString(ARGS_ORDER, orderId);
        args.putBoolean(ARGS_COMBINED_TASKS, isCombinedTasks);
        ChemicalInfoFragment fragment = new ChemicalInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            taskId = getArguments().getString(ARGS_TASKS);
            combinedTaskId = getArguments().getString(ARGS_COMBINED_ID);
            isCombinedTask = getArguments().getBoolean(ARGS_COMBINED_TASKS);
            combineOrder = getArguments().getString(ARGS_COMBINE_ORDER);
            orderId = getArguments().getString(ARGS_ORDER);
        }
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnSaveEventHandler) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FragmentToActivity");
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentChemicalInfoBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_chemical_info, container, false);
        return mFragmentChemicalInfoBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViewPager();
    }

    public void setViewPager() {
        try {
            mFragmentChemicalInfoBinding.viewPager.setOffscreenPageLimit(2);
            mAdapter = new TaskViewPagerAdapter(getChildFragmentManager(), getActivity());
            mAdapter.addFragment(ChemicalStandardFragment.newInstance(taskId, combinedTaskId, isCombinedTask), "STANDARD");
            mAdapter.addFragment(ChemicalActualFragment.newInstance(taskId, combinedTaskId, isCombinedTask, combineOrder, orderId), "ACTUAL");
            mFragmentChemicalInfoBinding.viewpagertab.setDistributeEvenly(true);
            mFragmentChemicalInfoBinding.viewPager.setAdapter(mAdapter);
            mFragmentChemicalInfoBinding.viewpagertab.setViewPager(mFragmentChemicalInfoBinding.viewPager);
            mFragmentChemicalInfoBinding.viewPager.setCurrentItem(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
