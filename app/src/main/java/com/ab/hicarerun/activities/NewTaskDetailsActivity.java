package com.ab.hicarerun.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.slidingpanelayout.widget.SlidingPaneLayout;
import androidx.viewpager.widget.PagerAdapter;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.hicarerun.BaseActivity;
import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.R;
import com.ab.hicarerun.adapter.TaskViewPagerAdapter;
import com.ab.hicarerun.databinding.ActivityNewTaskDetailsBinding;
import com.ab.hicarerun.fragments.ChemicalInfoFragment;
import com.ab.hicarerun.fragments.ChemicalMSTFragment;
import com.ab.hicarerun.fragments.ChemicalMSTInfoFragment;
import com.ab.hicarerun.fragments.ReferralFragment;
import com.ab.hicarerun.fragments.ServiceInfoFragment;
import com.ab.hicarerun.fragments.SignatureInfoFragment;
import com.ab.hicarerun.fragments.SignatureMSTInfoFragment;
import com.ab.hicarerun.handler.OnSaveEventHandler;
import com.ab.hicarerun.handler.UserTaskDetailsClickListener;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.GeneralModel.GeneralResponse;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.network.models.TaskModel.TaskChemicalList;
import com.ab.hicarerun.network.models.TaskModel.Tasks;
import com.ab.hicarerun.network.models.TaskModel.UpdateTaskResponse;
import com.ab.hicarerun.network.models.TaskModel.UpdateTasksRequest;
import com.ab.hicarerun.utils.AppUtils;
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
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import es.dmoral.toasty.Toasty;
import hyogeun.github.com.colorratingbarlib.ColorRatingBar;
import io.realm.RealmResults;

import static com.ab.hicarerun.BaseApplication.getRealm;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.EXPANDED;

public class NewTaskDetailsActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener, OnMapReadyCallback, UserTaskDetailsClickListener, OnSaveEventHandler {
    ActivityNewTaskDetailsBinding mActivityNewTaskDetailsBinding;
    private static final int TASK_BY_ID_REQUEST = 1000;
    private static final int UPDATE_REQUEST = 1000;
    int height = 100;
    int width = 100;
    private ProgressDialog progress;
    SupportMapFragment mapFragment;
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mGoogleMap;
    private LocationManager mLocationManager;
    public static final String ARGS_TASKS = "ARGS_TASKS";
    public static final String ARG_USER = "ARG_USER";
    public static final String LAT_LONG = "LAT_LONG";
    private static final String TAG = "NewTaskDetailsActivity";
    private Location mLocation;
    private long UPDATE_INTERVAL = 20 * 10000;  /* 10 secs */
    private long FASTEST_INTERVAL = 20000; /* 2 sec */
    private LocationRequest mLocationRequest;
    private LocationManager locationManager;
    private Tasks model;
    private Marker mCurrLocationMarker;
    private Marker mCustomerMarker;
    private TaskViewPagerAdapter mAdapter;
    private String userId = "";
    private String sta = "";
    private Boolean isIncentiveEnable = false;
    private int Incentive = 0;
    private boolean isTechnicianFeedbackEnable = false;
    private int TechnicianRating = 0;
    private int referralDiscount = 0;
    private Double Lat = 0.0;
    private Double Lon = 0.0;
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
    private String bankName = "";
    private String chequeNumber = "";
    private String chequeDate = "";
    private String chequeImage = "";
    private String incompleteReason = "";
    private HashMap<Integer, String> mMap = null;
    private List<TaskChemicalList> ChemReqList = null;
    private int Rate = 0;
    private Circle mCircle;
    private Toasty mToastToShow;
    private double mCircleRadius = 150;
    private byte[] bitUser = null;

    LatLngBounds.Builder builder;
    CameraUpdate cu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityNewTaskDetailsBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_new_task_details);
        mActivityNewTaskDetailsBinding.setHandler(this);
//        setViewPagerView();
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray typedArray = obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        mActivityNewTaskDetailsBinding.save.setBackgroundResource(backgroundResource);
        setSupportActionBar(mActivityNewTaskDetailsBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mActivityNewTaskDetailsBinding.pager.setOffscreenPageLimit(4);
        setTitle("");
        model = getIntent().getParcelableExtra(ARGS_TASKS);
        bitUser = getIntent().getByteArrayExtra(ARG_USER);
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
        progress = new ProgressDialog(this, R.style.TransparentProgressDialog);
        progress.setCancelable(false);
        getTaskDetailsById();
    }

    private void getTaskDetailsById() {
        try {
            try {
                AppUtils.getDataClean();
            } catch (Exception e) {
                e.printStackTrace();
            }
            RealmResults<LoginResponse> LoginRealmModels =
                    getRealm().where(LoginResponse.class).findAll();

            if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                userId = LoginRealmModels.get(0).getUserID();
                NetworkCallController controller = new NetworkCallController();
                controller.setListner(new NetworkResponseListner<GeneralResponse>() {

                    @Override
                    public void onResponse(int requestCode, GeneralResponse response) {
                        // add new record
                        getRealm().beginTransaction();
                        getRealm().copyToRealmOrUpdate(response.getData());
                        getRealm().commitTransaction();
                        sta = response.getData().getSchedulingStatus();
                        isIncentiveEnable = response.getData().getIncentiveEnable();
                        Incentive = Integer.parseInt(response.getData().getIncentivePoint());
                        isTechnicianFeedbackEnable = response.getData().getTechnicianFeedbackRequired();
                        TechnicianRating = Integer.parseInt(response.getData().getTechnicianRating());
                        referralDiscount = Integer.parseInt(response.getData().getReferralDiscount());
                        setViewPagerView();
                    }

                    @Override
                    public void onFailure(int requestCode) {
                    }
                });
                controller.getTaskDetailById(TASK_BY_ID_REQUEST, userId, model.getTaskId(), model.getCombinedTask(), NewTaskDetailsActivity.this);
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

    private void setViewPagerView() {
        try {
            mAdapter = new TaskViewPagerAdapter(getSupportFragmentManager(), this);

            if (sta.equals("Dispatched") || sta.equals("Incomplete")) {
                mAdapter.addFragment(ServiceInfoFragment.newInstance(model), "Service Info");
                mActivityNewTaskDetailsBinding.viewpagertab.setDistributeEvenly(false);
            } else {
                mAdapter.addFragment(ServiceInfoFragment.newInstance(model), "Service Info");
                mAdapter.addFragment(ChemicalInfoFragment.newInstance(model), "Chemicals");
                mAdapter.addFragment(ReferralFragment.newInstance(model), "Referrals");
                if (model.getCombinedTask()) {
                    mAdapter.addFragment(SignatureMSTInfoFragment.newInstance(model), "Signature");
                } else {
                    mAdapter.addFragment(SignatureInfoFragment.newInstance(model), "Signature");
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
                        lnrOffer.setVisibility(View.VISIBLE);
                        if (referralDiscount > 0) {
                            txtDiscount.setText(String.valueOf(referralDiscount));
                        } else {
                            lnrOffer.setVisibility(View.INVISIBLE);
                        }
                        break;
                    case 3:
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
                passData();
                mActivityNewTaskDetailsBinding.pager.setCurrentItem(0, true);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void passData() {
        SharedPreferencesUtility.savePrefBoolean(NewTaskDetailsActivity.this, SharedPreferencesUtility.PREF_REFRESH, true);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
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
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    public void onLocationChanged(Location location) {
        Lat = location.getLatitude();
        Lon = location.getLongitude();
        changeMap(location.getLatitude(), location.getLongitude());
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
        mGoogleMap = googleMap;
    }


    private void changeMap(Double lat, Double lon) {

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
            mGoogleMap.setMyLocationEnabled(false);
            mGoogleMap.setTrafficEnabled(false);
            mGoogleMap.setIndoorEnabled(false);
            mGoogleMap.getUiSettings().setCompassEnabled(false);
            mGoogleMap.setBuildingsEnabled(false);
            mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
            setBounds(latLong);
        } else {
            Toast.makeText(this,
                    "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                    .show();
        }

    }

    private void setBounds(LatLng latLong) {
        try {
            if (mGoogleMap != null) {
                mGoogleMap.clear();
                RealmResults<LoginResponse> LoginRealmModels =
                        getRealm().where(LoginResponse.class).findAll();

                if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                    LatLng latLongc = new LatLng(Double.parseDouble(model.getCustomerLatitude()), Double.valueOf(model.getCustomerLongitude()));
                    Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.customer_marker);
                    List<Marker> markersList = new ArrayList<>();
                    BitmapDescriptor CustomerMarkerIcon = BitmapDescriptorFactory.fromBitmap(AppUtils.createCustomMarker(this, b, model.getAccountName(), "Customer"));
                    MarkerOptions markerOptionsCust = new MarkerOptions();
                    markerOptionsCust.position(latLongc);
                    markerOptionsCust.title("Customer's Location");
                    markerOptionsCust.icon(CustomerMarkerIcon);

                    Bitmap bmp = BitmapFactory.decodeByteArray(bitUser, 0, bitUser.length);
                    BitmapDescriptor homeMarkerIcon = BitmapDescriptorFactory.fromBitmap(AppUtils.createCustomMarker(this, bmp, LoginRealmModels.get(0).getUserName(), "Resource"));
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


                    mCircle.setRadius(distanceRange+2000);
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
                    cu = CameraUpdateFactory.newLatLngBounds(bounds, 50);
                    /**call the map call back to know map is loaded or not*/
                    mGoogleMap.setOnMapLoadedCallback(() -> {
                        /**set animated zoom camera into map*/
                        mGoogleMap.animateCamera(cu);

                    });

                }
            }

        }catch (Exception e){
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

    protected void startLocationUpdates() {
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
        saveTaskDetails();
    }

    private void saveTaskDetails() {
        try {
            progress.show();
            Log.i("Status", Status);
            isAttachment = SharedPreferencesUtility.getPrefBoolean(this, SharedPreferencesUtility.PREF_ATTACHMENT);
            if (isGeneralChanged) {
                mActivityNewTaskDetailsBinding.pager.setCurrentItem(0);
                Toasty.error(this, "Please change status", Toast.LENGTH_SHORT, true).show();
                progress.dismiss();
            } else if (isEarlyCompletion && Status.equals("Completed")) {
                mActivityNewTaskDetailsBinding.pager.setCurrentItem(0);
                Toasty.error(this, "You are not allowed to close the job as you have not spent adequate time. Please follow the correct procedure and deliver the job properly", Toasty.LENGTH_LONG, true).show();
                progress.dismiss();
            } else if (isOnsiteOtpRequired && Status.equals("On-Site")) {
                mActivityNewTaskDetailsBinding.pager.setCurrentItem(0);
                Toasty.error(this, "On-Site OTP is required", Toast.LENGTH_SHORT, true).show();
                progress.dismiss();
            } else if (isOnsiteOtpValidated && Status.equals("On-Site")) {
                mActivityNewTaskDetailsBinding.pager.setCurrentItem(0);
                Toasty.error(this, "Invalid On-Site OTP", Toast.LENGTH_SHORT, true).show();
                progress.dismiss();
            } else if (isIncompleteReason && Status.equals("Incomplete")) {
                mActivityNewTaskDetailsBinding.pager.setCurrentItem(0);
                Toasty.error(this, "Please select incomplete reason", Toast.LENGTH_SHORT, true).show();
                progress.dismiss();
            } else if (isAmountCollectedRequired && Status.equals("Completed")) {
                mActivityNewTaskDetailsBinding.pager.setCurrentItem(0);
                Toasty.error(this, "Amount collected field is required", Toast.LENGTH_SHORT, true).show();
                progress.dismiss();
            } else if (isAmountCollectedEquals && Status.equals("Completed")) {
                mActivityNewTaskDetailsBinding.pager.setCurrentItem(0);
                Toasty.error(this, "Invalid amount", Toast.LENGTH_SHORT, true).show();
                progress.dismiss();
            } else if (isBankNameRequired && Status.equals("Completed")) {
                mActivityNewTaskDetailsBinding.pager.setCurrentItem(0);
                Toasty.error(this, "Please select bank name", Toast.LENGTH_SHORT, true).show();
                progress.dismiss();
            } else if (isChequeDateRequired && Status.equals("Completed")) {
                mActivityNewTaskDetailsBinding.pager.setCurrentItem(0);
                Toasty.error(this, "Please select cheque date", Toast.LENGTH_SHORT, true).show();
                progress.dismiss();
            } else if (isChequeNumberRequired && Status.equals("Completed")) {
                mActivityNewTaskDetailsBinding.pager.setCurrentItem(0);
                Toasty.error(this, "Cheque number is required", Toast.LENGTH_SHORT, true).show();
                progress.dismiss();
            } else if (isInvalidChequeNumber && Status.equals("Completed")) {
                mActivityNewTaskDetailsBinding.pager.setCurrentItem(0);
                Toasty.error(this, "Invalid cheque number", Toast.LENGTH_SHORT, true).show();
                progress.dismiss();
            } else if (isChequeImageRequired && Status.equals("Completed")) {
                mActivityNewTaskDetailsBinding.pager.setCurrentItem(0);
                Toasty.error(this, "Please upload cheque image", Toast.LENGTH_SHORT, true).show();
                progress.dismiss();
            } else if (isChemicalChanged && Status.equals("Completed")) {
                mActivityNewTaskDetailsBinding.pager.setCurrentItem(1);
                Toasty.error(this, "Enter the correct value of chemicals used", Toast.LENGTH_SHORT, true).show();
                progress.dismiss();
            } else if (isChemicalVerified && Status.equals("Completed")) {
                mActivityNewTaskDetailsBinding.pager.setCurrentItem(1);
                Toasty.error(this, "Chemical should be verified", Toast.LENGTH_SHORT, true).show();
                progress.dismiss();
            } else if (isOTPRequired && Status.equals("Completed")) {
                mActivityNewTaskDetailsBinding.pager.setCurrentItem(3);
                progress.dismiss();
                Toasty.error(this, "OTP field is required", Toast.LENGTH_SHORT, true).show();
            } else if (isOTPValidated && Status.equals("Completed")) {
                mActivityNewTaskDetailsBinding.pager.setCurrentItem(3);
                progress.dismiss();
                Toasty.error(this, "Invalid OTP", Toast.LENGTH_SHORT, true).show();
            } else if (isSignatureChanged && Status.equals("Completed")) {
                mActivityNewTaskDetailsBinding.pager.setCurrentItem(3);
                progress.dismiss();
                Toasty.error(this, "Signatory field is required", Toast.LENGTH_SHORT, true).show();
            } else if (isSignatureValidated && Status.equals("Completed")) {
                mActivityNewTaskDetailsBinding.pager.setCurrentItem(3);
                progress.dismiss();
                Toasty.error(this, "Customer signature is required", Toast.LENGTH_SHORT, true).show();
            } else if (isCardRequired && Status.equals("Completed")) {
                mActivityNewTaskDetailsBinding.pager.setCurrentItem(3);
                progress.dismiss();
                Toasty.error(this, "Please upload your job card", Toast.LENGTH_SHORT, true).show();
            } else {
                if (isTechnicianFeedbackEnable && Rate == 0 && Status.equals("Completed")) {
                    showRatingDialog();
                } else {
                    RealmResults<LoginResponse> LoginRealmModels =
                            getRealm().where(LoginResponse.class).findAll();
                    if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
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
                        if (model.getCombinedTask()) {
                            request.setCombinedTaskId(model.getCombinedTaskId());
                        } else {
                            request.setTaskId(model.getTaskId());
                        }
                        request.setCombinedTask(model.getCombinedTask());
                        request.setDuration(Duration);
                        request.setResourceId(UserId);
                        request.setTechnicianOnsiteOTP(OnsiteOTP);
                        request.setChemicalList(ChemReqList);
                        request.setIncompleteReason(incompleteReason);
                        request.setChequeImage(chequeImage);

                        NetworkCallController controller = new NetworkCallController();
                        controller.setListner(new NetworkResponseListner() {
                            @Override
                            public void onResponse(int requestCode, Object response) {
                                UpdateTaskResponse updateResponse = (UpdateTaskResponse) response;
                                if (updateResponse.getSuccess()) {
                                    progress.dismiss();
                                    Toasty.success(NewTaskDetailsActivity.this, updateResponse.getErrorMessage(), Toast.LENGTH_SHORT).show();

                                    if (isIncentiveEnable && Status.equals("Completed")) {
                                        showIncentiveDialog();
                                    } else {
                                        onBackPressed();
                                    }
                                } else {
                                    progress.dismiss();
                                    Toast.makeText(NewTaskDetailsActivity.this, updateResponse.getErrorMessage(), Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(int requestCode) {
                                progress.dismiss();
                            }
                        });
                        controller.updateTasks(UPDATE_REQUEST, request, NewTaskDetailsActivity.this);
                    }

                }
            }
        } catch (Exception e) {
            RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                AppUtils.sendErrorLogs(e.getMessage(), getClass().getSimpleName(), "getSaveMenu", lineNo, userName, DeviceName);
            }
        }

    }


    private void showIncentiveDialog() {
        View view = getLayoutInflater().inflate(R.layout.new_scratchcard_layout, null);
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        Window window = dialog.getWindow();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
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
            dialog.getWindow().setLayout(width, height);
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
    }

    private void showRatingDialog() {
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
            if (AppUtils.isGpsEnabled(NewTaskDetailsActivity.this)) {
                saveTaskDetails();
            }
        });

        alertDialog.setIcon(R.mipmap.logo);

        alertDialog.show();
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
        Amount_Collected = s;
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
    public void isJobCardEnable(Boolean b) {
        isCardRequired = b;
    }

}
