package com.ab.hicarerun.activities;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.ab.hicarerun.BaseActivity;
import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.R;
import com.ab.hicarerun.databinding.ActivityHomeBinding;
import com.ab.hicarerun.fragments.FaceRecognizationFragment;
import com.ab.hicarerun.fragments.HomeFragment;
import com.ab.hicarerun.fragments.NotificationFragment;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.HandShakeModel.HandShake;
import com.ab.hicarerun.network.models.IncentiveModel.Incentive;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.network.models.LogoutResponse;
import com.ab.hicarerun.network.models.ProfileModel.Profile;
import com.ab.hicarerun.network.models.UpdateAppModel.UpdateData;
import com.ab.hicarerun.service.LocationManager;
import com.ab.hicarerun.service.ServiceLocationSend;
import com.ab.hicarerun.service.listner.LocationManagerListner;
import com.ab.hicarerun.utils.AppUtils;
import com.ab.hicarerun.utils.DownloadApk;
import com.ab.hicarerun.utils.HandShakeReceiver;
import com.ab.hicarerun.utils.SharedPreferencesUtility;
import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.RealmResults;

public class HomeActivity extends BaseActivity implements FragmentManager.OnBackStackChangedListener, LocationManagerListner {
    ActivityHomeBinding mActivityHomeBinding;

    private static final int LOGOUT_REQ = 1000;
    private static final int UPDATE_REQ = 2000;
    private static final int REQ_PROFILE = 3000;
    private static final int REQ_INCENTIVE = 4000;
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
    private android.location.LocationManager locationManager;
    private AlarmManager mAlarmManager = null;
    private PendingIntent pendingUpdateIntent = null;
    private Bitmap bitUser = null;
    private ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityHomeBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_home);
//        setSupportActionBar(mActivityHomeBinding.toolbar)
        progress = new ProgressDialog(this, R.style.TransparentProgressDialog);
        progress.setCancelable(false);
        mActivityHomeBinding.toolbar.lnrDrawer.setOnClickListener(view -> mActivityHomeBinding.drawer.openDrawer(GravityCompat.START));
        locationManager =
                (android.location.LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
                && locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)) {
            progress.show();
            getServiceCalled();
            getTechDeails();
            getIncentiveDetails();
        } else {
            try {
                AppUtils.statusCheck(HomeActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mActivityHomeBinding.toolbar.lnrUser.setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, TechIdActivity.class).putExtra(HomeActivity.ARG_EVENT, false)));
        mActivityHomeBinding.toolbar.lnrWallet.setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, IncentivesActivity.class).putExtra(HomeActivity.ARG_EVENT, false)));

        try {
            isClicked = getIntent().getExtras().getBoolean(ARG_EVENT, false);
            LoginRealmModels =
                    BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                LocationManager.Builder builder = new LocationManager.Builder(this);
                builder.setLocationListner(this);
                builder.build();
                userId = LoginRealmModels.get(0).getUserID();
                SharedPreferencesUtility.savePrefString(HomeActivity.this, SharedPreferencesUtility.PREF_USERID, userId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        if (AppUtils.checkConnection(HomeActivity.this)) {
        } else {
            AppUtils.showOkActionAlertBox(HomeActivity.this, "Please check your internet connection!", (dialogInterface, i) -> finish());
        }
        try {
            AppUtils.statusCheck(HomeActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getVersionFromApi() {
        NetworkCallController controller = new NetworkCallController();
        controller.setListner(new NetworkResponseListner() {
            @Override
            public void onResponse(int requestCode, Object response) {
                UpdateData data = (UpdateData) response;
                checkCurrentVersion(data.getApkurl(), data.getVersion(), data.getApktype());
            }

            @Override
            public void onFailure(int requestCode) {

            }
        });
        controller.getUpdateApp(UPDATE_REQ);
    }

    private void checkCurrentVersion(final String apkurl, String version, final String apktype) {
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String mobileVersion = pInfo.versionName;

        if (Float.parseFloat(mobileVersion) < Float.parseFloat(version)) {
            String title = "New update available";
            String messageAlert = "<html><body><p>Please update your app to new version.<br><br>Current app version: " + mobileVersion + "<br><br>New version: " + version + "</p></body></html>";
            AppUtils.showDownloadActionAlertBox(HomeActivity.this, title, String.valueOf(Html.fromHtml(messageAlert)), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (AppUtils.checkConnection(HomeActivity.this)) {
                        ProgressDialog progress = new ProgressDialog(HomeActivity.this);
                        if (apktype.equalsIgnoreCase("url")) {
                            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            }
                        } else {
                            DownloadApk downloadAndInstall = new DownloadApk();
                            progress.setCancelable(false);
                            progress.setMessage("Downloading...");
                            downloadAndInstall.setContext(HomeActivity.this, progress);
                            downloadAndInstall.execute(apkurl);
                        }

                    } else {
                        AppUtils.showOkActionAlertBox(HomeActivity.this, "No Internet Found.", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                    }
                }
            });

        }
    }

    private void getServiceCalled() {
        try {
            if (isClicked) {
                userName = getIntent().getStringExtra(ARG_USER);
                SharedPreferencesUtility.savePrefString(HomeActivity.this, SharedPreferencesUtility.PREF_USERNAME, userName);
                items = getIntent().getParcelableArrayListExtra(ARG_HANDSHAKE);
                long REPEATED_TIME = 1000 * 60 * Integer.parseInt(items.get(1).getValue());
//                long REPEATED_TIME = 60000;
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
            } else {
                if (pendingUpdateIntent != null) {
                    mAlarmManager.cancel(pendingUpdateIntent);
                    getApplicationContext().stopService(new Intent(getApplicationContext(), ServiceLocationSend.class));
                }
                String time = SharedPreferencesUtility.getPrefString(HomeActivity.this, SharedPreferencesUtility.PREF_TIME);
                long REPEATED_TIME = 1000 * 60 * Integer.parseInt(time);
//                long REPEATED_TIME = 60000;
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
        } catch (Exception e) {
            RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                AppUtils.sendErrorLogs(e.getMessage(), getClass().getSimpleName(), "getServiceCalled", lineNo, userName, DeviceName);
            }
        }

    }

    private void getTechDeails() {
        try {
            RealmResults<LoginResponse> LoginRealmModels =
                    BaseApplication.getRealm().where(LoginResponse.class).findAll();
            String userId = LoginRealmModels.get(0).getUserID();
            if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                NetworkCallController controller = new NetworkCallController();
                controller.setListner(new NetworkResponseListner() {
                    @Override
                    public void onResponse(int requestCode, Object data) {
                        progress.dismiss();
                        Profile response = (Profile) data;
                        if (response.getProfilePic() != null) {
                            String base64 = response.getProfilePic();
                            byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            if (base64.length() > 0) {
                                mActivityHomeBinding.toolbar.imgUser.setImageBitmap(decodedByte);
                                bitUser = decodedByte;
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                bitUser.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                byte[] byteArray = stream.toByteArray();
                                addFragment(HomeFragment.newInstance(byteArray), "HomeActivity - HomeFragment");

                            }
                        }
                        initNavigationDrawer();
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

    private void getIncentiveDetails() {
        try {
            RealmResults<LoginResponse> LoginRealmModels =
                    BaseApplication.getRealm().where(LoginResponse.class).findAll();
            String userId = LoginRealmModels.get(0).getUserID();
            if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
                NetworkCallController controller = new NetworkCallController();
                controller.setListner(new NetworkResponseListner() {
                    @Override
                    public void onResponse(int requestCode, Object data) {
                        Incentive response = (Incentive) data;
                        mActivityHomeBinding.toolbar.txtIncentive.setText("\u20B9" + " " + response.getTotalIncentive());
                    }

                    @Override
                    public void onFailure(int requestCode) {

                    }
                });
                controller.getTechnicianIncentive(REQ_INCENTIVE, userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void initNavigationDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        LoginRealmModels =
                BaseApplication.getRealm().where(LoginResponse.class).findAll();
        if (LoginRealmModels != null && LoginRealmModels.size() > 0) {
            String isTsEnable = LoginRealmModels.get(0).getIsTechnician();
            Menu menu = navigationView.getMenu();
            MenuItem groom = menu.findItem(R.id.nav_groom);
            MenuItem jobCount = menu.findItem(R.id.nav_summary);
            if (isTsEnable.equals("0")) {
                groom.setVisible(true);
                jobCount.setVisible(true);
            } else {
                groom.setVisible(false);
                jobCount.setVisible(false);

            }
            PackageInfo pInfo = null;
            try {
                pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            String mobileVersion = pInfo.versionName;
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
        }
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            switch (id) {

                case R.id.nav_home:
//                    getSupportFragmentManager().beginTransaction().replace(mActivityHomeBinding.container.getId(), HomeFragment.newInstance()).addToBackStack(null).commit();
                    mActivityHomeBinding.drawer.closeDrawers();
                    break;


                case R.id.nav_incentive:
                    mActivityHomeBinding.drawer.closeDrawers();
                    startActivity(new Intent(HomeActivity.this, IncentivesActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
                    break;

                case R.id.nav_attendance:
                    mActivityHomeBinding.drawer.closeDrawers();
                    startActivity(new Intent(HomeActivity.this, AttendanceActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
                    break;


                case R.id.nav_onsite:
                    mActivityHomeBinding.drawer.closeDrawers();
                    startActivity(new Intent(HomeActivity.this, OnSiteTaskActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
                    break;

                case R.id.nav_groom:
                    mActivityHomeBinding.drawer.closeDrawers();
                    startActivity(new Intent(HomeActivity.this, TechnicianSeniorActivity.class).putExtra(HomeActivity.ARG_EVENT, false));
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

                case R.id.nav_logout:
                    mActivityHomeBinding.drawer.closeDrawers();
                    final AlertDialog.Builder dialog = new AlertDialog.Builder(HomeActivity.this);
                    dialog.setTitle("Logout");
                    dialog.setMessage("Do you want to logout?");
                    dialog.setPositiveButton("Yes", (dialogInterface, i) -> {

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

                    });
                    dialog.setNegativeButton("No", null);
                    dialog.show();

                    break;

            }
            return true;
        });



    }

    @Override
    public void onBackPressed() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount == 0) {
            showExitAlert();
        } else {
            super.onBackPressed();
        }
    }

    private void showExitAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Exit");
        dialog.setMessage("Do you want to exit?");
        dialog.setPositiveButton("Yes", (dialogInterface, i) -> finishAffinity());
        dialog.setNegativeButton("No", null);
        dialog.show();
    }

    @Override
    public void onBackStackChanged() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount == 0) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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


}
