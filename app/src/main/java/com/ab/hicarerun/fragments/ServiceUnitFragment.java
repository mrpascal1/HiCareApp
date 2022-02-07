package com.ab.hicarerun.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.activities.BarcodeVerificatonActivity;
import com.ab.hicarerun.activities.BrowserActivity;
import com.ab.hicarerun.adapter.ActivityAreaUnitAdapter;
import com.ab.hicarerun.adapter.ActivityTowerAdapter;
import com.ab.hicarerun.adapter.RecycleByActivityAdapter;
import com.ab.hicarerun.databinding.FragmentServiceUnitBinding;
import com.ab.hicarerun.handler.OnAddActivityClickHandler;
import com.ab.hicarerun.handler.OnSelectServiceClickHandler;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.activitymodel.ActivityData;
import com.ab.hicarerun.network.models.activitymodel.AreaActivity;
import com.ab.hicarerun.network.models.activitymodel.SaveServiceActivity;
import com.ab.hicarerun.network.models.activitymodel.ServiceActivity;
import com.ab.hicarerun.network.models.activitymodel.SubActivity;
import com.ab.hicarerun.network.models.generalmodel.GeneralData;
import com.ab.hicarerun.network.models.tsscannermodel.BaseResponse;
import com.ab.hicarerun.utils.AppUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ServiceUnitFragment extends BaseFragment implements OnAddActivityClickHandler, FloorBottomSheetFragment.onAreaSelectListener {
    FragmentServiceUnitBinding mFragmentServiceUnitBinding;
    public static final String ARGS_COMBINE_ORDER = "ARGS_COMBINE_ORDER";
    public static final String ARGS_SEQUENCE = "ARGS_SEQUENCE";
    public static final String ARGS_ORDER = "ARGS_ORDER";
    public static final String ARGS_IS_COMBINE = "ARGS_IS_COMBINE";
    private static final int REASONS_REQ = 3000;
    RecyclerView.LayoutManager layoutManager;
    ActivityTowerAdapter mAdapter;
    RecycleByActivityAdapter mActivityAdapter;
    ActivityAreaUnitAdapter mUnitAdapter;
    private RecyclerView recyclerView;
    private TextView txtTitle, serviceCodeTv;
    private TextView txtQty;
    private String combinedOrderId = "";
    private int sequenceNo = 0;
    private boolean isCombineTask = false;
    private String orderId = "";
    private String floor = "";
    private String status = "";
    private String areaType = "Common Area";
    List<ServiceActivity> subItems = null;
    List<AreaActivity> areaList = null;
    List<ServiceActivity> mActivityList = null;
    List<SubActivity> subActivityList = null;
    private List<String> mFloorList;
    HashMap<Integer, SaveServiceActivity> hashActivity = null;
    HashMap<Object, Object> saveChemicalMap = null;
    private List<SaveServiceActivity> mSaveActivityList = new ArrayList<>();
    private static final int UPDATE_REQ = 1000;
    private int activityPosition = 0;
    RealmResults<GeneralData> mGeneralRealmData = null;
    private boolean showBarcode = false;
    private int towerNo = 0;
    EditText chemicalValue;


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
            isCombineTask = getArguments().getBoolean(ARGS_IS_COMBINE, false);
            orderId = getArguments().getString(ARGS_ORDER);
            combinedOrderId = getArguments().getString(ARGS_COMBINE_ORDER);
            sequenceNo = getArguments().getInt(ARGS_SEQUENCE, 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentServiceUnitBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_service_unit, container, false);
        return mFragmentServiceUnitBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        subActivityList = new ArrayList<>();
        mGeneralRealmData =
                getRealm().where(GeneralData.class).findAll();
        if (mGeneralRealmData != null && mGeneralRealmData.size() > 0) {
            status = mGeneralRealmData.get(0).getSchedulingStatus();
            showBarcode = mGeneralRealmData.get(0).getShowBarcode();
        }
        if (showBarcode && !(status.equals("Completed") || status.equals("Incomplete"))) {
            mFragmentServiceUnitBinding.btnRodentScanner.setVisibility(View.VISIBLE);
            AppUtils.IS_QRCODE_THERE = true;
        } else {
            mFragmentServiceUnitBinding.btnRodentScanner.setVisibility(View.GONE);
            AppUtils.IS_QRCODE_THERE = false;
        }

        mFragmentServiceUnitBinding.recycleTower.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        mFragmentServiceUnitBinding.recycleTower.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new ActivityTowerAdapter(getActivity());
        mFragmentServiceUnitBinding.recycleTower.setAdapter(mAdapter);
        mFragmentServiceUnitBinding.recycleArea.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        mFragmentServiceUnitBinding.recycleArea.setLayoutManager(layoutManager);
        mActivityAdapter = new RecycleByActivityAdapter(getActivity(), status);
        mFragmentServiceUnitBinding.recycleArea.setAdapter(mActivityAdapter);
        mActivityAdapter.setOnItemClickHandler(this);
        getServiceByActivity(floor);
        mActivityAdapter.notifyDataSetChanged();
        mFragmentServiceUnitBinding.cardSheet.setOnClickListener(view1 -> {
            try {
                FloorBottomSheetFragment bottomSheetFragment = new FloorBottomSheetFragment(mFloorList);
                bottomSheetFragment.setListener(this);
                bottomSheetFragment.show(requireActivity().getSupportFragmentManager(), bottomSheetFragment.getTag());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        mFragmentServiceUnitBinding.btnRodentScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BarcodeVerificatonActivity.class);
                intent.putExtra(ARGS_COMBINE_ORDER, combinedOrderId);
                intent.putExtra(ARGS_ORDER, orderId);
                intent.putExtra(ARGS_IS_COMBINE, isCombineTask);
                intent.putExtra("barcodeType", "");
                startActivity(intent);

                Log.d("isCombine", combinedOrderId);
                Log.d("isCombine", String.valueOf(isCombineTask));
            }
        });
        mFragmentServiceUnitBinding.viewFloorTv.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), BrowserActivity.class);
            intent.putExtra("orderNo", isCombineTask ? combinedOrderId : orderId);
            intent.putExtra("sequenceNo", String.valueOf(sequenceNo));
            startActivity(intent);
        });
    }

    private void getServiceByActivity(String flr) {
        try {
            NetworkCallController controller = new NetworkCallController(this);
            controller.setListner(new NetworkResponseListner<List<ActivityData>>() {
                @Override
                public void onResponse(int requestCode, List<ActivityData> items) {
                    if (items != null && items.size() > 0) {
                        mFragmentServiceUnitBinding.lnrData.setVisibility(View.VISIBLE);
                        mFragmentServiceUnitBinding.txtNoData.setVisibility(View.GONE);
                        mActivityList = new ArrayList<>();
                        mAdapter.setData(items);
                        mAdapter.notifyDataSetChanged();
                        if (flr.equals("")) {
                            floor = items.get(0).getFloorList().get(0);
                            mFragmentServiceUnitBinding.txtArea.setText("Floor " + floor);
                        } else {
                            floor = flr;
                            mFragmentServiceUnitBinding.txtArea.setText("Floor " + floor);
                        }
                        mActivityAdapter.addData(areaType, items.get(mAdapter.getItemPosition()).getServiceActivity());
                        mActivityAdapter.notifyDataSetChanged();
                        mActivityList = mAdapter.getItem(mAdapter.getItemPosition()).getServiceActivity();
                        mFloorList = mAdapter.getItem(mAdapter.getItemPosition()).getFloorList();
                        mAdapter.setOnItemClickHandler(position -> {
                            mActivityList = new ArrayList<>();
                            mFloorList = new ArrayList<>();
                            mActivityList = mAdapter.getItem(position).getServiceActivity();
                            mFloorList = mAdapter.getItem(position).getFloorList();
                            areaType = mAdapter.getItem(position).getAreaType();
                            towerNo = mAdapter.getItem(position).getTower();
                            mActivityAdapter.addData(areaType, mActivityList);
                            mActivityAdapter.notifyDataSetChanged();
                        });
                        mFragmentServiceUnitBinding.btnRodentScanner.setVisibility(View.GONE);
                    } else {
                        if (showBarcode && !(status.equals("Completed") || status.equals("Incomplete"))) {
                            mFragmentServiceUnitBinding.btnRodentScanner.setVisibility(View.VISIBLE);
                            AppUtils.IS_QRCODE_THERE = true;
                        } else {
                            mFragmentServiceUnitBinding.btnRodentScanner.setVisibility(View.GONE);
                            AppUtils.IS_QRCODE_THERE = false;
                        }
                        mFragmentServiceUnitBinding.lnrData.setVisibility(View.GONE);
                        mFragmentServiceUnitBinding.txtNoData.setVisibility(View.VISIBLE);
                    }
                    dismissProgressDialog();
                }

                @Override
                public void onFailure(int requestCode) {
                    dismissProgressDialog();
                    mFragmentServiceUnitBinding.lnrData.setVisibility(View.GONE);
                    mFragmentServiceUnitBinding.txtNoData.setVisibility(View.VISIBLE);
                }
            });
            if (isCombineTask) {
                controller.getServiceActivityChemical(combinedOrderId, sequenceNo, "", true);
                //controller.getServiceActivityChemical("22011568660", 1, "", true);
            } else {
                controller.getServiceActivityChemical(orderId, sequenceNo, "", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFloorSelect(String floor, int position) {
        try {
            subItems = new ArrayList<>();
            mFragmentServiceUnitBinding.txtArea.setText("Floor " + floor);
            this.floor = floor;
            try {
                floor = mFloorList.get(position);
                for (int i = 0; i < mActivityList.size(); i++) {
                    ServiceActivity mActivityArea = new ServiceActivity();
                    if (floor.equals(mActivityList.get(position).getService_Code())) {
                        mActivityArea.setActivityId(mActivityList.get(i).getActivityId());
                        mActivityArea.setArea(mActivityList.get(i).getArea());
                        mActivityArea.setAreaIds(mActivityList.get(i).getAreaIds());
                        mActivityArea.setChemical_Name(mActivityList.get(i).getChemical_Name());
                        mActivityArea.setService_Code(mActivityList.get(i).getService_Code());
                        mActivityArea.setServiceActivityName(mActivityList.get(i).getServiceActivityName());
                        mActivityArea.setService_Code(mActivityList.get(i).getService_Code());
                        mActivityArea.setStatus(mActivityList.get(i).getStatus());
                        mActivityArea.setFloor(mActivityList.get(i).getFloor());
                        mActivityArea.setServiceActivityId(mActivityList.get(i).getServiceActivityId());
                        subItems.add(mActivityArea);
                    }
                }
                if (subItems != null && subItems.size() > 0) {
                    mActivityAdapter.addData(areaType, subItems);
                    mActivityAdapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAddActivityClick(int position) {
        areaList = new ArrayList<>();
        areaList = mActivityList.get(position).getArea();
        activityPosition = position;
        showAddActivityDialog(areaList, mActivityList.get(position).getUserConsumedChemicalQuantity(), mActivityList.get(position).getServiceActivityName(), mActivityList.get(position).getChemical_Name(), mActivityList, mActivityList.get(position).getChemical_Qty(), mActivityList.get(position).getChemical_Unit(), mActivityList.get(position).getService_Code());
    }

    private void showAddActivityDialog(List<AreaActivity> mAreaList, double userChemicalConsumed, String activityName, String chemical_name, List<ServiceActivity> mActivityList, String chemical_qty, String chemical_unit, String serviceCode) {
        try {
            LayoutInflater li = LayoutInflater.from(getActivity());
            View promptsView = li.inflate(R.layout.layout_activity_unit_dialog, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setView(promptsView);
            final AlertDialog alertDialog = alertDialogBuilder.create();
            hashActivity = new HashMap<>();
            saveChemicalMap = new HashMap<>();

            alertDialog.setCancelable(false);
            recyclerView = (RecyclerView) promptsView.findViewById(R.id.recycleView);
            final Button btnDone = (Button) promptsView.findViewById(R.id.btnDone);
            final Button btnCancel = (Button) promptsView.findViewById(R.id.btnCancel);
            final Button btnSkip = (Button) promptsView.findViewById(R.id.btnSkip);
            final Button verifyBtn = (Button) promptsView.findViewById(R.id.verifyBtn);
            final NestedScrollView nestedScrollView = promptsView.findViewById(R.id.nestedScrollView);
            nestedScrollView.post(()->{
                nestedScrollView.scrollTo(0,0);
            });
            txtTitle = (TextView) promptsView.findViewById(R.id.txtTitle);
            serviceCodeTv = (TextView) promptsView.findViewById(R.id.serviceCodeTv);
            txtQty = (TextView) promptsView.findViewById(R.id.txtQty);
            chemicalValue = (EditText) promptsView.findViewById(R.id.chemicalValueEt);
            txtTitle.setText(chemical_name + " - " + activityName);
            serviceCodeTv.setText(serviceCode);
            txtQty.setText(chemical_qty + " " + chemical_unit.toLowerCase());
            if (userChemicalConsumed > 0.0){
                chemicalValue.setText(userChemicalConsumed+"");
            }
            //txtQty.setText("Qty" + " - " + chemical_qty + " " + chemical_unit.toLowerCase());
            txtTitle.setTypeface(txtTitle.getTypeface(), Typeface.BOLD);
            txtQty.setTypeface(txtTitle.getTypeface(), Typeface.BOLD);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            mUnitAdapter = new ActivityAreaUnitAdapter(getActivity(), mAreaList, (position, value) -> {
                /*if (mAreaList.get(position).getActivity().get(0).isShowQR()){
                    verifyBtn.setVisibility(View.VISIBLE);
                }else {
                    verifyBtn.setVisibility(View.GONE);
                }*/
                SaveServiceActivity activityDetail = new SaveServiceActivity();
                activityDetail.setActivityId(mUnitAdapter.getItem(position).getActivityId());
                activityDetail.setServiceActivityId(mUnitAdapter.getItem(position).getService_Activity_Id());
                activityDetail.setAreaId(mUnitAdapter.getItem(position).getAreaId());
                activityDetail.setServiceNo(sequenceNo);
                activityDetail.setCompletionDateTime(String.valueOf(AppUtils.currentDateTimeWithTimeZone()));
                activityDetail.setServiceType(mUnitAdapter.getItem(position).getServices());
                activityDetail.setStatus(value);
                activityDetail.setAreaType(areaType);
                activityDetail.setTowerNo(towerNo);
                activityDetail.setFloorNo(mUnitAdapter.getItem(position).getFloorNo());
                hashActivity.put(mUnitAdapter.getItem(position).getAreaId(), activityDetail);
                if (!saveChemicalMap.containsValue(mUnitAdapter.getItem(position).getChemicalCode())){
                    saveChemicalMap.put("ActivityId", mUnitAdapter.getItem(position).getActivityId());
                    saveChemicalMap.put("ServiceActivityId", mUnitAdapter.getItem(position).getService_Activity_Id());
                    saveChemicalMap.put("AreaType", areaType);
                    saveChemicalMap.put("ChemicalId", mUnitAdapter.getItem(position).getChemicalId());
                    saveChemicalMap.put("ChemicalCode", mUnitAdapter.getItem(position).getChemicalCode());
                    saveChemicalMap.put("OrderNo", isCombineTask ? combinedOrderId : orderId);
                    saveChemicalMap.put("ServiceSequenceNo", sequenceNo);
                    saveChemicalMap.put("TowerNo", towerNo);
                    saveChemicalMap.put("FloorNo", mUnitAdapter.getItem(position).getFloorNo());
                }
//                mSaveActivityList.add(activityDetail);
            });
            recyclerView.setAdapter(mUnitAdapter);
            btnDone.setOnClickListener(v -> {
                Log.d("TAG-chem", txtTitle.getText().toString() + " : "+txtQty.getText().toString());
                Log.d("TAG-chemVal", chemicalValue.getText().toString());
                if (chemicalValue.getText().toString().trim().equals("")){
                    Toast.makeText(requireContext(), "Please enter chemical value", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    saveChemicalMap.put("ChemicalQuantity", Double.parseDouble(chemicalValue.getText().toString()));
                }
                saveChemicalConsumptionByServiceActivity(chemicalValue);
                updateActivityStatus(hashActivity, true, "", txtTitle.getText().toString(), txtQty.getText().toString());
                if (mActivityList.size() - 1 == activityPosition) {
                    alertDialog.dismiss();
                }
            });
            if (mActivityList.size() > 1) {
                btnSkip.setVisibility(View.VISIBLE);
            } else {
                btnSkip.setVisibility(View.GONE);
            }
//            if (mActivityList.size() - 1 == activityPosition) {
////                btnSkip.setVisibility(View.GONE);
//                activityPosition = 0;
//            } else {
//                activityPosition++;
////                btnSkip.setVisibility(View.VISIBLE);
//            }

            if (mActivityList.size() - 1 == activityPosition) {
                btnSkip.setVisibility(View.GONE);
            } else {
                btnSkip.setVisibility(View.VISIBLE);
            }

            String barcodeType = "";
            for (AreaActivity list: mAreaList){
                if (list.getActivity().get(0).isShowQR()){
                    barcodeType = list.getActivity().get(0).getqRType();
                    verifyBtn.setVisibility(View.VISIBLE);
                }else {
                    verifyBtn.setVisibility(View.GONE);
                }
                break;
            }
            String finalBarcodeType = barcodeType;
            verifyBtn.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), BarcodeVerificatonActivity.class);
                intent.putExtra(ARGS_COMBINE_ORDER, combinedOrderId);
                intent.putExtra(ARGS_ORDER, orderId);
                intent.putExtra(ARGS_IS_COMBINE, isCombineTask);
                intent.putExtra("barcodeType", finalBarcodeType);
                startActivity(intent);
                /*for (AreaActivity list: mAreaList){
                    if (list.getActivity().get(0).isShowQR()){
                        verifyBtn.setVisibility(View.VISIBLE);
                    }else {
                        verifyBtn.setVisibility(View.GONE);
                    }
                    break;
                }*/
            });

            btnSkip.setOnClickListener(view -> {
                if (mActivityList.size() - 1 == activityPosition) {
                    activityPosition = 0;
                    alertDialog.dismiss();
                } else {
                    activityPosition++;
                }
                serviceCodeTv.setText(mActivityList.get(activityPosition).getService_Code());
                txtTitle.setText(mActivityList.get(activityPosition).getChemical_Name() + " - " + mActivityList.get(activityPosition).getServiceActivityName());
                txtQty.setText(mActivityList.get(activityPosition).getChemical_Qty() + " " + mActivityList.get(activityPosition).getChemical_Unit().toLowerCase());
                if (hashActivity != null) {
                    hashActivity.clear();
                }
                mUnitAdapter = new ActivityAreaUnitAdapter(getActivity(), mActivityList.get(activityPosition).getArea(), (position, value) -> {
                    SaveServiceActivity activityDetail = new SaveServiceActivity();
                    activityDetail.setActivityId(mUnitAdapter.getItem(position).getActivityId());
                    activityDetail.setServiceActivityId(mUnitAdapter.getItem(position).getService_Activity_Id());
                    activityDetail.setAreaId(mUnitAdapter.getItem(position).getAreaId());
                    activityDetail.setServiceNo(sequenceNo);
                    activityDetail.setCompletionDateTime(String.valueOf(AppUtils.currentDateTimeWithTimeZone()));
                    activityDetail.setServiceType(mUnitAdapter.getItem(position).getServices());
                    activityDetail.setStatus(value);
                    activityDetail.setAreaType(areaType);
                    activityDetail.setFloorNo(mUnitAdapter.getItem(position).getFloorNo());
                    activityDetail.setTowerNo(towerNo);
                    hashActivity.put(mUnitAdapter.getItem(position).getAreaId(), activityDetail);
                    if (!saveChemicalMap.containsValue(mUnitAdapter.getItem(position).getChemicalCode())){
                        saveChemicalMap.put("ActivityId", mUnitAdapter.getItem(position).getActivityId());
                        saveChemicalMap.put("AreaType", areaType);
                        saveChemicalMap.put("ServiceActivityId", mUnitAdapter.getItem(position).getService_Activity_Id());
                        saveChemicalMap.put("ChemicalId", mUnitAdapter.getItem(position).getChemicalId());
                        saveChemicalMap.put("ChemicalCode", mUnitAdapter.getItem(position).getChemicalCode());
                        saveChemicalMap.put("OrderNo", isCombineTask ? combinedOrderId : orderId);
                        saveChemicalMap.put("ServiceSequenceNo", sequenceNo);
                        saveChemicalMap.put("TowerNo", towerNo);
                        saveChemicalMap.put("FloorNo", mUnitAdapter.getItem(position).getFloorNo());
                    }
                });
                recyclerView.setAdapter(mUnitAdapter);
            });

            mUnitAdapter.setOnSelectServiceClickHandler(new OnSelectServiceClickHandler() {
                @Override
                public void onItemClick(int position) {
                }

                @Override
                public void onRadioYesClicked(int position) {
                    SaveServiceActivity activityDetail = new SaveServiceActivity();
                    activityDetail.setActivityId(mUnitAdapter.getItem(position).getActivityId());
                    activityDetail.setServiceActivityId(mUnitAdapter.getItem(position).getService_Activity_Id());
                    activityDetail.setAreaId(mUnitAdapter.getItem(position).getAreaId());
                    activityDetail.setCompletionDateTime(String.valueOf(AppUtils.currentDateTimeWithTimeZone()));
                    activityDetail.setServiceType(mUnitAdapter.getItem(position).getServices());
                    activityDetail.setStatus("Yes");
                    activityDetail.setAreaType(areaType);
                    activityDetail.setFloorNo(mUnitAdapter.getItem(position).getFloorNo());
                    activityDetail.setTowerNo(towerNo);
                    hashActivity.put(mUnitAdapter.getItem(position).getAreaId(), activityDetail);
                    if (!saveChemicalMap.containsValue(mUnitAdapter.getItem(position).getChemicalCode())){
                        saveChemicalMap.put("ActivityId", mUnitAdapter.getItem(position).getActivityId());
                        saveChemicalMap.put("AreaType", areaType);
                        saveChemicalMap.put("ServiceActivityId", mUnitAdapter.getItem(position).getService_Activity_Id());
                        saveChemicalMap.put("ChemicalId", mUnitAdapter.getItem(position).getChemicalId());
                        saveChemicalMap.put("ChemicalCode", mUnitAdapter.getItem(position).getChemicalCode());
                        saveChemicalMap.put("OrderNo", isCombineTask ? combinedOrderId : orderId);
                        saveChemicalMap.put("ServiceSequenceNo", sequenceNo);
                        saveChemicalMap.put("TowerNo", towerNo);
                        saveChemicalMap.put("FloorNo", mUnitAdapter.getItem(position).getFloorNo());
                    }
                }

                @Override
                public void onRadioNoClicked(int position) {
                    SaveServiceActivity activityDetail = new SaveServiceActivity();
                    activityDetail.setActivityId(mUnitAdapter.getItem(position).getActivityId());
                    activityDetail.setServiceActivityId(mUnitAdapter.getItem(position).getService_Activity_Id());
                    activityDetail.setAreaId(mUnitAdapter.getItem(position).getAreaId());
                    activityDetail.setCompletionDateTime(String.valueOf(AppUtils.currentDateTimeWithTimeZone()));
                    activityDetail.setServiceType(mUnitAdapter.getItem(position).getServices());
                    activityDetail.setStatus("No");
                    activityDetail.setAreaType(areaType);
                    activityDetail.setFloorNo(mUnitAdapter.getItem(position).getFloorNo());
                    activityDetail.setTowerNo(towerNo);
                    hashActivity.put(mUnitAdapter.getItem(position).getAreaId(), activityDetail);
                    if (!saveChemicalMap.containsValue(mUnitAdapter.getItem(position).getChemicalCode())){
                        saveChemicalMap.put("ActivityId", mUnitAdapter.getItem(position).getActivityId());
                        saveChemicalMap.put("AreaType", areaType);
                        saveChemicalMap.put("ServiceActivityId", mUnitAdapter.getItem(position).getService_Activity_Id());
                        saveChemicalMap.put("ChemicalId", mUnitAdapter.getItem(position).getChemicalId());
                        saveChemicalMap.put("ChemicalCode", mUnitAdapter.getItem(position).getChemicalCode());
                        saveChemicalMap.put("OrderNo", isCombineTask ? combinedOrderId : orderId);
                        saveChemicalMap.put("ServiceSequenceNo", sequenceNo);
                        saveChemicalMap.put("TowerNo", towerNo);
                        saveChemicalMap.put("FloorNo", mUnitAdapter.getItem(position).getFloorNo());
                    }
                }
            });

            btnCancel.setOnClickListener(view -> {
                if (hashActivity != null) {
                    hashActivity.clear();
                    saveChemicalMap.clear();
                    mSaveActivityList.clear();
                }
                alertDialog.dismiss();
            });
            Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            alertDialog.setIcon(R.mipmap.logo);
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNotDoneClick(int position) {
        try {
            NetworkCallController controller = new NetworkCallController(this);
            controller.setListner(new NetworkResponseListner() {
                @Override
                public void onResponse(int requestCode, Object response) {
                    try {
                        List<String> list = (List<String>) response;
                        dismissProgressDialog();
                        final AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                        LayoutInflater inflater = LayoutInflater.from(getActivity());
                        final View v = inflater.inflate(R.layout.jeopardy_reasons_layout, null, false);
                        final RadioGroup radioGroup = (RadioGroup) v.findViewById(R.id.radiogrp);
                        String status = mActivityAdapter.getItem(position).getStatus();
                        if (list != null) {
                            for (int i = 0; i < list.size(); i++) {
                                final RadioButton rbn = new RadioButton(getActivity());
                                rbn.setId(i);
                                rbn.setText(list.get(i));
                                rbn.setTextSize(15);
                                if (status != null && status.equalsIgnoreCase(rbn.getText().toString())) {
                                    rbn.setChecked(true);
                                }
                                RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                                params.setMargins(10, 10, 2, 1);
                                radioGroup.addView(rbn, params);
                            }
                        }

                        builder.setView(v);
                        builder.setCancelable(false);

                        builder.setPositiveButton(getResources().getString(R.string.submit), (dialogInterface, i) -> {
                            RadioButton radioButton = (RadioButton) v.findViewById(radioGroup.getCheckedRadioButtonId());
                            if (radioGroup.getCheckedRadioButtonId() == -1) {
                                Toast.makeText(getActivity(), getResources().getString(R.string.please_select_atleast_one_reason), Toast.LENGTH_SHORT).show();
                                builder.setCancelable(false);
                            } else {
                                mSaveActivityList = new ArrayList<>();
                                SaveServiceActivity data = new SaveServiceActivity();
                                data.setActivityId(mActivityList.get(position).getActivityId());
                                data.setServiceActivityId(mActivityList.get(position).getServiceActivityId());
                                data.setAreaId(mActivityList.get(position).getAreaIds());
                                data.setServiceNo(sequenceNo);
                                data.setCompletionDateTime(String.valueOf(AppUtils.currentDateTimeWithTimeZone()));
                                data.setStatus(radioButton.getText().toString());
                                data.setServiceType("");
                                data.setAreaType(areaType);
                                data.setFloorNo(mActivityList.get(position).getFloor());
                                data.setTowerNo(towerNo);
                                mSaveActivityList.add(data);
                                updateActivityStatus(hashActivity, false, radioButton.getText().toString(), "", "");
                                dialogInterface.dismiss();
                            }
                        });
                        builder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel());
                        final AlertDialog dialog = builder.create();
                        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation_2;
                        dialog.show();

                    } catch (Exception e) {
                        e.printStackTrace();
                        dismissProgressDialog();
                    }
                }

                @Override
                public void onFailure(int requestCode) {

                }
            });
            controller.getNotDoneReasons(REASONS_REQ);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(int position) {

    }

    public static boolean isAnyTrue(List<String> arraylist) {
        for (String str : arraylist) {
            if (str.equals("true")) {
                return false;
            }
        }
        return true;
    }

    public void updateActivityStatus(HashMap<Integer, SaveServiceActivity> activity, boolean isServiceDone, String option, String s, String toString) {
        try {
            showProgressDialog();
            mSaveActivityList = new ArrayList<>(hashActivity.values());
            NetworkCallController controller = new NetworkCallController(this);
            controller.setListner(new NetworkResponseListner<BaseResponse>() {
                @Override
                public void onResponse(int requestCode, BaseResponse response) {
                    if (response.isSuccess()) {
                        chemicalValue.setText("");
                        if (mSaveActivityList != null) {
                            mSaveActivityList.clear();
                            hashActivity.clear();
                        }
                        if (mActivityList.size() - 1 == activityPosition) {
                            activityPosition = 0;
                        } else {
                            activityPosition++;
                        }
                        double chemicalConsumed = mActivityList.get(activityPosition).getUserConsumedChemicalQuantity();
                        if (chemicalConsumed > 0.0){
                            chemicalValue.setText(chemicalConsumed+"");
                        }
                        serviceCodeTv.setText(mActivityList.get(activityPosition).getService_Code());
                        txtTitle.setText(mActivityList.get(activityPosition).getChemical_Name() + " - " + mActivityList.get(activityPosition).getServiceActivityName());
                        txtQty.setText(mActivityList.get(activityPosition).getChemical_Qty() + " " + mActivityList.get(activityPosition).getChemical_Unit().toLowerCase());
                        if (mSaveActivityList != null) {
                            mSaveActivityList.clear();
                            hashActivity.clear();
                        }
                        mUnitAdapter = new ActivityAreaUnitAdapter(getActivity(), mActivityList.get(activityPosition).getArea(), (position, value) -> {
                            SaveServiceActivity activityDetail = new SaveServiceActivity();
                            activityDetail.setActivityId(mUnitAdapter.getItem(position).getActivityId());
                            activityDetail.setServiceActivityId(mUnitAdapter.getItem(position).getService_Activity_Id());
                            activityDetail.setAreaId(mUnitAdapter.getItem(position).getAreaId());
                            activityDetail.setServiceNo(sequenceNo);
                            activityDetail.setCompletionDateTime(String.valueOf(AppUtils.currentDateTimeWithTimeZone()));
                            activityDetail.setServiceType(mUnitAdapter.getItem(position).getServices());
                            activityDetail.setStatus(value);
                            activityDetail.setAreaType(areaType);
                            activityDetail.setFloorNo(mUnitAdapter.getItem(position).getFloorNo());
                            activityDetail.setTowerNo(towerNo);
                            hashActivity.put(mUnitAdapter.getItem(position).getAreaId(), activityDetail);
                            if (!saveChemicalMap.containsValue(mUnitAdapter.getItem(position).getChemicalCode())){
                                saveChemicalMap.put("ActivityId", mUnitAdapter.getItem(position).getActivityId());
                                saveChemicalMap.put("AreaType", areaType);
                                saveChemicalMap.put("ServiceActivityId", mUnitAdapter.getItem(position).getService_Activity_Id());
                                saveChemicalMap.put("ChemicalId", mUnitAdapter.getItem(position).getChemicalId());
                                saveChemicalMap.put("ChemicalCode", mUnitAdapter.getItem(position).getChemicalCode());
                                saveChemicalMap.put("OrderNo", isCombineTask ? combinedOrderId : orderId);
                                saveChemicalMap.put("ServiceSequenceNo", sequenceNo);
                                saveChemicalMap.put("TowerNo", towerNo);
                                saveChemicalMap.put("FloorNo", mUnitAdapter.getItem(position).getFloorNo());
                            }
//                            mSaveActivityList.add(activityDetail);
                        });
                        recyclerView.setAdapter(mUnitAdapter);
                        if (isServiceDone) {
                            Toasty.success(requireContext(), "Activity completed successfully", Toasty.LENGTH_SHORT).show();
                        } else {
                            Toasty.success(requireContext(), "Activity marked incomplete", Toasty.LENGTH_SHORT).show();
                        }
                        getServiceByActivity(floor);
                    }else {
                        dismissProgressDialog();
                    }
                }

                @Override
                public void onFailure(int requestCode) {
                    dismissProgressDialog();
                }
            });
            controller.updateActivityStatus(UPDATE_REQ, mSaveActivityList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveChemicalConsumptionByServiceActivity(EditText chemicalValue){
        showProgressDialog();
        NetworkCallController controller = new NetworkCallController();
        controller.setListner(new NetworkResponseListner<BaseResponse>() {
            @Override
            public void onResponse(int requestCode, BaseResponse response) {
                if (response != null) {
                    if (response.isSuccess()) {
                        AppUtils.CHEMICAL_CHANGED = true;
                        chemicalValue.setText("");
                        //Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show();
                        //updateActivityStatus(hashActivity, true, "", txtTitle.getText().toString(), txtQty.getText().toString());
                    }else {
                        Toast.makeText(requireContext(), "Failed to update chemical", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(requireContext(), "Failed to update chemical", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int requestCode) {
            }
        });
        controller.saveChemicalConsumptionByServiceActivity(Collections.singletonList(saveChemicalMap));
    }

}