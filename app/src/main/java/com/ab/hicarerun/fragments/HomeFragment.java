package com.ab.hicarerun.fragments;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.activities.HomeActivity;
import com.ab.hicarerun.activities.NewTaskDetailsActivity;
import com.ab.hicarerun.adapter.TaskListAdapter;
import com.ab.hicarerun.databinding.FragmentHomeBinding;
import com.ab.hicarerun.handler.OnCallListItemClickHandler;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.AttendanceModel.AttendanceRequest;
import com.ab.hicarerun.network.models.ExotelModel.ExotelResponse;
import com.ab.hicarerun.network.models.HandShakeModel.ContinueHandShakeResponse;
import com.ab.hicarerun.network.models.JeopardyModel.CWFJeopardyRequest;
import com.ab.hicarerun.network.models.JeopardyModel.CWFJeopardyResponse;
import com.ab.hicarerun.network.models.JeopardyModel.JeopardyReasonsList;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.network.models.NPSModel.NPSData;
import com.ab.hicarerun.network.models.ProfileModel.Profile;
import com.ab.hicarerun.network.models.TaskModel.TaskListResponse;
import com.ab.hicarerun.network.models.TaskModel.Tasks;
import com.ab.hicarerun.utils.AppUtils;
import com.ab.hicarerun.utils.SharedPreferencesUtility;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import io.realm.RealmResults;

public class HomeFragment extends BaseFragment implements NetworkResponseListner<TaskListResponse>, OnCallListItemClickHandler {
    FragmentHomeBinding mFragmentHomeBinding;
    RecyclerView.LayoutManager layoutManager;
    TaskListAdapter mAdapter;
    //    final Handler timerHandler = new Handler();
    private static final int TASKS_REQ = 1000;
    private static final int EXOTEL_REQ = 2000;
    private static final int CALL_REQUEST = 3000;
    private static final int CAM_REQUEST = 4000;
    private static final int JEOPARDY_REQUEST = 5000;
    private static final int CWF_REQUEST = 6000;
    private static final int TECH_REQ = 7000;
    private static final int TECH_NPS = 8000;
    private boolean isBack = false;
    private boolean isSkip = false;
    private Integer pageNumber = 1;
    private String UserId = "", IMEI = "", UserName = "";
    private String activityName = "";
    private String methodName = "";
    private NavigationView navigationView = null;
    private String taskId = "";
    List<Tasks> items = null;
    RealmResults<LoginResponse> LoginRealmModels = null;
    private boolean isParam = false;
    private byte[] bitUser = null;
    private static final String ARG_USER = "ARG_USER";
    AlertDialog alertDialog = null;
    private boolean isShowNPS = false;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bitUser = getArguments().getByteArray(ARG_USER);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentHomeBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
//        getActivity().setTitle("Home");
        navigationView = Objects.requireNonNull(getActivity()).findViewById(R.id.navigation_view);
        LinearLayout toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        LinearLayout tool = getActivity().findViewById(R.id.customToolbar);
        RelativeLayout relBottom = getActivity().findViewById(R.id.relBottom);
        RelativeLayout relCoin = getActivity().findViewById(R.id.relCoin);
        tool.setVisibility(View.VISIBLE);
        relBottom.setVisibility(View.VISIBLE);
        relCoin.setVisibility(View.GONE);
//        LinearLayout custom_toolbar = getActivity().findViewById(R.id.customToolbar);
//        custom_toolbar.setVisibility(View.GONE);
        DrawerLayout drawerLayout = getActivity().findViewById(R.id.drawer);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        activityName = getActivity().getClass().getSimpleName();
        apply();

        return mFragmentHomeBinding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();
//        timerHandler.postDelayed(timerRunnable, 500);
        try {
            isBack = SharedPreferencesUtility.getPrefBoolean(Objects.requireNonNull(getActivity()), SharedPreferencesUtility.PREF_REFRESH);
            if (isBack) {
                getAllTasks();
                AppUtils.getDataClean();
                SharedPreferencesUtility.savePrefBoolean(getActivity(), SharedPreferencesUtility.PREF_REFRESH, false);
            } else {
                AppUtils.getDataClean();
            }
            apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray typedArray = Objects.requireNonNull(getActivity()).obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        view.setBackgroundResource(backgroundResource);
        mFragmentHomeBinding.recycleView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        mFragmentHomeBinding.swipeRefreshLayout.setOnRefreshListener(
                this::getAllTasks);


        mFragmentHomeBinding.recycleView.setLayoutManager(layoutManager);

        mFragmentHomeBinding.swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_dark, android.R.color.holo_blue_light,
                android.R.color.holo_red_dark, android.R.color.holo_red_light,
                android.R.color.holo_green_dark, android.R.color.holo_green_light,
                android.R.color.holo_red_dark, android.R.color.holo_red_light);

        // specify an adapter (see also next example)
        mAdapter = new TaskListAdapter(getActivity(), this);
        mAdapter.setOnCallClickHandler(this);
        mFragmentHomeBinding.recycleView.setAdapter(mAdapter);

        getAllTasks();
        isShowNPS = SharedPreferencesUtility.getPrefBoolean(getActivity(), SharedPreferencesUtility.PREF_SHOW_NPS);
        if (isShowNPS)
            showNPSDialog();
        mFragmentHomeBinding.swipeRefreshLayout.setRefreshing(true);
    }


    private void getAllTasks() {
        try {
            SharedPreferencesUtility.savePrefBoolean(Objects.requireNonNull(getActivity()), SharedPreferencesUtility.PREF_REFRESH, false);
            if (getActivity() != null) {
                RealmResults<LoginResponse> LoginRealmModels =
                        BaseApplication.getRealm().where(LoginResponse.class).findAll();
                if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                    assert LoginRealmModels.get(0) != null;
                    UserName = LoginRealmModels.get(0).getUserName();
                    TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
//                    IMEI = telephonyManager.getDeviceId();
//                    IMEI = UUID.randomUUID().toString();
                    IMEI = Settings.Secure.getString(getActivity().getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                    assert LoginRealmModels.get(0) != null;
                    UserId = LoginRealmModels.get(0).getUserID();
                    NetworkCallController controller = new NetworkCallController(this);
                    controller.setListner(this);
                    controller.getTasksList(TASKS_REQ, UserId, IMEI);

                }
            }
        } catch (Exception e) {
            RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                assert mLoginRealmModels.get(0) != null;
                String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                AppUtils.sendErrorLogs(e.getMessage(), getClass().getSimpleName(), "getAllTasks", lineNo, userName, DeviceName);
            }
        }
    }

    @Override
    public void onResponse(int requestCode, TaskListResponse data) {
        try {
            AppUtils.getDataClean();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (data.getErrorMessage().equals("Absent") && !isSkip) {
            isParam = data.getParam();
            mFragmentHomeBinding.swipeRefreshLayout.setRefreshing(false);
            getAttendanceDialog();

        } else {
            mFragmentHomeBinding.swipeRefreshLayout.setRefreshing(false);
            if (items != null) {
                items.clear();
            }
            items = data.getData();
            if (items != null) {
                if (pageNumber == 1 && items.size() > 0) {
                    mAdapter.setData(items);
                    mAdapter.notifyDataSetChanged();
                    mFragmentHomeBinding.emptyTask.setVisibility(View.GONE);
                } else if (items.size() > 0) {
                    mAdapter.addData(items);
                    mAdapter.notifyDataSetChanged();
                    mFragmentHomeBinding.emptyTask.setVisibility(View.GONE);
                } else {
                    mFragmentHomeBinding.emptyTask.setVisibility(View.VISIBLE);
                }
            } else {
                mFragmentHomeBinding.emptyTask.setVisibility(View.VISIBLE);
            }

            mAdapter.setOnItemClickHandler(position -> {
                if (items.get(position).getDetailVisible()) {
                    try {
                        AppUtils.getDataClean();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        Intent intent = new Intent(getActivity(), NewTaskDetailsActivity.class);
                        intent.putExtra(NewTaskDetailsActivity.ARGS_TASKS, items.get(position).getTaskId());
                        intent.putExtra(NewTaskDetailsActivity.ARGS_COMBINED_TASKS, items.get(position).getCombinedTask());
                        intent.putExtra(NewTaskDetailsActivity.ARGS_COMBINED_TASKS_ID, items.get(position).getCombinedTaskId());
                        intent.putExtra(NewTaskDetailsActivity.ARGS_COMBINED_ORDER, items.get(position).getCombinedOrderNumber());
                        intent.putExtra(NewTaskDetailsActivity.ARGS_COMBINED_TYPE, items.get(position).getCombinedServiceType());
                        intent.putExtra(NewTaskDetailsActivity.ARGS_NEXT_TASK, items.get(position).getNext_Task_Id());
//                        intent.putExtra(NewTaskDetailsActivity.ARGS_LATITUDE, items.get(position).getCustomerLatitude());
//                        intent.putExtra(NewTaskDetailsActivity.ARGS_LONGITUDE, items.get(position).getCustomerLongitude());
//                        intent.putExtra(NewTaskDetailsActivity.ARGS_NAME, items.get(position).getAccountName());
//                        intent.putExtra(NewTaskDetailsActivity.ARGS_MOBILE, items.get(position).getTechnicianMobileNo());
//                        intent.putExtra(NewTaskDetailsActivity.ARGS_SEQUENCE, String.valueOf(items.get(position).getSequenceNumber()));
//                        intent.putExtra(NewTaskDetailsActivity.ARGS_TAG, items.get(position).getTag());
//                        intent.putExtra(NewTaskDetailsActivity.ARG_USER, bitUser);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toasty.info(Objects.requireNonNull(getActivity()), getResources().getString(R.string.please_complete_your_previous_job_first), Toasty.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void getAttendanceDialog() {
        try {
            if (alertDialog != null) {
                alertDialog.dismiss();
            }

            LayoutInflater li = LayoutInflater.from(getActivity());

            View promptsView = li.inflate(R.layout.dialog_mark_attendance, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

            alertDialogBuilder.setView(promptsView);

            alertDialogBuilder.setTitle(getResources().getString(R.string.mark_attendance_home));

            // create alert dialog
            alertDialog = alertDialogBuilder.create();
            final AppCompatTextView txt_head =
                    (AppCompatTextView) promptsView.findViewById(R.id.txt_head);
            final AppCompatButton btn_send =
                    (AppCompatButton) promptsView.findViewById(R.id.btn_send);
            final AppCompatButton btnSkip =
                    (AppCompatButton) promptsView.findViewById(R.id.btn_skip);

            if (isParam) {
                btnSkip.setVisibility(View.VISIBLE);
            } else {
                btnSkip.setVisibility(View.GONE);
            }

            txt_head.setText(getResources().getString(R.string.welcome_attendance) + UserName + getResources().getString(R.string.please_mark_your_attendance_with_the_face_recognition));

            btn_send.setOnClickListener(v -> {
                alertDialog.dismiss();
                replaceFragment(FaceRecognizationFragment.newInstance(true, "", ""), "HomeFragment-FaceRecognizationFragment");
            });

            btnSkip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    isSkip = true;
                    try {

                        if ((HomeActivity) getActivity() != null) {
                            RealmResults<LoginResponse> LoginRealmModels =
                                    BaseApplication.getRealm().where(LoginResponse.class).findAll();
                            if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                                String BatteryStatistics = String.valueOf(AppUtils.getMyBatteryLevel(getActivity()));
                                AttendanceRequest request = AppUtils.getDeviceInfo(getActivity(), "", BatteryStatistics, true);
                                NetworkCallController controller = new NetworkCallController(HomeFragment.this);
                                controller.setListner(new NetworkResponseListner() {
                                    @Override
                                    public void onResponse(int requestCode, Object data) {
                                        ContinueHandShakeResponse response = (ContinueHandShakeResponse) data;
                                        if (response.getSuccess()) {
                                            Toasty.success(getActivity(), getResources().getString(R.string.attendance_marked_successfully), Toasty.LENGTH_SHORT).show();
                                            alertDialog.dismiss();
                                            getAllTasks();
                                            showNPSDialog();

                                        } else {
                                            Toast.makeText(getActivity(), getResources().getString(R.string.attendance_failed_please_try_again), Toast.LENGTH_SHORT).show();
                                            getAllTasks();
                                        }
                                    }

                                    @Override
                                    public void onFailure(int requestCode) {

                                    }
                                });
                                controller.getTechAttendance(CAM_REQUEST, request);
                            }
                        }
                    } catch (Exception e) {
                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                            String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                            String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                            String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                            AppUtils.sendErrorLogs(e.getMessage(), getClass().getSimpleName(), "getAttendanceDialog", lineNo, userName, DeviceName);
                        }
                    }
                    getAllTasks();
                }
            });
            alertDialog.setIcon(R.mipmap.logo);
            alertDialog.setCancelable(false);
            alertDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onFailure(int requestCode) {
        mFragmentHomeBinding.swipeRefreshLayout.setRefreshing(false);
        mFragmentHomeBinding.emptyTask.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    @Override
    public void onPrimaryMobileClicked(int position) {
        try {
            if (AppUtils.checkConnection(Objects.requireNonNull(getActivity()))) {
                String primaryNumber = mAdapter.getItem(position).getPrimaryMobile();
                String techNumber = "";

                if ((HomeActivity) getActivity() != null) {
                    RealmResults<LoginResponse> LoginRealmModels =
                            BaseApplication.getRealm().where(LoginResponse.class).findAll();
                    if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                        assert LoginRealmModels.get(0) != null;
                        techNumber = LoginRealmModels.get(0).getPhoneNumber();
                    }
                }

                if (techNumber == null || techNumber.length() == 0) {
                    AppUtils.showOkActionAlertBox(getActivity(), getResources().getString(R.string.technicain_number_is_unavailable), (dialogInterface, i) -> dialogInterface.cancel());
                } else if (primaryNumber == null || primaryNumber.trim().length() == 0) {
                    AppUtils.showOkActionAlertBox(getActivity(), getResources().getString(R.string.customer_mobile_number_is_unavailable), (dialogInterface, i) -> dialogInterface.cancel());
                } else {
                    getExotelCalled(primaryNumber, techNumber);
                }
            } else {
                AppUtils.showOkActionAlertBox(getActivity(), getResources().getString(R.string.no_internet_connection), (dialogInterface, i) -> dialogInterface.dismiss());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onAlternateMobileClicked(int position) {
        try {
            if (AppUtils.checkConnection(Objects.requireNonNull(getActivity()))) {
                String secondaryNumber = mAdapter.getItem(position).getAltMobile();
                String techNumber = "";

                if ((HomeActivity) getActivity() != null) {
                    RealmResults<LoginResponse> LoginRealmModels =
                            BaseApplication.getRealm().where(LoginResponse.class).findAll();
                    if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                        assert LoginRealmModels.get(0) != null;
                        techNumber = LoginRealmModels.get(0).getPhoneNumber();
                    }
                }
                if (techNumber == null || techNumber.trim().length() == 0) {

                    AppUtils.showOkActionAlertBox(getActivity(), getResources().getString(R.string.technicain_number_is_unavailable), (dialogInterface, i) -> dialogInterface.cancel());
                } else if (secondaryNumber == null || secondaryNumber.trim().length() == 0) {
                    AppUtils.showOkActionAlertBox(getActivity(), getResources().getString(R.string.customer_alternate_number_is_unavailable), (dialogInterface, i) -> dialogInterface.cancel());
                } else {
                    getExotelCalled(secondaryNumber, techNumber);
                }
            } else {

                AppUtils.showOkActionAlertBox(getActivity(), getResources().getString(R.string.no_internet_connection), (dialogInterface, i) -> dialogInterface.dismiss());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onTelePhoneClicked(int position) {
        try {
            if (AppUtils.checkConnection(Objects.requireNonNull(getActivity()))) {
                String secondaryNumber = mAdapter.getItem(position).getAltMobile();
                String techNumber = "";

                if ((HomeActivity) getActivity() != null) {
                    RealmResults<LoginResponse> LoginRealmModels =
                            BaseApplication.getRealm().where(LoginResponse.class).findAll();
                    if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                        assert LoginRealmModels.get(0) != null;
                        techNumber = LoginRealmModels.get(0).getPhoneNumber();
                    }
                }
                if (techNumber == null || techNumber.trim().length() == 0) {

                    AppUtils.showOkActionAlertBox(getActivity(), getResources().getString(R.string.technicain_number_is_unavailable), (dialogInterface, i) -> dialogInterface.cancel());
                } else if (secondaryNumber == null || secondaryNumber.trim().length() == 0) {
                    AppUtils.showOkActionAlertBox(getActivity(), getResources().getString(R.string.customer_phone_number_is_unnavailable), (dialogInterface, i) -> dialogInterface.cancel());
                } else {
                    getExotelCalled(secondaryNumber, techNumber);
                }
            } else {

                AppUtils.showOkActionAlertBox(getActivity(), getResources().getString(R.string.no_internet_connection), (dialogInterface, i) -> dialogInterface.dismiss());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onTrackLocationIconClicked(int position) {
        try {
            if ((HomeActivity) getActivity() != null) {
                if (((HomeActivity) getActivity()).getmLocation() != null) {
                    if (mAdapter.getItem(position).getCustomerLat() != null && !mAdapter.getItem(position).getCustomerLat().equals("") && !mAdapter.getItem(position).getCustomerLat().equals("0.0")) {
                        double latitude = Double.parseDouble(mAdapter.getItem(position).getCustomerLat());
                        double longitude = Double.parseDouble(mAdapter.getItem(position).getCustomerLong());
                        String uri = "http://maps.google.com/maps?f=d&hl=en&saddr=" + ((HomeActivity) getActivity()).getmLocation().getLatitude() + "," + ((HomeActivity) getActivity()).getmLocation().getLongitude() + "&daddr=" + latitude + "," + longitude;
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                        startActivity(Intent.createChooser(intent, "HiCare Run"));
                    } else {
                        Toast.makeText(getActivity(),  getResources().getString(R.string.customer_location_not_found), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(),  getResources().getString(R.string.customer_location_not_found), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                assert mLoginRealmModels.get(0) != null;
                String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                AppUtils.sendErrorLogs(e.getMessage(), getClass().getSimpleName(), "onTrackLocationIconClicked", lineNo, userName, DeviceName);
            }
        }
    }

    @Override
    public void onTechnicianHelplineClicked(final int position) {
        try {

            if (items.get(position).getDetailVisible()) {
                NetworkCallController controller = new NetworkCallController(this);
                controller.setListner(new NetworkResponseListner() {
                    @Override
                    public void onResponse(int requestCode, Object response) {
                        try {
                            List<JeopardyReasonsList> list = (List<JeopardyReasonsList>) response;
                            dismissProgressDialog();
                            final AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                            LayoutInflater inflater = LayoutInflater.from(getActivity());
                            final View v = inflater.inflate(R.layout.jeopardy_reasons_layout, null, false);
                            final RadioGroup radioGroup = (RadioGroup) v.findViewById(R.id.radiogrp);

                            if (list != null) {
                                for (int i = 0; i < list.size(); i++) {
                                    final RadioButton rbn = new RadioButton(getActivity());
                                    rbn.setId(i);
                                    rbn.setText(list.get(i).getResonName());
                                    rbn.setTextSize(15);
                                    RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                                    params.setMargins(10, 10, 2, 1);
                                    radioGroup.addView(rbn, params);
                                }
                            }

                            builder.setView(v);
                            builder.setCancelable(false);
                            builder.setPositiveButton(getResources().getString(R.string.submit_helpline), (dialogInterface, i) -> {
                                RadioButton radioButton = (RadioButton) v.findViewById(radioGroup.getCheckedRadioButtonId());
                                if (radioGroup.getCheckedRadioButtonId() == -1) {
                                    Toast.makeText(getActivity(), getResources().getString(R.string.please_select_atleast_one_reason), Toast.LENGTH_SHORT).show();
                                    builder.setCancelable(false);
                                } else {
                                    if (items != null) {
                                        Log.i("taskId", items.get(position).getTaskId());
                                        techHelpline(items.get(position).getTaskId(), "Technician Helpline", "Technician_HelpLine"
                                                , radioButton.getText().toString());
                                    }
                                    dialogInterface.dismiss();
                                }
                            });

                            builder.setNegativeButton(getResources().getString(R.string.cancel_helpline), (dialogInterface, i) -> dialogInterface.cancel());
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
                controller.getJeopardyReasons(JEOPARDY_REQUEST, items.get(position).getTaskId());
            } else {
                Toasty.info(Objects.requireNonNull(getActivity()), getResources().getString(R.string.complete_first_job), Toasty.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onResourcePartnerPic(Profile profile) {
        showPartnerId(profile);
    }

    private void getTechDetails(int position) {
        try {
            NetworkCallController controller = new NetworkCallController(this);
            controller.setListner(new NetworkResponseListner() {
                @Override
                public void onResponse(int requestCode, Object data) {
                    Profile response = (Profile) data;
                    showPartnerId(response);
                }

                @Override
                public void onFailure(int requestCode) {
                }
            });
            controller.getTechnicianProfile(TECH_REQ, items.get(position).getHelper_Resource_Id());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showNPSDialog() {
        try {
            if ((HomeActivity) getActivity() != null) {
                RealmResults<LoginResponse> LoginRealmModels =
                        BaseApplication.getRealm().where(LoginResponse.class).findAll();
                if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                    String resourceId = LoginRealmModels.get(0).getUserID();
                    LayoutInflater li = LayoutInflater.from(getActivity());
                    View promptsView = li.inflate(R.layout.layout_nps_dialog, null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                    alertDialogBuilder.setView(promptsView);
                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    final ImageView imgCancel =
                            promptsView.findViewById(R.id.imgCancel);
                    final ImageView imgNps =
                            promptsView.findViewById(R.id.imgNPS);
                    final TextView txtScore =
                            promptsView.findViewById(R.id.txtScore);
                    final TextView txtNps =
                            promptsView.findViewById(R.id.txtNPS);
                    final TextView txtMonth =
                            promptsView.findViewById(R.id.txtMonth);

                    Animation animShake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
                    imgCancel.startAnimation(animShake);

                    imgCancel.setOnClickListener(view -> alertDialog.dismiss());


                    NetworkCallController controller = new NetworkCallController(HomeFragment.this);
                    controller.setListner(new NetworkResponseListner() {
                        @Override
                        public void onResponse(int requestCode, Object response) {
                            NPSData data = (NPSData) response;
                            Picasso.get().load(data.getTechBadge()).into(imgNps);
                            SharedPreferencesUtility.savePrefBoolean(getActivity(), SharedPreferencesUtility.PREF_SHOW_NPS, false);
                            txtNps.setTypeface(Typeface.MONOSPACE, Typeface.NORMAL);
                            txtScore.setTypeface(Typeface.MONOSPACE, Typeface.NORMAL);
                            txtNps.setText(data.getMonthlyNPS());
                            txtScore.setText(data.getPreviousDayNPS());
                            txtMonth.setText(data.getNPS_Month());
                        }

                        @Override
                        public void onFailure(int requestCode) {

                        }
                    });
                    controller.getNPSData(TECH_NPS, resourceId);
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showPartnerId(Profile response) {
        try {
            LayoutInflater li = LayoutInflater.from(getActivity());
            View promptsView = li.inflate(R.layout.layout_partner_dialog, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
            alertDialogBuilder.setView(promptsView);
            final AlertDialog alertDialog = alertDialogBuilder.create();
            final ImageView imgTech =
                    promptsView.findViewById(R.id.imgPartner);
            final TextView txtTechName =
                    promptsView.findViewById(R.id.txtTechName);
            final TextView txtCode =
                    promptsView.findViewById(R.id.txtCode);
            final LinearLayout lnrCode =
                    promptsView.findViewById(R.id.lnr_added);
            final LinearLayout lnrView =
                    promptsView.findViewById(R.id.lnrView);
            final LinearLayout callTech =
                    promptsView.findViewById(R.id.callTech);
            txtTechName.setText(response.getFirstName());
            txtTechName.setTypeface(txtTechName.getTypeface(), Typeface.BOLD);
            if (response.getEmployeeCode() != null) {
                lnrCode.setVisibility(View.VISIBLE);
                txtCode.setText(response.getEmployeeCode());
            } else {
                lnrCode.setVisibility(View.GONE);
            }
            if (response.getProfilePic() != null) {
                String base64 = response.getProfilePic();
                byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                if (base64.length() > 0) {
                    if (decodedByte != null) {
                        imgTech.setImageBitmap(decodedByte);
                    }
                }
            }

            if (!response.getMobile().equals("") && response.getMobile() != null) {
                callTech.setVisibility(View.VISIBLE);
            } else {
                callTech.setVisibility(View.GONE);
            }
            callTech.setOnClickListener(view -> {
                alertDialog.dismiss();
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + response.getMobile()));
                startActivity(callIntent);
            });

            lnrView.setOnClickListener(view -> alertDialog.dismiss());

            alertDialog.show();
            alertDialog.setIcon(R.mipmap.logo);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void techHelpline(String taskId, String jeopardyText, String batchName, String remark) {
        try {
            NetworkCallController controller = new NetworkCallController(this);
            CWFJeopardyRequest request = new CWFJeopardyRequest();
            request.setTaskId(taskId);
            request.setJeopardyText(jeopardyText);
            request.setBatchName(batchName);
            request.setRemark(remark);
            controller.setListner(new NetworkResponseListner() {
                @Override
                public void onResponse(int requestCode, Object data) {
                    CWFJeopardyResponse response = (CWFJeopardyResponse) data;
                    if (response.getSuccess()) {
                        Toasty.success(Objects.requireNonNull(getActivity()), response.getResponseMessage(), Toasty.LENGTH_LONG).show();
                    } else {
                        Toasty.success(Objects.requireNonNull(getActivity()), response.getResponseMessage(), Toasty.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(int requestCode) {

                }
            });
            controller.postCWFJepoardy(CWF_REQUEST, request);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onItemClick(int positon) {
    }

    private void getExotelCalled(String customerNumber, String techNumber) {
        try {
            NetworkCallController controller = new NetworkCallController();
            controller.setListner(new NetworkResponseListner() {
                @Override
                public void onResponse(int requestCode, Object data) {
                    ExotelResponse exotelResponse = (ExotelResponse) data;

                    if (exotelResponse.getSuccess()) {
                        try {
                            String number = exotelResponse.getResponseMessage().concat(exotelResponse.getData());
                            String pause = String.valueOf(PhoneNumberUtils.PAUSE);
                            String postNo = String.valueOf(PhoneNumberUtils.extractPostDialPortion(exotelResponse.getData()));
                            Log.i("num", pause);
                            String comma = Uri.encode(" ");
                            StringBuilder build = new StringBuilder();
                            build.append(exotelResponse.getResponseMessage());
                            build.append(pause);
                            build.append(exotelResponse.getData());
                            Intent callIntent = new Intent(Intent.ACTION_DIAL);
                            callIntent.setData(Uri.parse("tel:" + build.toString()));
                            startActivity(callIntent);
                            Log.i("num", build.toString());

                        } catch (Exception e) {
                            RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                            if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                                assert mLoginRealmModels.get(0) != null;
                                String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                                String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                                String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                                AppUtils.sendErrorLogs(e.getMessage(), getClass().getSimpleName(), "getExotelCalled", lineNo, userName, DeviceName);
                            }
                        }

                    } else {
                        Toast.makeText(getActivity(), "Failed.", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(int requestCode) {

                }
            });
            controller.getExotelCalled(EXOTEL_REQ, customerNumber, techNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
//        timerHandler.removeCallbacks(timerRunnable);
        super.onPause();
    }


    private void apply() {
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.getMenu().getItem(1).setChecked(false);
        navigationView.getMenu().getItem(2).setChecked(false);
        navigationView.getMenu().getItem(3).setChecked(false);
        navigationView.getMenu().getItem(4).setChecked(false);

    }
}
