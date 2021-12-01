package com.ab.hicarerun.activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ab.hicarerun.BaseActivity;
import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.BuildConfig;
import com.ab.hicarerun.R;
import com.ab.hicarerun.adapter.CheckListParentAdapter;
import com.ab.hicarerun.adapter.SurveyAdapter;
import com.ab.hicarerun.adapter.TaskViewPagerAdapter;
import com.ab.hicarerun.adapter.tms.TmsChipsAdapter;
import com.ab.hicarerun.adapter.tms.TmsQuestionsParentAdapter;
import com.ab.hicarerun.databinding.ActivityNewTaskDetailsBinding;
import com.ab.hicarerun.fragments.ChemicalActivityFragment;
import com.ab.hicarerun.fragments.ChemicalActualFragment;
import com.ab.hicarerun.fragments.ChemicalInfoFragment;
import com.ab.hicarerun.fragments.ConsultationFragment;
import com.ab.hicarerun.fragments.ReferralFragment;
import com.ab.hicarerun.fragments.ServiceActivityFragment;
import com.ab.hicarerun.fragments.ServiceInfoFragment;
import com.ab.hicarerun.fragments.ServiceUnitFragment;
import com.ab.hicarerun.fragments.SignatureInfoFragment;
import com.ab.hicarerun.fragments.SignatureMSTInfoFragment;
import com.ab.hicarerun.fragments.tms.TmsUtils;
import com.ab.hicarerun.handler.OnSaveEventHandler;
import com.ab.hicarerun.handler.UserTaskDetailsClickListener;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.CheckListModel.CheckListResponse;
import com.ab.hicarerun.network.models.CheckListModel.SaveCheckListRequest;
import com.ab.hicarerun.network.models.CheckListModel.UploadCheckListData;
import com.ab.hicarerun.network.models.CheckListModel.UploadCheckListRequest;
import com.ab.hicarerun.network.models.GeneralModel.GeneralResponse;
import com.ab.hicarerun.network.models.GeneralModel.TaskCheckList;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.network.models.TaskModel.TaskChemicalList;
import com.ab.hicarerun.network.models.TaskModel.UpdateTaskResponse;
import com.ab.hicarerun.network.models.TaskModel.UpdateTasksRequest;
import com.ab.hicarerun.network.models.TmsModel.QuestionList;
import com.ab.hicarerun.network.models.TmsModel.QuestionTabList;
import com.ab.hicarerun.utils.AppUtils;
import com.ab.hicarerun.utils.GPSUtils;
import com.ab.hicarerun.utils.LocaleHelper;
import com.ab.hicarerun.utils.SharedPreferencesUtility;
import com.ab.hicarerun.utils.notifications.ScratchRelativeLayout;
import com.clock.scratch.ScratchView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import es.dmoral.toasty.Toasty;
import hyogeun.github.com.colorratingbarlib.ColorRatingBar;
import io.realm.RealmResults;

import static com.ab.hicarerun.BaseApplication.getRealm;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.EXPANDED;

public class NewTaskDetailsActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener, OnMapReadyCallback, UserTaskDetailsClickListener, OnSaveEventHandler {
    ActivityNewTaskDetailsBinding mActivityNewTaskDetailsBinding;
    private OnAboutDataReceivedListener mAboutDataListener;
    private static final int TASK_BY_ID_REQUEST = 1000;
    private static final int UPDATE_REQUEST = 2000;
    private static final int SAVE_CHECK_LIST = 3000;
    private static final int UPLOAD_REQ = 4000;
    static final int REQUEST_TAKE_PHOTO = 1;
    private String selectedImagePath = "";
    private boolean isUpiPaymentNotDone = false;
    private int checkPosition = 0;
    private Bitmap bitmap;
    int height = 100;
    int width = 100;
    private ProgressDialog progress;
    SupportMapFragment mapFragment;
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mGoogleMap;
    private LocationManager mLocationManager;
    public static final String ARGS_TASKS = "ARGS_TASKS";
    public static final String ARGS_RESOURCE = "ARGS_RESOURCE";
    public static final String ARGS_COMBINED_TASKS_ID = "ARGS_COMBINED_TASKS_ID";
    public static final String ARGS_COMBINED_TASKS = "ARGS_COMBINED_TASKS";
    public static final String ARGS_COMBINED_TYPE = "ARGS_COMBINED_TYPE";
    public static final String ARGS_COMBINED_ORDER = "ARGS_COMBINED_ORDER";
    public static final String ARGS_LATITUDE = "ARGS_LATITUDE";
    public static final String ARGS_LONGITUDE = "ARGS_LONGITUDE";
    public static final String ARGS_NAME = "ARGS_NAME";
    public static final String ARGS_NEXT_TASK = "ARGS_NEXT_TASK";
    private boolean isFinalSave = false;
    private static final int REQUEST_CODE = 1234;
    private boolean mPermissions;


    ServiceInfoFragment.ServiceInfoListener mCallback = new ServiceInfoFragment.ServiceInfoListener() {
        @Override
        public void onPostJobButtonClicked() {
            isFinalSave = false;
            showCompletionDialog();
        }

        @Override
        public void onTmsPostJobButtonClicked() {
            isFinalSave = false;
            showTmsCompletionDialog();
        }
    };
    public static final String LAT_LONG = "LAT_LONG";
    private static final String TAG = "NewTaskDetailsActivity";
    private Location mLocation;
    private long UPDATE_INTERVAL = 5 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 5000; /* 2 sec */
    private LocationRequest mLocationRequest;
    private LocationManager locationManager;
    private Marker mCurrLocationMarker;
    private Marker mCustomerMarker;
    private TaskViewPagerAdapter mAdapter;
    private CheckListParentAdapter mCheckAdapter;
    private TmsQuestionsParentAdapter mCheckTmsAdapter;

    private String userId = "";
    private String sta = "";
    private boolean isConsultationRequired = false;
    private String Renewal_Type = "";
    private String Renewal_Order_No = "";
    private Boolean isIncentiveEnable = false;
    private int Incentive = 0;
    private boolean isTechnicianFeedbackEnable = false;
    private int TechnicianRating = 0;
    private int referralDiscount = 0;
    private Double Lat = 0.0;
    private Double Lon = 0.0;
    private boolean isGPS = false;

    private String Status = "", Payment_Mode = "", Amount_Collected = "", Amount_To_Collected = "", Actual_Size = "", Standard_Size = "", Feedback_Code = "", signatory = "", Signature = "", Duration = "", OnsiteOTP = "";
    private boolean isGeneralChanged = false;
    private boolean isChemicalChanged = false;
    private boolean isChemicalVerified = false;
    private boolean isPaymentChanged = false;
    private boolean isSignatureChanged = false;
    private boolean isFeedbackRequired = false;
    private boolean isCardRequired = false;
    private boolean isOTPValidated = false;
    private boolean isOTPRequired = false;
    private boolean isOnsiteOtpValidated = false;
    private boolean isOnsiteOtpRequired = false;
    private boolean isSignatureValidated = false;
    private boolean isAmountCollectedRequired = false;
    private boolean isAmountCollectedEquals = false;
    private boolean isBankNameRequired = false;
    private boolean isChequeDateRequired = false;
    private boolean isChequeNumberRequired = false;
    private boolean isInvalidChequeNumber = false;
    private boolean isChequeImageRequired = false;
    private boolean isEarlyCompletion = false;
    private boolean isIncompleteReason = false;
    private boolean isAttachment = false;
    private boolean isActualChemicalChanged = false;
    private boolean isWorkTypeNotChecked = false;
    private boolean isPaymentOtpRequired = false;
    private boolean isPaymentOtpValidated = false;
    private boolean isPaymentModeNotChanged = false;
    private boolean isOnsiteImageRequired = false;
    private boolean isPostJobCompletionDone = false;
    private boolean isQRThere = false;
    private String bankName = "";
    private String NotRenewalReason = "";
    private String chequeNumber = "";
    private String chequeDate = "";
    private String chequeImage = "";
    private String incompleteReason = "";
    private String flushoutReason = "";
    private String SRAppointmentType = "";
    private HashMap<Integer, String> map = new HashMap<>();
    private HashMap<Integer, String> mMap = null;
    private List<TaskChemicalList> ChemReqList = null;
    private int Rate = 0;
    private Circle mCircle;
    private Toasty mToastToShow;
    private double mCircleRadius = 150;
    //    private Bitmap bitUser;
    private String onSiteMaskImage = "";
    File mPhotoFile;
    //    private byte[] bitUser = null;
    LatLngBounds.Builder builder;
    CameraUpdate cu;
    private String taskId = "";
    private String resourceId = "";
    private String combinedTaskId = "";
    private Boolean isCombinedTasks = false;
    private String combinedOrderId = "";
    private String orderId = "";
    private int sequenceNo = 0;
    private boolean isActivityThere = false;
    private String combinedTaskTypes = "";
    private String nextTaskId = "";
    private Double customerLatitude = 0.0;
    private Double customerLongitude = 0.0;
    private String accountName = "";
    private String technicianMobileNo = "";
    private String mActualAmountToCollect = "";
    private String mOnsiteImagePath = "";
    private boolean showSignature = false;
    private List<TaskCheckList> mTaskCheckList = null;
    private List<TaskCheckList> mOnsiteCheckList = new ArrayList<>();
    private List<SaveCheckListRequest> mSaveList = new ArrayList<>();
    HashSet<SaveCheckListRequest> saveHashSet = null;
    private String assignmentStartDate = "";
    private String assignmentStartTime = "";
    private String assignmentEndTime = "";
    private String referralQuestion = "";
    public static boolean referralChanged = false;
    public static boolean referralChecked = false;
    public static String isCompleted = "no";
    public static String referralInstructions = "";
    String currChip = "";
    int currPos = 0;
    int qId = -1;
    int cBy = -1;
    boolean isFromTms = false;
    boolean isLast = false;
    public String typeName = "";
    private ImageUploaded imageUploaded = null;

    //   @Override
    //  protected void attachBaseContext(Context base) {
    //     super.attachBaseContext(LocaleHelper.onAttach(base, LocaleHelper.getLanguage(base)));
    // }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityNewTaskDetailsBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_new_task_details);

        referralChecked = false;
        referralChanged = false;
        referralInstructions = "";
        mActivityNewTaskDetailsBinding.setHandler(this);
        progress = new ProgressDialog(this, R.style.TransparentProgressDialog);
        progress.setCancelable(false);
        taskId = getIntent().getStringExtra(ARGS_TASKS);
        resourceId = getIntent().getStringExtra(ARGS_RESOURCE);
        AppUtils.getConsAndInsData(taskId/*"a239D000000YajWQAS"*/, resourceId/*"a1r9D000000OUNqQAO"*/, LocaleHelper.getLanguage(NewTaskDetailsActivity.this));
        //AppUtils.getTmsQuestions("23213");
        combinedTaskId = getIntent().getStringExtra(ARGS_COMBINED_TASKS_ID);
        isCombinedTasks = getIntent().getBooleanExtra(ARGS_COMBINED_TASKS, false);
        combinedOrderId = getIntent().getStringExtra(ARGS_COMBINED_ORDER);
        combinedTaskTypes = getIntent().getStringExtra(ARGS_COMBINED_TYPE);
        nextTaskId = getIntent().getStringExtra(ARGS_NEXT_TASK);
        new GPSUtils(this).turnGPSOn(isGPSEnable -> {
            try {
                // turn on GPS
                if (isGPSEnable) {
                    progress.show();
                    getTaskDetailsById();
                } else {
                    isGPS = isGPSEnable;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });


//        setViewPagerView();
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray typedArray = obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        mActivityNewTaskDetailsBinding.save.setBackgroundResource(backgroundResource);
        setSupportActionBar(mActivityNewTaskDetailsBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mActivityNewTaskDetailsBinding.pager.setOffscreenPageLimit(5);
        setTitle("");

        mActivityNewTaskDetailsBinding.toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        checkLocation(); //check whether location service is enable or not in your  phone
        mActivityNewTaskDetailsBinding.slidingLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState.name().equalsIgnoreCase("Collapsed")) {

                    if (mActivityNewTaskDetailsBinding.imgSheet.getTag() != null && mActivityNewTaskDetailsBinding.imgSheet.getTag().toString().equals("180")) {
                        ObjectAnimator anim = ObjectAnimator.ofFloat(mActivityNewTaskDetailsBinding.imgSheet, "rotation", 180, 0);
                        anim.setDuration(300);
                        anim.start();
                        mActivityNewTaskDetailsBinding.imgSheet.setTag("");
                    }
                    mActivityNewTaskDetailsBinding.toolbar.animate()
                            .translationY(0)
                            .alpha(1.0f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    mActivityNewTaskDetailsBinding.toolbar.setVisibility(View.VISIBLE);
                                }
                            });
                } else if (newState.name().equalsIgnoreCase("Expanded")) {
                    ObjectAnimator anim = ObjectAnimator.ofFloat(mActivityNewTaskDetailsBinding.imgSheet, "rotation", 0, 180);
                    anim.setDuration(300);
                    anim.start();
                    mActivityNewTaskDetailsBinding.imgSheet.setTag(180 + "");
                    mActivityNewTaskDetailsBinding.toolbar.animate()
                            .translationY(0)
                            .alpha(0.0f)
                            .setDuration(300)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    mActivityNewTaskDetailsBinding.toolbar.setVisibility(View.GONE);
                                }
                            });
                }
            }
        });
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String base64 = intent.getStringExtra("base64");
            uploadOnsiteImage(base64);
            Log.d("receiver", "Got message: " + base64);
        }
    };


    public void getTaskDetailsById() {
        try {
            try {
                AppUtils.getDataClean();
            } catch (Exception e) {
                e.printStackTrace();
            }
            RealmResults<LoginResponse> LoginRealmModels =
                    getRealm().where(LoginResponse.class).findAll();

            if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                assert LoginRealmModels.get(0) != null;
                userId = LoginRealmModels.get(0).getUserID();
                NetworkCallController controller = new NetworkCallController();
                controller.setListner(new NetworkResponseListner<GeneralResponse>() {
                    @Override
                    public void onResponse(int requestCode, GeneralResponse response) {
                        try {
                            // add new record
                            getRealm().beginTransaction();
                            getRealm().copyToRealmOrUpdate(response.getData());
                            getRealm().commitTransaction();
                            mTaskCheckList = new ArrayList<>();
                            if (response.getData().getUpiTransactionId() == null) {
                                isUpiPaymentNotDone = true;
                            } else {
                                isUpiPaymentNotDone = false;
                            }
//                            AppUtils.getServiceChemicalArea(149, 1, "CMS", true);
                            isPostJobCompletionDone = response.getData().getPostJob_Checklist_Done();
                            sta = response.getData().getSchedulingStatus();
                            isCompleted = response.getData().getSchedulingStatus();
                            referralQuestion = response.getData().getRefferalQuestion();
                            referralChecked = response.getData().getCustomerInterestedToGiveRefferals();
                            referralInstructions = response.getData().getCustomerRefferalAlert();
                            AppUtils.isInspectionDone = response.getData().getConsultationInspectionDone();
                            if (response.getData().getInspectionInfestationLevel() != null) {
                                AppUtils.infestationLevel = response.getData().getInspectionInfestationLevel();
                            }
                            isIncentiveEnable = response.getData().getIncentiveEnable();
                            isConsultationRequired = response.getData().getConsultationInspectionRequired();
                            Incentive = Integer.parseInt(response.getData().getIncentivePoint());
                            isTechnicianFeedbackEnable = response.getData().getTechnicianFeedbackRequired();
                            TechnicianRating = Integer.parseInt(response.getData().getTechnicianRating());
                            accountName = response.getData().getCustName();
                            customerLatitude = response.getData().getCustomerLatitude();
                            customerLongitude = response.getData().getCustomerLongitude();
                            technicianMobileNo = response.getData().getTechnicianMobileNo();
                            referralDiscount = Integer.parseInt(response.getData().getReferralDiscount());
                            mActualAmountToCollect = response.getData().getActualAmountToCollect();
                            //typeName = response.getData().getTaskTypeName();
                            if (response.getData().getTaskTypeName().contains("Termites for")){
                                typeName = "TMS";
                                AppUtils.getTmsQuestions("21213", progress);
                                AppUtils.getServiceDeliveryQuestions("21213");
                            }
                            mTaskCheckList = response.getData().getTaskCheckList();
                            isOnsiteImageRequired = response.getData().getOnsite_Image_Required();
                            mOnsiteImagePath = response.getData().getOnsite_Image_Path();
                            Renewal_Type = response.getData().getRenewal_Type();
                            sequenceNo = Integer.parseInt(response.getData().getService_Sequence_Number());
                            orderId = response.getData().getOrderNumber();
                            isActivityThere = response.getData().getServiceActivityRequired();
                            isQRThere = response.getData().getShowBarcode();
                            Renewal_Order_No = response.getData().getRenewal_Order_No();
                            if (Renewal_Type != null && Renewal_Type.equals("Renewal")) {
                                if (Renewal_Order_No != null && !Renewal_Order_No.equals("")) {
                                    AppUtils.NOT_RENEWAL_DONE = false;
                                } else {
                                    if (response.getData().getNo_Renewal_Reason() != null && !response.getData().getNo_Renewal_Reason().equals("")) {
                                        AppUtils.NOT_RENEWAL_DONE = false;
                                    } else {
                                        AppUtils.NOT_RENEWAL_DONE = true;
                                    }
                                }
                            } else {
                                AppUtils.NOT_RENEWAL_DONE = false;
                            }

                            if (response.getData().getTag() != null) {
                                SRAppointmentType = response.getData().getTag();
                            }
                            mOnsiteCheckList = mTaskCheckList;
                            setViewPagerView();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int requestCode) {
                    }
                });
                controller.getTaskDetailById(TASK_BY_ID_REQUEST, userId, taskId, isCombinedTasks, LocaleHelper.getLanguage(this), NewTaskDetailsActivity.this, progress);
            }
        } catch (Exception e) {
            RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                AppUtils.sendErrorLogs(e.getMessage(), getClass().getSimpleName(), "getTaskDetailsById", lineNo, userName, DeviceName);
            }
        }
    }

    private void showSurveyDialog() {
//        try {
        LayoutInflater li = LayoutInflater.from(NewTaskDetailsActivity.this);
        View promptsView = li.inflate(R.layout.layout_onsite_survey_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NewTaskDetailsActivity.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        alertDialogBuilder.setView(promptsView);
        final AlertDialog alertDialog = alertDialogBuilder.create();

        final ExpandableListView expandableListView = promptsView.findViewById(R.id.expandableListView);
        SurveyAdapter mAdapter;
        HashMap<String, List<String>> expandableListDetail;
        List<String> expandableListTitle;
        List<TaskCheckList> listTitle;
        List<TaskCheckList> listHistory;
        List<String> listOptions;

        expandableListDetail = new HashMap<>();
        listTitle = new ArrayList<>();
        listHistory = new ArrayList<>();
        listOptions = new ArrayList<>();
        listOptions = Arrays.asList(getResources().getStringArray(R.array.check_list));

        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        listHistory = mTaskCheckList;

        if (listHistory != null && listHistory.size() > 0) {
            for (int i = 0; i < listHistory.size(); i++) {
                expandableListTitle.add(listHistory.get(i).getTitle());
//                expandableListDetail.put(listHistory.get(i).getTitle(), listHistory.get(i).getOptions());
//                listOptions = Arrays.asList(mTaskCheckList.get(i).getUserOptions().split("|"));
            }
        }
        mAdapter = new SurveyAdapter(this, expandableListTitle, expandableListDetail/*, listHistory, listOptions*/);
        expandableListView.setAdapter(mAdapter);

        for (int i = 0; i < expandableListView.getExpandableListAdapter().getGroupCount(); i++) {
            expandableListView.expandGroup(i);
        }

        int fillColor = ContextCompat.getColor(this, R.color.colorPrimary);
        int emptyColor = ContextCompat.getColor(this, R.color.greenlight);
        int separatorColor = ContextCompat.getColor(this, R.color.transparent);

//        ProgressBarDrawable progressDrawable = new ProgressBarDrawable(mTaskCheckList.size(), fillColor, emptyColor, separatorColor);
//        progressBar.setProgressDrawable(progressDrawable);
//        progressBar.setProgress(1);
//        progressBar.setMax(mTaskCheckList.size());

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private void setViewPagerView() {
        try {
            mAdapter = new TaskViewPagerAdapter(getSupportFragmentManager(), this);

            if (sta.equals("Dispatched") || sta.equals("Incomplete")) {
                mAdapter.addFragment(ServiceInfoFragment.newInstance(taskId, combinedTaskId, isCombinedTasks, combinedTaskTypes, combinedOrderId, mCallback), getResources().getString(R.string.service_info));
                mActivityNewTaskDetailsBinding.viewpagertab.setDistributeEvenly(false);
            } else {
                mAdapter.addFragment(ServiceInfoFragment.newInstance(taskId, combinedTaskId, isCombinedTasks, combinedTaskTypes, combinedOrderId, mCallback), getResources().getString(R.string.service_info));
                mAdapter.addFragment(ChemicalActualFragment.newInstance(taskId, combinedTaskId, isCombinedTasks, combinedOrderId, orderId), getResources().getString(R.string.chemical_info));
                if (isActivityThere) {
                    mAdapter.addFragment(ServiceUnitFragment.newInstance(isCombinedTasks, combinedOrderId, sequenceNo, orderId), "Activity");
                }
                mAdapter.addFragment(ReferralFragment.newInstance(taskId, technicianMobileNo, referralQuestion), getResources().getString(R.string.referral_info));
                if (isCombinedTasks) {
                    mAdapter.addFragment(SignatureMSTInfoFragment.newInstance(taskId, combinedTaskId, combinedTaskTypes), getResources().getString(R.string.signature_info));
                } else {
                    mAdapter.addFragment(SignatureInfoFragment.newInstance(taskId, Renewal_Type, Renewal_Order_No), getResources().getString(R.string.signature_info));
                }
                mActivityNewTaskDetailsBinding.viewpagertab.setDistributeEvenly(true);
            }

            if (sta.equals("Dispatched")) {
                mActivityNewTaskDetailsBinding.slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                mActivityNewTaskDetailsBinding.slidingLayout.setTouchEnabled(true);
            } else {
                mActivityNewTaskDetailsBinding.slidingLayout.setTouchEnabled(false);
                mActivityNewTaskDetailsBinding.tray.setOnClickListener(view -> {
                    if (mActivityNewTaskDetailsBinding.slidingLayout.getPanelState() == EXPANDED) {
                        mActivityNewTaskDetailsBinding.slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    } else {
                        mActivityNewTaskDetailsBinding.slidingLayout.setPanelState(EXPANDED);
                    }
                });
                mActivityNewTaskDetailsBinding.slidingLayout.getChildAt(1).setOnClickListener(null);
                mActivityNewTaskDetailsBinding.slidingLayout.setPanelState(EXPANDED);
            }

            if (sta.equals("Completed") || sta.equals("Incomplete")) {
                mActivityNewTaskDetailsBinding.save.setText(sta);
                mActivityNewTaskDetailsBinding.lnrSave.setEnabled(false);
            } else {
                mActivityNewTaskDetailsBinding.lnrSave.setVisibility(View.VISIBLE);
                mActivityNewTaskDetailsBinding.lnrSave.setEnabled(true);
            }
            mActivityNewTaskDetailsBinding.pager.setAdapter(mAdapter);
            final LayoutInflater inflater = LayoutInflater.from(this);
            final Resources res = getResources();

            mActivityNewTaskDetailsBinding.viewpagertab.setCustomTabView((container, position, adapter) -> {
                View itemView = inflater.inflate(R.layout.layout_task_tabs, container, false);
                TextView text = (TextView) itemView.findViewById(R.id.custom_tab_text);
                text.setText(adapter.getPageTitle(position));
                text.setTypeface(text.getTypeface(), Typeface.BOLD);
                LinearLayout lnrOffer = (LinearLayout) itemView.findViewById(R.id.lnrOffer);
                TextView txtDiscount = (TextView) itemView.findViewById(R.id.txtDiscount);

                switch (position) {
                    case 0:
                        lnrOffer.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        lnrOffer.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        if (isActivityThere) {
                            lnrOffer.setVisibility(View.INVISIBLE);
                        } else {
                            lnrOffer.setVisibility(View.VISIBLE);
                        }
                        break;

                    case 3:
                        if (isActivityThere) {
                            lnrOffer.setVisibility(View.VISIBLE);
                        } else {
                            lnrOffer.setVisibility(View.INVISIBLE);
                        }

                        if (referralDiscount > 0) {
                            txtDiscount.setText(String.valueOf(referralDiscount));
                        } else {
                            lnrOffer.setVisibility(View.INVISIBLE);
                        }
                        break;

                    case 4:
                        lnrOffer.setVisibility(View.INVISIBLE);
                        break;
                    default:
                        throw new IllegalStateException("Invalid position: " + position);
                }
                return itemView;
            });
            mActivityNewTaskDetailsBinding.viewpagertab.setViewPager(mActivityNewTaskDetailsBinding.pager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            Log.i("incompleteReason", incompleteReason);

            if (mActivityNewTaskDetailsBinding.pager.getCurrentItem() == 0) {
                try {
                    AppUtils.getDataClean();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                passData();
                finish();
                super.onBackPressed();
            } else {
                if (sta.equals("On-Site") && Status.equals("Completed") && mActivityNewTaskDetailsBinding.pager.getCurrentItem() == 0) {
                    passData();
                    finish();
                } else {
                    mActivityNewTaskDetailsBinding.pager.setCurrentItem(0, true);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void passData() {
        try {
            SharedPreferencesUtility.savePrefBoolean(NewTaskDetailsActivity.this, SharedPreferencesUtility.PREF_REFRESH, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        try {
            Log.i(TAG, "Connection Suspended");
            mGoogleApiClient.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        try {
            Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            Lat = location.getLatitude();
            Lon = location.getLongitude();
            changeMap(location.getLatitude(), location.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
//                passData();
//                AppUtils.getDataClean();
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mGoogleMap = googleMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeMap(Double lat, Double lon) {

        try {
            Log.d(TAG, "Reaching map" + mGoogleMap);

            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            // check if map is created successfully or not
            if (mGoogleMap != null) {
                LatLng latLong = new LatLng(lat, lon);
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.setTrafficEnabled(false);
                mGoogleMap.setIndoorEnabled(false);
                mGoogleMap.getUiSettings().setCompassEnabled(false);
                mGoogleMap.setBuildingsEnabled(false);
                mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
                setBounds(latLong);
            } else {
                Toast.makeText(this,
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setBounds(LatLng latLong) {
        try {
            if (mGoogleMap != null) {
                mGoogleMap.clear();
                RealmResults<LoginResponse> LoginRealmModels =
                        getRealm().where(LoginResponse.class).findAll();
                if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                    LatLng latLongc = new LatLng(customerLatitude, customerLongitude);
                    Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.customer_marker);
                    List<Marker> markersList = new ArrayList<>();
                    BitmapDescriptor CustomerMarkerIcon = BitmapDescriptorFactory.fromBitmap(AppUtils.createCustomMarker(this, b, accountName, "Customer"));
                    MarkerOptions markerOptionsCust = new MarkerOptions();
                    markerOptionsCust.position(latLongc);
                    markerOptionsCust.title("Customer's Location");
                    markerOptionsCust.icon(CustomerMarkerIcon);
//                    String techPic = SharedPreferencesUtility.getPrefString(NewTaskDetailsActivity.this, SharedPreferencesUtility.PREF_USER_PIC);
//                    if (techPic != null) {
//                        byte[] decodedString = Base64.decode(techPic, Base64.DEFAULT);
//                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//                        bitUser = decodedByte;
//                    }
//                    Bitmap bmp = BitmapFactory.decodeByteArray(bitUser, 0, bitUser.length);
                    BitmapDescriptor homeMarkerIcon = BitmapDescriptorFactory.fromBitmap(AppUtils.createCustomMarker(this, b, LoginRealmModels.get(0).getUserName(), "Resource"));
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLong);
                    markerOptions.title("You are here");
                    markerOptions.icon(homeMarkerIcon);

                    Location locationA = new Location("Technicain");

                    locationA.setLatitude(latLong.latitude);
                    locationA.setLongitude(latLong.longitude);

                    Location locationB = new Location("Customer");

                    locationB.setLatitude(latLongc.latitude);
                    locationB.setLongitude(latLongc.longitude);
                    float distanceRange = locationA.distanceTo(locationB);

                    mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
                    markersList.add(mCurrLocationMarker);
                    mCustomerMarker = mGoogleMap.addMarker(markerOptionsCust);
                    markersList.add(mCustomerMarker);

                    mCircle = mGoogleMap.addCircle(new CircleOptions()
                            .strokeWidth(1)
                            .radius(1000)
                            .center(mCurrLocationMarker.getPosition())
                            .strokeColor(Color.parseColor("#55000000"))
                            .fillColor(Color.parseColor("#55000000")));
                    mCircle.setRadius(distanceRange * 1.2);
                    float[] distance = new float[2];
                    for (int m = 0; m < markersList.size(); m++) {
                        Marker marker = markersList.get(m);
                        LatLng position = marker.getPosition();
                        double lat = position.latitude;
                        double lon = position.longitude;

                        Location.distanceBetween(lat, lon, latLong.latitude,
                                latLong.longitude, distance);

                        boolean inCircle = distance[0] <= distanceRange;
                        marker.setVisible(inCircle);
                    }
                    /**create for loop for get the latLngbuilder from the marker list*/
                    builder = new LatLngBounds.Builder();
                    for (Marker m : markersList) {
                        builder.include(m.getPosition());
                    }
                    /**initialize the padding for map boundary*/
                    /**create the bounds from latlngBuilder to set into map camera*/
                    LatLngBounds bounds = builder.build();
                    /**create the camera with bounds and padding to set into map*/
                    cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);
                    /**call the map call back to know map is loaded or not*/
                    mGoogleMap.setOnMapLoadedCallback(() -> {
                        /**set animated zoom camera into map*/
                        mGoogleMap.animateCamera(cu);

                    });
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        try {
            super.onStart();
            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStop() {
        try {
            super.onStop();
            if (mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void startLocationUpdates() {
        try {
            // Create the location request
            mLocationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(UPDATE_INTERVAL)
                    .setFastestInterval(FASTEST_INTERVAL);
            // Request location updates
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            Log.d("reque", "--->>>>");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", (paramDialogInterface, paramInt) -> {

                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                })
                .setNegativeButton("Cancel", (paramDialogInterface, paramInt) -> {

                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onSaveTaskClick(View view) {
        try {
            new GPSUtils(this).turnGPSOn(isGPSEnable -> {
                // turn on GPS
                if (isGPSEnable) {
                    saveTaskDetails();
                } else {
                    isGPS = isGPSEnable;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void refreshMyData() {
        getTaskDetailsById();
    }

    private void saveTaskDetails() {
        try {
            progress.show();
            Log.i("Status", Status);
            isAttachment = SharedPreferencesUtility.getPrefBoolean(this, SharedPreferencesUtility.PREF_ATTACHMENT);
            if (isGeneralChanged) {
                mActivityNewTaskDetailsBinding.pager.setCurrentItem(0);
                Toasty.error(this, getResources().getString(R.string.please_change_status_service), Toast.LENGTH_SHORT, true).show();
                progress.dismiss();
            }
//            else if (isOnsiteImageRequired && mOnsiteImagePath == null && Status.equals("On-Site")) {
//                captureTechImage();
//                progress.dismiss();
//            }
            else if (isEarlyCompletion && Status.equals("Completed")) {
                mActivityNewTaskDetailsBinding.pager.setCurrentItem(0);
                Toasty.error(this, getResources().getString(R.string.job_time_serice), Toasty.LENGTH_LONG, true).show();
                progress.dismiss();
            } else if (isOnsiteOtpRequired && Status.equals("On-Site")) {
                mActivityNewTaskDetailsBinding.pager.setCurrentItem(0);
                Toasty.error(this, getResources().getString(R.string.onsite_otp_required_service), Toast.LENGTH_SHORT, true).show();
                progress.dismiss();
            } else if (isOnsiteOtpValidated && Status.equals("On-Site")) {
                mActivityNewTaskDetailsBinding.pager.setCurrentItem(0);
                Toasty.error(this, getResources().getString(R.string.invalid_onsite_otp_service), Toast.LENGTH_SHORT, true).show();
                progress.dismiss();
            } else if (isIncompleteReason && Status.equals("Incomplete")) {
                mActivityNewTaskDetailsBinding.pager.setCurrentItem(0);
                Toasty.error(this, getResources().getString(R.string.please_select_incomplete_reason), Toast.LENGTH_SHORT, true).show();
                progress.dismiss();
            } else if (isPaymentModeNotChanged && Status.equals("Completed")) {
                mActivityNewTaskDetailsBinding.pager.setCurrentItem(0);
                Toasty.error(this, getResources().getString(R.string.please_change_payment_mode), Toast.LENGTH_SHORT, true).show();
                progress.dismiss();
            } else if (isPaymentOtpRequired && Status.equals("Completed")) {
                mActivityNewTaskDetailsBinding.pager.setCurrentItem(0);
                Toasty.error(this, getResources().getString(R.string.payment_otp_is_required), Toast.LENGTH_SHORT, true).show();
                progress.dismiss();
            } else if (isPaymentOtpValidated && Status.equals("Completed")) {
                mActivityNewTaskDetailsBinding.pager.setCurrentItem(0);
                Toasty.error(this, getResources().getString(R.string.invalid_payment_otp), Toast.LENGTH_SHORT, true).show();
                progress.dismiss();
            } else if (isAmountCollectedRequired && Status.equals("Completed")) {
                mActivityNewTaskDetailsBinding.pager.setCurrentItem(0);
                Toasty.error(this, getResources().getString(R.string.amount_collected_field_is_required), Toast.LENGTH_SHORT, true).show();
                progress.dismiss();
            } else if (isAmountCollectedEquals && Status.equals("Completed")) {
                mActivityNewTaskDetailsBinding.pager.setCurrentItem(0);
                Toasty.error(this, getResources().getString(R.string.invalid_otp_service), Toast.LENGTH_SHORT, true).show();
                progress.dismiss();
            } else if (isBankNameRequired && Status.equals("Completed")) {
                mActivityNewTaskDetailsBinding.pager.setCurrentItem(0);
                Toasty.error(this, getResources().getString(R.string.please_select_bank_name), Toast.LENGTH_SHORT, true).show();
                progress.dismiss();
            } else if (isChequeDateRequired && Status.equals("Completed")) {
                mActivityNewTaskDetailsBinding.pager.setCurrentItem(0);
                Toasty.error(this, getResources().getString(R.string.please_select_cheque_date), Toast.LENGTH_SHORT, true).show();
                progress.dismiss();
            } else if (isChequeNumberRequired && Status.equals("Completed")) {
                mActivityNewTaskDetailsBinding.pager.setCurrentItem(0);
                Toasty.error(this, getResources().getString(R.string.cheque_number_is_required), Toast.LENGTH_SHORT, true).show();
                progress.dismiss();
            } else if (isInvalidChequeNumber && Status.equals("Completed")) {
                mActivityNewTaskDetailsBinding.pager.setCurrentItem(0);
                Toasty.error(this, getResources().getString(R.string.invalid_cheque_number), Toast.LENGTH_SHORT, true).show();
                progress.dismiss();
            } else if (isUpiPaymentNotDone && Status.equals("Completed") && (Payment_Mode.equalsIgnoreCase("paytm") || (Payment_Mode.equalsIgnoreCase("phonepay")))) {
                mActivityNewTaskDetailsBinding.pager.setCurrentItem(0);
                Toasty.error(this, "Please capture payment through UPI.", Toast.LENGTH_SHORT, true).show();
                progress.dismiss();

            } else if (isChequeImageRequired && Status.equals("Completed")) {
                mActivityNewTaskDetailsBinding.pager.setCurrentItem(0);
                Toasty.error(this, getResources().getString(R.string.please_upload_cheque_image), Toast.LENGTH_SHORT, true).show();
                progress.dismiss();
            } else if (isChemicalChanged && Status.equals("Completed")) {
                mActivityNewTaskDetailsBinding.pager.setCurrentItem(1);
                Toasty.error(this, getResources().getString(R.string.enter_the_collect_values_of_chemicals_used), Toast.LENGTH_SHORT, true).show();
                progress.dismiss();
            } else if (isChemicalVerified && Status.equals("Completed")) {
                mActivityNewTaskDetailsBinding.pager.setCurrentItem(1);
                Toasty.error(this, getResources().getString(R.string.chamical_should_be_verified), Toast.LENGTH_SHORT, true).show();
                progress.dismiss();
            } else if (AppUtils.IS_QRCODE_THERE && Status.equals("Completed")) {
                if (isActivityThere) {
                    mActivityNewTaskDetailsBinding.pager.setCurrentItem(2);
                } else {
                    mActivityNewTaskDetailsBinding.pager.setCurrentItem(1);
                }
                Toasty.error(this, "Please scan equipment", Toast.LENGTH_SHORT, true).show();
                progress.dismiss();
            } else if (Status.equals("Completed") && AppUtils.NOT_RENEWAL_DONE) {
                if (isActivityThere) {
                    mActivityNewTaskDetailsBinding.pager.setCurrentItem(4);
                } else {
                    mActivityNewTaskDetailsBinding.pager.setCurrentItem(3);
                }

                progress.dismiss();
                Toasty.error(this, "This is the last service, kindly ask the customer for Service Renewal", Toast.LENGTH_SHORT, true).show();
            } else if (isSignatureChanged && Status.equals("Completed")) {
                if (isActivityThere) {
                    mActivityNewTaskDetailsBinding.pager.setCurrentItem(4);
                } else {
                    mActivityNewTaskDetailsBinding.pager.setCurrentItem(3);
                }
                progress.dismiss();
                Toasty.error(this, getResources().getString(R.string.signatory_field_is_required), Toast.LENGTH_SHORT, true).show();
            } else if (isOTPRequired && Status.equals("Completed")) {
                if (isActivityThere) {
                    mActivityNewTaskDetailsBinding.pager.setCurrentItem(4);
                } else {
                    mActivityNewTaskDetailsBinding.pager.setCurrentItem(3);
                }
                progress.dismiss();
                Toasty.error(this, getResources().getString(R.string.otp_field_is_required), Toast.LENGTH_SHORT, true).show();
            } else if (isOTPValidated && Status.equals("Completed")) {
                if (isActivityThere) {
                    mActivityNewTaskDetailsBinding.pager.setCurrentItem(4);
                } else {
                    mActivityNewTaskDetailsBinding.pager.setCurrentItem(3);
                }
                progress.dismiss();
                Toasty.error(this, getResources().getString(R.string.invalid_otp_ss), Toast.LENGTH_SHORT, true).show();
            } else if (isWorkTypeNotChecked && Status.equals("Completed")) {
                if (isActivityThere) {
                    mActivityNewTaskDetailsBinding.pager.setCurrentItem(4);
                } else {
                    mActivityNewTaskDetailsBinding.pager.setCurrentItem(3);
                }
                progress.dismiss();
                Toasty.error(this, getResources().getString(R.string.please_select_correct_type_of_service_done), Toast.LENGTH_SHORT, true).show();
            } else if (isSignatureValidated && Status.equals("Completed")) {
                if (isActivityThere) {
                    mActivityNewTaskDetailsBinding.pager.setCurrentItem(4);
                } else {
                    mActivityNewTaskDetailsBinding.pager.setCurrentItem(3);
                }
                progress.dismiss();
                Toasty.error(this, getResources().getString(R.string.customer_signature_is_required), Toast.LENGTH_SHORT, true).show();
            } else if (isCardRequired && Status.equals("Completed")) {
                if (isActivityThere) {
                    mActivityNewTaskDetailsBinding.pager.setCurrentItem(4);
                } else {
                    mActivityNewTaskDetailsBinding.pager.setCurrentItem(3);
                }
                progress.dismiss();
                Toasty.error(this, getResources().getString(R.string.please_upload_your_job_card_service), Toast.LENGTH_SHORT, true).show();
            } else if (isConsultationRequired && !AppUtils.isInspectionDone && Status.equals("Completed")) {
                mActivityNewTaskDetailsBinding.pager.setCurrentItem(0);
                progress.dismiss();
                Toasty.error(this, "Please complete Consultation & Inspection required for the service", Toast.LENGTH_SHORT, true).show();


            } else if (Status.equals("Completed") && mTaskCheckList != null && !isPostJobCompletionDone) {
                mActivityNewTaskDetailsBinding.pager.setCurrentItem(0);
                progress.dismiss();
                Toasty.error(this, "Please complete Post Job Check-List required for the service", Toast.LENGTH_SHORT, true).show();
            } else if (Status.equals("Completed") && SRAppointmentType.equalsIgnoreCase("complaint") && assignmentStartDate.equals("") && (AppUtils.infestationLevel != null && AppUtils.infestationLevel.equalsIgnoreCase("high infestation"))) {
                if (isActivityThere) {
                    mActivityNewTaskDetailsBinding.pager.setCurrentItem(4);
                } else {
                    mActivityNewTaskDetailsBinding.pager.setCurrentItem(3);
                }
                progress.dismiss();
                Toasty.error(this, "Please select flush-out appointment", Toast.LENGTH_SHORT, true).show();
            } else if (Status.equals("Completed") && (SRAppointmentType.equalsIgnoreCase("flushout") || SRAppointmentType.equalsIgnoreCase("incomplete flushout")) && assignmentStartDate.equals("") && (AppUtils.infestationLevel != null && AppUtils.infestationLevel.equalsIgnoreCase("high infestation"))) {
                if (isActivityThere) {
                    mActivityNewTaskDetailsBinding.pager.setCurrentItem(4);
                } else {
                    mActivityNewTaskDetailsBinding.pager.setCurrentItem(3);
                }
                progress.dismiss();
                Toasty.error(this, "Please select gel appointment", Toast.LENGTH_SHORT, true).show();
            } else if (Status.equals("Completed") && !referralChanged){
                if (isActivityThere) {
                    mActivityNewTaskDetailsBinding.pager.setCurrentItem(3);
                }else {
                    mActivityNewTaskDetailsBinding.pager.setCurrentItem(2);
                }
                progress.dismiss();
                Toasty.error(this, "Please complete the referral process", Toast.LENGTH_SHORT, true).show();
            } else if (Status.equals("Completed") && isTechnicianFeedbackEnable && Rate == 0) {
                progress.dismiss();
                showRatingDialog();
            } else if (Status.equals("Completed") && mTaskCheckList != null && !isPostJobCompletionDone) {
                progress.dismiss();
                isFinalSave = true;
                showCompletionDialog();
            }
            /* else if(Status.equals("Completed") && isIncentiveEnable) {
                if (isTechnicianFeedbackEnable && Rate == 0 && Status.equals("Completed")) {
                    showRatingDialog();
                }*/
            else {
                if (Status.equalsIgnoreCase("Completed")){
                    if (referralChanged){
                        finalSave();
                    }else {
                        if (isActivityThere) {
                            mActivityNewTaskDetailsBinding.pager.setCurrentItem(3);
                        } else {
                            mActivityNewTaskDetailsBinding.pager.setCurrentItem(2);
                        }
                        progress.dismiss();
                        Toasty.error(this, "Please complete the referral process", Toast.LENGTH_SHORT, true).show();
                    }
                }else {
                    finalSave();
                }
            }
        } catch (Exception e) {
            RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                assert mLoginRealmModels.get(0) != null;
                String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                AppUtils.sendErrorLogs(e.getMessage(), getClass().getSimpleName(), "getSaveMenu", lineNo, userName, DeviceName);
            }
        }
    }

    private void finalSave() {
        try {
            RealmResults<LoginResponse> LoginRealmModels =
                    getRealm().where(LoginResponse.class).findAll();
            if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                assert LoginRealmModels.get(0) != null;
                String UserId = LoginRealmModels.get(0).getUserID();
                UpdateTasksRequest request = new UpdateTasksRequest();
                request.setSchedulingStatus(Status);
                request.setPaymentMode(Payment_Mode);
                request.setAmountCollected(Amount_Collected);
                request.setAmountToCollect(Amount_To_Collected);
                request.setActualPropertySize(Actual_Size);
                request.setStandardPropertySize(Standard_Size);
                request.setTechnicianRating(Rate);
                request.setTechnicianOTP(Feedback_Code);
                request.setSignatory(signatory);
                request.setBankName(bankName);
                request.setChequeDate(chequeDate);
                request.setChequeNo(chequeNumber);
                request.setCustomerSign(Signature);
                request.setLatitude(String.valueOf(Lat));
                request.setLongitude(String.valueOf(Lon));
                if (isCombinedTasks) {
                    request.setCombinedTaskId(combinedTaskId);
                } else {
                    request.setTaskId(taskId);
                }
                request.setCombinedTask(isCombinedTasks);
                request.setDuration(Duration);
                request.setResourceId(UserId);
                request.setTechnicianOnsiteOTP(OnsiteOTP);
                request.setChemicalList(ChemReqList);
                request.setChemicalChanged(isActualChemicalChanged);
                request.setIncompleteReason(incompleteReason);
                request.setChequeImage(chequeImage);
                request.setFlushOutReason(flushoutReason);
                request.setNext_Task_Id(nextTaskId);
                request.setActualAmountToCollect(mActualAmountToCollect);
                request.setNext_SR_Service_Date(assignmentStartDate);
                request.setNext_SR_Service_Start_Time(assignmentStartTime);
                request.setNext_SR_Service_End_Time(assignmentEndTime);
                request.setCustomerInterestedToGiveRefferals(referralChecked);

                NetworkCallController controller = new NetworkCallController();
                controller.setListner(new NetworkResponseListner() {
                    @Override
                    public void onResponse(int requestCode, Object response) {
                        try {
                            UpdateTaskResponse updateResponse = (UpdateTaskResponse) response;
                            if (updateResponse.getSuccess()) {
                                AppUtils.infestationLevel = "";
                                Toasty.success(NewTaskDetailsActivity.this, updateResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();
                                if (isIncentiveEnable && Status.equals("Completed")) {
                                    showIncentiveDialog();
                                } else {
                                    AppUtils.getDataClean();
                                    passData();
                                    finish();
                                }
                            } else {
                                Toasty.error(NewTaskDetailsActivity.this, updateResponse.getErrorMessage(), Toasty.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int requestCode) {
                    }
                });
                controller.updateTasks(UPDATE_REQUEST, request, NewTaskDetailsActivity.this, progress);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == GPSUtils.GPS_REQUEST) {
                    isGPS = true; // flag maintain before get location
                    progress.show();
                    getTaskDetailsById();
                } else if (requestCode == REQUEST_TAKE_PHOTO) {
                    try {
                        selectedImagePath = mPhotoFile.getPath();
                        if (selectedImagePath.length() > 0) {
                            Bitmap bit = new BitmapDrawable(getResources(),
                                    selectedImagePath).getBitmap();
                            int i = (int) (bit.getHeight() * (1024.0 / bit.getWidth()));
                            bitmap = Bitmap.createScaledBitmap(bit, 1024, i, true);
                        }

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                        byte[] b = baos.toByteArray();
                        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
//                        uploadOnsiteImage(encodedImage);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        if (mPermissions) {
            if (checkCameraHardware(this)) {
                // Open the Camera
                startCamera2();
            } else {
                showSnackBar("You need a camera to use this application", Snackbar.LENGTH_INDEFINITE);
            }
        } else {
            verifyPermissions();
        }
    }

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    private void startCamera2() {
        Intent intent = new Intent(this, Camera2Activity.class);
        intent.putExtra(AppUtils.CAMERA_ORIENTATION, "BACK");
        startActivity(intent);
    }

    private void showSnackBar(final String text, final int length) {
        View view = findViewById(android.R.id.content).getRootView();
        Snackbar.make(view, text, length).show();
    }

    public void verifyPermissions() {
        Log.d("TAG", "verifyPermissions: asking user for permissions.");
        String[] permissions = {
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(),
                permissions[1]) == PackageManager.PERMISSION_GRANTED) {
            mPermissions = true;
            init();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    permissions,
                    REQUEST_CODE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (mPermissions) {
                init();
            } else {
                verifyPermissions();
            }
        }
    }


    private void showTmsCompletionDialog() {
        try {
            LayoutInflater li = LayoutInflater.from(NewTaskDetailsActivity.this);
            View promptsView = li.inflate(R.layout.tms_completion_check_list_dialog, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NewTaskDetailsActivity.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
            alertDialogBuilder.setView(promptsView);
            final AlertDialog alertDialog = alertDialogBuilder.create();
            final RecyclerView recyclerView =
                    promptsView.findViewById(R.id.recycleView);
            final RecyclerView chipsRecyclerView =
                    promptsView.findViewById(R.id.chipsRecyclerView);
            final AppCompatButton btnSend = promptsView.findViewById(R.id.btnSave);
            final AppCompatButton nextChipBtn = promptsView.findViewById(R.id.nextChipBtn);
            final AppCompatButton backChipBtn = promptsView.findViewById(R.id.backChipBtn);
            final TextView txtTitle = promptsView.findViewById(R.id.txtTitle);
            ArrayList<QuestionList> currentList = new ArrayList<>();

            txtTitle.setTypeface(txtTitle.getTypeface(), Typeface.BOLD);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

            chipsRecyclerView.setHasFixedSize(true);
            chipsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

            if (map != null) {
                map.clear();
            }

            mCheckTmsAdapter = new TmsQuestionsParentAdapter(this);
            TmsChipsAdapter chipsAdapter = new TmsChipsAdapter(this, AppUtils.tmsServiceDeliveryChips);
            recyclerView.setAdapter(mCheckTmsAdapter);
            chipsRecyclerView.setAdapter(chipsAdapter);

            chipsAdapter.setOnItemClickHandler((position, category) -> {
                currChip = category;
                currPos = position;

                if (currPos == AppUtils.tmsServiceDeliveryChips.size()-1){
                    isLast = true;
                    nextChipBtn.setVisibility(View.GONE);
                }else{
                    isLast = false;
                    nextChipBtn.setVisibility(View.VISIBLE);
                }
                if (currPos == 0){
                    backChipBtn.setVisibility(View.GONE);
                }else{
                    backChipBtn.setVisibility(View.VISIBLE);
                }
                for (QuestionTabList it : AppUtils.tmsServiceDeliveryList) {
                    if (it.getQuestionTab().equalsIgnoreCase(category)){
                        currentList.clear();
                        currentList.addAll(it.getQuestionList());
                        mCheckTmsAdapter.addData(it.getQuestionList());
                        mCheckTmsAdapter.notifyDataSetChanged();
                    }
                }
                chipsRecyclerView.post(() -> chipsRecyclerView.smoothScrollToPosition(position));
                recyclerView.post(() -> recyclerView.smoothScrollToPosition(0));

                validate(currentList, btnSend, nextChipBtn);

            });

            mCheckTmsAdapter.setOnItemClickListener((position, questionId, answer) -> {
                validate(currentList, btnSend, nextChipBtn);
            });

            mCheckTmsAdapter.setOnCameraClickHandler(new TmsQuestionsParentAdapter.OnCameraClickListener() {
                @Override
                public void onCameraClicked(int position, Integer questionId, int clickedBy) {
                    qId = questionId;
                    cBy = clickedBy;
                    checkPosition = position;
                    isFromTms = true;
                    AppUtils.CAMERA_SCREEN = "Post-Job";
                    LocalBroadcastManager.getInstance(NewTaskDetailsActivity.this).registerReceiver(mMessageReceiver,
                            new IntentFilter(AppUtils.CAMERA_SCREEN));
                    //checkPosition = position;
//                requestStoragePermission(true);
                    init();
                }

                @Override
                public void onCancelClicked(int position, Integer questionId, int clickedBy) {
                    qId = questionId;
                    cBy = clickedBy;
                    checkPosition = position;
                    ArrayList<QuestionList> tmsCList = new ArrayList<>();
                    for (QuestionTabList it : AppUtils.tmsServiceDeliveryList) {
                        if (it.getQuestionTab().equals(currChip)) {
                            tmsCList.addAll(it.getQuestionList());
                        }
                    }
                    for (QuestionList it: tmsCList) {
                        if (it.getQuestionId() == qId) {
                            if (it.getPictureURL().isEmpty()) {
                                it.setPictureURL(null);
                            }
                        }
                    }
                    mCheckTmsAdapter.notifyDataSetChanged();
                    validate(currentList, btnSend, nextChipBtn);
                }
            });
            setOnUploadedListener(() -> {
                validate(currentList, btnSend, nextChipBtn);
            });
            nextChipBtn.setOnClickListener(v -> {
                if (currPos < AppUtils.tmsServiceDeliveryChips.size()-1){
                    currPos += 1;
                    recyclerView.startAnimation(TmsUtils.inFromRightAnimation());
                    chipsAdapter.nextChip(currPos);
                }else{
                    Log.d("TAG", "Last");
                }
            });
            backChipBtn.setOnClickListener(v -> {
                if (currPos > 0){
                    currPos -= 1;
                    recyclerView.startAnimation(TmsUtils.inFromLeftAnimation());
                    chipsAdapter.backChip(currPos);
                }else{
                    Log.d("TAG", "Last");
                }
            });

            validate(currentList, btnSend, nextChipBtn);
            //saveHashSet = new HashSet<>();

            btnSend.setOnClickListener(v -> {
                Log.d("TAG", "Save "+AppUtils.tmsServiceDeliveryList);
                if (currentList != null && currentList.size() > 0) {
                    if (TmsUtils.isListChecked(currentList)) {
                        if (TmsUtils.isImgChecked(currentList)) {
                            saveCheckList(alertDialog);
                            alertDialog.dismiss();
                        } else {
                            Toasty.error(this, "Image required!", Toasty.LENGTH_SHORT).show();
                        }
                    } else {
                        Toasty.error(this, "All Questions are mandatory.", Toasty.LENGTH_SHORT).show();
                    }
                } else {
                    Toasty.error(this, "All Questions are mandatory.", Toasty.LENGTH_SHORT).show();
                }
            });

            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.show();
            alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showCompletionDialog() {
        try {
            LayoutInflater li = LayoutInflater.from(NewTaskDetailsActivity.this);
            View promptsView = li.inflate(R.layout.completion_check_list_dialog, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NewTaskDetailsActivity.this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
            alertDialogBuilder.setView(promptsView);
            final AlertDialog alertDialog = alertDialogBuilder.create();
            final RecyclerView recyclerView =
                    promptsView.findViewById(R.id.recycleView);
            final AppCompatButton btnSend = promptsView.findViewById(R.id.btnSave);
            final TextView txtTitle = promptsView.findViewById(R.id.txtTitle);

            txtTitle.setTypeface(txtTitle.getTypeface(), Typeface.BOLD);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            if (map != null) {
                map.clear();
            }

            mCheckAdapter = new CheckListParentAdapter(this, mTaskCheckList, (position, option) -> {
                mOnsiteCheckList.get(position).setTaskId(taskId);
                mOnsiteCheckList.get(position).setResourceId(resourceId);
                if (mCheckAdapter.getItem(position).getTakePicture()) {
                    if (mCheckAdapter.getItem(position).getSelectedAnswer().equalsIgnoreCase("No")) {
                        mOnsiteCheckList.get(position).setImageRequired(false);
                    } else {
                        mOnsiteCheckList.get(position).setImageRequired(true);
                    }
                } else {
                    mOnsiteCheckList.get(position).setImageRequired(false);
                }
                mOnsiteCheckList.get(position).setSelectedAnswer(mCheckAdapter.getItem(position).getSelectedAnswer());
                mOnsiteCheckList.get(position).setImagePath(mCheckAdapter.getItem(position).getImagePath());
            });
            recyclerView.setAdapter(mCheckAdapter);
            mCheckAdapter.setOnItemClickHandler(position -> {
                isFromTms = false;
                AppUtils.CAMERA_SCREEN = "Post-Job";
                LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                        new IntentFilter(AppUtils.CAMERA_SCREEN));
                checkPosition = position;
//                requestStoragePermission(true);
                init();
            });
            saveHashSet = new HashSet<>();
            btnSend.setOnClickListener(v -> {

                if (mOnsiteCheckList != null && mOnsiteCheckList.size() > 0) {
                    if (isListChecked(mOnsiteCheckList)) {
                        if (isImageChecked(mOnsiteCheckList)) {
                            saveCheckList(alertDialog);
                            alertDialog.dismiss();
                        } else {
                            Toasty.error(this, "Image required!", Toasty.LENGTH_SHORT).show();
                        }
                    } else {
                        Toasty.error(this, "All Questions are mandatory.", Toasty.LENGTH_SHORT).show();
                    }
                } else {
                    Toasty.error(this, "All Questions are mandatory.", Toasty.LENGTH_SHORT).show();
                }
            });

            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.show();
            alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void validate(ArrayList<QuestionList> currentList, AppCompatButton btnSend, AppCompatButton nextChipBtn){
        if (TmsUtils.isImgChecked(currentList)) {
            if (TmsUtils.isListChecked(currentList)) {
                if (isLast) {
                    btnSend.setEnabled(true);
                    btnSend.setAlpha(1.0f);
                }else{
                    btnSend.setEnabled(false);
                    btnSend.setAlpha(0.6f);
                }
                nextChipBtn.setEnabled(true);
                nextChipBtn.setAlpha(1.0f);
            } else {
                btnSend.setEnabled(false);
                btnSend.setAlpha(0.6f);

                nextChipBtn.setEnabled(false);
                nextChipBtn.setAlpha(0.6f);
            }
        } else {
            btnSend.setEnabled(false);
            btnSend.setAlpha(0.6f);

            nextChipBtn.setEnabled(false);
            nextChipBtn.setAlpha(0.6f);
        }
        btnSend.setEnabled(true);
        btnSend.setAlpha(1f);
    }

    private boolean isImageChecked(List<TaskCheckList> mSaveList) {
        boolean isRequired = true;
        for (TaskCheckList data : mSaveList) {
            if (data.getImageRequired()) {
                if (data.getImagePath() != null && !data.getImagePath().equals("")) {
                    isRequired = true;
                } else {
                    isRequired = false;
                    break;
                }
            }
        }
        return isRequired;
    }

    private boolean isListChecked(List<TaskCheckList> mSaveList) {
        for (TaskCheckList data : mSaveList) {
            if (data.getSelectedAnswer() == null || data.getSelectedAnswer().equals("")) {
                return false;
            }
        }
        return true;
    }


    private void requestStoragePermission(boolean isCamera) {
        try {
            Dexter.withActivity(this)
                    .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            // check if all permissions are granted
                            if (report.areAllPermissionsGranted()) {
                                if (isCamera) {
                                    dispatchTakePictureIntent();
                                }
                            }
                            // check for permanent denial of any permission
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                // show alert dialog navigating to Settings
                                showSettingsDialog();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions,
                                                                       PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    })
                    .withErrorListener(
                            error -> Toast.makeText(this, "Error occurred! ", Toast.LENGTH_SHORT)
                                    .show())
                    .onSameThread()
                    .check();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void showSettingsDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Need Permissions");
            builder.setMessage(
                    "This app needs permission to use this feature. You can grant them in app settings.");
            builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
                dialog.cancel();
                openSettings();
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // navigating user to app settings
    private void openSettings() {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, 101);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void dispatchTakePictureIntent() {
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    // Error occurred while creating the File
                }
                if (photoFile != null) {
                    try {
                        Uri photoURI = FileProvider.getUriForFile(this,
                                BuildConfig.APPLICATION_ID + ".provider",
                                photoFile);
                        mPhotoFile = photoFile;
                        takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", Camera.CameraInfo.CAMERA_FACING_BACK);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFile = File.createTempFile(mFileName, ".jpg", storageDir);
        return mFile;
    }


    private void uploadOnsiteImage(String base64) {
        try {

            RealmResults<LoginResponse> LoginRealmModels =
                    BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                String UserId = LoginRealmModels.get(0).getUserID();
                UploadCheckListRequest request = new UploadCheckListRequest();
                request.setResourceId(UserId);
                request.setFileUrl("");
                request.setFileName("");
                request.setTaskId(taskId);
                request.setFileContent(base64);

                NetworkCallController controller = new NetworkCallController();
                controller.setListner(new NetworkResponseListner<UploadCheckListData>() {
                    @Override
                    public void onResponse(int requestCode, UploadCheckListData response) {
                        Log.d("TAG", "Save "+response.getFileUrl());
                        if (isFromTms) {
                            ArrayList<QuestionList> tmsCList = new ArrayList<>();
                            for (QuestionTabList it : AppUtils.tmsServiceDeliveryList) {
                                if (it.getQuestionTab().equals(currChip)) {
                                    tmsCList.addAll(it.getQuestionList());
                                }
                            }
                            ArrayList<String> url = new ArrayList<>();
                            url.clear();
                            url.add(response.getFileUrl());
                            for (QuestionList it: tmsCList){
                                if (it.getQuestionId().equals(qId)){
                                    if (it.getPictureURL() == null){
                                        it.setPictureURL(url);
                                    }else {
                                        it.getPictureURL().add(response.getFileUrl());
                                    }
                                }
                            }
                            mCheckTmsAdapter.notifyDataSetChanged();
                            LocalBroadcastManager.getInstance(NewTaskDetailsActivity.this).unregisterReceiver(mMessageReceiver);
                            imageUploaded.uploaded();
                        } else {
                            mCheckAdapter.getItem(checkPosition).setIconUrl(response.getFileUrl());
                            mTaskCheckList.get(checkPosition).setIconUrl(response.getFileUrl());
                            mTaskCheckList.get(checkPosition).setImagePath(response.getFileUrl());
                            mOnsiteCheckList.get(checkPosition).setImagePath(response.getFileUrl());
                            mCheckAdapter.notifyItemChanged(checkPosition);
                        }
                    }

                    @Override
                    public void onFailure(int requestCode) {
                    }
                });
                controller.uploadCheckListAttachment(UPLOAD_REQ, request);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void saveCheckList(AlertDialog alertDialog) {
        try {
            NetworkCallController controller = new NetworkCallController();
            controller.setListner(new NetworkResponseListner<CheckListResponse>() {
                @Override
                public void onResponse(int requestCode, CheckListResponse response) {
                    if (response.getIsSuccess()) {
                        isPostJobCompletionDone = true;
                        if (Status.equalsIgnoreCase("Completed")) {
                            if (referralChanged) {
                                isFinalSave = true;
                            } else {
                                isFinalSave = false;
                                if (isActivityThere) {
                                    mActivityNewTaskDetailsBinding.pager.setCurrentItem(3);
                                } else {
                                    mActivityNewTaskDetailsBinding.pager.setCurrentItem(2);
                                }
                                progress.dismiss();
                                Toasty.error(NewTaskDetailsActivity.this, "Please complete the referral process", Toast.LENGTH_SHORT, true).show();
                            }
                        }
                        if (isFinalSave) {
                            progress.show();
                            finalSave();
                        }
                        mSaveList.clear();
                        alertDialog.dismiss();
                    } else {
                        alertDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(int requestCode) {

                }
            });
            if (!typeName.equalsIgnoreCase("TMS")) {
                controller.saveCheckList(SAVE_CHECK_LIST, mOnsiteCheckList);
            }else {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("TaskId", "23213");
                hashMap.put("type", "Service Delivery");
                hashMap.put("QuestionTabList", AppUtils.tmsServiceDeliveryList);
                controller.saveServiceDelivery(SAVE_CHECK_LIST, Collections.singletonList(hashMap));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void showIncentiveDialog() {
        try {
            View view = getLayoutInflater().inflate(R.layout.new_scratchcard_layout, null);
            final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
            Window window = dialog.getWindow();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            assert window != null;
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setBackgroundDrawableResource(R.color.darkblack);
            dialog.setContentView(view);
            dialog.show();

            final AppCompatTextView txtReward =
                    view.findViewById(R.id.txtReward);
            final AppCompatTextView txtIncentive =
                    view.findViewById(R.id.txtIncentive);
            final AppCompatTextView txtLose =
                    view.findViewById(R.id.txtLose);
            final AppCompatImageView imgAward =
                    view.findViewById(R.id.imgAward);
            final AppCompatImageView imgNoAward =
                    view.findViewById(R.id.imgNoAward);
            final AppCompatImageView imgCancel =
                    view.findViewById(R.id.imgCancel);
            final ScratchRelativeLayout scratch =
                    view.findViewById(R.id.scratch);
            final AppCompatTextView txtMsg =
                    view.findViewById(R.id.winningMsg);
            final AppCompatTextView txtEarned =
                    view.findViewById(R.id.txtEarned);
            final AppCompatTextView txtHicare =
                    view.findViewById(R.id.txtHicare);

            if (dialog != null) {
                int width = ViewGroup.LayoutParams.MATCH_PARENT;
                int height = ViewGroup.LayoutParams.MATCH_PARENT;
                Objects.requireNonNull(dialog.getWindow()).setLayout(width, height);
                imgCancel.setOnClickListener(v -> {
                    onBackPressed();
                    try {
                        AppUtils.getDataClean();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();


                });

                String[] array = getApplicationContext().getResources().getStringArray(R.array.randomApplause);
                String randomStr = array[new Random().nextInt(array.length)];
                if (Incentive == 0) {
                    imgAward.setVisibility(View.INVISIBLE);
                    imgNoAward.setVisibility(View.VISIBLE);
                    txtLose.setVisibility(View.VISIBLE);
                    txtIncentive.setVisibility(View.GONE);
                    txtReward.setVisibility(View.GONE);
                    txtMsg.setText("Oops!");

                } else {
                    imgAward.setVisibility(View.VISIBLE);
                    imgNoAward.setVisibility(View.INVISIBLE);
                    txtLose.setVisibility(View.GONE);
                    txtIncentive.setVisibility(View.VISIBLE);
                    txtReward.setVisibility(View.VISIBLE);
                    txtIncentive.setText(Incentive + " Points");
                    txtMsg.setText(randomStr);
                }

                int[] images = {R.drawable.gift_three, R.drawable.ift1, R.drawable.ift2};
                Random rand = new Random();
                scratch.setWatermark(images[rand.nextInt(images.length)]);

                scratch.setEraseStatusListener(new ScratchView.EraseStatusListener() {
                    @Override
                    public void onProgress(int percent) {
                        if (percent > 30) {
                            imgCancel.setVisibility(View.VISIBLE);
                            txtMsg.setVisibility(View.VISIBLE);
                            if (Incentive > 0) {
                                txtEarned.setVisibility(View.VISIBLE);
                                txtHicare.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onCompleted(View view) {

                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showRatingDialog() {
        try {
            LayoutInflater li = LayoutInflater.from(this);

            View promptsView = li.inflate(R.layout.dialog_rating, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            alertDialogBuilder.setView(promptsView);

            alertDialogBuilder.setTitle("Customer Ratings");
            final AlertDialog alertDialog = alertDialogBuilder.create();

            final ColorRatingBar ratingBar =
                    promptsView.findViewById(R.id.rating_bar);

            final AppCompatButton btn_submit =
                    promptsView.findViewById(R.id.btn_submit);

            btn_submit.setOnClickListener(v -> {
                Rate = (int) ratingBar.getRating();
                alertDialog.dismiss();
                new GPSUtils(this).turnGPSOn(isGPSEnable -> {
                    // turn on GPS
                    if (isGPSEnable) {
//                    saveTaskDetails();
                    } else {
                        isGPS = isGPSEnable;
                    }
                });

            });
            alertDialog.setIcon(R.mipmap.logo);
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void status(String s) {
        Status = s;
    }

    @Override
    public void mode(String s) {
        Payment_Mode = s;
    }

    @Override
    public void amountCollected(String s) {
//        String tag = "android:switcher:" + R.id.viewpagertab + ":" + 1;
        Amount_Collected = s;
//        Log.i("AMOUNT_COLLECTED", s);
//        mAboutDataListener.onDataReceived(s);
    }

    @Override
    public void amountCollectedAndType(String amount, String type) {
        if (amount != null && type != null) {
            mAboutDataListener.onDataReceived(amount, type);
        }
    }

    @Override
    public void amountToCollect(String s) {
        Amount_To_Collected = s;
    }

    @Override
    public void feedbackCode(String s) {
        Feedback_Code = s;
    }


    @Override
    public void signatory(String s) {
        signatory = s;
    }

    @Override
    public void signature(String s) {
        Signature = s;
    }

    @Override
    public void duration(String s) {
        Duration = s;
    }

    @Override
    public void chemicalList(HashMap<Integer, String> map) {
        mMap = map;
    }

    @Override
    public void chemReqList(List<TaskChemicalList> mList) {
        ChemReqList = mList;
    }

    @Override
    public void isGeneralChanged(Boolean b) {
        isGeneralChanged = b;
    }

    @Override
    public void isChemicalChanged(Boolean b) {
        isChemicalChanged = b;
    }

    @Override
    public void isChemicalVerified(Boolean b) {
        isChemicalVerified = b;
    }

    @Override
    public void isActualChemicalChanged(Boolean b) {
        isActualChemicalChanged = b;
    }

    @Override
    public void isPaymentChanged(Boolean b) {
        isPaymentChanged = b;
    }

    @Override
    public void isSignatureChanged(Boolean b) {
        isSignatureChanged = b;
    }

    @Override
    public void isSignatureValidated(Boolean b) {
        isSignatureValidated = b;
    }


    @Override
    public void isOTPValidated(Boolean b) {
        isOTPValidated = b;
    }

    @Override
    public void isOTPRequired(Boolean b) {
        isOTPRequired = b;
    }

    @Override
    public void isFeedbackRequired(Boolean b) {
        isFeedbackRequired = b;
    }

    @Override
    public void getIncompleteReason(String s) {
        incompleteReason = s;
    }

    @Override
    public void isAttachmentError(Boolean b) {
//        isCardRequired = b;
    }

    @Override
    public void isIncompleteReason(Boolean b) {
        isIncompleteReason = b;
    }

    @Override
    public void bankName(String s) {
        bankName = s;
    }

    @Override
    public void chequeNumber(String s) {
        chequeNumber = s;
    }

    @Override
    public void chequeDate(String s) {
        chequeDate = s;
    }

    @Override
    public void chequeImage(String s) {
        chequeImage = s;
    }

    @Override
    public void isAmountCollectedRequired(Boolean b) {
        isAmountCollectedRequired = b;
    }

    @Override
    public void isBankNameRequired(Boolean b) {
        isBankNameRequired = b;
    }

    @Override
    public void isChequeDateRequired(Boolean b) {
        isChequeDateRequired = b;
    }

    @Override
    public void isChequeNumberRequired(Boolean b) {
        isChequeNumberRequired = b;
    }

    @Override
    public void isInvalidChequeNumber(Boolean b) {
        isInvalidChequeNumber = b;
    }

    @Override
    public void isChequeImageRequired(Boolean b) {
        isChequeImageRequired = b;
    }

    @Override
    public void isACEquals(Boolean b) {
        isAmountCollectedEquals = b;
    }

    @Override
    public void isOnsiteOtp(Boolean b) {
        isOnsiteOtpValidated = b;
    }

    @Override
    public void isEmptyOnsiteOtp(Boolean b) {
        isOnsiteOtpRequired = b;
    }

    @Override
    public void onSiteOtp(String s) {
        OnsiteOTP = s;
    }

    @Override
    public void isEarlyCompletion(Boolean b) {
        isEarlyCompletion = b;
    }

    @Override
    public void isUPIPaymentNotDone(Boolean b) {
        isUpiPaymentNotDone = b;
    }

    @Override
    public void isJobCheckListDone(Boolean b) {
        isPostJobCompletionDone = b;
    }

    @Override
    public void isJobCardEnable(Boolean b) {
        isCardRequired = b;
    }

    @Override
    public void isWorkTypeNotChecked(Boolean b) {
        isWorkTypeNotChecked = b;
    }

    @Override
    public void FlushOutReason(String s) {
        flushoutReason = s;
    }

    @Override
    public void isPaymentOtpRequired(Boolean b) {
        isPaymentOtpRequired = b;
    }

    @Override
    public void isPaymentOtpvalidated(Boolean b) {
        isPaymentOtpValidated = b;
    }

    @Override
    public void isPaymentModeNotChanged(Boolean b) {
        isPaymentModeNotChanged = b;
    }


    @Override
    public void assignmentStartDate(String s) {
        assignmentStartDate = s;
    }


    @Override
    public void assignmentStartTime(String s) {
        assignmentStartTime = s;
    }

    @Override
    public void assignmentEndTime(String s) {
        assignmentEndTime = s;
    }


    public interface OnAboutDataReceivedListener {
        void onDataReceived(String amount, String type);
    }

    public void setAboutDataListener(OnAboutDataReceivedListener listener) {
        this.mAboutDataListener = listener;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }
    interface ImageUploaded{
        void uploaded();
    }
    void setOnUploadedListener(ImageUploaded imageUploaded){
        this.imageUploaded = imageUploaded;
    }
}
