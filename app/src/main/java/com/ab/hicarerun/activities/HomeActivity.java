package com.ab.hicarerun.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.assist.AssistContent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ab.hicarerun.BaseActivity;
import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.ActivityHomeBinding;
import com.ab.hicarerun.fragments.AttendanceViewFragment;
import com.ab.hicarerun.fragments.BazaarFragment;
import com.ab.hicarerun.fragments.HomeFragment;
import com.ab.hicarerun.fragments.IncentiveFragment;
import com.ab.hicarerun.fragments.LeaderBoardFragment;
import com.ab.hicarerun.fragments.NotificationFragment;
import com.ab.hicarerun.fragments.OffersFragment;
import com.ab.hicarerun.fragments.TechIdFragment;
import com.ab.hicarerun.fragments.VoucherFragment;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.HandShakeModel.HandShake;
import com.ab.hicarerun.network.models.IncentiveModel.Incentive;
import com.ab.hicarerun.network.models.IncentiveModel.IncentiveData;
import com.ab.hicarerun.network.models.KarmaModel.Karma;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.network.models.LogoutResponse;
import com.ab.hicarerun.network.models.MenuModel.MenuData;
import com.ab.hicarerun.network.models.ProductCartModel.ProductCart;
import com.ab.hicarerun.network.models.ProfileModel.Profile;
import com.ab.hicarerun.service.LocationManager;
import com.ab.hicarerun.service.ServiceLocationSend;
import com.ab.hicarerun.service.listner.LocationManagerListner;
import com.ab.hicarerun.utils.AppUtils;
import com.ab.hicarerun.utils.CustomBottomNavigation;
import com.ab.hicarerun.utils.GPSUtils;
import com.ab.hicarerun.utils.HandShakeReceiver;
import com.ab.hicarerun.utils.LocaleHelper;
import com.ab.hicarerun.utils.SharedPreferencesUtility;
import com.ab.hicarerun.utils.notifications.OneSIgnalHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.navigation.NavigationView;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import io.realm.RealmResults;

import static com.ab.hicarerun.BaseApplication.getRealm;


public class HomeActivity extends BaseActivity implements FragmentManager.OnBackStackChangedListener, LocationManagerListner {
    ActivityHomeBinding mActivityHomeBinding;

    private static final int LOGOUT_REQ = 1000;
    private static final int UPDATE_REQ = 2000;
    private static final int REQ_PROFILE = 3000;
    private static final int REQ_INCENTIVE = 4000;
    private static final int REQ_KARMA = 5000;
    private static int VIDEO_REQUEST = 100;
    private Location mLocation;
    private LocationManagerListner mListner;
    public static final String ARG_HANDSHAKE = "ARG_HANDSHAKE";
    public static final String ARG_EVENT = "ARG_EVENT";
    public static final String ARG_USER = "ARG_USER";
    RealmResults<LoginResponse> LoginRealmModels = null;
    List<HandShake> items = null;
    boolean isClicked = false;
    String userName = "";
    String userId = "";
    int value = 0;
    private boolean isGPS = false;
    private android.location.LocationManager locationManager;
    private AlarmManager mAlarmManager = null;
    private PendingIntent pendingUpdateIntent = null;
    private Bitmap bitUser = null;
    //    private byte[] userByte = null;
    private ProgressDialog progress;
    private CameraManager mCameraManager;
    private String mCameraId;
    private List<MenuData> menuData = null;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        try {
            super.onSaveInstanceState(outState);
            //  mActivityHomeBinding.bottomNavigation.onSaveInstanceState(outState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        try {
            super.attachBaseContext(LocaleHelper.onAttach(base, LocaleHelper.getLanguage(base)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        intent.setClassName("com.miui.securitycenter",
                "com.miui.permcenter.permissions.PermissionsEditorActivity");
        intent.putExtra("extra_pkgname", getPackageName());
        startActivity(intent);*/
        mActivityHomeBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_home);
//        setSupportActionBar(mActivityHomeBinding.toolbar);
        progress = new ProgressDialog(this, R.style.TransparentProgressDialog);
        progress.setCancelable(false);
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            mCameraId = mCameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        try {
            mCameraManager.setTorchMode(mCameraId, false);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        Log.d("TAG", new OneSIgnalHelper(this).getmStrUserID());
        AppUtils.IS_FLASH_ON = true;
        mActivityHomeBinding.toolbar.imgFlash.setImageResource(R.drawable.flash_off);
        mActivityHomeBinding.toolbar.lnrDrawer.setOnClickListener(view -> mActivityHomeBinding.drawer.openDrawer(GravityCompat.START));
        try {
            new GPSUtils(this).turnGPSOn(isGPSEnable -> {
                // turn on GPS
                if (isGPSEnable) {
                    try {
                        progress.show();
                        getServiceCalled();
                        getTechDeails();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

//                    getIncentiveDetails();
                } else {
                    isGPS = isGPSEnable;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
//        }

//        CustomBottomNavigation curvedBottomNavigationView = findViewById(R.id.customBottomBar);
//        curvedBottomNavigationView.inflateMenu(R.menu.bottom_navigation_menu);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Superhero.ttf");
        mActivityHomeBinding.toolbar.txtLifeCount.setTypeface(typeface);
        mActivityHomeBinding.toolbar.txtRemLife.setTypeface(typeface);
        mActivityHomeBinding.toolbar.txtLife.setTypeface(typeface);
        mActivityHomeBinding.toolbar.lnrUser.setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, TechIdActivity.class).putExtra(HomeActivity.ARG_EVENT, false)));
        mActivityHomeBinding.toolbar.lnrWallet.setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, IncentivesActivity.class).putExtra(HomeActivity.ARG_EVENT, false)));
        mActivityHomeBinding.toolbar.lnrKarma.setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, KarmaActivity.class).putExtra(HomeActivity.ARG_EVENT, false)));
        mActivityHomeBinding.toolbar.lnrVideo.setOnClickListener(v -> {
            Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            if (videoIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(videoIntent, VIDEO_REQUEST);
            }
        });
        mActivityHomeBinding.toolbar.lnrFlash.setOnClickListener(v -> {
            try {
                if (AppUtils.IS_FLASH_ON) {
                    mCameraManager.setTorchMode(mCameraId, true);
                    AppUtils.IS_FLASH_ON = false;
                    mActivityHomeBinding.toolbar.imgFlash.setImageResource(R.drawable.flash_on);
                } else {
                    mCameraManager.setTorchMode(mCameraId, false);
                    AppUtils.IS_FLASH_ON = true;
                    mActivityHomeBinding.toolbar.imgFlash.setImageResource(R.drawable.flash_off);
                }

            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        });

        try {
            isClicked = Objects.requireNonNull(getIntent().getExtras()).getBoolean(ARG_EVENT, false);
            LoginRealmModels =
                    BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                LocationManager.Builder builder = new LocationManager.Builder(this);
                builder.setLocationListner(this);
                builder.build();
                userId = Objects.requireNonNull(LoginRealmModels.get(0)).getUserID();
                AppUtils.resourceId = userId;
                SharedPreferencesUtility.savePrefString(HomeActivity.this, SharedPreferencesUtility.PREF_USERID, userId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        mActivityHomeBinding.bottomNavigation.initWithSaveInstanceState(savedInstanceState);

        mActivityHomeBinding.bottomNavigation.addSpaceItem(new SpaceItem("Home", R.drawable.ic_home));
        mActivityHomeBinding.bottomNavigation.addSpaceItem(new SpaceItem("Incentives", R.drawable.ic_rupees));
        mActivityHomeBinding.bottomNavigation.addSpaceItem(new SpaceItem("Attendance", R.drawable.ic_icon));
        mActivityHomeBinding.bottomNavigation.addSpaceItem(new SpaceItem("ID Card", R.drawable.ic__avatar_user));
        mActivityHomeBinding.bottomNavigation.hideAllBadges();
        mActivityHomeBinding.fab.bringToFront();
        mActivityHomeBinding.relCoin.setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, OfferActivity.class)));

        handleIntent(getIntent());
        getResourcesKarma();
    }

    private void getResourcesKarma() {
        try {
            LoginRealmModels =
                    BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                userId = Objects.requireNonNull(LoginRealmModels.get(0)).getUserID();
                NetworkCallController controller = new NetworkCallController();
                controller.setListner(new NetworkResponseListner<Karma>() {
                    @Override
                    public void onResponse(int requestCode, Karma response) {
                        try {
                            if (response != null) {
                                mActivityHomeBinding.toolbar.lnrKarma.setVisibility(View.VISIBLE);
                                int progress = response.getTotalPointsPending();
                                Drawable batteryProgressD = mActivityHomeBinding.toolbar.progressBar.getProgressDrawable();
                                batteryProgressD.setLevel(progress * 100);
                                mActivityHomeBinding.toolbar.progressBar.setProgress(progress);
                                mActivityHomeBinding.toolbar.txtRemLife.setText(String.valueOf(response.getTotalPointsPending()) + "/" + String.valueOf(response.getTotalPoints()));
                                mActivityHomeBinding.toolbar.txtLifeCount.setText(String.valueOf(response.getLifeLineIndex()));
                            } else {
                                mActivityHomeBinding.toolbar.lnrKarma.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int requestCode) {

                    }
                });
                controller.getKarmaResources(REQ_KARMA, userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setupNavigationView() {
        try {
            if (mActivityHomeBinding.bottomNavigation != null) {
                // Select first menu item by default and show Fragment accordingly.
                Menu menu = mActivityHomeBinding.customNavigation.getMenu();
                selectFragment(menu.getItem(0));
                mActivityHomeBinding.customNavigation.setOnNavigationItemSelectedListener(menuItem -> {
                    selectFragment(menuItem);
                    return false;
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProvideAssistContent(AssistContent outContent) {
        super.onProvideAssistContent(outContent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                outContent.setStructuredData((new JSONObject())
                        .put("@type", "ExerciseObservation")
                        .put("name", "My last runs")
                        .put("url", "https://fit-actions.firebaseapp.com/stats")
                        .toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }


    private void selectFragment(MenuItem item) {
        try {
            item.setChecked(true);
            switch (item.getItemId()) {
                case R.id.nav_home:
                    replaceFragment(HomeFragment.newInstance(/*userByte*/), "HOME");
                    break;
                case R.id.nav_incentive:
                    replaceFragment(IncentiveFragment.newInstance(), "INCENTIVE");
                    break;
                case R.id.nav_bazaar:
                    replaceFragment(BazaarFragment.newInstance(), "BAZAAR");
                    break;
                case R.id.nav_rewards:
//                replaceFragment(VoucherFragment.newInstance(), "HomeActivity-NotificationFragment");
                    break;
                case R.id.nav_attendance:
                    replaceFragment(AttendanceViewFragment.newInstance(), "ATTENDANCE");
                    break;
                case R.id.nav_leader:
                    startActivity(new Intent(HomeActivity.this, LeaderBoardActivity.class));
                    break;

                case R.id.nav_language:
                    mActivityHomeBinding.drawer.closeDrawers();
                    showLanguageDialog();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void showLanguageDialog() {
        try {
            LayoutInflater li = LayoutInflater.from(HomeActivity.this);
            View promptsView = li.inflate(R.layout.layout_language_dialog, null);
            androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(HomeActivity.this);
            alertDialogBuilder.setView(promptsView);
            final androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
            final CardView lanEnglish =
                    promptsView.findViewById(R.id.lanEnglish);
            final CardView lanHindi =
                    promptsView.findViewById(R.id.lanHindi);
            final CardView lanMarathi =
                    promptsView.findViewById(R.id.lanMarathi);
            final CardView lanGujrati =
                    promptsView.findViewById(R.id.lanGujrati);
            final CardView lanTamil =
                    promptsView.findViewById(R.id.lanTamil);
            final CardView lanTelugu =
                    promptsView.findViewById(R.id.lanTelugu);
            final CardView lanKannad =
                    promptsView.findViewById(R.id.lanKannad);
            final Button btnContinue = promptsView.findViewById(R.id.btnOk);
            btnContinue.setOnClickListener(view -> alertDialog.dismiss());

            lanEnglish.setOnClickListener(view -> {
                AppUtils.updateViews("en", HomeActivity.this);
                alertDialog.dismiss();
//                recreate();
                startActivity(getIntent());
            });

            lanHindi.setOnClickListener(view -> {
                AppUtils.updateViews("hi", HomeActivity.this);
                alertDialog.dismiss();
//                recreate();
                startActivity(getIntent());
            });


            lanMarathi.setOnClickListener(view -> {
                AppUtils.updateViews("mr", HomeActivity.this);
                alertDialog.dismiss();
//                recreate();
                startActivity(getIntent());
            });

            lanGujrati.setOnClickListener(view -> {
                AppUtils.updateViews("gu", HomeActivity.this);
                alertDialog.dismiss();
                startActivity(getIntent());
            });


            lanTamil.setOnClickListener(view -> {
                AppUtils.updateViews("ta", HomeActivity.this);
                alertDialog.dismiss();
                startActivity(getIntent());
            });

            lanTelugu.setOnClickListener(view -> {
                AppUtils.updateViews("te", HomeActivity.this);
                alertDialog.dismiss();
                startActivity(getIntent());
            });

            lanKannad.setOnClickListener(view -> {
                AppUtils.updateViews("kn", HomeActivity.this);
                alertDialog.dismiss();
                startActivity(getIntent());
            });


            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == Activity.RESULT_OK && requestCode == GPSUtils.GPS_REQUEST) {
                try {
                    isGPS = true; // flag maintain before get location
                    progress.show();
                    getServiceCalled();
                    getTechDeails();
                    getResourcesKarma();
//                getIncentiveDetails();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (requestCode == VIDEO_REQUEST && resultCode == RESULT_OK) {
                AssetFileDescriptor videoAsset = getContentResolver().openAssetFileDescriptor(data.getData(), "r");
                FileInputStream fis = videoAsset.createInputStream();
                File root = new File(Environment.getExternalStorageDirectory(), "/InspectionVideo/");  //you can replace RecordVideo by the specific folder where you want to save the video
                if (!root.exists()) {
                    System.out.println("No directory");
                    root.mkdirs();
                }

                File file;
                file = new File(root, "android_" + System.currentTimeMillis() + ".mp4");

                FileOutputStream fos = new FileOutputStream(file);

                byte[] buf = new byte[1024];
                int len;
                while ((len = fis.read(buf)) > 0) {
                    fos.write(buf, 0, len);
                }
                fis.close();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        try {
            super.onResume();
            if (AppUtils.checkConnection(HomeActivity.this)) {
            } else {
                AppUtils.showOkActionAlertBox(HomeActivity.this, "Please check your internet connection!", (dialogInterface, i) -> finish());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getServiceCalled() {
        try {
            if (isClicked) {
                try {
                    userName = getIntent().getStringExtra(ARG_USER);
                    SharedPreferencesUtility.savePrefString(HomeActivity.this, SharedPreferencesUtility.PREF_USERNAME, userName);
                    items = (List<HandShake>) getIntent().getSerializableExtra(ARG_HANDSHAKE);
                    assert items != null;
                    value = Integer.parseInt(items.get(1).getValue());
                    long REPEATED_TIME = 1000 * 60 * 6;
                    SharedPreferencesUtility.savePrefString(HomeActivity.this, SharedPreferencesUtility.PREF_INTERVAL, String.valueOf(REPEATED_TIME));
                    Log.i("callHandshake", String.valueOf(REPEATED_TIME));
                    SharedPreferencesUtility.savePrefString(HomeActivity.this, SharedPreferencesUtility.PREF_TIME, items.get(1).getValue());
                    if (items.get(0).getText().equals("EnableTrace")) {
                        if (items.get(0).getValue().equals("true")) {
                            Intent intent = new Intent(getApplicationContext(), HandShakeReceiver.class);
                            intent.setAction("HandshakeAction");
                            pendingUpdateIntent = PendingIntent.getBroadcast(getApplicationContext(),
                                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                            Calendar futureDate = Calendar.getInstance();
                            mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                            if (android.os.Build.VERSION.SDK_INT >= 19) {
                                mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, futureDate.getTime().getTime(), REPEATED_TIME, pendingUpdateIntent);
                            } else {
                                mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, futureDate.getTime().getTime(), REPEATED_TIME, pendingUpdateIntent);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                try {
                    if (pendingUpdateIntent != null) {
                        mAlarmManager.cancel(pendingUpdateIntent);
                        getApplicationContext().stopService(new Intent(getApplicationContext(), ServiceLocationSend.class));
                    }
                    String time = SharedPreferencesUtility.getPrefString(HomeActivity.this, SharedPreferencesUtility.PREF_TIME);
//                    if (time != null) {
//                        Log.i("TIMEX", time);
//                        int t = Integer.parseInt(time);
//                        value = t;
//                    }
                    long REPEATED_TIME = 1000 * 60 * 6;
                    Intent intent = new Intent(getApplicationContext(), HandShakeReceiver.class);
                    intent.setAction("HandshakeAction");
                    pendingUpdateIntent = PendingIntent.getBroadcast(getApplicationContext(),
                            0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    Calendar futureDate = Calendar.getInstance();
                    mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                    if (android.os.Build.VERSION.SDK_INT >= 19) {
                        mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, futureDate.getTime().getTime(), REPEATED_TIME, pendingUpdateIntent);
                    } else {
                        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, futureDate.getTime().getTime(), REPEATED_TIME, pendingUpdateIntent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getTechDeails() {
        try {
            RealmResults<LoginResponse> LoginRealmModels =
                    BaseApplication.getRealm().where(LoginResponse.class).findAll();
            assert LoginRealmModels.get(0) != null;
            String userId = LoginRealmModels.get(0).getUserID();
            if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                NetworkCallController controller = new NetworkCallController();
                controller.setListner(new NetworkResponseListner() {
                    @Override
                    public void onResponse(int requestCode, Object data) {
                        progress.dismiss();
                        if (data != null) {
                            Profile response = (Profile) data;
                            if (response.getProfilePic() != null) {
                                String base64 = response.getProfilePic();
                                byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                if (base64.length() > 0) {
                                    mActivityHomeBinding.toolbar.imgUser.setImageBitmap(decodedByte);
                                    bitUser = decodedByte;
                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                }
                            }

                        }
//                        setupNavigationView();
//                        initNavigationDrawer();
                        getResourceMenu();

                    }

                    @Override
                    public void onFailure(int requestCode) {
                        progress.dismiss();
                    }
                });
                controller.getTechnicianProfile(REQ_PROFILE, userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setDrawer() {
        try {
            NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
            if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                PackageInfo pInfo = null;
                try {
                    pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                assert pInfo != null;
                String mobileVersion = pInfo.versionName;
                assert LoginRealmModels.get(0) != null;
                String Uname = LoginRealmModels.get(0).getUserName();

                View header = navigationView.getHeaderView(0);
                TextView name = (TextView) header.findViewById(R.id.drawer_name);
                TextView version = (TextView) header.findViewById(R.id.txtVersion);
                ImageView imgUser = header.findViewById(R.id.navUser);
                name.setText("Hi, " + Uname);
                version.setText("V " + mobileVersion);
                if (bitUser != null) {
                    imgUser.setImageBitmap(bitUser);
                }
                mActivityHomeBinding.navigationView.setCheckedItem(0);

            }

            if (navigationView != null) {
                Menu menuBottom = mActivityHomeBinding.customNavigation.getMenu();
                selectFragment(menuBottom.getItem(0));
                mActivityHomeBinding.customNavigation.setOnNavigationItemSelectedListener(menuItem -> {
                    selectFragment(menuItem);
                    return false;
                });
                Menu menu = navigationView.getMenu();
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                if (menuData.size() > 0) {
                    for (int i = 0; i < menuData.size(); i++) {
                        menu.add(menuData.get(i).getTitle());
                        MenuItem mi = menu.getItem(i);
                        mi.setVisible(menuData.get(i).getIsVisible());
                        Glide.with(this).asBitmap().load(menuData.get(i).getIcon()).into(new SimpleTarget<Bitmap>(100, 100) {
                            @Override
                            public void onResourceReady(@NonNull @NotNull Bitmap resource, @Nullable @org.jetbrains.annotations.Nullable Transition<? super Bitmap> transition) {
                                mi.setIcon(new BitmapDrawable(getResources(), resource));
                            }
                        });
                    }

                }

            }

            NavigationView.OnNavigationItemSelectedListener item_click_listener = item -> {

                if (item.toString().equalsIgnoreCase("Home")) {
                    getSupportFragmentManager().beginTransaction().replace(mActivityHomeBinding.container.getId(), HomeFragment.newInstance()).addToBackStack(null).commit();
                    mActivityHomeBinding.drawer.closeDrawers();
                } else if (item.toString().equalsIgnoreCase(this.getResources().getString(R.string.inventory))) {
                    mActivityHomeBinding.drawer.closeDrawers();
                    startActivity(new Intent(HomeActivity.this, InventoryActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
                } else if (item.toString().equalsIgnoreCase(this.getResources().getString(R.string.shiksha))) {
                    mActivityHomeBinding.drawer.closeDrawers();
                    startActivity(new Intent(HomeActivity.this, ActivityQuizCategory.class).putExtra(HomeActivity.ARG_EVENT, false));
                } else if (item.toString().equalsIgnoreCase("Self Access")) {
                    mActivityHomeBinding.drawer.closeDrawers();
                    startActivity(new Intent(HomeActivity.this, AssessChatActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
                } else if (item.toString().equalsIgnoreCase("HiCare Products")) {
                    mActivityHomeBinding.drawer.closeDrawers();
                    startActivity(new Intent(HomeActivity.this, HicareProductsActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
                } else if (item.toString().equalsIgnoreCase("Rewards")) {
                    mActivityHomeBinding.drawer.closeDrawers();
                    replaceFragment(IncentiveFragment.newInstance(), "INCENTIVE");
                } else if (item.toString().equalsIgnoreCase("Your Attendance")) {
                    mActivityHomeBinding.drawer.closeDrawers();
                    replaceFragment(AttendanceViewFragment.newInstance(), "ATTENDANCE");
                } else if (item.toString().equalsIgnoreCase("On-Site Tasks")) {
                    mActivityHomeBinding.drawer.closeDrawers();
                    startActivity(new Intent(HomeActivity.this, OnSiteTaskActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
                } else if (item.toString().equalsIgnoreCase("Bazaar")) {
                    mActivityHomeBinding.drawer.closeDrawers();
                    startActivity(new Intent(HomeActivity.this, BazaarActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
                } else if (item.toString().equalsIgnoreCase("Grooming")) {
                    mActivityHomeBinding.drawer.closeDrawers();
                    startActivity(new Intent(HomeActivity.this, TechnicianSeniorActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
                } else if (item.toString().equalsIgnoreCase("Daily Check-Up")) {
                    mActivityHomeBinding.drawer.closeDrawers();
                    startActivity(new Intent(HomeActivity.this, TechnicianRoutineActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
                } else if (item.toString().equalsIgnoreCase("KYC Verification")) {
                    mActivityHomeBinding.drawer.closeDrawers();
                    startActivity(new Intent(HomeActivity.this, KycActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
                } else if (item.toString().equalsIgnoreCase("Assign QR Code")) {
                    mActivityHomeBinding.drawer.closeDrawers();
                    startActivity(new Intent(HomeActivity.this, TSScannerActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
                } else if (item.toString().equalsIgnoreCase("TS Verification")) {
                    mActivityHomeBinding.drawer.closeDrawers();
                    startActivity(new Intent(HomeActivity.this, TSVerificationActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
                } else if (item.toString().equalsIgnoreCase("Today's Job Count")) {
                    mActivityHomeBinding.drawer.closeDrawers();
                    startActivity(new Intent(HomeActivity.this, TechChemicalCountActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
                } else if (item.toString().equalsIgnoreCase("My ID Card")) {
                    mActivityHomeBinding.drawer.closeDrawers();
                    startActivity(new Intent(HomeActivity.this, TechIdActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
                } else if (item.toString().equalsIgnoreCase("Notifications")) {
                    mActivityHomeBinding.drawer.closeDrawers();
                    getSupportFragmentManager().beginTransaction().replace(mActivityHomeBinding.container.getId(), NotificationFragment.newInstance()).addToBackStack(null).commit();
                } else if (item.toString().equalsIgnoreCase("Training")) {
                    mActivityHomeBinding.drawer.closeDrawers();
                    startActivity(new Intent(HomeActivity.this, TrainingActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
                } else if (item.toString().equalsIgnoreCase("Referral Code")) {
                    mActivityHomeBinding.drawer.closeDrawers();
                    startActivity(new Intent(HomeActivity.this, VoucherActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
                } else if (item.toString().equalsIgnoreCase("Change Language")) {
                    mActivityHomeBinding.drawer.closeDrawers();
                    showLanguageDialog();
                } else if (item.toString().equalsIgnoreCase("Help")) {
                    mActivityHomeBinding.drawer.closeDrawers();
                    startActivity(new Intent(HomeActivity.this, HelpActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
                } else if (item.toString().equalsIgnoreCase("Logout")) {
                    mActivityHomeBinding.drawer.closeDrawers();
                    final AlertDialog.Builder dialog = new AlertDialog.Builder(HomeActivity.this);
                    dialog.setTitle(getString(R.string.logout_exit));
                    dialog.setMessage(getString(R.string.logout_do_you_really_want_to_exit));
                    dialog.setPositiveButton(getString(R.string.yes), (dialogInterface, j) -> {
                        dialogInterface.dismiss();
                        getLogout();
                    });
                    dialog.setNegativeButton(getString(R.string.no), (dialogInterface, j) -> dialogInterface.dismiss());
                    dialog.show();
                }

//                for(int i=0; i< menuData.size(); i++){
//                    if (menuData.get(i).getIntentName().equalsIgnoreCase("Home")) {
//                        getSupportFragmentManager().beginTransaction().replace(mActivityHomeBinding.container.getId(), HomeFragment.newInstance()).addToBackStack(null).commit();
//                        mActivityHomeBinding.drawer.closeDrawers();
//                    }else if( menuData.get(i).getIntentName().equalsIgnoreCase("Quiz")){
//                        mActivityHomeBinding.drawer.closeDrawers();
//                        startActivity(new Intent(HomeActivity.this, ActivityQuizCategory.class).putExtra(HomeActivity.ARG_EVENT, false));
//                    }
//                    else if( menuData.get(i).getIntentName().equalsIgnoreCase("SelfAccess")){
//                        mActivityHomeBinding.drawer.closeDrawers();
//                        startActivity(new Intent(HomeActivity.this, AssessChatActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
//                    }
//                    else if( menuData.get(i).getIntentName().equalsIgnoreCase("Products")){
//                        mActivityHomeBinding.drawer.closeDrawers();
//                        startActivity(new Intent(HomeActivity.this, HicareProductsActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
//                    }
//                    else if( menuData.get(i).getIntentName().equalsIgnoreCase("Rewards")){
//                        mActivityHomeBinding.drawer.closeDrawers();
//                        startActivity(new Intent(HomeActivity.this, IncentivesActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
//                    }
//                    else if( menuData.get(i).getIntentName().equalsIgnoreCase("Attendance")){
//                        mActivityHomeBinding.drawer.closeDrawers();
//                        startActivity(new Intent(HomeActivity.this, AttendanceActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
//                    }
//                    else if( menuData.get(i).getIntentName().equalsIgnoreCase("OnSite")){
//                        mActivityHomeBinding.drawer.closeDrawers();
//                        startActivity(new Intent(HomeActivity.this, OnSiteTaskActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
//                    }
//                    else if( menuData.get(i).getIntentName().equalsIgnoreCase("Bazaar")){
//                        mActivityHomeBinding.drawer.closeDrawers();
//                        startActivity(new Intent(HomeActivity.this, BazaarActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
//                    }
//                    else if( menuData.get(i).getIntentName().equalsIgnoreCase("Grooming")){
//                        mActivityHomeBinding.drawer.closeDrawers();
//                        startActivity(new Intent(HomeActivity.this, TechnicianSeniorActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
//                    }
//                    else if( menuData.get(i).getIntentName().equalsIgnoreCase("DailyCheck")){
//                        mActivityHomeBinding.drawer.closeDrawers();
//                        startActivity(new Intent(HomeActivity.this, TechnicianRoutineActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
//                    }
//                    else if( menuData.get(i).getIntentName().equalsIgnoreCase("KYC")){
//                        mActivityHomeBinding.drawer.closeDrawers();
//                        startActivity(new Intent(HomeActivity.this, KycActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
//                    }
//                    else if( menuData.get(i).getIntentName().equalsIgnoreCase("AssignQR")){
//                        mActivityHomeBinding.drawer.closeDrawers();
//                        startActivity(new Intent(HomeActivity.this, TSScannerActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
//                    }
//                    else if( menuData.get(i).getIntentName().equalsIgnoreCase("TSVerification")){
//                        mActivityHomeBinding.drawer.closeDrawers();
//                        startActivity(new Intent(HomeActivity.this, TSVerificationActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
//                    }
//                    else if( menuData.get(i).getIntentName().equalsIgnoreCase("JobCount")){
//                        mActivityHomeBinding.drawer.closeDrawers();
//                        startActivity(new Intent(HomeActivity.this, TechChemicalCountActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
//                    } else if( menuData.get(i).getIntentName().equalsIgnoreCase("IDCard")){
//                        mActivityHomeBinding.drawer.closeDrawers();
//                        startActivity(new Intent(HomeActivity.this, TechIdActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
//                    }
//                    else if( menuData.get(i).getIntentName().equalsIgnoreCase("Notifications")){
//                        mActivityHomeBinding.drawer.closeDrawers();
//                        getSupportFragmentManager().beginTransaction().replace(mActivityHomeBinding.container.getId(), NotificationFragment.newInstance()).addToBackStack(null).commit();
//                    }
//                    else if( menuData.get(i).getIntentName().equalsIgnoreCase("Training")){
//                        mActivityHomeBinding.drawer.closeDrawers();
//                        startActivity(new Intent(HomeActivity.this, TrainingActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
//                    }
//                    else if( menuData.get(i).getIntentName().equalsIgnoreCase("Referral")){
//                        mActivityHomeBinding.drawer.closeDrawers();
//                        startActivity(new Intent(HomeActivity.this, VoucherActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
//                    }
//                    else if( menuData.get(i).getIntentName().equalsIgnoreCase("Language")){
//                        mActivityHomeBinding.drawer.closeDrawers();
//                        showLanguageDialog();                    }
//                    else if( menuData.get(i).getIntentName().equalsIgnoreCase("Help")){
//                        mActivityHomeBinding.drawer.closeDrawers();
//                        startActivity(new Intent(HomeActivity.this, HelpActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
//                    }
//                    else if( menuData.get(i).getIntentName().equalsIgnoreCase("Logout")){
//                        mActivityHomeBinding.drawer.closeDrawers();
//                        final AlertDialog.Builder dialog = new AlertDialog.Builder(HomeActivity.this);
//                        dialog.setTitle(getString(R.string.logout_exit));
//                        dialog.setMessage(getString(R.string.logout_do_you_really_want_to_exit));
//                        dialog.setPositiveButton(getString(R.string.yes), (dialogInterface, j) -> {
//                            dialogInterface.dismiss();
//                            getLogout();
//                        });
//                        dialog.setNegativeButton(getString(R.string.no), (dialogInterface, j) -> dialogInterface.dismiss());
//                        dialog.show();
//                    }
//
//
//
//                }


                mActivityHomeBinding.drawer.closeDrawers();
                return true;
            };

            navigationView.setNavigationItemSelectedListener(item_click_listener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getResourceMenu() {
        try {


            NetworkCallController controller = new NetworkCallController();
            controller.setListner(new NetworkResponseListner<List<MenuData>>() {

                @Override
                public void onResponse(int requestCode, List<MenuData> response) {
                    menuData = new ArrayList<>();
                    menuData = response;
                    /*for(MenuData m : menuData){
                        if (m.getTitle().equalsIgnoreCase("Shiksha")){
                            m.setIsVisible(true);
                        }
                    }*/
                    setDrawer();
                }

                @Override
                public void onFailure(int requestCode) {

                }
            });
            controller.getResourceMenu(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void getIncentiveDetails() {
//        try {
//            RealmResults<LoginResponse> LoginRealmModels =
//                    BaseApplication.getRealm().where(LoginResponse.class).findAll();
//            assert LoginRealmModels.get(0) != null;
//            String userId = LoginRealmModels.get(0).getUserID();
//            if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
//                NetworkCallController controller = new NetworkCallController();
//                controller.setListner(new NetworkResponseListner() {
//                    @Override
//                    public void onResponse(int requestCode, Object data) {
//                        IncentiveData response = (IncentiveData) data;
//                        mActivityHomeBinding.toolbar.txtIncentive.setText("\u20B9" + " " + response.getTotalIncentiveAmount());
//                    }
//
//                    @Overrideco
//                    public void onFailure(int requestCode) {
//
//                    }
//                });
//                controller.getTechnicianIncentive(REQ_INCENTIVE, userId);
//            }
//        } catch (Exception e) {
//            getLogout();
//            e.printStackTrace();
//        }
//    }

    public void initNavigationDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        LoginRealmModels =
                BaseApplication.getRealm().where(LoginResponse.class).findAll();
        if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
            assert LoginRealmModels.get(0) != null;
            String isTsEnable = LoginRealmModels.get(0).getIsTechnician();
            Menu menu = navigationView.getMenu();
            MenuItem groom = menu.findItem(R.id.nav_groom);
            MenuItem jobCount = menu.findItem(R.id.nav_summary);
            MenuItem routineCheck = menu.findItem(R.id.nav_routine);
            MenuItem assignCode = menu.findItem(R.id.nav_tsScanner);
            MenuItem tsVerify = menu.findItem(R.id.nav_tsVerfication);
            MenuItem inventory = menu.findItem(R.id.nav_inventory);
            if (isTsEnable.equals("0")) {
                groom.setVisible(true);
                jobCount.setVisible(true);
                routineCheck.setVisible(true);
                assignCode.setVisible(true);
                tsVerify.setVisible(true);
                inventory.setVisible(true);

            } else {
                groom.setVisible(false);
                jobCount.setVisible(false);
                routineCheck.setVisible(false);
                assignCode.setVisible(false);
                tsVerify.setVisible(false);
                inventory.setVisible(false);
            }
            PackageInfo pInfo = null;
            try {
                pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            assert pInfo != null;
            String mobileVersion = pInfo.versionName;
            assert LoginRealmModels.get(0) != null;
            String Uname = LoginRealmModels.get(0).getUserName();

            View header = navigationView.getHeaderView(0);
            TextView name = (TextView) header.findViewById(R.id.drawer_name);
            TextView version = (TextView) header.findViewById(R.id.txtVersion);
            ImageView imgUser = header.findViewById(R.id.navUser);
            name.setText("Hi, " + Uname);
            version.setText("V " + mobileVersion);
            if (bitUser != null) {
                imgUser.setImageBitmap(bitUser);
            }
            mActivityHomeBinding.navigationView.setCheckedItem(0);

        }
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            switch (id) {

                case R.id.nav_home:
//                    getSupportFragmentManager().beginTransaction().replace(mActivityHomeBinding.container.getId(), HomeFragment.newInstance()).addToBackStack(null).commit();
                    mActivityHomeBinding.drawer.closeDrawers();
                    break;

                case R.id.nav_inventory:
                    mActivityHomeBinding.drawer.closeDrawers();
                    startActivity(new Intent(HomeActivity.this, InventoryActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
                    break;

                case R.id.nav_quiz:
                    mActivityHomeBinding.drawer.closeDrawers();
                    startActivity(new Intent(HomeActivity.this, ActivityQuizCategory.class).putExtra(HomeActivity.ARG_EVENT, false));
                    break;

                case R.id.nav_chat:
                    mActivityHomeBinding.drawer.closeDrawers();
                    startActivity(new Intent(HomeActivity.this, AssessChatActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
                    break;

                case R.id.nav_products:
                    mActivityHomeBinding.drawer.closeDrawers();
                    startActivity(new Intent(HomeActivity.this, HicareProductsActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
                    break;

                case R.id.nav_incentive:
                    mActivityHomeBinding.drawer.closeDrawers();
                    startActivity(new Intent(HomeActivity.this, IncentivesActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
                    break;

                case R.id.nav_attendance:
                    mActivityHomeBinding.drawer.closeDrawers();
                    startActivity(new Intent(HomeActivity.this, AttendanceActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
                    break;

                case R.id.nav_bazaar:
                    mActivityHomeBinding.drawer.closeDrawers();
                    startActivity(new Intent(HomeActivity.this, BazaarActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
                    break;

                case R.id.nav_onsite:
                    mActivityHomeBinding.drawer.closeDrawers();
                    startActivity(new Intent(HomeActivity.this, OnSiteTaskActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
                    break;

                case R.id.nav_groom:
                    mActivityHomeBinding.drawer.closeDrawers();
                    startActivity(new Intent(HomeActivity.this, TechnicianSeniorActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
                    break;

                case R.id.nav_routine:
                    mActivityHomeBinding.drawer.closeDrawers();
                    startActivity(new Intent(HomeActivity.this, TechnicianRoutineActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
                    break;

                case R.id.nav_kyc:
                    mActivityHomeBinding.drawer.closeDrawers();
                    startActivity(new Intent(HomeActivity.this, KycActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
                    break;

                case R.id.nav_tsScanner:
                    mActivityHomeBinding.drawer.closeDrawers();
                    startActivity(new Intent(HomeActivity.this, TSScannerActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
                    break;

                case R.id.nav_tsVerfication:
                    mActivityHomeBinding.drawer.closeDrawers();
                    startActivity(new Intent(HomeActivity.this, TSVerificationActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
                    break;

                case R.id.nav_summary:
                    mActivityHomeBinding.drawer.closeDrawers();
                    startActivity(new Intent(HomeActivity.this, TechChemicalCountActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
                    break;

                case R.id.nav_notifications:
                    getSupportFragmentManager().beginTransaction().replace(mActivityHomeBinding.container.getId(), NotificationFragment.newInstance()).addToBackStack(null).commit();
                    mActivityHomeBinding.drawer.closeDrawers();
                    break;

                case R.id.nav_training:
                    mActivityHomeBinding.drawer.closeDrawers();
                    startActivity(new Intent(HomeActivity.this, TrainingActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
                    break;

                case R.id.nav_voucher:
                    mActivityHomeBinding.drawer.closeDrawers();
                    startActivity(new Intent(HomeActivity.this, VoucherActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
                    break;


                case R.id.nav_help:
                    mActivityHomeBinding.drawer.closeDrawers();
                    startActivity(new Intent(HomeActivity.this, HelpActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
                    break;

                case R.id.nav_myid:
                    mActivityHomeBinding.drawer.closeDrawers();
                    startActivity(new Intent(HomeActivity.this, TechIdActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
                    break;

                case R.id.nav_language:
                    mActivityHomeBinding.drawer.closeDrawers();
                    showLanguageDialog();
                    break;

                case R.id.nav_logout:
                    mActivityHomeBinding.drawer.closeDrawers();
                    final AlertDialog.Builder dialog = new AlertDialog.Builder(HomeActivity.this);
                    dialog.setTitle(getString(R.string.logout_exit));
                    dialog.setMessage(getString(R.string.logout_do_you_really_want_to_exit));
                    dialog.setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        getLogout();
                    });
                    dialog.setNegativeButton(getString(R.string.no), (dialogInterface, i) -> dialogInterface.dismiss());
                    dialog.show();

                    break;

            }
            return true;
        });


    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String appLinkAction = intent.getAction();
        Uri appLinkData = intent.getData();
        if (Intent.ACTION_VIEW.equals(appLinkAction) && appLinkData != null) {
            String recipeId = appLinkData.getLastPathSegment();
            Uri appData = Uri.parse("content://com.recipe_app/recipe/").buildUpon()
                    .appendPath(recipeId).build();
            //     showRecipe(appData);
        }
    }


    private void getLogout() {
        try {
            NetworkCallController controller = new NetworkCallController();
            String UserId = SharedPreferencesUtility.getPrefString(HomeActivity.this, SharedPreferencesUtility.PREF_USERID);

            controller.setListner(new NetworkResponseListner() {
                @Override
                public void onResponse(int requestCode, Object response) {
                    LogoutResponse logres = (LogoutResponse) response;

                    if (logres.getSuccess()) {
                        if (pendingUpdateIntent != null) {
                            mAlarmManager.cancel(pendingUpdateIntent);
                            getApplicationContext().stopService(new Intent(getApplicationContext(), ServiceLocationSend.class));
                        }
                        SharedPreferencesUtility.savePrefBoolean(getApplicationContext(), SharedPreferencesUtility.IS_USER_LOGIN,
                                false);
                        SharedPreferencesUtility.savePrefBoolean(getApplicationContext(), SharedPreferencesUtility.IS_SKIP_VIDEO,
                                false);
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(HomeActivity.this, "Logout failed! try again.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(int requestCode) {

                }
            });

            controller.getLogout(LOGOUT_REQ, UserId, HomeActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        return super.onPrepareOptionsMenu(menu);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_matrix, menu);
        //return true;
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onBackPressed() {
        try {
            if (mActivityHomeBinding.customNavigation.getSelectedItemId() == R.id.nav_home || mActivityHomeBinding.customNavigation.getSelectedItemId() == R.id.nav_leader) {
                mActivityHomeBinding.navigationView.setCheckedItem(0);
                showExitAlert();
            } else {
                mActivityHomeBinding.customNavigation.setSelectedItemId(R.id.nav_home);
                mActivityHomeBinding.navigationView.setCheckedItem(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showExitAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.logout_exit));
        dialog.setMessage(getString(R.string.logout_do_you_really_want_to_exit));
        dialog.setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
            dialogInterface.dismiss();
            finishAffinity();
        });
        dialog.setNegativeButton(getString(R.string.no), (dialogInterface, i) -> dialogInterface.dismiss());
        dialog.show();
    }

    @Override
    public void onBackStackChanged() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
//
        try {
            if (backStackEntryCount == 0) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            } else {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        if( getSupportFragmentManager().getBackStackEntryCount() <= 1){
//            // pop all the fragment and remove the listener
//            getSupportFragmentManager().popBackStack("HOME", POP_BACK_STACK_INCLUSIVE);
//            getSupportFragmentManager().removeOnBackStackChangedListener(this);
//            // set the home button selected
//            mActivityHomeBinding.customNavigation.getMenu().getItem(0).setChecked(true);
//        }

    }

    @Override
    public void locationFetched(Location mLocation, Location oldLocation, String time, String locationProvider) {
        this.mLocation = mLocation;
        if (mListner != null) {
            mListner.locationFetched(mLocation, oldLocation, time, locationProvider);
        }
    }

    public Location getmLocation() {
        return mLocation;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mCameraManager.setTorchMode(mCameraId, false);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        AppUtils.IS_FLASH_ON = true;
        mActivityHomeBinding.toolbar.imgFlash.setImageResource(R.drawable.flash_off);
    }
}