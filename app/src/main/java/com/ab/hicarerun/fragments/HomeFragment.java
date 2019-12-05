package com.ab.hicarerun.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.activities.HomeActivity;
import com.ab.hicarerun.activities.NewTaskDetailsActivity;
import com.ab.hicarerun.activities.TaskDetailsActivity;
import com.ab.hicarerun.adapter.TaskListAdapter;
import com.ab.hicarerun.databinding.FragmentHomeBinding;
import com.ab.hicarerun.handler.OnCallListItemClickHandler;
import com.ab.hicarerun.handler.OnListItemClickHandler;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.AttachmentModel.GetAttachmentList;
import com.ab.hicarerun.network.models.AttendanceModel.AttendanceRequest;
import com.ab.hicarerun.network.models.ExotelModel.ExotelResponse;
import com.ab.hicarerun.network.models.GeneralModel.GeneralData;
import com.ab.hicarerun.network.models.GeneralModel.IncompleteReason;
import com.ab.hicarerun.network.models.HandShakeModel.ContinueHandShakeResponse;
import com.ab.hicarerun.network.models.JeopardyModel.CWFJeopardyRequest;
import com.ab.hicarerun.network.models.JeopardyModel.CWFJeopardyResponse;
import com.ab.hicarerun.network.models.JeopardyModel.JeopardyReasonsList;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.network.models.TaskModel.TaskListResponse;
import com.ab.hicarerun.network.models.TaskModel.Tasks;
import com.ab.hicarerun.utils.AppUtils;
import com.ab.hicarerun.utils.SharedPreferencesUtility;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import io.realm.RealmResults;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends BaseFragment implements NetworkResponseListner<TaskListResponse>, OnCallListItemClickHandler {
    FragmentHomeBinding mFragmentHomeBinding;
    RecyclerView.LayoutManager layoutManager;
    TaskListAdapter mAdapter;
    final Handler timerHandler = new Handler();
    Runnable timerRunnable;
    private static final int TASKS_REQ = 1000;
    private static final int EXOTEL_REQ = 2000;
    private static final int CALL_REQUEST = 3000;
    private static final int CAM_REQUEST = 4000;
    private static final int JEOPARDY_REQUEST = 5000;
    private static final int CWF_REQUEST = 6000;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentHomeBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        getActivity().setTitle("Home");
        navigationView = getActivity().findViewById(R.id.navigation_view);
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        activityName = getActivity().getClass().getSimpleName();
        apply();

//        timerRunnable = new Runnable() {
//            @Override
//            public void run() {
//                mAdapter.notifyDataSetChanged();
//                timerHandler.postDelayed(this, 60000); //run every minute
//            }
//        };
        return mFragmentHomeBinding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();
//        timerHandler.postDelayed(timerRunnable, 500);
        try {
            isBack = SharedPreferencesUtility.getPrefBoolean(getActivity(), SharedPreferencesUtility.PREF_REFRESH);
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

        mFragmentHomeBinding.recycleView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());

        mFragmentHomeBinding.swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        getAllTasks();
                    }
                });

        mFragmentHomeBinding.recycleView.setLayoutManager(layoutManager);


        mFragmentHomeBinding.swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_dark, android.R.color.holo_blue_light,
                android.R.color.holo_red_dark, android.R.color.holo_red_light,
                android.R.color.holo_green_dark, android.R.color.holo_green_light,
                android.R.color.holo_red_dark, android.R.color.holo_red_light);

        // specify an adapter (see also next example)
        mAdapter = new TaskListAdapter(getActivity());
        mAdapter.setOnCallClickHandler(this);
        mFragmentHomeBinding.recycleView.setAdapter(mAdapter);

        getAllTasks();
        mFragmentHomeBinding.swipeRefreshLayout.setRefreshing(true);
    }


    private void getAllTasks() {
        try {
            SharedPreferencesUtility.savePrefBoolean(getActivity(), SharedPreferencesUtility.PREF_REFRESH, false);
            if ((HomeActivity) getActivity() != null) {
                RealmResults<LoginResponse> LoginRealmModels =
                        BaseApplication.getRealm().where(LoginResponse.class).findAll();
                if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
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
                    IMEI = telephonyManager.getDeviceId();
                    UserId = LoginRealmModels.get(0).getUserID();
                    NetworkCallController controller = new NetworkCallController(this);
                    controller.setListner(this);
                    controller.getTasksList(TASKS_REQ, UserId, IMEI);

                }
            }
        } catch (Exception e) {
            RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
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


            mAdapter.setOnItemClickHandler(new OnListItemClickHandler() {
                @Override
                public void onItemClick(int position) {
                    if (items.get(position).getDetailVisible()) {
                        try {
                            AppUtils.getDataClean();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(getActivity(), TaskDetailsActivity.class);
                        intent.putExtra(TaskDetailsActivity.ARGS_TASKS, items.get(position));
                        startActivity(intent);

//                        Intent intent = new Intent(getActivity(), NewTaskDetailsActivity.class);
//                        intent.putExtra(NewTaskDetailsActivity.ARGS_TASKS, items.get(position));
//                        startActivity(intent);
//                        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);


                    } else {
                        Toasty.info(getActivity(), "Please complete your previous job first.", Toasty.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    private void getAttendanceDialog() {
        LayoutInflater li = LayoutInflater.from(getActivity());

        View promptsView = li.inflate(R.layout.dialog_mark_attendance, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        alertDialogBuilder.setView(promptsView);

        alertDialogBuilder.setTitle("Mark Attendance");

        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();
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

        txt_head.setText("Welcome " + UserName + ", please mark your attendance with the face recognization.");

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                replaceFragment(FaceRecognizationFragment.newInstance(true, ""), "HomeFragment-FaceRecognizationFragment");
            }


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
                                        Toasty.success(getActivity(), "Attendance marked successfully.", Toasty.LENGTH_SHORT).show();
                                        replaceFragment(HomeFragment.newInstance(), "FaceRecognizationFragment-HomeFragment");
                                    } else {
                                        Toast.makeText(getActivity(), "Attendance Failed, please try again.", Toast.LENGTH_SHORT).show();
                                        getAttendanceDialog();
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
    }


    @Override
    public void onFailure(int requestCode) {
        mFragmentHomeBinding.swipeRefreshLayout.setRefreshing(false);
        mFragmentHomeBinding.emptyTask.setVisibility(View.GONE);
    }

    @Override
    public void onPrimaryMobileClicked(int position) {
        if (AppUtils.checkConnection(getActivity())) {
            String primaryNumber = mAdapter.getItem(position).getPrimaryMobile();
            String techNumber = "";

            if ((HomeActivity) getActivity() != null) {
                RealmResults<LoginResponse> LoginRealmModels =
                        BaseApplication.getRealm().where(LoginResponse.class).findAll();
                if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                    techNumber = LoginRealmModels.get(0).getPhoneNumber();
                }
            }

            if (techNumber == null || techNumber.length() == 0) {
                AppUtils.showOkActionAlertBox(getActivity(), "Technician number is unavaible, please contact to Administrator.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
            } else if (primaryNumber == null || primaryNumber.trim().length() == 0) {
                AppUtils.showOkActionAlertBox(getActivity(), "Customer mobile number is unavaible.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
            } else {
                getExotelCalled(primaryNumber, techNumber);
            }
        } else {
            AppUtils.showOkActionAlertBox(getActivity(), "No Internet Connection.", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        }
    }


    @Override
    public void onAlternateMobileClicked(int position) {
        if (AppUtils.checkConnection(getActivity())) {
            String secondaryNumber = mAdapter.getItem(position).getAltMobile();
            String techNumber = "";

            if ((HomeActivity) getActivity() != null) {
                RealmResults<LoginResponse> LoginRealmModels =
                        BaseApplication.getRealm().where(LoginResponse.class).findAll();
                if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                    techNumber = LoginRealmModels.get(0).getPhoneNumber();
                }
            }
            if (techNumber == null || techNumber.trim().length() == 0) {

                AppUtils.showOkActionAlertBox(getActivity(), "Technician number is unavaible, please contact to Administrator.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
            } else if (secondaryNumber == null || secondaryNumber.trim().length() == 0) {
                AppUtils.showOkActionAlertBox(getActivity(), "Customer alt. mobile number is unavaible.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
            } else {
                getExotelCalled(secondaryNumber, techNumber);
            }
        } else {

            AppUtils.showOkActionAlertBox(getActivity(), "No Internet Connection.", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        }
    }

    @Override
    public void onTelePhoneClicked(int position) {

        if (AppUtils.checkConnection(getActivity())) {
            String secondaryNumber = mAdapter.getItem(position).getAltMobile();
            String techNumber = "";

            if ((HomeActivity) getActivity() != null) {
                RealmResults<LoginResponse> LoginRealmModels =
                        BaseApplication.getRealm().where(LoginResponse.class).findAll();
                if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                    techNumber = LoginRealmModels.get(0).getPhoneNumber();
                }
            }
            if (techNumber == null || techNumber.trim().length() == 0) {

                AppUtils.showOkActionAlertBox(getActivity(), "Technician number is unavaible, please contact to Administrator.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
            } else if (secondaryNumber == null || secondaryNumber.trim().length() == 0) {
                AppUtils.showOkActionAlertBox(getActivity(), "Customer phone number is unavaible.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
            } else {
                getExotelCalled(secondaryNumber, techNumber);
            }
        } else {

            AppUtils.showOkActionAlertBox(getActivity(), "No Internet Connection.", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
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
                        Toast.makeText(getActivity(), "Customer location not found!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Customer location not found!", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                AppUtils.sendErrorLogs(e.getMessage(), getClass().getSimpleName(), "onTrackLocationIconClicked", lineNo, userName, DeviceName);
            }
        }

    }

    @Override
    public void onTechnicianHelplineClicked(final int position) {
        if (items.get(position).getDetailVisible()) {
            NetworkCallController controller = new NetworkCallController(this);
            controller.setListner(new NetworkResponseListner() {
                @Override
                public void onResponse(int requestCode, Object response) {
                    try {
                        List<JeopardyReasonsList> list = (List<JeopardyReasonsList>) response;
                        dismissProgressDialog();
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                RadioButton radioButton = (RadioButton) v.findViewById(radioGroup.getCheckedRadioButtonId());
                                if (radioGroup.getCheckedRadioButtonId() == -1) {
                                    Toast.makeText(getActivity(), "Please select at least one reason...", Toast.LENGTH_SHORT).show();
                                    builder.setCancelable(false);
                                } else {
                                    if (items != null) {
                                        Log.i("taskId", items.get(position).getTaskId());
                                        techHelpline(items.get(position).getTaskId(), "Technician Helpline", "Technician_HelpLine"
                                                , radioButton.getText().toString());
                                    }
                                    dialogInterface.dismiss();
                                }
                            }
                        });

                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();

                            }
                        });
                        final AlertDialog dialog = builder.create();
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;

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
            controller.getJeopardyReasons(JEOPARDY_REQUEST);
        } else {
            Toasty.info(getActivity(), "Please complete your previous job first.", Toasty.LENGTH_SHORT).show();
        }

    }

    private void techHelpline(String taskId, String jeopardyText, String batchName, String remark) {
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
                    Toasty.success(getActivity(), response.getResponseMessage(), Toasty.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), response.getResponseMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int requestCode) {

            }
        });
        controller.postCWFJepoardy(CWF_REQUEST, request);
    }

    @Override
    public void onItemClick(int positon) {

    }

    private void getExotelCalled(String customerNumber, String techNumber) {
        NetworkCallController controller = new NetworkCallController();
        controller.setListner(new NetworkResponseListner() {
            @Override
            public void onResponse(int requestCode, Object data) {
                ExotelResponse exotelResponse = (ExotelResponse) data;

                if (exotelResponse.getSuccess()) {
                    try {
                        String number = exotelResponse.getResponseMessage() + "," + exotelResponse.getData();

//                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel:", number, null));
//                            getActivity().startActivity(intent);
//                        }
//
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            Intent callIntent = new Intent(Intent.ACTION_DIAL);
//                            callIntent.setData(Uri.parse("tel:" + number));
//                            getActivity().startActivity(callIntent);
//                        }

                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:" + number));
                        startActivity(callIntent);

                    } catch (Exception e) {
                        RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
                        if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
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
    }

//    class PhoneCallListener extends PhoneStateListener {
//
//        private boolean isPhoneCalling = false;
//
//        String LOG_TAG = "RENEWAL_CALL";
//
//        @Override
//        public void onCallStateChanged(int state, String incomingNumber) {
//
//            try {
//                if (TelephonyManager.CALL_STATE_RINGING == state) {
//                    // phone ringing
//                    Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
//                }
//                if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
//                    Log.i(LOG_TAG, "OFFHOOK");
//                    isPhoneCalling = true;
//                }
//                if (TelephonyManager.CALL_STATE_IDLE == state) {
//                    Log.i(LOG_TAG, "IDLE");
//                    if (isPhoneCalling) {
//                        Log.i(LOG_TAG, "restart app");
//                        Intent i = null;
//                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//                            i = Objects.requireNonNull(getActivity()).getPackageManager()
//                                    .getLaunchIntentForPackage(
//                                            getActivity().getPackageName());
//                            if (i != null) {
//                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                getActivity().startActivity(i);
//                            } else {
//                                Toast.makeText(getActivity(), "Not able to make call!", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            Toast.makeText(getActivity(), "Not able to make call!", Toast.LENGTH_SHORT).show();
//                        }
//
//                        isPhoneCalling = false;
//                    }
//
//
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//
//        }
//    }

    @Override
    public void onPause() {
        timerHandler.removeCallbacks(timerRunnable);
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
