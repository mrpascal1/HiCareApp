package com.ab.hicarerun.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;

import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ab.hicarerun.R;
import com.ab.hicarerun.activities.HomeActivity;
import com.ab.hicarerun.network.NetworkCallController;
import com.ab.hicarerun.network.NetworkResponseListner;
import com.ab.hicarerun.network.models.attendancemodel.AttendanceRequest;
import com.ab.hicarerun.network.models.chemicalmodel.TowerData;
import com.ab.hicarerun.network.models.consulationmodel.Data;
import com.ab.hicarerun.network.models.generalmodel.GeneralData;
import com.ab.hicarerun.network.models.generalmodel.GeneralPaymentMode;
import com.ab.hicarerun.network.models.generalmodel.GeneralTaskStatus;
import com.ab.hicarerun.network.models.generalmodel.IncompleteReason;
import com.ab.hicarerun.network.models.handshakemodel.HandShake;
import com.ab.hicarerun.network.models.loggermodel.ErrorLog;
import com.ab.hicarerun.network.models.loggermodel.ErrorLoggerModel;
import com.ab.hicarerun.network.models.tmsmodel.QuestionImageUrl;
import com.ab.hicarerun.network.models.tmsmodel.QuestionTabList;
import com.ab.hicarerun.network.models.tmsmodel.TmsData;
import com.google.gson.Gson;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;


public class AppUtils {

    private static final int CAM_REQ = 1000;
    private static final int HANDSHAKE_REQUEST = 2000;
    private static final int ERROR_REQUEST = 3000;
    private static final int RESOURCE_REQ = 4000;
    private static final int CONSINS_REQ = 5000;
    private static final int ACTIVITY_REQ = 6000;
    public static int Ins_Size = 0;
    public static String taskTypeName = "";

    public static List<Data> dataList = new ArrayList<>();
    public static List<Data> consulationList = null;
    public static List<Data> inspectionList = null;
    public static List<Data> ConsInsList = null;

    public static List<String> tmsConsultationChips = null;
    public static List<String> tmsInspectionChips = null;
    public static ArrayList<String> tmsServiceDeliveryChips = null;
    public static ArrayList<QuestionTabList> tmsConsultationList = null;
    public static ArrayList<QuestionTabList> tmsInspectionList = null;
    public static ArrayList<QuestionTabList> tmsServiceDeliveryList = null;
    public static ArrayList<QuestionImageUrl> tmsImageUrls = null;
    public static List<TmsData> tmsConsInsList = null;

    public static List<TowerData> towerData = null;
    public static List<TowerData> mCommonTowerList = null;
    public static List<TowerData> mRegularTowerList = null;
    public static boolean isInspectionDone = false;
    public static boolean isTmsInspectionDone = false;
    public static boolean isServiceDeliveryFilled = false;
    public static boolean isPulseSubmitted = false;
    public static boolean showRoachInspectionButton = false;
    public static boolean isB2BJob = false;
    public static String sequenceNo = "";
    public static String resourceId = "";
    public static String appointmentDate = "";
    public static String infestationLevel = "";
    public static String appointmentStartTime = "";
    public static String appointmentEndTime = "";
    public static String CAMERA_ORIENTATION = "CAMERA_ORIENTATION";
    public static String CAMERA_SCREEN = "";
    public static String taskId = "";
    public static String status = "";
    public static String accountId = "";
    public static boolean NOT_RENEWAL_DONE = false;
    public static boolean IS_ACTIVITY_THERE = false;
    public static boolean IS_QRCODE_THERE = true;
    public static boolean IS_FLASH_ON= true;
    public static boolean CHEMICAL_CHANGED = false;
    public static boolean IS_COMBINED_TASK = false;
    public static HashMap<Integer, String> checkItems = new HashMap<>();

    public static Bitmap createCustomMarker(Context context, Bitmap resource, String accountName, String _name) {

        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_custom_marker, null);
        RelativeLayout relMarker = (RelativeLayout) marker.findViewById(R.id.marker);
        CircleImageView markerImage = (CircleImageView) marker.findViewById(R.id.user_dp);
        markerImage.setImageBitmap(resource);
        TextView txt_name = (TextView) marker.findViewById(R.id.name);
        txt_name.setTypeface(txt_name.getTypeface(), Typeface.BOLD);
        txt_name.setText(accountName);
        if (_name.equalsIgnoreCase("Customer")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                relMarker.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.pink_sec));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                relMarker.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary));

            }
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);

        return bitmap;
    }

    public static Resources updateViews(String languageCode, Context activity) {
        Context context = LocaleHelper.setLocale(activity, languageCode);
        return context.getResources();
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    public static boolean checkConnection(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

        if (activeNetworkInfo != null) { // connected to the internet
            // Toast.makeText(context, activeNetworkInfo.getTypeName(), Toast.LENGTH_SHORT).show();

            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return true;
            } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                return true;
            }
        }
        return false;
    }

    public static void showOkActionAlertBox(Context context, String mStrMessage, DialogInterface.OnClickListener mClickListener) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        mBuilder.setMessage(mStrMessage);
        mBuilder.setPositiveButton("ok", mClickListener);
        mBuilder.setCancelable(false);
        mBuilder.create().show();
    }


    public static void showDownloadActionAlertBox(Context context, String title, String mStrMessage, DialogInterface.OnClickListener mClickListener) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        mBuilder.setIcon(R.mipmap.logo);
        mBuilder.setTitle(title);
        mBuilder.setMessage(mStrMessage);
        mBuilder.setPositiveButton("UPDATE", mClickListener);
        mBuilder.setCancelable(false);
        mBuilder.create().show();
    }

    public static void showDownloadAlertBox(Context context) {

    }

    public static void getCurrentTimeUsingCalendar() {
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm a");
        String formattedDate = dateFormat.format(date);
        System.out.println("Current time of the day using Calendar - 24 hour format: " + formattedDate);
    }


    public static String compareDates(String d1, String d2) {
        String date_result = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1 = sdf.parse(d1);
            Date date2 = sdf.parse(d2);
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(date2);
//            cal.add(Calendar.MINUTE, 15);
//            Date dateAfter = cal.getTime();
            if (date1.after(date2)) {
                date_result = "afterdate";

            }
            if (date1.before(date2)) {
                date_result = "beforedate";
            }

            if (date1.equals(date2)) {
                date_result = "equalsdate";
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date_result;
    }

    public static String compareLoginDates(String d1, String d2) {
        String date_result = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = sdf.parse(d1);
            Date date2 = sdf.parse(d2);
            if (date1.after(date2)) {
                date_result = "after";
            }
            if (date1.before(date2)) {
                date_result = "before";
            }
            if (date1.equals(date2)) {
                date_result = "equal";
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date_result;
    }

    public static String covertTimeToText(String dataDate) {

        String convertTime = null;
        String suffix = "ago";

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault());
            Date pasTime = dateFormat.parse(dataDate);

            Date nowTime = new Date();

            long dateDiff = nowTime.getTime() - pasTime.getTime();

            long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day = TimeUnit.MILLISECONDS.toDays(dateDiff);

            if (second < 60) {
                if (second == 1) {
                    convertTime = second + " second " + suffix;
                } else {
                    convertTime = second + " seconds " + suffix;
                }
            } else if (minute < 60) {
                if (minute == 1) {
                    convertTime = minute + " minute " + suffix;
                } else {
                    convertTime = minute + " minutes " + suffix;
                }
            } else if (hour < 24) {
                if (hour == 1) {
                    convertTime = hour + " hour " + suffix;
                } else {
                    convertTime = hour + " hours " + suffix;
                }
            } else if (day >= 7) {
                if (day >= 365) {
                    long tempYear = day / 365;
                    if (tempYear == 1) {
                        convertTime = tempYear + " year " + suffix;
                    } else {
                        convertTime = tempYear + " years " + suffix;
                    }
                } else if (day >= 30) {
                    long tempMonth = day / 30;
                    if (tempMonth == 1) {
                        convertTime = (day / 30) + " month " + suffix;
                    } else {
                        convertTime = (day / 30) + " months " + suffix;
                    }
                } else {
                    long tempWeek = day / 7;
                    if (tempWeek == 1) {
                        convertTime = (day / 7) + " week " + suffix;
                    } else {
                        convertTime = (day / 7) + " weeks " + suffix;
                    }
                }
            } else {
                if (day == 1) {
                    convertTime = day + " day " + suffix;
                } else {
                    convertTime = day + " days " + suffix;
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("TimeAgo", e.getMessage() + "");
        }
        return convertTime;
    }

    public static String formatTime(String dateIn, String format) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm aa", Locale.ENGLISH);
        Date date = simpleDateFormat.parse(dateIn);
        simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    public static String reFormatRedeemedDate(String dateIn, String format) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.ENGLISH);
        Date date = simpleDateFormat.parse(dateIn);
        simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    public static String reFormatDateAndTime(String dateIn, String format) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm aa", Locale.ENGLISH);
        Date date = simpleDateFormat.parse(dateIn);
        simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    public static String getFormatted(String dateIn, String format, String pattern) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);
        Date date = simpleDateFormat.parse(dateIn);
        simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    public static String currentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
        Date date1 = new Date();
        return dateFormat.format(date1);
    }

    public static String currentDateTimeWithTimeZone() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSZZZZZ", Locale.ENGLISH);
        Date date1 = new Date();
        return dateFormat.format(date1);
    }

    public static String currentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = new Date();
        return dateFormat.format(date1);
    }

    @SuppressLint("SimpleDateFormat")
    public static String getTomorrowDate() {
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.DATE, 1);
        return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
    }

    public static String formattedCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        Date date1 = new Date();
        return dateFormat.format(date1);
    }

    public static void getDataClean() {
        try {
            Realm.getDefaultInstance().beginTransaction();
            Realm.getDefaultInstance().where(GeneralData.class).findAll().deleteAllFromRealm();
            Realm.getDefaultInstance().where(GeneralTaskStatus.class).findAll().deleteAllFromRealm();
            Realm.getDefaultInstance().where(GeneralPaymentMode.class).findAll().deleteAllFromRealm();
            Realm.getDefaultInstance().where(IncompleteReason.class).findAll().deleteAllFromRealm();
            Realm.getDefaultInstance().commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {
        BadgeDrawable badge;
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }
        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }

    public static String reFormatDateTime(String dateIn, String format) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = simpleDateFormat.parse(dateIn);
        simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    public static String millisTOHr(Long millis) throws ParseException {

        String hms = String.format("%02d hr %02d min", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        System.out.println(hms);
        return hms;
    }

    public static String reFormatTime(String time, String format) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        Date date = simpleDateFormat.parse(time);
        simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    public static void getHandShakeCall(final String username, final Activity context) {
        try {
            NetworkCallController controller = new NetworkCallController();
            controller.setListner(new NetworkResponseListner() {
                @Override
                public void onResponse(int requestCode, Object data) {
                    SharedPreferencesUtility.savePrefBoolean(context,
                            SharedPreferencesUtility.IS_USER_LOGIN, true);
                    List<HandShake> items = (List<HandShake>) data;
                    Intent intent = new Intent(context, HomeActivity.class);
                    intent.putExtra(HomeActivity.ARG_HANDSHAKE, (Serializable) items);
                    intent.putExtra(HomeActivity.ARG_EVENT, true);
                    intent.putExtra(HomeActivity.ARG_USER, username);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                    context.finish();
                }

                @Override
                public void onFailure(int requestCode) {

                }
            });
            controller.getHandShake(HANDSHAKE_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public static String checkConnectionSpeed(long size) {
        try {
            String hrSize = null;

            double b = size;
            double k = size / 1024.0;
            double m = ((size / 1024.0) / 1024.0);
            double g = (((size / 1024.0) / 1024.0) / 1024.0);
            double t = ((((size / 1024.0) / 1024.0) / 1024.0) / 1024.0);

            DecimalFormat dec = new DecimalFormat("0.00");

            if (t > 1) {
                hrSize = dec.format(t).concat(" TB");
            } else if (g > 1) {
                hrSize = dec.format(g).concat(" GB");
            } else if (m > 1) {
                hrSize = dec.format(m).concat(" MB");
            } else if (k > 1) {
                hrSize = dec.format(k).concat(" KB");
            } else {
                hrSize = dec.format(b).concat(" Bytes");
            }

            return hrSize;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "-";
    }


    public static AttendanceRequest getDeviceInfo(final Activity context, String encodedImage, String batteryStatistics, boolean isSkip) {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");

        String userId = SharedPreferencesUtility.getPrefString(context, SharedPreferencesUtility.PREF_USERID);
        String strUsername = SharedPreferencesUtility.getPrefString(context, SharedPreferencesUtility.PREF_USERNAME);
        String DeviceIMEINumber = "";
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        try {

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

            }
//            DeviceIMEINumber = telephonyManager.getDeviceId();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                try {
//                    DeviceIMEINumber = UUID.randomUUID().toString();
//                    OneSIgnalHelper oneSIgnalHelper = new OneSIgnalHelper(context);
//                    DeviceIMEINumber = oneSIgnalHelper.getmStrUserID();
                    DeviceIMEINumber = Settings.Secure.getString(context.getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
//                    Device
//                    Number = UUID.randomUUID().toString();
//                    OneSIgnalHelper oneSIgnalHelper = new OneSIgnalHelper(context);
//                    DeviceIMEINumber = oneSIgnalHelper.getmStrUserID();
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
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = pInfo.versionName;

        String DeviceTime = currentDateTime();
        String PhoneMake = Build.VERSION.SDK_INT + "," + Build.MODEL + "," + Build.PRODUCT + "," + Build.MANUFACTURER + "," + version;
        String DeviceName = Build.DEVICE;
        AttendanceRequest request = new AttendanceRequest();
        request.setDeviceIMEINumber(DeviceIMEINumber);
        request.setDeviceName(DeviceName);
        request.setDeviceTime(DeviceTime);
        request.setGPSConnected(true);
        request.setLatitude(String.valueOf(((HomeActivity) context).getmLocation().getLatitude()));
        request.setLongitude(String.valueOf(((HomeActivity) context).getmLocation().getLongitude()));
        request.setUserName(strUsername);
        request.setUserId(userId);
        request.setTechId(userId);
        request.setLoggedIn(true);
        request.setPhoneMake(PhoneMake);
        request.setBatteryStatistics(batteryStatistics);
        request.setResourceImage(encodedImage);
        request.setSkipAttendance(isSkip);

        return request;
    }

    public static int getMyBatteryLevel(Context context) {

        Intent batteryIntent = context.registerReceiver(null,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        return batteryIntent.getIntExtra("level", -1);
    }

    public static void statusCheck(Context context) {
        try {
            final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps(context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean isGpsEnabled(Context context) {

        final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps(context);
            return false;
        } else {
            return true;
        }

    }

    private static void buildAlertMessageNoGps(final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    try {
                        Intent mIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(mIntent);
                        dialog.dismiss();
                        Activity activity = (Activity) context;
                        activity.finishAffinity();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                })
                .setNegativeButton("No, thanks", (dialog, id) -> {
                    dialog.cancel();
                    Activity activity = (Activity) context;
                    activity.finishAffinity();
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    public static Integer getBadgeImage(String type) {
        int image = 0;
        switch (type) {
            case "Platinum":

                image = R.drawable.ic_award_platinum;

                break;

            case "Gold":

                image = R.drawable.ic_award_gold;

                break;

            case "Silver":

                image = R.drawable.ic_award_silver;

                break;

            case "Bronze":

                image = R.drawable.ic_award_bronze;

                break;

            default:

                image = R.drawable.ic_award_bronze;

                break;

        }
        return image;
    }


    public static void sendErrorLogs(String error, String activityName, String methodName, String lineNo, String userName, String deviceName) {
        try {
            Gson gson = new Gson();
            Date currentTime = Calendar.getInstance().getTime();
            String DT = String.valueOf(currentTime);
            ErrorLoggerModel request = new ErrorLoggerModel();
            ErrorLog data = new ErrorLog();
            data.setApplicationName("HicareRun Mobile");
            data.setApplicationType("Mobile");
            data.setLevel("Error");
            data.setLogMessage(error + userName + deviceName);
            data.setSource("HiCareRun");
            data.setType("Mobile");
            data.setUserId(0);
            data.setLineNo(lineNo);
            data.setMethodName(methodName);
            request.setApplicationName("HicareRun Mobile");
            request.setApplicationType("Mobile");
            request.setSource("HiCareRun");
//
            request.setData(gson.toJson(data));
            NetworkCallController controller = new NetworkCallController();
            controller.setListner(new NetworkResponseListner() {
                @Override
                public void onResponse(int requestCode, Object data) {

                }

                @Override
                public void onFailure(int requestCode) {

                }
            });
            controller.sendErrorLog(ERROR_REQUEST, request);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getCurrentTimeStamp() {
        String s = "";
        try {
            DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            dateFormatter.setLenient(false);
            Date today = new Date();
            s = dateFormatter.format(today);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }


    public static void getResourceImage(String id, CircleImageView img) {
        try {
            NetworkCallController controller = new NetworkCallController();
            controller.setListner(new NetworkResponseListner() {
                @Override
                public void onResponse(int requestCode, Object response) {
                    String base64 = (String) response;
                    byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    if (base64.length() > 0) {
                        img.setImageBitmap(decodedByte);
                    }
                }

                @Override
                public void onFailure(int requestCode) {

                }
            });
            controller.getResourceProfilePicture(RESOURCE_REQ, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void getConsAndInsData(String taskId, String resourceId, String lang) {
        try {
            NetworkCallController controller = new NetworkCallController();
            controller.setListner(new NetworkResponseListner<List<Data>>() {
                @Override
                public void onResponse(int requestCode, List<Data> items) {
                    consulationList = new ArrayList<>();
                    inspectionList = new ArrayList<>();
                    ConsInsList = new ArrayList<>();
                    if (items != null && items.size() > 0) {
                        ConsInsList = items;
                        for (Data data : items) {
                            if (data.getQuestioncategory().equals("Consultation")) {
                                consulationList.add(data);
//                                mAdapter.addData(consulationList);
//                                mAdapter.notifyDataSetChanged();
                            } else {
                                inspectionList.add(data);
                                Ins_Size = inspectionList.size();
                            }
                        }
//                        mAdapter.setOnItemClickHandler(position -> {
//                            checkPosition = position;
//                            requestStoragePermission(true);
//                        });
                    }
                }

                @Override
                public void onFailure(int requestCode) {
                }
            });
            controller.getConsolution(CONSINS_REQ, resourceId, taskId, lang);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getTmsQuestions(String taskId, String lan, ProgressDialog progressDialog) {
        try {
            progressDialog.show();
            NetworkCallController controller = new NetworkCallController();
            controller.setListner(new NetworkResponseListner<List<TmsData>>() {
                @Override
                public void onResponse(int requestCode, List<TmsData> items) {
                    tmsConsultationChips = new ArrayList<>();
                    tmsInspectionChips = new ArrayList<>();
                    tmsConsultationList = new ArrayList<>();
                    tmsInspectionList = new ArrayList<>();
                    tmsConsInsList = new ArrayList<>();
                    tmsImageUrls = new ArrayList<>();

                    tmsImageUrls.add(new QuestionImageUrl(0, null));
                    tmsImageUrls.add(new QuestionImageUrl(1, null));
                    tmsImageUrls.add(new QuestionImageUrl(2, null));
                    if (items != null && items.size() > 0) {
                        tmsConsInsList = items;
                        Log.d("TAG", "Called");
                        for (TmsData data : items) {
                            Log.d("TAG", "Looping : Inside Data");
                            if (data.getType().equalsIgnoreCase("Consultation")) {
                                Log.d("TAG", "Found consultation");
                                for (int i =0; i<data.getQuestionTabList().size(); i++){
                                    tmsConsultationChips.add(data.getQuestionTabList().get(i).getQuestionDisplayTab());
                                }
                                Log.d("TAG", "Chips "+tmsConsultationChips.toString());
                                tmsConsultationList.addAll(data.getQuestionTabList());
//                                mAdapter.addData(consulationList);
//                                mAdapter.notifyDataSetChanged();
                            } else {
                                for (int i =0; i<data.getQuestionTabList().size(); i++){
                                    tmsInspectionChips.add(data.getQuestionTabList().get(i).getQuestionDisplayTab());
                                }
                                tmsInspectionList.addAll(data.getQuestionTabList());
                            }
                        }
//                        mAdapter.setOnItemClickHandler(position -> {
//                            checkPosition = position;
//                            requestStoragePermission(true);
//                        });
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(int requestCode) {
                    progressDialog.dismiss();
                }
            });
            controller.getTmsQuestions(1011, taskId, lan);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getServiceDeliveryQuestions(String taskId, String lan){
        try {
            NetworkCallController controller = new NetworkCallController();
            controller.setListner(new NetworkResponseListner<List<TmsData>>() {
                @Override
                public void onResponse(int requestCode, List<TmsData> items) {
                    tmsServiceDeliveryChips = new ArrayList<>();
                    tmsServiceDeliveryList = new ArrayList<>();
                    if (items != null && items.size() > 0) {
                        Log.d("TAG", "Called");
                        for (TmsData data : items) {
                            Log.d("TAG", "Looping : Inside Data");
                            if (data.getType().equalsIgnoreCase("Service Delivery")){
                                for (int i =0; i<data.getQuestionTabList().size(); i++){
                                    tmsServiceDeliveryChips.add(data.getQuestionTabList().get(i).getQuestionDisplayTab());
                                }
                                tmsServiceDeliveryList.addAll(data.getQuestionTabList());
                            }
                        }
                    }
                }

                @Override
                public void onFailure(int requestCode) {

                }
            });
            controller.getServiceDeliveryQuestions(1011, taskId, lan);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
