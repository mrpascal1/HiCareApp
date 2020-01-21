package com.ab.hicarerun.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ab.hicarerun.R;
import com.ab.hicarerun.adapter.RecyclerViewAreaAdapter;
import com.ab.hicarerun.databinding.FragmentBottomSheetBinding;
import com.ab.hicarerun.network.models.OnSiteModel.OnSiteArea;
import com.ab.hicarerun.utils.SharedPreferencesUtility;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

import static com.ab.hicarerun.BaseApplication.getRealm;

/**
 * A simple {@link Fragment} subclass.
 */
public class BottomSheetFragment extends BottomSheetDialogFragment {
    FragmentBottomSheetBinding mFragmentBottomSheetBinding;
    private RecyclerViewAreaAdapter mAdapter;
    ArrayList<String> areaList = null;
    private RealmResults<OnSiteArea> AreaRealmListResults;
    List<OnSiteArea> subItems = null;
    private String Area = "";
    onAreaSelectListener mListener;

    public BottomSheetFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mFragmentBottomSheetBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_sheet, container, false);
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
        AreaRealmListResults = getRealm().where(OnSiteArea.class).findAll();
        getAreaList();
    }

    public void setListener(onAreaSelectListener listener) {
        mListener = listener;
    }


    private void getAreaList() {
        try {
            if (AreaRealmListResults != null) {
                if (AreaRealmListResults.size() > 0) {
                    areaList = new ArrayList<>();
                    subItems = new ArrayList<>();
                    for (int i = 0; i < AreaRealmListResults.size(); i++) {
                        if (!areaList.contains(AreaRealmListResults.get(i).getAreaTypeC()))
                            areaList.add(AreaRealmListResults.get(i).getAreaTypeC());
                    }
                    mAdapter.setData(AreaRealmListResults);
                    mAdapter.setArea(areaList);
                    mAdapter.setOnItemClickHandler(position -> {
                        if (mListener != null){
                            mListener.onAreaSelect(areaList.get(position), position);
                            dismiss();
                        }
                    });
                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "No Area Available!", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public interface onAreaSelectListener {
        void onAreaSelect(String Area, int position);
    }
}

