package com.ab.hicarerun.fragments;

import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
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
import com.ab.hicarerun.adapter.AddChemicalActivityAdapter;
import com.ab.hicarerun.adapter.ChemicalActivityAdapter;
import com.ab.hicarerun.adapter.ChemicalTowerAdapter;
import com.ab.hicarerun.databinding.FragmentServiceActivityBinding;
import com.ab.hicarerun.handler.OnAddActivityClickHandler;
import com.ab.hicarerun.handler.OnSelectServiceClickHandler;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.ChemicalModel.ActivityData;
import com.ab.hicarerun.network.models.ChemicalModel.AreaData;
import com.ab.hicarerun.network.models.ChemicalModel.SaveActivityRequest;
import com.ab.hicarerun.network.models.ChemicalModel.ServiceChemicalData;
import com.ab.hicarerun.network.models.GeneralModel.GeneralData;
import com.ab.hicarerun.network.models.TSScannerModel.BaseResponse;
import com.ab.hicarerun.utils.AppUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import io.realm.RealmResults;


public class ServiceActivityFragment extends BaseFragment implements OnAddActivityClickHandler, FloorBottomSheetFragment.onAreaSelectListener {
    FragmentServiceActivityBinding fragmentServiceActivityBinding;
    public static final String ARGS_COMBINE_ORDER = "ARGS_COMBINE_ORDER";
    public static final String ARGS_SEQUENCE = "ARGS_SEQUENCE";
    public static final String ARGS_ORDER = "ARGS_ORDER";
    public static final String ARGS_IS_COMBINE = "ARGS_IS_COMBINE";
    ChemicalTowerAdapter mAdapter;
    ChemicalActivityAdapter mAreaAdapter;
    AddChemicalActivityAdapter mAddAdapter;
    RecyclerView.LayoutManager layoutManager;
    private List<AreaData> mAreaList;
    private List<String> mFloorList;
    private List<ActivityData> mActivityList;
    private List<SaveActivityRequest> mSaveActivityList = new ArrayList<>();
    HashMap<Integer, SaveActivityRequest> hashActivity = null;
    private static final int REASONS_REQ = 3000;
    private static final int UPDATE_REQ = 1000;
    private String floor = "";
    List<AreaData> subItems = null;
    List<AreaData> defaultSubItems = null;
    private String combinedOrderId = "";
    private int sequenceNo = 0;
    private boolean isCombineTask = false;
    private String orderId = "";
    RealmResults<GeneralData> mGeneralRealmData = null;


    public ServiceActivityFragment() {
        // Required empty public constructor
    }


    public static ServiceActivityFragment newInstance(Boolean isCombinedTasks, String combinedOrderId, int sequenceNo, String orderId) {
        ServiceActivityFragment fragment = new ServiceActivityFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_COMBINE_ORDER, combinedOrderId);
        args.putString(ARGS_ORDER, orderId);
        args.putBoolean(ARGS_IS_COMBINE, isCombinedTasks);
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
        fragmentServiceActivityBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_service_activity, container, false);
        return fragmentServiceActivityBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentServiceActivityBinding.recycleTower.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        fragmentServiceActivityBinding.recycleTower.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new ChemicalTowerAdapter(getActivity());
        fragmentServiceActivityBinding.recycleTower.setAdapter(mAdapter);

        fragmentServiceActivityBinding.recycleArea.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        fragmentServiceActivityBinding.recycleArea.setLayoutManager(layoutManager);
        mAreaAdapter = new ChemicalActivityAdapter(getActivity());
        fragmentServiceActivityBinding.recycleArea.setAdapter(mAreaAdapter);
        mAreaAdapter.setOnItemClickHandler(this);
        getServiceAreaChemical(floor);
        mAreaAdapter.notifyDataSetChanged();

        fragmentServiceActivityBinding.cardSheet.setOnClickListener(view1 -> {
            try {
                FloorBottomSheetFragment bottomSheetFragment = new FloorBottomSheetFragment(mFloorList);
                bottomSheetFragment.setListener(this);
                bottomSheetFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), bottomSheetFragment.getTag());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void getServiceAreaChemical(String flr) {
        try {
            NetworkCallController controller = new NetworkCallController(this);
            controller.setListner(new NetworkResponseListner<List<ServiceChemicalData>>() {
                @Override
                public void onResponse(int requestCode, List<ServiceChemicalData> items) {
                    if (items != null && items.size() > 0) {
                        fragmentServiceActivityBinding.lnrData.setVisibility(View.VISIBLE);
                        fragmentServiceActivityBinding.txtNoData.setVisibility(View.GONE);
                        mAreaList = new ArrayList<>();
                        mAdapter.setData(items);
                        mAdapter.notifyDataSetChanged();
                        if (flr.equals("")) {
                            floor = items.get(0).getFloorList().get(0);
                            fragmentServiceActivityBinding.txtArea.setText("Floor " + floor);
                        } else {
                            floor = flr;
                            fragmentServiceActivityBinding.txtArea.setText("Floor " + floor);
                        }
                        mAreaAdapter.addData(items.get(0).getArea());
                        mAreaAdapter.notifyDataSetChanged();
                        mAreaList = mAdapter.getItem(0).getArea();
                        mFloorList = mAdapter.getItem(0).getFloorList();


                        mAdapter.setOnItemClickHandler(position -> {
                            mAreaList = new ArrayList<>();
                            mFloorList = new ArrayList<>();
                            mAreaList = mAdapter.getItem(position).getArea();
                            mFloorList = mAdapter.getItem(position).getFloorList();
                            mAreaAdapter.addData(mAreaList);
                            mAreaAdapter.notifyDataSetChanged();
                        });

                    } else {
                        fragmentServiceActivityBinding.lnrData.setVisibility(View.GONE);
                        fragmentServiceActivityBinding.txtNoData.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(int requestCode) {
                    fragmentServiceActivityBinding.lnrData.setVisibility(View.GONE);
                    fragmentServiceActivityBinding.txtNoData.setVisibility(View.VISIBLE);
                }
            });
            if (isCombineTask) {
                controller.getServiceAreaChemical(combinedOrderId, sequenceNo, "", true);
            } else {
                controller.getServiceAreaChemical(orderId, sequenceNo, "", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showAddActivityDialog(List<ActivityData> mActivityList) {
        try {
            LayoutInflater li = LayoutInflater.from(getActivity());
            View promptsView = li.inflate(R.layout.layout_add_chemcal_activity_dialog, null);
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
            final TextView txtTitle =
                    (TextView) promptsView.findViewById(R.id.txtTitle);
            txtTitle.setTypeface(txtTitle.getTypeface(), Typeface.BOLD);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            mAddAdapter = new AddChemicalActivityAdapter(getActivity(), mActivityList, (position, isCheckedList) -> {
                if (isAnyTrue(isCheckedList)) {
                    btnDone.setEnabled(false);
                    btnDone.setText(getResources().getString(R.string.button_select));
                    btnDone.setAlpha(0.5f);
                } else {
                    btnDone.setEnabled(true);
                    btnDone.setText(getResources().getString(R.string.done_onsite));
                    btnDone.setAlpha(1f);
                }
            });
            recyclerView.setAdapter(mAddAdapter);
            btnDone.setOnClickListener(v -> {
                updateActivityStatus(mSaveActivityList, true, "");
                alertDialog.dismiss();
            });

            mAddAdapter.setOnSelectServiceClickHandler(new OnSelectServiceClickHandler() {
                @Override
                public void onItemClick(int position) {

                }

                @Override
                public void onRadioYesClicked(int position) {
                    SaveActivityRequest activityDetail = new SaveActivityRequest();
                    activityDetail.setActivityId(mAddAdapter.getItem(position).getActivityId());
                    activityDetail.setAreaId(mAddAdapter.getItem(position).getAreaId());
                    activityDetail.setCompletionDateTime(String.valueOf(AppUtils.currentDateTimeWithTimeZone()));
                    activityDetail.setServiceType(mAddAdapter.getItem(position).getServiceCode());
                    activityDetail.setStatus("Yes");
                    mSaveActivityList.add(activityDetail);
                }

                @Override
                public void onRadioNoClicked(int position) {
                    SaveActivityRequest activityDetail = new SaveActivityRequest();
                    activityDetail.setActivityId(mAddAdapter.getItem(position).getActivityId());
                    activityDetail.setAreaId(mAddAdapter.getItem(position).getAreaId());
                    activityDetail.setCompletionDateTime(String.valueOf(AppUtils.currentDateTimeWithTimeZone()));
                    activityDetail.setServiceType(mAddAdapter.getItem(position).getServiceCode());
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

    public static boolean isAnyTrue(List<String> arraylist) {
        for (String str : arraylist) {
            if (str.equals("true")) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onAddActivityClick(int position) {
        try {
            mActivityList = new ArrayList<>();
            mActivityList = mAreaList.get(position).getActivity();
            showAddActivityDialog(mActivityList);
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
                        String status = mAreaAdapter.getItem(position).getStatus();
                        if (list != null) {
                            for (int i = 0; i < list.size(); i++) {
                                final RadioButton rbn = new RadioButton(getActivity());
                                rbn.setId(i);
                                rbn.setText(list.get(i));
                                rbn.setTextSize(15);
                                if (status!=null && status.equalsIgnoreCase(rbn.getText().toString())) {
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
                                SaveActivityRequest data = new SaveActivityRequest();
                                data.setActivityId(mAreaList.get(position).getActivityId());
                                data.setAreaId(mAreaList.get(position).getAreaId());
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

    @Override
    public void onFloorSelect(String floor, int position) {
        try {
            subItems = new ArrayList<>();
            fragmentServiceActivityBinding.txtArea.setText("Floor " + floor);
            this.floor = floor;
            try {
                floor = mFloorList.get(position);
                for (int i = 0; i < mAreaList.size(); i++) {
                    AreaData mOnSiteArea = new AreaData();
                    if (floor.equals(mAreaList.get(position).getFloorNo())) {
                        mOnSiteArea.setActivity(mAreaList.get(i).getActivity());
                        mOnSiteArea.setActivityId(mAreaList.get(i).getActivityId());
                        mOnSiteArea.setAreaId(mAreaList.get(i).getAreaId());
                        mOnSiteArea.setAreaName(mAreaList.get(i).getAreaName());
                        mOnSiteArea.setFloorNo(mAreaList.get(i).getFloorNo());
                        mOnSiteArea.setServices(mAreaList.get(i).getServices());

                        subItems.add(mOnSiteArea);
                    }
                }

                if (subItems != null && subItems.size() > 0) {
                    mAreaAdapter.addData(subItems);
                    mAreaAdapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateActivityStatus(List<SaveActivityRequest> activity, boolean isServiceDone, String option) {
        try {
            NetworkCallController controller = new NetworkCallController(this);
            controller.setListner(new NetworkResponseListner<BaseResponse>() {
                @Override
                public void onResponse(int requestCode, BaseResponse response) {
                    if (response.isSuccess()) {
                        if (isServiceDone) {
                            Toasty.success(getActivity(), "Activity updated successfully", Toasty.LENGTH_SHORT).show();
                        } else {
                            Toasty.success(getActivity(), "Activity marked incomplete", Toasty.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(int requestCode) {

                }
            });
            controller.updateActivityServiceStatus(UPDATE_REQ, activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}