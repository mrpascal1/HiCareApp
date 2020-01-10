package com.ab.hicarerun.fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.adapter.ChemicalRecycleAdapter;
import com.ab.hicarerun.adapter.ChemicalRecycleMSTAdapter;
import com.ab.hicarerun.databinding.FragmentChemicalMstinfoBinding;
import com.ab.hicarerun.handler.OnAddChemicalClickHandler;
import com.ab.hicarerun.handler.OnSaveEventHandler;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.ChemicalModel.Chemicals;
import com.ab.hicarerun.network.models.ChemicalModel.MSTChemicals;
import com.ab.hicarerun.network.models.GeneralModel.GeneralData;
import com.ab.hicarerun.network.models.TaskModel.TaskChemicalList;
import com.ab.hicarerun.network.models.TaskModel.Tasks;
import com.ab.hicarerun.utils.AppUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChemicalMSTInfoFragment extends BaseFragment implements NetworkResponseListner, OnAddChemicalClickHandler {
    FragmentChemicalMstinfoBinding mFragmentChemicalMstinfoBinding;
    private static final String ARG_TASK = "ARG_TASK";
    private static final int CHEMICAL_REQ = 1000;
    private Tasks model;
    private OnSaveEventHandler mCallback;
    List<String> expandableListTitle;
    HashMap<String, List<Chemicals>> expandableListDetail;
    ChemicalRecycleMSTAdapter mMSTAdapter;
    RealmResults<GeneralData> mGeneralRealmData = null;
    private Boolean isVerified = false;
    private String ActualStatus = "";
    List<MSTChemicals> items = null;

    private HashMap<Integer, String> map = new HashMap<>();
    private HashMap<Integer, String> chemMap = new HashMap<>();
    private List<TaskChemicalList> ChemList = new ArrayList<>();
    private boolean isChemicalChecked = false;
    List<Chemicals> subChemicals = null;
    private ChemicalRecycleAdapter mAdapter;

    public ChemicalMSTInfoFragment() {
        // Required empty public constructor
    }

    public static ChemicalMSTInfoFragment newInstance(Tasks taskId) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_TASK, taskId);
        ChemicalMSTInfoFragment fragment = new ChemicalMSTInfoFragment();
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
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnSaveEventHandler) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FragmentToActivity");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentChemicalMstinfoBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_chemical_mstinfo, container, false);
        return mFragmentChemicalMstinfoBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGeneralRealmData =
                getRealm().where(GeneralData.class).findAll();
        mFragmentChemicalMstinfoBinding.expandableChemicalView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        mFragmentChemicalMstinfoBinding.expandableChemicalView.setItemsCanFocus(true);
        setMSTChemicals();
    }

    private void setMSTChemicals() {
        try {
            if (mGeneralRealmData != null && mGeneralRealmData.size() > 0) {
                isVerified = mGeneralRealmData.get(0).getAutoSubmitChemicals();
                ActualStatus = mGeneralRealmData.get(0).getSchedulingStatus();
                NetworkCallController controller = new NetworkCallController(this);
                controller.setListner(this);
                controller.getMSTChemicals(CHEMICAL_REQ, model.getCombinedTaskId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(int requestCode, Object response) {
        try {
            items = (List<MSTChemicals>) response;
            expandableListDetail = new HashMap<>();
            expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
            if (items != null) {
                if (items.size() > 0) {
                    for (int i = 0; i < items.size(); i++) {
                        expandableListTitle.add(items.get(i).getTaskNo());
                        expandableListDetail.put(items.get(i).getTaskNo(), items.get(i).getData());
                        subChemicals = expandableListDetail.get(expandableListTitle);
                    }
                    mMSTAdapter = new ChemicalRecycleMSTAdapter(getActivity(), expandableListTitle, expandableListDetail, mFragmentChemicalMstinfoBinding.expandableChemicalView);
//                    mMSTAdapter = new ChemicalRecycleMSTAdapter(getActivity(), expandableListTitle, expandableListDetail, mFragmentChemicalMstinfoBinding.expandableChemicalView, (position, charSeq) -> {
//                        try {
//                            if (charSeq != null && map != null) {
//                                map.put(position, charSeq);
//                                Log.i("MAP_VALUE", map.get(position));
//                                if (map.containsValue("")) {
//                                    map.remove(position);
//                                } else {
//                                    ChemList.clear();
//                                    for (int i = 0; i < items.size(); i++) {
//                                        for (int j = 0; j <= map.size(); j++) {
//                                            TaskChemicalList ChemModel = new TaskChemicalList();
//                                            ChemModel.setId(expandableListDetail.get(expandableListTitle.get(i)).get(j).getId());
//                                            ChemModel.setCWFProductName(expandableListDetail.get(expandableListTitle.get(i)).get(j).getCWFProductName());
//                                            ChemModel.setConsumption(expandableListDetail.get(expandableListTitle.get(i)).get(j).getConsumption());
//                                            ChemModel.setStandard(expandableListDetail.get(expandableListTitle.get(i)).get(j).getStandard_Usage());
//                                            ChemModel.setActual(map.get(j));
//                                            ChemList.add(ChemModel);
//                                            getValidation(j);
//                                        }
//                                    }
//                                    mCallback.chemReqList(ChemList);
//                                }
//                                for (int i = 0; i < subChemicals.size(); i++)
//                                    getValidation(i);
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    });
                    mFragmentChemicalMstinfoBinding.expandableChemicalView.setAdapter(mMSTAdapter);
                    for (int i = 0; i < mFragmentChemicalMstinfoBinding.expandableChemicalView.getExpandableListAdapter().getGroupCount(); i++) {
                        //Expand group
                        mFragmentChemicalMstinfoBinding.expandableChemicalView.expandGroup(i);
                    }
                }
//                callAfterResponse(checkVerified);
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
                Log.i("chemicalcount", String.valueOf(position));
                Log.i("mapcount", String.valueOf(map.size()));
                if (!isVerified) {
                    if (map.size() == expandableListDetail.size()) {
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

    private void callAfterResponse(AppCompatCheckBox checkVerified, RelativeLayout relCheck) {
        try {
            if (isVerified || ActualStatus.equals("Completed") || ActualStatus.equals("Incomplete")) {
                checkVerified.setEnabled(false);
                checkVerified.setChecked(true);
            } else {
                relCheck.setVisibility(View.VISIBLE);
                checkVerified.setEnabled(true);
                checkVerified.setChecked(false);
            }
            for (int i = 0; i < mAdapter.getItemCount(); i++) {
                getValidation(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAddChemicalClicked(int parent) {
        addChemicalDialog(parent);
    }

    private void addChemicalDialog(int parent) {

        RealmResults<GeneralData> mGeneralRealmData =
                getRealm().where(GeneralData.class).findAll();
        if (mGeneralRealmData != null && mGeneralRealmData.size() > 0) {

            LayoutInflater li = LayoutInflater.from(getActivity());

            View promptsView = li.inflate(R.layout.layout_chemical_dialog, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setView(promptsView);
            final AlertDialog alertDialog = alertDialogBuilder.create();
            RecyclerView.LayoutManager layoutManager;

            final AppCompatCheckBox checkVerified =
                    promptsView.findViewById(R.id.check_chem_verified);
            final LinearLayout lnrSubmit =
                    promptsView.findViewById(R.id.lnrSubmit);
            final RecyclerView recyclerView =
                    promptsView.findViewById(R.id.recycleView);
            final RelativeLayout relCheck =
                    promptsView.findViewById(R.id.rel_chemicals);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            mAdapter = new ChemicalRecycleAdapter(getActivity(), model.getCombinedTask(), (position, charSeq) -> {
                try {
                    if (charSeq != null && chemMap != null) {
                        chemMap.put(position, charSeq);
                        Log.i("MAP_VALUE", chemMap.get(position));
                        if (chemMap.containsValue("")) {
                            chemMap.remove(position);
                        } else {
                            ChemList.clear();
                            for (int i = 0; i < chemMap.size(); i++) {
                                TaskChemicalList ChemModel = new TaskChemicalList();
                                ChemModel.setId(mAdapter.getItem(i).getId());
                                ChemModel.setCWFProductName(mAdapter.getItem(i).getName());
                                ChemModel.setConsumption(mAdapter.getItem(i).getConsumption());
                                ChemModel.setStandard(mAdapter.getItem(i).getStandard());
                                ChemModel.setActual(chemMap.get(i));
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
            recyclerView.setAdapter(mAdapter);
            checkVerified.setOnCheckedChangeListener((buttonView, isChecked) -> {
                try {
                    isChemicalChecked = isChecked;
                    for (int i = 0; i < mAdapter.getItemCount(); i++)
                        getValidation(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            if (expandableListDetail.get(expandableListTitle.get(parent)) == null) {
                try {

                    if (subChemicals != null) {
                        if (subChemicals.size() > 0) {
                            mAdapter.setData(subChemicals);
                            mAdapter.notifyDataSetChanged();
                            callAfterResponse(checkVerified, relCheck);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            lnrSubmit.setOnClickListener(v -> alertDialog.dismiss());
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
    }

    @Override
    public void onItemClick(int parent, int child) {

    }
}
