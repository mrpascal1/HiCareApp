package com.ab.hicarerun.fragments;

import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.adapter.ActivityAreaUnitAdapter;
import com.ab.hicarerun.adapter.ActivityTowerAdapter;
import com.ab.hicarerun.adapter.AddChemicalActivityAdapter;
import com.ab.hicarerun.adapter.ChemicalActivityAdapter;
import com.ab.hicarerun.adapter.ChemicalTowerAdapter;
import com.ab.hicarerun.adapter.RecycleByActivityAdapter;
import com.ab.hicarerun.databinding.FragmentServiceUnitBinding;
import com.ab.hicarerun.handler.OnAddActivityClickHandler;
import com.ab.hicarerun.handler.OnSelectServiceClickHandler;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.ActivityModel.ActivityData;
import com.ab.hicarerun.network.models.ActivityModel.AreaActivity;
import com.ab.hicarerun.network.models.ActivityModel.SaveServiceActivity;
import com.ab.hicarerun.network.models.ActivityModel.ServiceActivity;
import com.ab.hicarerun.network.models.ChemicalModel.AreaData;
import com.ab.hicarerun.network.models.ChemicalModel.SaveActivityRequest;
import com.ab.hicarerun.network.models.ChemicalModel.ServiceChemicalData;
import com.ab.hicarerun.network.models.TSScannerModel.BaseResponse;
import com.ab.hicarerun.utils.AppUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

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
    private String combinedOrderId = "";
    private int sequenceNo = 0;
    private boolean isCombineTask = false;
    private String orderId = "";
    private String floor = "";
    List<ServiceActivity> subItems = null;
    List<AreaActivity> areaList = null;
    List<ServiceActivity> mActivityList = null;
    private List<String> mFloorList;
    HashMap<Integer, SaveActivityRequest> hashActivity = null;
    private List<SaveServiceActivity> mSaveActivityList = new ArrayList<>();
    private static final int UPDATE_REQ = 1000;
    private int activityPosition = 0;

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
        mFragmentServiceUnitBinding.recycleTower.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        mFragmentServiceUnitBinding.recycleTower.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new ActivityTowerAdapter(getActivity());
        mFragmentServiceUnitBinding.recycleTower.setAdapter(mAdapter);

        mFragmentServiceUnitBinding.recycleArea.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        mFragmentServiceUnitBinding.recycleArea.setLayoutManager(layoutManager);
        mActivityAdapter = new RecycleByActivityAdapter(getActivity());
        mFragmentServiceUnitBinding.recycleArea.setAdapter(mActivityAdapter);
        mActivityAdapter.setOnItemClickHandler(this);
        getServiceByActivity(floor);
        mActivityAdapter.notifyDataSetChanged();


        mFragmentServiceUnitBinding.cardSheet.setOnClickListener(view1 -> {
            try {
                FloorBottomSheetFragment bottomSheetFragment = new FloorBottomSheetFragment(mFloorList);
                bottomSheetFragment.setListener(this);
                bottomSheetFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), bottomSheetFragment.getTag());
            } catch (Exception e) {
                e.printStackTrace();
            }
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
                        mActivityAdapter.addData(items.get(0).getServiceActivity());
                        mActivityAdapter.notifyDataSetChanged();
                        mActivityList = mAdapter.getItem(0).getServiceActivity();
                        mFloorList = mAdapter.getItem(0).getFloorList();

                        mAdapter.setOnItemClickHandler(position -> {
                            mActivityList = new ArrayList<>();
                            mFloorList = new ArrayList<>();
                            mActivityList = mAdapter.getItem(position).getServiceActivity();
                            mFloorList = mAdapter.getItem(position).getFloorList();
                            mActivityAdapter.addData(mActivityList);
                            mActivityAdapter.notifyDataSetChanged();
                        });

                    } else {
                        mFragmentServiceUnitBinding.lnrData.setVisibility(View.GONE);
                        mFragmentServiceUnitBinding.txtNoData.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(int requestCode) {
                    mFragmentServiceUnitBinding.lnrData.setVisibility(View.GONE);
                    mFragmentServiceUnitBinding.txtNoData.setVisibility(View.VISIBLE);
                }
            });
            if (isCombineTask) {
                controller.getServiceActivityChemical(combinedOrderId, sequenceNo, "", true);
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
                    mActivityAdapter.addData(subItems);
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
        showAddActivityDialog(areaList, mActivityList.get(position).getServiceActivityName(), mActivityList.get(position).getServiceActivityId(), mActivityList);
    }

    private void showAddActivityDialog(List<AreaActivity> mAreaList, String activityName, Integer serviceActivityId, List<ServiceActivity> mActivityList) {
        try {
            LayoutInflater li = LayoutInflater.from(getActivity());
            View promptsView = li.inflate(R.layout.layout_activity_unit_dialog, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setView(promptsView);
            final AlertDialog alertDialog = alertDialogBuilder.create();
            hashActivity = new HashMap<>();
            alertDialog.setCancelable(false);
            final RecyclerView recyclerView =
                    (RecyclerView) promptsView.findViewById(R.id.recycleView);
            final Button btnDone =
                    (Button) promptsView.findViewById(R.id.btnDone);
            final Button btnCancel =
                    (Button) promptsView.findViewById(R.id.btnCancel);
            final Button btnSkip =
                    (Button) promptsView.findViewById(R.id.btnSkip);
            final TextView txtTitle =
                    (TextView) promptsView.findViewById(R.id.txtTitle);
            txtTitle.setText(activityName);
            txtTitle.setTypeface(txtTitle.getTypeface(), Typeface.BOLD);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            mUnitAdapter = new ActivityAreaUnitAdapter(getActivity(), mAreaList, (position, value) -> {
                SaveServiceActivity activityDetail = new SaveServiceActivity();
                activityDetail.setActivityId(mUnitAdapter.getItem(position).getActivityId());
                activityDetail.setServiceActivityId(serviceActivityId);
                activityDetail.setAreaId(mUnitAdapter.getItem(position).getAreaId());
                activityDetail.setCompletionDateTime(String.valueOf(AppUtils.currentDateTimeWithTimeZone()));
                activityDetail.setServiceType(mUnitAdapter.getItem(position).getServices());
                activityDetail.setStatus(value);
                mSaveActivityList.add(activityDetail);
            });
            recyclerView.setAdapter(mUnitAdapter);
            btnDone.setOnClickListener(v -> {
                updateActivityStatus(mSaveActivityList, true, "");
                alertDialog.dismiss();
            });
            if (mActivityList.size() > 1) {
                btnSkip.setVisibility(View.VISIBLE);
            } else {
                btnSkip.setVisibility(View.GONE);
            }

            btnSkip.setOnClickListener(view -> {
                if (mActivityList.size() > 1) {
                    btnSkip.setVisibility(View.VISIBLE);
                } else {
                    btnSkip.setVisibility(View.GONE);
                }
                if (mActivityList.size() - 1 == activityPosition) {
                    activityPosition = 0;
                } else {
                    activityPosition++;
                }
                txtTitle.setText(mActivityList.get(activityPosition).getServiceActivityName());
                mUnitAdapter = new ActivityAreaUnitAdapter(getActivity(), mActivityList.get(activityPosition).getArea(), (position, value) -> {
                    SaveServiceActivity activityDetail = new SaveServiceActivity();
                    activityDetail.setActivityId(mUnitAdapter.getItem(position).getActivityId());
                    activityDetail.setServiceActivityId(serviceActivityId);
                    activityDetail.setAreaId(mUnitAdapter.getItem(position).getAreaId());
                    activityDetail.setCompletionDateTime(String.valueOf(AppUtils.currentDateTimeWithTimeZone()));
                    activityDetail.setServiceType(mUnitAdapter.getItem(position).getServices());
                    activityDetail.setStatus(value);
                    mSaveActivityList.add(activityDetail);
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
                    activityDetail.setServiceActivityId(serviceActivityId);
                    activityDetail.setAreaId(mUnitAdapter.getItem(position).getAreaId());
                    activityDetail.setCompletionDateTime(String.valueOf(AppUtils.currentDateTimeWithTimeZone()));
                    activityDetail.setServiceType(mUnitAdapter.getItem(position).getServices());
                    activityDetail.setStatus("Yes");
                    mSaveActivityList.add(activityDetail);
                }

                @Override
                public void onRadioNoClicked(int position) {
                    SaveServiceActivity activityDetail = new SaveServiceActivity();
                    activityDetail.setActivityId(mUnitAdapter.getItem(position).getActivityId());
                    activityDetail.setServiceActivityId(0);
                    activityDetail.setAreaId(mUnitAdapter.getItem(position).getAreaId());
                    activityDetail.setCompletionDateTime(String.valueOf(AppUtils.currentDateTimeWithTimeZone()));
                    activityDetail.setServiceType(mUnitAdapter.getItem(position).getServices());
                    activityDetail.setStatus("No");
                    mSaveActivityList.add(activityDetail);
                }
            });

            btnCancel.setOnClickListener(view -> alertDialog.cancel());
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
                        final AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
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
                                data.setCompletionDateTime(String.valueOf(AppUtils.currentDateTimeWithTimeZone()));
                                data.setStatus(radioButton.getText().toString());
                                data.setServiceType("");
                                mSaveActivityList.add(data);
                                updateActivityStatus(mSaveActivityList, false, radioButton.getText().toString());
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

    public void updateActivityStatus(List<SaveServiceActivity> activity, boolean isServiceDone, String option) {
        try {
            NetworkCallController controller = new NetworkCallController(this);
            controller.setListner(new NetworkResponseListner<BaseResponse>() {
                @Override
                public void onResponse(int requestCode, BaseResponse response) {
                    if (response.isSuccess()) {
                        if (mSaveActivityList != null) {
                            mSaveActivityList.clear();
                        }
                        if (isServiceDone) {
                            Toasty.success(getActivity(), "Activity completed successfully", Toasty.LENGTH_SHORT).show();
                        } else {
                            Toasty.success(getActivity(), "Activity marked incomplete", Toasty.LENGTH_SHORT).show();
                        }
                        getServiceByActivity(floor);
                    }
                }

                @Override
                public void onFailure(int requestCode) {

                }
            });
            controller.updateActivityStatus(UPDATE_REQ, activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}