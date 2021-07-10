package com.ab.hicarerun.fragments;


import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.BaseFragment;
import com.ab.hicarerun.R;
import com.ab.hicarerun.activities.HomeActivity;
import com.ab.hicarerun.activities.NewTaskDetailsActivity;
import com.ab.hicarerun.adapter.AssessmentReportAdapter;
import com.ab.hicarerun.adapter.ChemicalDialogAdapter;
import com.ab.hicarerun.adapter.ResourceCheckListAdapter;
import com.ab.hicarerun.adapter.TaskListAdapter;
import com.ab.hicarerun.databinding.FragmentHomeBinding;
import com.ab.hicarerun.handler.CovidCheckListHandler;
import com.ab.hicarerun.handler.OnCallListItemClickHandler;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.AttachmentModel.MSTAttachment;
import com.ab.hicarerun.network.models.AttendanceModel.AttendanceRequest;
import com.ab.hicarerun.network.models.ChemicalModel.Chemicals;
import com.ab.hicarerun.network.models.DialingModel.DialingResponse;
import com.ab.hicarerun.network.models.ExotelModel.ExotelResponse;
import com.ab.hicarerun.network.models.HandShakeModel.ContinueHandShakeResponse;
import com.ab.hicarerun.network.models.JeopardyModel.CWFJeopardyRequest;
import com.ab.hicarerun.network.models.JeopardyModel.CWFJeopardyResponse;
import com.ab.hicarerun.network.models.JeopardyModel.JeopardyReasonsList;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.network.models.NPSModel.NPSData;
import com.ab.hicarerun.network.models.ProfileModel.Profile;
import com.ab.hicarerun.network.models.SelfAssessModel.AssessmentReport;
import com.ab.hicarerun.network.models.SelfAssessModel.ResourceCheckList;
import com.ab.hicarerun.network.models.SelfAssessModel.SelfAssessmentRequest;
import com.ab.hicarerun.network.models.SelfAssessModel.SelfAssessmentResponse;
import com.ab.hicarerun.network.models.TaskModel.TaskChemicalList;
import com.ab.hicarerun.network.models.TaskModel.TaskListResponse;
import com.ab.hicarerun.network.models.TaskModel.Tasks;
import com.ab.hicarerun.utils.AppUtils;
import com.ab.hicarerun.utils.LocaleHelper;
import com.ab.hicarerun.utils.SharedPreferencesUtility;
import com.google.android.material.navigation.NavigationView;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.review.testing.FakeReviewManager;
import com.google.android.play.core.tasks.Task;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import io.realm.RealmResults;

public class HomeFragment extends BaseFragment implements NetworkResponseListner<TaskListResponse>, OnCallListItemClickHandler {
    FragmentHomeBinding mFragmentHomeBinding;
    RecyclerView.LayoutManager layoutManager;
    TaskListAdapter mAdapter;
    //    final Handler timerHandler = new Handler();
    private static final String COVID_CHECK = "COVID_CHECK";

    private static final int TASKS_REQ = 1000;
    private static final int EXOTEL_REQ = 2000;
    private static final int EXOTEL_REQ_V2 = 9000;
    private static final int CALL_REQUEST = 3000;
    private static final int CAM_REQUEST = 4000;
    private static final int JEOPARDY_REQUEST = 5000;
    private static final int CWF_REQUEST = 6000;
    private static final int TECH_REQ = 7000;
    private static final int TECH_NPS = 8000;
    private static final int ASSESS_REQUEST = 9000;
    private static final int REQ_PROFILE = 31000;

    private static final int SAVE_ASSESSMENT = 9000;
    private boolean isBack = false;
    private boolean isSkip = false;
    private Integer pageNumber = 1;
    private String UserId = "", IMEI = "", UserName = "";
    private String activityName = "";
    private String methodName = "";
    private NavigationView navigationView = null;
    private HashMap<Integer, Boolean> checkMap = new HashMap<>();
    private HashMap<Integer, String> tempMap = new HashMap<>();

    private String taskId = "";
    List<Tasks> items = null;
    RealmResults<LoginResponse> LoginRealmModels = null;
    private boolean isParam = false;
    //    private byte[] bitUser = null;
    private static final String ARG_USER = "ARG_USER";
    AlertDialog alertDialog = null;
    private boolean isShowNPS = false;
    private boolean isResourceSaved = false;
    private ResourceCheckListAdapter mCheckListAdapter;
    private AssessmentReportAdapter mAssessAdapter;
    private List<SelfAssessmentRequest> checkList = null;
    private List<Boolean> isCheckList = null;
    private List<ResourceCheckList> ResList = null;
    ReviewManager manager;
    private ReviewInfo reviewInfo;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(/*byte[] bitUser*/) {
        Bundle args = new Bundle();
//        args.putByteArray(ARG_USER, bitUser);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

//    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            // Get extra data included in the Intent
//            String message = intent.getStringExtra("message");
//            if (message.equalsIgnoreCase("recieved")) {
//                showCovidCheckList();
//                getAllTasks();
//                showResourceCheckList();
//            }
//
//        }
//    };

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mFragmentHomeBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
//        getActivity().setTitle("Home");
        navigationView = getActivity().findViewById(R.id.navigation_view);
        LinearLayout toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);
        LinearLayout tool = getActivity().findViewById(R.id.customToolbar);
        RelativeLayout relBottom = getActivity().findViewById(R.id.relBottom);
        RelativeLayout relCoin = getActivity().findViewById(R.id.relCoin);
        tool.setVisibility(View.GONE);
        relBottom.setVisibility(View.VISIBLE);
        relCoin.setVisibility(View.VISIBLE);
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
        try {
            AppUtils.IS_ACTIVITY_THERE = false;
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
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray typedArray = getActivity().obtainStyledAttributes(attrs);
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
//        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
//                new IntentFilter(COVID_CHECK));
        getAllTasks();
        launchMarket();
        isShowNPS = SharedPreferencesUtility.getPrefBoolean(getActivity(), SharedPreferencesUtility.PREF_SHOW_NPS);

        isResourceSaved = SharedPreferencesUtility.getPrefBoolean(getActivity(), SharedPreferencesUtility.PREF_RESOURCE_SAVED);

//        if (isShowNPS)
//            showNPSDialog();
        mFragmentHomeBinding.swipeRefreshLayout.setRefreshing(true);
        if (isResourceSaved) {
            showResourceCheckList();
//            showCovidCheckList();
        }

        mFragmentHomeBinding.lnrAssess.setOnClickListener(v -> showAssessmentReport());
    }

    private void launchMarket() {
        manager = ReviewManagerFactory.create(getActivity());
//        manager = new FakeReviewManager(getActivity());
        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                reviewInfo = task.getResult();
                Task<Void> flow = manager.launchReviewFlow(getActivity(), reviewInfo);
                flow.addOnCompleteListener(taskdone -> {
                    // This is the next follow of your app
                });
            }
        });
    }

    private void showCovidCheckList() {
        try {
            CovidCheckFragment dialog = CovidCheckFragment.newInstance();
            dialog.show(getActivity().getSupportFragmentManager(), "check_up");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
    }

    private void showAssessmentReport() {
        try {
            if ((HomeActivity) getActivity() != null) {
                RealmResults<LoginResponse> LoginRealmModels =
                        BaseApplication.getRealm().where(LoginResponse.class).findAll();
                if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                    String resourceId = LoginRealmModels.get(0).getUserID();
                    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.layout_assessment_report, null);
                    dialogBuilder.setView(dialogView);
                    final AlertDialog alertDialog = dialogBuilder.create();
                    final RecyclerView recycleView = (RecyclerView) dialogView.findViewById(R.id.recycleView);
                    final Button btnOk = (Button) dialogView.findViewById(R.id.btnOk);
                    final CircleImageView imgUser = (CircleImageView) dialogView.findViewById(R.id.imgUser);
                    final TextView txtTitle = (TextView) dialogView.findViewById(R.id.txtTitle);
                    final TextView txtUpdatedOn = (TextView) dialogView.findViewById(R.id.txtUpdatedOn);
//                    Bitmap bitmap = BitmapFactory.decodeByteArray(bitUser, 0, bitUser.length);
//                    imgUser.setImageBitmap(bitmap);

                    NetworkCallController controller = new NetworkCallController();
                    controller.setListner(new NetworkResponseListner() {
                        @Override
                        public void onResponse(int requestCode, Object data) {
                            Profile response = (Profile) data;
                            if (response.getProfilePic() != null) {
                                String base64 = response.getProfilePic();
                                byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                if (base64.length() > 0) {
                                    imgUser.setImageBitmap(decodedByte);
                                }
                            }
                        }

                        @Override
                        public void onFailure(int requestCode) {

                        }
                    });
                    controller.getTechnicianProfile(REQ_PROFILE, resourceId);

                    txtTitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

                    RecyclerView.LayoutManager lm = new LinearLayoutManager(getActivity());
                    recycleView.setLayoutManager(lm);
                    recycleView.setItemAnimator(new DefaultItemAnimator());
                    mAssessAdapter = new AssessmentReportAdapter(getActivity());
                    recycleView.setAdapter(mAssessAdapter);

                    NetworkCallController controller1 = new NetworkCallController(HomeFragment.this);
                    controller1.setListner(new NetworkResponseListner<List<AssessmentReport>>() {
                        @Override
                        public void onResponse(int requestCode, List<AssessmentReport> items) {
                            try {
                                if (items != null) {
                                    txtUpdatedOn.setText("Updated " + AppUtils.covertTimeToText(items.get(0).getCreatedOn()));
                                    if (pageNumber == 1 && items.size() > 0) {
                                        mAssessAdapter.setData(items);
                                        mAssessAdapter.notifyDataSetChanged();
                                        alertDialog.show();

                                    } else if (items.size() > 0) {
                                        mAssessAdapter.addData(items);
                                        mAssessAdapter.notifyDataSetChanged();
                                        alertDialog.show();
                                    } else {
                                        pageNumber--;
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int requestCode) {

                        }
                    });
                    controller1.getAssessmentResponse(ASSESS_REQUEST, resourceId, LocaleHelper.getLanguage(getActivity()));
                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });

                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    alertDialog.setCanceledOnTouchOutside(false);

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showResourceCheckList() {
        try {
            if ((HomeActivity) getActivity() != null) {
                RealmResults<LoginResponse> LoginRealmModels =
                        BaseApplication.getRealm().where(LoginResponse.class).findAll();
                if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                    String resourceId = LoginRealmModels.get(0).getUserID();
                    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.layout_self_assess_list, null);
                    dialogBuilder.setView(dialogView);
                    final AlertDialog alertDialog = dialogBuilder.create();
                    final RecyclerView recycleView = (RecyclerView) dialogView.findViewById(R.id.recycleView);
                    final Button btnSave = (Button) dialogView.findViewById(R.id.btnSave);
                    final TextView txtTitle = (TextView) dialogView.findViewById(R.id.txtTitle);

                    txtTitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

                    RecyclerView.LayoutManager lm = new LinearLayoutManager(getActivity());
                    recycleView.setLayoutManager(lm);
                    recycleView.setItemAnimator(new DefaultItemAnimator());
                    mCheckListAdapter = new ResourceCheckListAdapter(getActivity(), (position, isChecked, temperature) -> {
                        try {
                            if (checkMap != null && tempMap != null) {
                                checkMap.put(position, isChecked);
                                tempMap.put(position, temperature);
                                Log.i("MAP_VALUE", Objects.requireNonNull(tempMap.get(position)));
                                checkList = new ArrayList<>();
                                isCheckList = new ArrayList<>();
                                for (int i = 0; i < mCheckListAdapter.getItemCount(); i++) {
                                    SelfAssessmentRequest checkModel = new SelfAssessmentRequest();
                                    checkModel.setOptionId(mCheckListAdapter.getItem(i).getId());
                                    checkModel.setOptionTitle(mCheckListAdapter.getItem(i).getTitle());
                                    checkModel.setCreatedBy(resourceId);
                                    checkModel.setDisplayOptionTitle(mCheckListAdapter.getItem(i).getDisplayTitle());
                                    if (checkMap.containsKey(i)) {
                                        checkModel.setIsSelected(checkMap.get(i));
                                    } else {
                                        checkModel.setIsSelected(false);
                                    }

                                    if (tempMap.containsKey(i)) {
                                        checkModel.setOptionText(tempMap.get(i));
                                    } else {
                                        checkModel.setOptionText("");
                                    }
                                    checkList.add(checkModel);
                                    isCheckList.add(checkList.get(i).getIsSelected());
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    });
                    recycleView.setAdapter(mCheckListAdapter);
                    NetworkCallController controller = new NetworkCallController(HomeFragment.this);
                    controller.setListner(new NetworkResponseListner<List<ResourceCheckList>>() {
                        @Override
                        public void onResponse(int requestCode, List<ResourceCheckList> items) {
                            try {

                                if (items != null) {
                                    ResList = new ArrayList<>();
                                    ResList = items;
                                    if (pageNumber == 1 && items.size() > 0) {
                                        mCheckListAdapter.setData(items);
                                        mCheckListAdapter.notifyDataSetChanged();
                                        if (mCheckListAdapter.getItemCount() > 0) {
                                            alertDialog.show();
                                            alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                                        }
                                    } else if (items.size() > 0) {
                                        mCheckListAdapter.addData(items);
                                        mCheckListAdapter.notifyDataSetChanged();
                                        if (mCheckListAdapter.getItemCount() > 0) {
                                            alertDialog.show();
                                            alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                                        }
                                    } else {
                                        pageNumber--;
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int requestCode) {
                        }
                    });
                    controller.getResourceCheckList(ASSESS_REQUEST, resourceId, LocaleHelper.getLanguage(getActivity()));
                    btnSave.setOnClickListener(v -> {
                        if (isCheckList != null && isListChecked(isCheckList) && checkList != null) {
                            if (!checkList.get(0).getOptionText().equals("")) {
                                double temperature = Double.parseDouble(checkList.get(0).getOptionText());
                                if (temperature >= 97 && temperature <= 99) {
                                    NetworkCallController controller1 = new NetworkCallController(HomeFragment.this);
                                    controller1.setListner(new NetworkResponseListner<SelfAssessmentResponse>() {
                                        @Override
                                        public void onResponse(int requestCode, SelfAssessmentResponse response) {
                                            if (response.getIsSuccess()) {
                                                alertDialog.dismiss();
                                                SharedPreferencesUtility.savePrefBoolean(getActivity(), SharedPreferencesUtility.PREF_RESOURCE_SAVED, false);
                                                Toasty.success(getActivity(), response.getData(), Toast.LENGTH_LONG).show();
                                            } else {
                                                Toasty.error(getActivity(), response.getErrorMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(int requestCode) {

                                        }
                                    });
                                    controller1.saveSelfAssessment(SAVE_ASSESSMENT, checkList);
                                } else {
                                    Toasty.error(getActivity(), "Enter correct temperature in °F", Toasty.LENGTH_LONG).show();
                                }
                            } else {
                                Toasty.error(getActivity(), "Please enter your temperature in °F!", Toasty.LENGTH_SHORT).show();
                            }
                        } else {
                            Toasty.error(getActivity(), "All fields are mandatory.", Toasty.LENGTH_SHORT).show();
                        }
                    });
                    dialogBuilder.setCancelable(false);
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//                  alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.setCancelable(false);

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isListChecked(List<Boolean> isCheckList) {
        for (Boolean isChecked : isCheckList) {
            if (!isChecked) {
                return false;
            }
        }
        return true;
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
                    try {
                        NetworkCallController controller = new NetworkCallController(this);
                        controller.setListner(this);
                        controller.getTasksList(TASKS_REQ, UserId, IMEI);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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
//        getAttendanceDialog();
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
            try {
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
            } catch (Exception e) {
                e.printStackTrace();
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
                        intent.putExtra(NewTaskDetailsActivity.ARGS_RESOURCE, UserId);
                        intent.putExtra(NewTaskDetailsActivity.ARGS_COMBINED_TASKS, items.get(position).getCombinedTask());
                        intent.putExtra(NewTaskDetailsActivity.ARGS_COMBINED_TASKS_ID, items.get(position).getCombinedTaskId());
                        intent.putExtra(NewTaskDetailsActivity.ARGS_COMBINED_ORDER, items.get(position).getCombinedOrderNumber());
                        intent.putExtra(NewTaskDetailsActivity.ARGS_COMBINED_TYPE, items.get(position).getCombinedServiceType());
                        intent.putExtra(NewTaskDetailsActivity.ARGS_NEXT_TASK, items.get(position).getNext_Task_Id());
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

            txt_head.setText(getResources().getString(R.string.welcome_attendance) + " " + UserName + getResources().getString(R.string.please_mark_your_attendance_with_the_face_recognition));

            btn_send.setOnClickListener(v -> {
                alertDialog.dismiss();
//                SharedPreferencesUtility.savePrefBoolean(getActivity(), SharedPreferencesUtility.PREF_RESOURCE_SAVED, true);
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
                                        SelfAssessmentResponse response = (SelfAssessmentResponse) data;
                                        if (response.getIsSuccess()) {
                                            Toasty.success(getActivity(), getResources().getString(R.string.attendance_marked_successfully), Toasty.LENGTH_SHORT).show();
                                            alertDialog.dismiss();
                                            getAllTasks();
                                            if (response.getParam1()) {
                                                showResourceCheckList();
//                                                showCovidCheckList();
                                            } else {
                                                showNPSDialog();
                                            }


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
//        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mMessageReceiver);
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
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
                        getCallTriggered(primaryNumber, techNumber);
                    } else {
                        getExotelCalled(primaryNumber, techNumber);
                    }
                }
            } else {
                AppUtils.showOkActionAlertBox(getActivity(), getResources().getString(R.string.no_internet_connection), (dialogInterface, i) -> dialogInterface.dismiss());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void getCallTriggered(String custNo, String techNo) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        mBuilder.setTitle("Trigger Call");
        mBuilder.setIcon(R.mipmap.logo);
        mBuilder.setMessage("Do you want to trigger call?");
        mBuilder.setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
            dialogInterface.dismiss();
            NetworkCallController controller = new NetworkCallController(this);
            controller.setListner(new NetworkResponseListner<DialingResponse>() {
                @Override
                public void onResponse(int requestCode, DialingResponse response) {
                    if (response.getIsSuccess()) {
                        Toasty.success(getActivity(), response.getData(), Toasty.LENGTH_LONG).show();
                    } else {
                        Toasty.error(getActivity(), response.getData(), Toasty.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(int requestCode) {

                }
            });
            controller.getExotelCalledV2(EXOTEL_REQ_V2, custNo, techNo);
        });
        mBuilder.setNegativeButton(getString(R.string.no), (dialogInterface, i) -> dialogInterface.dismiss());
        mBuilder.setCancelable(true);
        mBuilder.create().show();
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
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                        getCallTriggered(secondaryNumber, techNumber);
                    } else {
                        getExotelCalled(secondaryNumber, techNumber);
                    }

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
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                        getCallTriggered(secondaryNumber, techNumber);
                    } else {
                        getExotelCalled(secondaryNumber, techNumber);
                    }
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
                        Toast.makeText(getActivity(), getResources().getString(R.string.customer_location_not_found), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.customer_location_not_found), Toast.LENGTH_SHORT).show();
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
//                            HashMap<String, String> lanMap = new HashMap<>();
                            dismissProgressDialog();
                            final AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                            LayoutInflater inflater = LayoutInflater.from(getActivity());
                            final View v = inflater.inflate(R.layout.jeopardy_reasons_layout, null, false);
                            final RadioGroup radioGroup = (RadioGroup) v.findViewById(R.id.radiogrp);

                            if (list != null && list.size() > 0) {
                                for (int i = 0; i < list.size(); i++) {
//                                    lanMap.put(list.get(i).getResonName(), list.get(position).getDisplayName());
                                    final RadioButton rbn = new RadioButton(getActivity());
                                    rbn.setId(i);
                                    rbn.setText(list.get(i).getDisplayName());
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
                                        Log.i("reason_name", list.get(radioGroup.getCheckedRadioButtonId()).getResonName());
                                        techHelpline(items.get(position).getTaskId(), "Technician Helpline", "Technician_HelpLine"
                                                , /*radioButton.getText().toString()*/
                                                list.get(radioGroup.getCheckedRadioButtonId()).getResonName());
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
                controller.getJeopardyReasons(JEOPARDY_REQUEST, items.get(position).getTaskId(), LocaleHelper.getLanguage(getActivity()));
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
