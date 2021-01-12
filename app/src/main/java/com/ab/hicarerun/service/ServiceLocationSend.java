package com.ab.hicarerun.service;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import android.os.PowerManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.R;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.HandShakeModel.ContinueHandShakeRequest;
import com.ab.hicarerun.network.models.LoginResponse;
import com.ab.hicarerun.utils.AppUtils;
import com.ab.hicarerun.utils.HandShakeReceiver;
import com.ab.hicarerun.utils.SharedPreferencesUtility;
import com.ab.hicarerun.utils.notifications.OneSIgnalHelper;


import java.security.Provider;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import io.realm.RealmResults;

public class ServiceLocationSend extends Service implements LocationListener {

    String str_lat, str_lng, strUsername = "", userId = "", DeviceIMEINumber, DeviceTime, BatteryStatistics, PhoneMake, DeviceName;
    String timeIntervalValue, enableTraceValue;
    Boolean IsLoggedIn, IsGPSConnected;
    double lat, lng;
    private static final int CONTINUT_HANDSHAKE_REQUEST = 1000;
    LocationManager locationManager;
    Location location;
    String highestSpeed = "-";
    String lowestSpeed = "-";
    private PowerManager.WakeLock mWakeLock = null;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            startMyOwnForeground();
        else {
            startForeground(1, new Notification());
        }
        super.onCreate();
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "com.ab.hicarerun";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("HiCare is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setSmallIcon(R.mipmap.logo)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                        R.mipmap.logo))
                .build();
        startForeground(2, notification);
    }


    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.e("Service Started", "Service Started");
        try {
            final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "sample:myapp");
            mWakeLock.acquire();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post((Runnable) () -> {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            Log.e("Running", "Running call");
            final Location location = getLastKnownLocation();
            if (location != null) {
                lat = (double) (location.getLatitude());
                lng = (double) (location.getLongitude());
                Log.e("lat", "Lattitude:" + lat);
                Log.e("long", "Longitude:" + lng);
                onLocationChanged(location);
            } else {

                Log.e("lat_long ", "null");
                onLocationChanged(location);
            }

            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                Log.e("Called to GPS", "APi Hit ifff");
                getDeviceDetails(ServiceLocationSend.this, true, false);
                getContinueHandShake(ServiceLocationSend.this);

            } else {
                Log.e("Called to GPS", "APi Hit else");
                getDeviceDetails(ServiceLocationSend.this, true, true);
                getContinueHandShake(ServiceLocationSend.this);
            }
        });

        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (mWakeLock!=null && mWakeLock.isHeld())
                mWakeLock.release();
        }catch (Exception e){
            e.printStackTrace();
        }


    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        try {
            String time = SharedPreferencesUtility.getPrefString(getApplicationContext(), SharedPreferencesUtility.PREF_INTERVAL);
//            long REPEATED_TIME = Long.parseLong(time);
            long REPEATED_TIME = 1000 * 60 * 6;
            getApplicationContext().stopService(new Intent(getApplicationContext(), ServiceLocationSend.class));
            Log.e("TAG", "Service Killed");
            Intent intent = new Intent(getApplicationContext(), HandShakeReceiver.class);
            intent.setAction("HandshakeAction");
            PendingIntent pendingUpdateIntent = PendingIntent.getBroadcast(getApplicationContext(),
                    0, intent, PendingIntent.FLAG_ONE_SHOT);
            Calendar futureDate = Calendar.getInstance();
            AlarmManager mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            if (Build.VERSION.SDK_INT >= 19) {
                mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, futureDate.getTime().getTime(), REPEATED_TIME, pendingUpdateIntent);
            } else {
                mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, futureDate.getTime().getTime(), REPEATED_TIME, pendingUpdateIntent);
            }

            super.onTaskRemoved(rootIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void getDeviceDetails(Context context, Boolean isloggedInsuccess, Boolean isGPSEnabled) {

        try {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");


            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(ServiceLocationSend.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                //return;
            }
            str_lat = String.valueOf(lat);
            str_lng = String.valueOf(lng);

            strUsername = SharedPreferencesUtility.getPrefString(context, SharedPreferencesUtility.PREF_USERNAME);
            userId = SharedPreferencesUtility.getPrefString(context, SharedPreferencesUtility.PREF_USERID);

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    try {
//                    DeviceIMEINumber = telephonyManager.getImei();
//                        DeviceIMEINumber = UUID.randomUUID().toString();
//                        OneSIgnalHelper oneSIgnalHelper = new OneSIgnalHelper(this);
//                        DeviceIMEINumber = oneSIgnalHelper.getmStrUserID();
                        DeviceIMEINumber = Settings.Secure.getString(context.getContentResolver(),
                                Settings.Secure.ANDROID_ID);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
//                    DeviceIMEINumber = telephonyManager.getDeviceId();
//                        DeviceIMEINumber = UUID.randomUUID().toString();
//                        OneSIgnalHelper oneSIgnalHelper = new OneSIgnalHelper(this);
//                        DeviceIMEINumber = oneSIgnalHelper.getmStrUserID();
                        DeviceIMEINumber = Settings.Secure.getString(context.getContentResolver(),
                                Settings.Secure.ANDROID_ID);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            PackageInfo pInfo = null;
            try {
                pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                String version = pInfo.versionName;
                DeviceTime = dateformat.format(c.getTime());
                BatteryStatistics = String.valueOf(getMyBatteryLevel(ServiceLocationSend.this));
                PhoneMake = Build.VERSION.SDK_INT + "," + Build.MODEL + "," + Build.PRODUCT + "," + Build.MANUFACTURER + "," + version;
                DeviceName = Build.DEVICE;

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            if (isloggedInsuccess) {
                IsLoggedIn = true;
            } else {
                IsLoggedIn = false;
            }

            if (isGPSEnabled) {
                IsGPSConnected = true;
            } else {
                IsGPSConnected = false;
            }

            Log.e("TAG", "Lat" + str_lat);
            Log.e("TAG", "Long" + str_lng);
            Log.e("TAG", "str_Username: " + strUsername);
            Log.e("TAG", "IMEI: " + DeviceIMEINumber);
            Log.e("TAG", "Battery: " + BatteryStatistics);
            Log.e("TAG", "Phone Make: " + PhoneMake);
            Log.e("TAG", "Device Name: " + DeviceName);
            Log.e("TAG", "isLogeedinnn: " + IsLoggedIn);
            Log.e("TAG", "GPS: " + IsGPSConnected);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void getContinueHandShake(Context context) {


        Log.e("TAG", "Lat" + str_lat);
        Log.e("TAG", "Long" + str_lng);
        Log.e("TAG", "tech_id: " + userId);
        Log.e("TAG", "UserId: " + userId);
        Log.e("TAG", "str_Username: " + strUsername);
        Log.e("TAG", "IMEI: " + DeviceIMEINumber);
        Log.e("TAG", "Battery: " + BatteryStatistics);
        Log.e("TAG", "Phone Make: " + PhoneMake);
        Log.e("TAG", "Device Name: " + DeviceName);
        Log.e("TAG", "Device Time: " + DeviceTime);
        Log.e("TAG", "isLogeedinnn: " + IsLoggedIn);
        Log.e("TAG", "GPS: " + IsGPSConnected);

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        if (netInfo != null) {
            try {
                String hrSize = null;
                NetworkCapabilities nc = null;
                Log.e("TAG", "Internet Conection: " + netInfo.isConnected());
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
                    int downSpeed = nc.getLinkDownstreamBandwidthKbps();
                    int upSpeed = nc.getLinkUpstreamBandwidthKbps();
                    highestSpeed = AppUtils.checkConnectionSpeed(upSpeed);
                    lowestSpeed = AppUtils.checkConnectionSpeed(downSpeed);
                    Log.e("TAG", "Internet Speed: " + "--DownSpeed :- " + lowestSpeed + "--UpSpeed :- " + highestSpeed);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Log.e("TAG", "Internet Conection: " + false);
        }


        try {
            NetworkCallController controller = new NetworkCallController();
            ContinueHandShakeRequest request = new ContinueHandShakeRequest();
            request.setLatitude(str_lat);
            request.setLongitude(str_lng);
            request.setTechId(userId);
            request.setUserId(userId);
            request.setUserName(strUsername);
            request.setDeviceIMEINumber(DeviceIMEINumber);
            request.setDeviceTime(DeviceTime);
            request.setBatteryStatistics(BatteryStatistics);
            request.setPhoneMake(PhoneMake);
            request.setDeviceName(DeviceName);
            request.setLoggedIn(IsLoggedIn);
            request.setGPSConnected(IsGPSConnected);
            request.setConnectionSpeed("Highest Speed : " + highestSpeed + " Lowest Speed : " + lowestSpeed);
            if (netInfo != null) {
                request.setConnected(netInfo.isConnected());
            } else {
                request.setConnected(false);
            }

            controller.setListner(new NetworkResponseListner() {
                @Override
                public void onResponse(int requestCode, Object response) {
                    Log.e("response", response.toString());
                }

                @Override
                public void onFailure(int requestCode) {

                }
            });
            controller.getContinueHandShake(CONTINUT_HANDSHAKE_REQUEST, request);

        } catch (Exception e) {
            RealmResults<LoginResponse> mLoginRealmModels = BaseApplication.getRealm().where(LoginResponse.class).findAll();
            if (mLoginRealmModels != null && mLoginRealmModels.size() > 0) {
                String userName = "TECHNICIAN NAME : " + mLoginRealmModels.get(0).getUserName();
                String lineNo = String.valueOf(new Exception().getStackTrace()[0].getLineNumber());
                String DeviceName = "DEVICE_NAME : " + Build.DEVICE + ", DEVICE_VERSION : " + Build.VERSION.SDK_INT;
                AppUtils.sendErrorLogs(e.getMessage(), getClass().getSimpleName(), "getContinueHandShake", lineNo, userName, DeviceName);
            }
        }
    }

    public int getMyBatteryLevel(Context context) {
        Intent batteryIntent = context.registerReceiver(null,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        return batteryIntent.getIntExtra("level", -1);
    }


    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
        } else {
            lat = 0.0;
            lng = 0.0;
        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public Location getLastKnownLocation() {
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

            }
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            } else {
                bestLocation = l;
            }
        }
        return bestLocation;
    }

}
