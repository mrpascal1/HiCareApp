package com.ab.hicarerun.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.adapter.TaskViewPagerAdapter;
import com.ab.hicarerun.databinding.FragmentChemicalInfoBinding;
import com.ab.hicarerun.handler.OnSaveEventHandler;

import org.jetbrains.annotations.NotNull;

public class ChemicalActivityFragment  extends BaseFragment {
    FragmentChemicalInfoBinding mFragmentChemicalInfoBinding;
    public static final String ARGS_COMBINE_ORDER = "ARGS_COMBINE_ORDER";
    public static final String ARGS_SEQUENCE = "ARGS_SEQUENCE";
    public static final String ARGS_ORDER = "ARGS_ORDER";
    public static final String ARGS_IS_COMBINE = "ARGS_IS_COMBINE";
    TaskViewPagerAdapter mAdapter;
    private OnSaveEventHandler mCallback;
    //    private Tasks model;
    private int sequenceNo = 0;
    private String combinedOrderId = "";
    private String orderId = "";
    private boolean isCombinedTask = false;

    public ChemicalActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle oldInstanceState) {
        super.onSaveInstanceState(oldInstanceState);
        oldInstanceState.clear();
    }

    public static ChemicalActivityFragment newInstance(Boolean isCombinedTasks, String combinedOrderId, int sequenceNo, String orderId) {
        Bundle args = new Bundle();
        ChemicalActivityFragment fragment = new ChemicalActivityFragment();
        args.putString(ARGS_COMBINE_ORDER, combinedOrderId);
        args.putString(ARGS_ORDER, orderId);
        args.putBoolean(ARGS_IS_COMBINE, isCombinedTasks);
        args.putInt(ARGS_SEQUENCE, sequenceNo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isCombinedTask = getArguments().getBoolean(ARGS_COMBINE_ORDER, false);
            orderId = getArguments().getString(ARGS_ORDER);
            combinedOrderId = getArguments().getString(ARGS_COMBINE_ORDER);
            sequenceNo = getArguments().getInt(ARGS_SEQUENCE, 0);
            combinedOrderId = getArguments().getString(ARGS_ORDER);
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
            mAdapter.addFragment(ServiceUnitFragment.newInstance(isCombinedTask, combinedOrderId, sequenceNo, orderId), "By Service Activity");
            mAdapter.addFragment(ServiceActivityFragment.newInstance(isCombinedTask, combinedOrderId, sequenceNo, orderId), "By Service Unit");
            mFragmentChemicalInfoBinding.viewpagertab.setDistributeEvenly(true);
            mFragmentChemicalInfoBinding.viewPager.setAdapter(mAdapter);
            mFragmentChemicalInfoBinding.viewpagertab.setViewPager(mFragmentChemicalInfoBinding.viewPager);
            mFragmentChemicalInfoBinding.viewPager.setCurrentItem(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
