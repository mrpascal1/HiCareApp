package com.ab.hicarerun.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.FragmentServiceUnitBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServiceUnitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServiceUnitFragment extends BaseFragment {
    FragmentServiceUnitBinding mFragmentServiceUnitBinding;
    public static final String ARGS_COMBINE_ORDER = "ARGS_COMBINE_ORDER";
    public static final String ARGS_SEQUENCE = "ARGS_SEQUENCE";
    public static final String ARGS_ORDER = "ARGS_ORDER";
    public static final String ARGS_IS_COMBINE = "ARGS_IS_COMBINE";
    private String combinedOrderId = "";
    private int sequenceNo = 0;
    private boolean isCombineTask = false;
    private String orderId = "";

    public ServiceUnitFragment() {
        // Required empty public constructor
    }

    public static ServiceUnitFragment newInstance(boolean isCombinedTask, String combinedOrderId, int sequenceNo, String orderId) {
        ServiceUnitFragment fragment = new ServiceUnitFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_COMBINE_ORDER, combinedOrderId);
        args.putString(ARGS_ORDER, orderId);
        args.putBoolean(ARGS_IS_COMBINE, isCombinedTask);
        args.putInt(ARGS_SEQUENCE, sequenceNo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isCombineTask = getArguments().getBoolean(ARGS_COMBINE_ORDER, false);
            orderId = getArguments().getString(ARGS_ORDER);
            combinedOrderId = getArguments().getString(ARGS_COMBINE_ORDER);
            sequenceNo = getArguments().getInt(ARGS_SEQUENCE, 0);
            combinedOrderId = getArguments().getString(ARGS_ORDER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentServiceUnitBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_service_unit, container, false);
        return mFragmentServiceUnitBinding.getRoot();
    }
}