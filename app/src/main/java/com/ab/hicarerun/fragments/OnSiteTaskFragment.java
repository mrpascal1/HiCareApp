package com.ab.hicarerun.fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
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
import android.widget.Toast;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.activities.OnSiteAccountDetailsActivity;
import com.ab.hicarerun.adapter.AddActivityAdapter;
import com.ab.hicarerun.adapter.OnSiteTasksAdapter;
import com.ab.hicarerun.databinding.FragmentOnsiteTaskBinding;
import com.ab.hicarerun.handler.OnAddActivityClickHandler;
import com.ab.hicarerun.handler.OnSelectServiceClickHandler;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.AttachmentModel.MSTAttachment;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.network.models.OnSiteModel.Account;
import com.ab.hicarerun.network.models.OnSiteModel.ActivityDetail;
import com.ab.hicarerun.network.models.OnSiteModel.OnSiteArea;
import com.ab.hicarerun.network.models.OnSiteModel.OnSiteAreaResponse;
import com.ab.hicarerun.network.models.OnSiteModel.SaveAccountAreaRequest;
import com.ab.hicarerun.network.models.OnSiteModel.SaveAccountAreaResponse;
import com.ab.hicarerun.utils.AppUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class OnSiteTaskFragment extends BaseFragment implements OnAddActivityClickHandler, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener,
        BottomSheetFragment.onAreaSelectListener {
    FragmentOnsiteTaskBinding mFragmentOnSiteTaskBinding;
    OnSiteTasksAdapter mAdapter;
    private AddActivityAdapter addAdapter;
    RecyclerView.LayoutManager layoutManager;
    private static final int AREA_REQ = 1000;
    private static final int SAVE_REQUEST = 2000;
    private static final int REASONS_REQ = 3000;
    private static final String ARG_ACCOUNT = "ARG_ACCOUNT";
    ArrayList<String> areaList = null;
    List<String> serviceList = null;
    private Account model;
    List<OnSiteArea> subItems = null;
    List<OnSiteArea> defaultSubItems = null;
    List<ActivityDetail> activityItems = null;
    HashMap<String, ActivityDetail> hashActivity = null;
    private String Area = "";
    private Integer pageNumber = 1;
    private Double Lat = 0.0;
    private Double Lon = 0.0;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLocation;
    private long UPDATE_INTERVAL = 2 * 10000;  /* 10 secs */
    private long FASTEST_INTERVAL = 20000; /* 2 sec */
    private Boolean isServiceDone = true;
    private RealmResults<OnSiteArea> AreaRealmListResults;

    public OnSiteTaskFragment() {
        // Required empty public constructor
    }

    public static OnSiteTaskFragment newInstance(Account model) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_ACCOUNT, model);
        OnSiteTaskFragment fragment = new OnSiteTaskFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            model = getArguments().getParcelable(ARG_ACCOUNT);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle oldInstanceState)
    {
        super.onSaveInstanceState(oldInstanceState);
        oldInstanceState.clear();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentOnSiteTaskBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_onsite_task, container, false);
//        mFragmentOnSiteTaskBinding.setHandler(this);
        getActivity().setTitle(getResources().getString(R.string.tool_activities));
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        return mFragmentOnSiteTaskBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFragmentOnSiteTaskBinding.recycleView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        mFragmentOnSiteTaskBinding.recycleView.setLayoutManager(layoutManager);
        mAdapter = new OnSiteTasksAdapter(getActivity());
        mAdapter.setOnItemClickHandler(this);
        mFragmentOnSiteTaskBinding.recycleView.setAdapter(mAdapter);
        getAccountActivityArea(Area);
        mFragmentOnSiteTaskBinding.lnrArea.setOnClickListener(view12 -> {
//                mFragmentOnSiteTaskBinding.spnArea.performClick();
        });
        mFragmentOnSiteTaskBinding.cardSheet.setOnClickListener(view1 -> {
            try {
                BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
                bottomSheetFragment.setListener(this);
                bottomSheetFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), bottomSheetFragment.getTag());
            }catch (Exception e){
                e.printStackTrace();
            }

        });
    }


    private void getAccountActivityArea(String area) {
        try {
            if ((OnSiteAccountDetailsActivity) getActivity() != null) {
                RealmResults<LoginResponse> LoginRealmModels =
                        BaseApplication.getRealm().where(LoginResponse.class).findAll();
                if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                    assert LoginRealmModels.get(0) != null;
                    String userId = LoginRealmModels.get(0).getUserID();
                    NetworkCallController controller = new NetworkCallController(this);
                    controller.setListner(new NetworkResponseListner<OnSiteAreaResponse>() {
                        @Override
                        public void onResponse(int requestCode, OnSiteAreaResponse response) {
                            // delete all previous record
                            Realm.getDefaultInstance().beginTransaction();
                            Realm.getDefaultInstance().where(OnSiteArea.class).findAll().deleteAllFromRealm();
                            Realm.getDefaultInstance().commitTransaction();

                            // add new record
                            getRealm().beginTransaction();
                            getRealm().copyToRealmOrUpdate(response.getData());
                            getRealm().commitTransaction();
                            areaList = new ArrayList<>();

                            if (response.getData() != null && response.getData().size() > 0) {
                                mFragmentOnSiteTaskBinding.emptyTask.setVisibility(View.GONE);
                                mFragmentOnSiteTaskBinding.lnrArea.setVisibility(View.VISIBLE);
                                if (area.equals("")) {
                                    assert response.getData().get(0) != null;
                                    Area = response.getData().get(0).getAreaTypeC();
                                    mFragmentOnSiteTaskBinding.txtArea.setText(Area);
                                } else {
                                    Area = area;
                                    mFragmentOnSiteTaskBinding.txtArea.setText(Area);
                                }
                                for (int i = 0; i < response.getData().size(); i++) {
                                    if (!areaList.contains(response.getData().get(i).getAreaTypeC()))
                                        areaList.add(response.getData().get(i).getAreaTypeC());
                                }
                                getSubList(response.getData());
                            } else {
                                mFragmentOnSiteTaskBinding.emptyTask.setVisibility(View.VISIBLE);
                                mFragmentOnSiteTaskBinding.lnrArea.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(int requestCode) {
                        }
                    });
                    controller.getAccountAreaActivity(AREA_REQ, model.getId(), userId);
                }
            }
        } catch (Exception e) {
            RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                assert mLoginRealmModels.get(0) != null;
                String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                AppUtils.sendErrorLogs(e.getMessage(), getClass().getSimpleName(), "getGroomingDetails", lineNo, userName, DeviceName);
            }
        }
    }

    private void getSubList(final List<OnSiteArea> items) {
        try {
            subItems = new ArrayList<>();
            defaultSubItems = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                OnSiteArea mOnSiteArea = new OnSiteArea();
                if (Area.equals(items.get(i).getAreaTypeC())) {
                    mOnSiteArea.setId(items.get(i).getId());
                    mOnSiteArea.setName(items.get(i).getName());
                    mOnSiteArea.setAccountC(items.get(i).getAccountC());
                    mOnSiteArea.setActivityDetail(items.get(i).getActivityDetail());
                    mOnSiteArea.setAreaTypeC(items.get(i).getAreaTypeC());
                    mOnSiteArea.setAreaSubTypeC(items.get(i).getAreaSubTypeC());
                    mOnSiteArea.setServiceNameC(items.get(i).getServiceNameC());
                    mOnSiteArea.setLastActivityOn_Text(items.get(i).getLastActivityOn_Text());
                    mOnSiteArea.setTotalCompletedCount(items.get(i).getTotalCompletedCount());
                    subItems.add(mOnSiteArea);
                }
            }
            if (subItems != null) {
                if (pageNumber == 1 && subItems.size() > 0) {
                    mAdapter.setData(subItems);
                    mAdapter.notifyDataSetChanged();
                    mFragmentOnSiteTaskBinding.emptyTask.setVisibility(View.GONE);
                } else if (subItems.size() > 0) {
                    mAdapter.addData(subItems);
                    mAdapter.notifyDataSetChanged();
                    mFragmentOnSiteTaskBinding.emptyTask.setVisibility(View.GONE);
                } else {
                    mFragmentOnSiteTaskBinding.emptyTask.setVisibility(View.VISIBLE);
                }
            } else {
                mFragmentOnSiteTaskBinding.emptyTask.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAddActivityClick(int position) {
        try {
            serviceList = new ArrayList<>();
            serviceList = subItems.get(position).getActivityDetail();
            addActivityDialog(subItems.get(position));
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onNotDoneClick(final int position) {
        try {
            NetworkCallController controller = new NetworkCallController(OnSiteTaskFragment.this);
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

                        if (list != null) {
                            for (int i = 0; i < list.size(); i++) {
                                final RadioButton rbn = new RadioButton(getActivity());
                                rbn.setId(i);
                                rbn.setText(list.get(i));
                                rbn.setTextSize(15);
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
                                getSaveActivity(subItems.get(position), false, radioButton.getText().toString());
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

    private void addActivityDialog(final OnSiteArea onSiteArea) {
        try {
            if (getActivity() != null) {
                RealmResults<LoginResponse> LoginRealmModels =
                        BaseApplication.getRealm().where(LoginResponse.class).findAll();
                if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                    assert LoginRealmModels.get(0) != null;
                    final String userId = LoginRealmModels.get(0).getUserID();
                    LayoutInflater li = LayoutInflater.from(getActivity());
                    View promptsView = li.inflate(R.layout.layout_add_activity_dialog, null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setView(promptsView);
                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    activityItems = new ArrayList<>();
                    hashActivity = new HashMap<>();
                    alertDialog.setCancelable(false);
                    final RecyclerView recyclerView =
                            (RecyclerView) promptsView.findViewById(R.id.recycleView);
                    final Button btnDone =
                            (Button) promptsView.findViewById(R.id.btnDone);
                    final Button btnCancel =
                            (Button) promptsView.findViewById(R.id.btnCancel);
                    recyclerView.setHasFixedSize(true);
                    layoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(layoutManager);
                    addAdapter = new AddActivityAdapter(getActivity(), serviceList, (position, isCheckedList) -> {
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

//                    addAdapter = new AddActivityAdapter(getActivity(), serviceList, (position, isYes, isNo) -> {
//                        if (isYes) {
//                            btnDone.setEnabled(true);
//                            btnDone.setText("Done");
//                            btnDone.setAlpha(1f);
//                        } else if (isNo) {
//                            btnDone.setEnabled(false);
//                            btnDone.setText("Select");
//                            btnDone.setAlpha(0.5f);
//                        }
//                    });
                    recyclerView.setAdapter(addAdapter);
                    btnDone.setOnClickListener(v -> {
                        getSaveActivity(onSiteArea, true, "");
//                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.dismiss();
                    });

                    addAdapter.setOnSelectServiceClickHandler(new OnSelectServiceClickHandler() {
                        @Override
                        public void onRadioYesClicked(int position) {
                            ActivityDetail activityDetail = new ActivityDetail();
                            activityDetail.setActivityId(0);
                            activityDetail.setCreatedBy(0);
                            activityDetail.setServiceType(serviceList.get(position));
                            activityDetail.setLat(String.valueOf(Lat));
                            activityDetail.setLon(String.valueOf(Lon));
                            activityDetail.setStartTime(AppUtils.currentDateTime());
                            activityDetail.setEndTime(AppUtils.currentDateTime());
                            activityDetail.setIsServiceDone(true);
                            activityDetail.setModifiedBy(0);
                            activityDetail.setServiceNotDoneReason("");
                            hashActivity.put(serviceList.get(position), activityDetail);
//                            for(int i = 0; i < activityItems.size(); i++){
//                                if (!activityItems.get(i).getServiceType().contains(activityDetail.getServiceType())) {
//                                    activityItems.add(activityDetail);
//                                    break;
//                                }
//                            }

                        }

                        @Override
                        public void onRadioNoClicked(int position) {
                            ActivityDetail activityDetail = new ActivityDetail();
                            activityDetail.setActivityId(0);
                            activityDetail.setCreatedBy(0);
                            activityDetail.setServiceType(serviceList.get(position));
                            activityDetail.setLat(String.valueOf(Lat));
                            activityDetail.setLon(String.valueOf(Lon));
                            activityDetail.setStartTime(AppUtils.currentDateTime());
                            activityDetail.setEndTime(AppUtils.currentDateTime());
                            activityDetail.setIsServiceDone(false);
                            activityDetail.setModifiedBy(0);
                            activityDetail.setServiceNotDoneReason("");
//                            if (!activityItems.contains(activityDetail)) {
//                                activityItems.add(activityDetail);
//                            }
                            hashActivity.put(serviceList.get(position), activityDetail);

//                            for(int i = 0; i < activityItems.size(); i++){
//                                if (!activityItems.get(i).getServiceType().contains(activityDetail.getServiceType())) {
//                                    activityItems.add(activityDetail);
//                                    break;
//                                }
//                            }
                        }

                        @Override
                        public void onItemClick(int position) {

                        }
                    });

                    btnCancel.setOnClickListener(view -> alertDialog.cancel());

                    alertDialog.setIcon(R.mipmap.logo);
                    alertDialog.show();
                }
            }
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

    private void getSaveActivity(OnSiteArea onSiteArea, final boolean isServiceDone, String NotDoneReason) {
        try {
            if ((OnSiteAccountDetailsActivity) getActivity() != null) {
                RealmResults<LoginResponse> LoginRealmModels =
                        BaseApplication.getRealm().where(LoginResponse.class).findAll();
                if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                    assert LoginRealmModels.get(0) != null;
                    final String userId = LoginRealmModels.get(0).getUserID();
                    NetworkCallController controller = new NetworkCallController(OnSiteTaskFragment.this);
                    SaveAccountAreaRequest request = new SaveAccountAreaRequest();
                    request.setAccountId(model.getId());
                    request.setAccountName(model.getName());
                    request.setAccountNo(model.getCustomerIdC());
                    request.setAreaSubType(onSiteArea.getAreaSubTypeC());
                    request.setAreaType(onSiteArea.getAreaTypeC());
                    request.setServiceRequestId("");
                    request.setServiceRequestNo("");
                    request.setResource_Id(userId);
                    request.setTaskId("");
                    request.setCreatedBy(0);
                    request.setModifiedBy(0);

                    if (!isServiceDone) {
                        activityItems = new ArrayList<>();
                        ActivityDetail activityDetail = new ActivityDetail();
                        activityDetail.setActivityId(0);
                        activityDetail.setServiceType("Not Done");
                        activityDetail.setCreatedBy(0);
                        activityDetail.setLat(String.valueOf(Lat));
                        activityDetail.setLon(String.valueOf(Lon));
                        activityDetail.setStartTime(AppUtils.currentDateTime());
                        activityDetail.setEndTime(AppUtils.currentDateTime());
                        activityDetail.setIsServiceDone(false);
                        activityDetail.setModifiedBy(0);
                        activityDetail.setServiceNotDoneReason(NotDoneReason);
                        if (!areaList.contains(activityDetail)) {
                            activityItems.add(activityDetail);
                        }
                        request.setActivityDetail(activityItems);
                    } else {
                        activityItems = new ArrayList<>(hashActivity.values());
                        request.setActivityDetail(activityItems);
                    }
                    controller.setListner(new NetworkResponseListner() {
                        @Override
                        public void onResponse(int requestCode, Object response) {
                            SaveAccountAreaResponse saveResponse = (SaveAccountAreaResponse) response;
                            if (saveResponse.getSuccess()) {
                                if (isServiceDone) {
                                    getAccountActivityArea(Area);
                                    Toasty.success(getActivity(), getResources().getString(R.string.activity_successfully_added), Toasty.LENGTH_LONG).show();
                                } else {
                                    getAccountActivityArea(Area);
                                    Toasty.success(getActivity(), getResources().getString(R.string.reason_submitted_successfully), Toasty.LENGTH_LONG).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(int requestCode) {

                        }
                    });
                    controller.getSaveAccountAreaActivity(SAVE_REQUEST, request);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLocation == null) {
            startLocationUpdates();
        }
        if (mLocation != null) {
        } else {
            Toast.makeText(getActivity(), "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    private void startLocationUpdates() {
        try {
            // Create the location request
            mLocationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(UPDATE_INTERVAL)
                    .setFastestInterval(FASTEST_INTERVAL);
            // Request location updates
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        Lat = location.getLatitude();
        Lon = location.getLongitude();
    }

    @Override
    public void onAreaSelect(String Area, int position) {

        try {
            AreaRealmListResults = getRealm().where(OnSiteArea.class).findAll();
            subItems = new ArrayList<>();
            mFragmentOnSiteTaskBinding.txtArea.setText(Area);
            this.Area = Area;
            try {
                if (AreaRealmListResults != null) {
                    if (AreaRealmListResults.size() > 0) {
                        Area = areaList.get(position);
                        for (int i = 0; i < AreaRealmListResults.size(); i++) {
                            OnSiteArea mOnSiteArea = new OnSiteArea();
                            if (Area.equals(AreaRealmListResults.get(i).getAreaTypeC())) {
                                mOnSiteArea.setId(AreaRealmListResults.get(i).getId());
                                mOnSiteArea.setName(AreaRealmListResults.get(i).getName());
                                mOnSiteArea.setAccountC(AreaRealmListResults.get(i).getAccountC());
                                mOnSiteArea.setActivityDetail(AreaRealmListResults.get(i).getActivityDetail());
                                mOnSiteArea.setAreaTypeC(AreaRealmListResults.get(i).getAreaTypeC());
                                mOnSiteArea.setAreaSubTypeC(AreaRealmListResults.get(i).getAreaSubTypeC());
                                mOnSiteArea.setServiceNameC(AreaRealmListResults.get(i).getServiceNameC());
                                mOnSiteArea.setLastActivityOn_Text(AreaRealmListResults.get(i).getLastActivityOn_Text());
                                subItems.add(mOnSiteArea);
                            }
                        }
                    }
                }
                if (subItems != null) {
                    if (pageNumber == 1 && subItems.size() > 0) {
                        mAdapter.setData(subItems);
                        mAdapter.notifyDataSetChanged();
                        mFragmentOnSiteTaskBinding.emptyTask.setVisibility(View.GONE);
                    } else if (subItems.size() > 0) {
                        mAdapter.addData(subItems);
                        mAdapter.notifyDataSetChanged();
                        mFragmentOnSiteTaskBinding.emptyTask.setVisibility(View.GONE);
                    } else {
                        mFragmentOnSiteTaskBinding.emptyTask.setVisibility(View.VISIBLE);
                    }
                } else {
                    mFragmentOnSiteTaskBinding.emptyTask.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

