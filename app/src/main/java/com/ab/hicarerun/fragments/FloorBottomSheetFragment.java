package com.ab.hicarerun.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ab.hicarerun.R;
import com.ab.hicarerun.adapter.RecyclerViewAreaAdapter;
import com.ab.hicarerun.databinding.FragmentFloorBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class FloorBottomSheetFragment extends BottomSheetDialogFragment {
    FragmentFloorBottomSheetBinding mFragmentBottomSheetBinding;
    private RecyclerViewAreaAdapter mAdapter;
    onAreaSelectListener mListener;
    private List<String> mFloorList = new ArrayList<>();

    public FloorBottomSheetFragment(List<String> mFloorList) {
        this.mFloorList = mFloorList;
    }

    @Override
    public void onSaveInstanceState(Bundle oldInstanceState) {
        super.onSaveInstanceState(oldInstanceState);
        oldInstanceState.clear();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mFragmentBottomSheetBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_floor_bottom_sheet, container, false);
        // init the bottom sheet behavior
//        bottomSheetBehavior = BottomSheetBehavior.from(mFragmentBottomSheetBinding.bottomSheet);
        return mFragmentBottomSheetBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFragmentBottomSheetBinding.imgClose.setOnClickListener(view1 -> dismiss());
        mFragmentBottomSheetBinding.lnrClose.setOnClickListener(view12 -> dismiss());
        mAdapter = new RecyclerViewAreaAdapter(getActivity());
        mFragmentBottomSheetBinding.recycleArea.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFragmentBottomSheetBinding.recycleArea.setHasFixedSize(true);
        mFragmentBottomSheetBinding.recycleArea.setClipToPadding(false);
        mFragmentBottomSheetBinding.recycleArea.setAdapter(mAdapter);
        getAreaList();
    }

    public void setListener(onAreaSelectListener listener) {
        mListener = listener;
    }


    private void getAreaList() {
        try {

            if (mFloorList.size() > 0) {
                mAdapter.setArea(mFloorList);
                mAdapter.setOnItemClickHandler(position -> {
                    if (mListener != null) {
                        mListener.onFloorSelect(mFloorList.get(position), position);
                        dismiss();
                    }
                });
                mAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.no_area_available), Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public interface onAreaSelectListener {
        void onFloorSelect(String floor, int position);
    }
}

