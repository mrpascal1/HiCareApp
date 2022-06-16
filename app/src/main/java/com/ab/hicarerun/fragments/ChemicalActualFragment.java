package com.ab.hicarerun.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.activities.BarcodeVerificatonActivity;
import com.ab.hicarerun.activities.NewTaskDetailsActivity;
import com.ab.hicarerun.adapter.ChemicalRecycleAdapter;
import com.ab.hicarerun.databinding.FragmentChemicalActualBinding;
import com.ab.hicarerun.handler.ChemicalVisitListener;
import com.ab.hicarerun.handler.OnSaveEventHandler;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.chemicalcmodel.ChemicalConsumption;
import com.ab.hicarerun.network.models.chemicalmodel.Chemicals;
import com.ab.hicarerun.network.models.generalmodel.GeneralData;
import com.ab.hicarerun.network.models.taskmodel.TaskChemicalList;
import com.ab.hicarerun.utils.AppUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import io.realm.RealmResults;

public class ChemicalActualFragment extends BaseFragment implements NetworkResponseListner<List<Chemicals>> {
    FragmentChemicalActualBinding mFragmentChemicalInfoBinding;
    public static final String ARGS_TASKS = "ARGS_TASKS";
    public static final String ARGS_COMBINED_TASKS = "ARGS_COMBINED_TASKS";
    public static final String ARGS_COMBINED_ID = "ARGS_COMBINED_ID";
    public static final String ARGS_COMBINE_ORDER = "ARGS_COMBINE_ORDER";
    public static final String ARGS_ORDER = "ARGS_ORDER";
    public static final String ARGS_SEQUENCE = "ARGS_SEQUENCE";
    public static final String ARGS_COMBINED_SEQUENCE = "ARGS_COMBINED_SEQUENCE";
    private static final int CHEMICAL_REQ = 1000;
    private Boolean isVerified = false;
    RecyclerView.LayoutManager layoutManager;
    private Integer pageNumber = 1;
    ProgressDialog mProgressBar;
    ChemicalRecycleAdapter mAdapter;
    private OnSaveEventHandler mCallback;
    private HashMap<Integer, String> map = new HashMap<>();
    private List<TaskChemicalList> ChemList = new ArrayList<>();
    RealmResults<GeneralData> mGeneralRealmData = null;
    private String status = "";
    private String ActualStatus = "";
    private boolean isChemicalChecked = false;
    //    private Tasks model;
    private String taskId = "";
    private String combinedTaskId = "";
    private boolean isCombinedTask = false;
    private boolean showStandardChemicals = false;
    private boolean showBarcode = false;
    private boolean isActivityThere = false;
    private String combineOrder = "";
    private String orderId = "";
    private String sequenceNo = "";
    private String combinedSequenceNo = "";

    public ChemicalActualFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle oldInstanceState) {
        super.onSaveInstanceState(oldInstanceState);
        oldInstanceState.clear();
    }

    public static ChemicalActualFragment newInstance(String taskId, String combinedTaskId, boolean isCombinedTasks, String combineOrder, String orderId, String sequenceNo, String combinedSequenceNo) {
        Bundle args = new Bundle();
        args.putString(ARGS_TASKS, taskId);
        args.putString(ARGS_COMBINED_ID, combinedTaskId);
        args.putString(ARGS_COMBINE_ORDER, combineOrder);
        args.putString(ARGS_ORDER, orderId);
        args.putBoolean(ARGS_COMBINED_TASKS, isCombinedTasks);
        args.putString(ARGS_SEQUENCE, sequenceNo);
        args.putString(ARGS_COMBINED_SEQUENCE, combinedSequenceNo);
        ChemicalActualFragment fragment = new ChemicalActualFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            taskId = getArguments().getString(ARGS_TASKS);
            combinedTaskId = getArguments().getString(ARGS_COMBINED_ID);
            combineOrder = getArguments().getString(ARGS_COMBINE_ORDER);
            orderId = getArguments().getString(ARGS_ORDER);
            isCombinedTask = getArguments().getBoolean(ARGS_COMBINED_TASKS);
            sequenceNo = getArguments().getString(ARGS_SEQUENCE);
            combinedSequenceNo = getArguments().getString(ARGS_COMBINED_SEQUENCE);
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
        mFragmentChemicalInfoBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_chemical_actual, container, false);
        return mFragmentChemicalInfoBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGeneralRealmData =
                getRealm().where(GeneralData.class).findAll();
        mFragmentChemicalInfoBinding.recycleView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        mFragmentChemicalInfoBinding.recycleView.setLayoutManager(layoutManager);
        ViewCompat.setNestedScrollingEnabled(mFragmentChemicalInfoBinding.recycleView, false);
        if (isCombinedTask) {
            mFragmentChemicalInfoBinding.txtType.setVisibility(View.VISIBLE);
        } else {
            mFragmentChemicalInfoBinding.txtType.setVisibility(View.GONE);
        }
        mFragmentChemicalInfoBinding.txtUnit.setVisibility(View.GONE);
        if (map != null) {
            map.clear();
        }
        if (mGeneralRealmData != null && mGeneralRealmData.size() > 0) {
            showStandardChemicals = mGeneralRealmData.get(0).getShow_Standard_Chemicals();
            showBarcode = mGeneralRealmData.get(0).getShowBarcode();
            isActivityThere = mGeneralRealmData.get(0).getServiceActivityRequired();
        }
//        if (showStandardChemicals) {
//            mFragmentChemicalInfoBinding.txtStandard.setVisibility(View.VISIBLE);
//        } else {
//            mFragmentChemicalInfoBinding.txtStandard.setVisibility(View.GONE);
//        }
        if (showBarcode && !isActivityThere && !(status.equals("Completed") || status.equals("Incomplete"))) {
            mFragmentChemicalInfoBinding.btnRodentScanner.setVisibility(View.VISIBLE);
            AppUtils.IS_QRCODE_THERE = true;
        } else {
            mFragmentChemicalInfoBinding.btnRodentScanner.setVisibility(View.GONE);
            AppUtils.IS_QRCODE_THERE = false;
        }
        mFragmentChemicalInfoBinding.txtStandard.setVisibility(View.VISIBLE);
        mFragmentChemicalInfoBinding.txtType.setVisibility(View.VISIBLE);
        mAdapter = new ChemicalRecycleAdapter(getActivity(), isCombinedTask, showStandardChemicals, "Actual", (position, charSeq) -> {
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
                            ChemModel.setOrignal(mAdapter.getItem(i).getOrignal());
                            if (mAdapter.getItem(i).getOrignal() != null) {
                                if (mAdapter.getItem(i).getOrignal().equals(map.get(i))) {
                                    ChemModel.setChemicalChanged(false);
                                } else {
                                    ChemModel.setChemicalChanged(true);
                                }
                            } else {
                                ChemModel.setChemicalChanged(true);
                            }

                            ChemModel.setActual(map.get(i));
                            ChemList.add(ChemModel);
                        }
                        mCallback.chemReqList(ChemList);
                    }
                    mCallback.isActualChemicalChanged(isChemicalChanged(ChemList));
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

        mFragmentChemicalInfoBinding.btnRodentScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BarcodeVerificatonActivity.class);
                intent.putExtra(ARGS_COMBINE_ORDER, combineOrder);
                intent.putExtra(ARGS_ORDER, orderId);
                intent.putExtra(ARGS_COMBINED_TASKS, isCombinedTask);
                intent.putExtra("barcodeType", "");
                startActivity(intent);
            }
        });

        setChemicals();
        ((NewTaskDetailsActivity) requireActivity()).setOnChemicalVisitListener(new ChemicalVisitListener() {
            @Override
            public void refresh() {
                setChemicals();
            }
        });
    }

    private static boolean isChemicalChanged(List<TaskChemicalList> arraylist) {
        for (TaskChemicalList list : arraylist) {
            if (list.getChemicalChanged()) {
                return true;
            }
        }
        return false;
    }

    private void setChemicals() {
        try {
            showProgressDialog();
            if (mGeneralRealmData != null && mGeneralRealmData.size() > 0) {
                assert mGeneralRealmData.get(0) != null;
                isVerified = mGeneralRealmData.get(0).getAutoSubmitChemicals();
                assert mGeneralRealmData.get(0) != null;
                ActualStatus = mGeneralRealmData.get(0).getSchedulingStatus();
                NetworkCallController controller = new NetworkCallController(this);
                controller.setListner(this);
                if (isCombinedTask) {
                    controller.getMSTChemicals(CHEMICAL_REQ, combinedTaskId);
                } else {
                    controller.getChemicals(CHEMICAL_REQ, taskId);
                }

                callAfterResponse();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onResponse(int requestCode, List<Chemicals> items) {
        try {
            if (items != null) {
                if (pageNumber == 1 && items.size() > 0) {
                    mAdapter.setData(items);
                    mAdapter.notifyDataSetChanged();
                    Log.d("TAG", "Called only this");
                    if (isCombinedTask){
                        getChemicalConsumptionForAllServiceActivity(combineOrder, combinedSequenceNo, items);
                    }else {
                        getChemicalConsumptionForAllServiceActivity(orderId, sequenceNo, items);
                    }
                } else if (items.size() > 0) {
                    mAdapter.addData(items);
                    Log.d("TAG", "this is also called");
                    mAdapter.notifyDataSetChanged();
                } else {
                    pageNumber--;
                }
                callAfterResponse();
            }else {
                dismissProgressDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(int requestCode) {
        dismissProgressDialog();
    }

    private void getValidation(int position) {
        try {
            if (mGeneralRealmData != null && mGeneralRealmData.size() > 0) {
                assert mGeneralRealmData.get(0) != null;
                isVerified = mGeneralRealmData.get(0).getAutoSubmitChemicals();
                Log.i("chemicalcount", String.valueOf(mAdapter.getItemCount()));
                Log.i("mapcount", String.valueOf(map.size()));
                Log.i("mapcount", String.valueOf(map.keySet() + " , " + map.values()));
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

    private void getChemicalConsumptionForAllServiceActivity(String orderNo, String serviceSequenceNo, List<Chemicals> items){
        showProgressDialog();
        NetworkCallController controller = new NetworkCallController();
        controller.setListner(new NetworkResponseListner<ChemicalConsumption>() {
            @Override
            public void onResponse(int requestCode, ChemicalConsumption response) {
                HashMap<String, String> map = new HashMap<>();
                if (response != null) {
                    if (response.isSuccess()) {
                        AppUtils.CHEMICAL_CHANGED = false;
                        if (response.getData() != null){
                            int size = response.getData().size();
                            ArrayList<Integer> indexToDisable = new ArrayList<>();
                            for (int i = 0; i < size; i++) {
                                for (int j = 0; j < items.size(); j++){
                                    if (response.getData().get(i).getChemicalCode().equals(items.get(j).getChemicalProductCode().getProductCode())){
                                        map.put(response.getData().get(i).getChemicalCode(), String.valueOf(response.getData().get(i).getChemicalQuantity()));
                                        indexToDisable.add(j);
                                    }
                                }
                            }
                            mAdapter.setData(items, map, indexToDisable);
                            mAdapter.notifyDataSetChanged();
                            dismissProgressDialog();
                            /*new Thread(() -> {
                                requireActivity().runOnUiThread(() -> {
                                });
                            }).start();*/
                        }else {
                            dismissProgressDialog();
                        }
                    }else {
                        dismissProgressDialog();
                    }
                }else {
                    dismissProgressDialog();
                }
            }

            @Override
            public void onFailure(int requestCode) {
                dismissProgressDialog();
            }
        });
        controller.getChemicalConsumptionForAllServiceActivity(orderNo, serviceSequenceNo);
    }
}
