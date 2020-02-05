package com.ab.hicarerun.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.adapter.ChemicalRecycleAdapter;
import com.ab.hicarerun.databinding.FragmentChemicalMstBinding;
import com.ab.hicarerun.handler.OnSaveEventHandler;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.ChemicalModel.Chemicals;
import com.ab.hicarerun.network.models.GeneralModel.GeneralData;
import com.ab.hicarerun.network.models.TaskModel.TaskChemicalList;
import com.ab.hicarerun.network.models.TaskModel.Tasks;
import com.ab.hicarerun.utils.AppUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChemicalMSTFragment extends BaseFragment implements NetworkResponseListner {
    FragmentChemicalMstBinding mFragmentChemicalInfoBinding;
    private static final String ARG_TASK = "ARG_TASK";
    private static final int CHEMICAL_REQ = 1000;
    private Boolean isVerified = false;
    RecyclerView.LayoutManager layoutManager;
    private Integer pageNumber = 1;
    ProgressDialog mProgressBar;
    ChemicalRecycleAdapter mAdapter;
    private OnSaveEventHandler mCallback;
    private HashMap<Integer, String> map = new HashMap<>();
    private List<TaskChemicalList> ChemList = new ArrayList<>();
    List<Chemicals> items = null;
    RealmResults<GeneralData> mGeneralRealmData = null;
    private String status = "";
    private String ActualStatus = "";
    private boolean isChemicalChecked = false;
    private Tasks model;

    public ChemicalMSTFragment() {
        // Required empty public constructor
    }

    public static ChemicalMSTFragment newInstance(Tasks taskId) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_TASK, taskId);
        ChemicalMSTFragment fragment = new ChemicalMSTFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            model = getArguments().getParcelable(ARG_TASK);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            AppUtils.statusCheck(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
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
        mFragmentChemicalInfoBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_chemical_mst, container, false);
//        mProgressBar = new ProgressDialog(getActivity());
        return mFragmentChemicalInfoBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFragmentChemicalInfoBinding.recycleView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        mFragmentChemicalInfoBinding.recycleView.setLayoutManager(layoutManager);
        mGeneralRealmData =
                getRealm().where(GeneralData.class).findAll();
        mAdapter = new ChemicalRecycleAdapter(getActivity(),model.getCombinedTask(), (position, charSeq) -> {
            try {
                if (charSeq != null && map != null) {
                    map.put(position, charSeq);
                    Log.i("MAP_VALUE", Objects.requireNonNull(map.get(position)));
                    if (map.containsValue("")) {
                        map.remove(position);
                    } else {
                        ChemList.clear();
                        for (int i = 0; i < map.size(); i++) {
                            TaskChemicalList ChemModel = new TaskChemicalList();
                            ChemModel.setId(mAdapter.getItem(i).getId());
                            ChemModel.setCWFProductName(mAdapter.getItem(i).getName());
                            ChemModel.setConsumption(mAdapter.getItem(i).getConsumption());
                            ChemModel.setStandard(mAdapter.getItem(i).getStandard());
                            ChemModel.setActual(map.get(i));
                            ChemList.add(ChemModel);
                        }
                        mCallback.chemReqList(ChemList);
                    }
                    for (int i = 0; i < mAdapter.getItemCount(); i++)
                        getValidation(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });


        mFragmentChemicalInfoBinding.checkChemVerified.setOnCheckedChangeListener((buttonView, isChecked) -> {
            try {
                isChemicalChecked = isChecked;
                for (int i = 0; i < mAdapter.getItemCount(); i++)
                    getValidation(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        mFragmentChemicalInfoBinding.recycleView.setAdapter(mAdapter);
        if (items == null) {
            setChemicals();
        }

    }

    private void setChemicals() {
        try {
            if (mGeneralRealmData != null && mGeneralRealmData.size() > 0) {
                isVerified = mGeneralRealmData.get(0).getAutoSubmitChemicals();
                ActualStatus = mGeneralRealmData.get(0).getSchedulingStatus();
                NetworkCallController controller = new NetworkCallController(this);
                controller.setListner(this);
                controller.getMSTChemicals(CHEMICAL_REQ, model.getCombinedTaskId());
                callAfterResponse();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onResponse(int requestCode, Object data) {
        try {
            items = (List<Chemicals>) data;
            if (items != null) {
                if (pageNumber == 1 && items.size() > 0) {
                    mAdapter.setData(items);
                    mAdapter.notifyDataSetChanged();
                } else if (items.size() > 0) {
                    mAdapter.addData(items);
                    mAdapter.notifyDataSetChanged();
                } else {
                    pageNumber--;
                }
                callAfterResponse();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(int requestCode) {
    }

    public void getValidation(int position) {
        try {
            if (mGeneralRealmData != null && mGeneralRealmData.size() > 0) {
                isVerified = mGeneralRealmData.get(0).getAutoSubmitChemicals();
                Log.i("chemicalcount", String.valueOf(mAdapter.getItemCount()));
                Log.i("mapcount", String.valueOf(map.size()));
                if (!isVerified) {
                    if (map.size() == mAdapter.getItemCount()) {
                        mCallback.isChemicalChanged(false);
                    } else {
                        mCallback.isChemicalChanged(true);
                    }
                    if (isChemicalChecked) {
                        mCallback.isChemicalVerified(false);
                    } else {
                        mCallback.isChemicalVerified(true);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callAfterResponse() {
        try {
            if (isVerified || ActualStatus.equals("Completed") || ActualStatus.equals("Incomplete")) {
                mFragmentChemicalInfoBinding.checkChemVerified.setEnabled(false);
                mFragmentChemicalInfoBinding.checkChemVerified.setChecked(true);
            } else {
                mFragmentChemicalInfoBinding.relChemicals.setVisibility(View.VISIBLE);
                mFragmentChemicalInfoBinding.checkChemVerified.setEnabled(true);
                mFragmentChemicalInfoBinding.checkChemVerified.setChecked(false);
            }
            for (int i = 0; i < mAdapter.getItemCount(); i++) {
                getValidation(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}