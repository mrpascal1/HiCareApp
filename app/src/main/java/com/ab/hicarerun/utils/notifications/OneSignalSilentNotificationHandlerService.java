package com.ab.hicarerun.utils.notifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.ab.hicarerun.BaseApplication;
import com.ab.hicarerun.R;
import com.ab.hicarerun.activities.HomeActivity;
import com.ab.hicarerun.activities.KarmaActivity;
import com.ab.hicarerun.activities.OfferActivity;
import com.ab.hicarerun.activities.ZoomTransparentPopupActivity;
import com.ab.hicarerun.utils.ZoomBroadcast;
import com.ab.hicarerun.utils.ZoomNotificationClearer;
import com.onesignal.BuildConfig;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OneSignal;
import com.onesignal.OneSignalNotificationManager;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

public class OneSignalSilentNotificationHandlerService extends Service implements OneSignal.OSNotificationOpenedHandler {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Context context;
    String notificationId;

    public OneSignalSilentNotificationHandlerService(Context context) {
        this.context = context.getApplicationContext();
    }

    public OneSignalSilentNotificationHandlerService() {

    }

    protected boolean onNotificationProcessing(OSNotification notification) {
        Logger.d("...Here we go ...");
        Logger.d("...Magic begins ...");

        notificationId = notification.getNotificationId();
        String mStrHeader = notification.getTitle();
        String mStrDescription = notification.getBody();
        int mIntPopupType = 0;
        final JSONObject mJsonObjectAdditionalData = notification.getAdditionalData();
        try {
            if (mJsonObjectAdditionalData != null) {
                Logger.d("additionalData:: mJsonObjectAdditionalData " + mJsonObjectAdditionalData.toString());
                Log.e("data", mJsonObjectAdditionalData.toString());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger.d("Header :: mStrHeader " + mStrHeader);
        Logger.d("Description :: mStrDescription " + mStrDescription);

        if (mStrHeader != null && mStrHeader.equals("Payment Received")) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                try {
//                    Intent fullScreenIntent = new Intent(this, ActivityTransparentPopup.class);
//                    fullScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK /*| Intent.FLAG_ACTIVITY_CLEAR_TASK*/ | Intent.FLAG_ACTIVITY_NO_HISTORY);
//                    fullScreenIntent.putExtra(ActivityTransparentPopup.INTENT_CONSTANT_ARG_POPUP_TYPE, mIntPopupType);
//                    fullScreenIntent.putExtra(ActivityTransparentPopup.INTENT_CONSTANT_ARG_POPUP_HEADER, mStrHeader);
//                    fullScreenIntent.putExtra(ActivityTransparentPopup.INTENT_CONSTANT_ARG_POPUP_DESCRIPTION, mStrDescription);
////                    Intent rIntent = this.getPackageManager()
////                            .getLaunchIntentForPackage(this.getPackageName() );
//                    PendingIntent intent = PendingIntent.getActivity(
//                            this, 0,
//                            fullScreenIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//                    AlarmManager manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
//                    manager.set(AlarmManager.RTC, System.currentTimeMillis(), intent);
//                    System.exit(2);

                    NotificationManager notificationManager =
                            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                    String CHANNEL_ID = BuildConfig.APPLICATION_ID.concat("_notification_id");
                    String CHANNEL_NAME = BuildConfig.APPLICATION_ID.concat("_notification_name");
                    assert notificationManager != null;

                    NotificationChannel mChannel = notificationManager.getNotificationChannel(CHANNEL_ID);
                    if (mChannel == null) {
                        mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                        notificationManager.createNotificationChannel(mChannel);
                    }

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);

                    builder.setSmallIcon(R.mipmap.logo)
                            .setContentTitle(context.getString(R.string.app_name))
                            .setContentText("Renewal")
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setCategory(NotificationCompat.CATEGORY_CALL)
                            .setFullScreenIntent(openNotificationPopup(mIntPopupType, mStrHeader, mStrDescription), true)
                            .setAutoCancel(true)
                            .setOngoing(true);

                    Notification notify = builder.build();
                    notificationManager.notify(2, notify);
                } catch (Exception e) {
                    Log.e("notify", e.getMessage());
                    e.printStackTrace();
                }

            } else {
                try {
                    startActivity(new Intent(this, ActivityTransparentPopup.class)
//                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY)
                            .putExtra(ActivityTransparentPopup.INTENT_CONSTANT_ARG_POPUP_TYPE, mIntPopupType)
                            .putExtra(ActivityTransparentPopup.INTENT_CONSTANT_ARG_POPUP_HEADER, mStrHeader)
                            .putExtra(ActivityTransparentPopup.INTENT_CONSTANT_ARG_POPUP_DESCRIPTION, mStrDescription));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

//            openParkingViolationScreen(mIntPopupType, mStrHeader, mStrDescription);


//            try {
//                Intent notifyIntent = new Intent(this, ActivityTransparentPopup.class);
//                notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                        | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                notifyIntent.putExtra(ActivityTransparentPopup.INTENT_CONSTANT_ARG_POPUP_TYPE, mIntPopupType);
//                notifyIntent.putExtra(ActivityTransparentPopup.INTENT_CONSTANT_ARG_POPUP_HEADER, mStrHeader);
//                notifyIntent.putExtra(ActivityTransparentPopup.INTENT_CONSTANT_ARG_POPUP_DESCRIPTION, mStrDescription);
//                PendingIntent notifyPendingIntent = PendingIntent.getActivity(
//                        this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
//                );
//                notifyPendingIntent.send();
//            } catch (PendingIntent.CanceledException e) {
//                e.printStackTrace();
//            }
        }

        /**
         *  Zoom Meeting notification starter
         * */
        Log.d("TAG", "HEADER: "+mStrHeader);
        if (mStrHeader != null && mStrHeader.equalsIgnoreCase("Inspection Meeting")) {

            if (!BaseApplication.inAMeeting) {

                //Uri soundUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.mu_inspection_request2);
                MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.mu_inspection_request2);
                mediaPlayer.start();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    try {
                        //Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/raw/mu_inspection_request2");

//                    Intent fullScreenIntent = new Intent(this, ActivityTransparentPopup.class);
//                    fullScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK /*| Intent.FLAG_ACTIVITY_CLEAR_TASK*/ | Intent.FLAG_ACTIVITY_NO_HISTORY);
//                    fullScreenIntent.putExtra(ActivityTransparentPopup.INTENT_CONSTANT_ARG_POPUP_TYPE, mIntPopupType);
//                    fullScreenIntent.putExtra(ActivityTransparentPopup.INTENT_CONSTANT_ARG_POPUP_HEADER, mStrHeader);
//                    fullScreenIntent.putExtra(ActivityTransparentPopup.INTENT_CONSTANT_ARG_POPUP_DESCRIPTION, mStrDescription);
////                    Intent rIntent = this.getPackageManager()
////                            .getLaunchIntentForPackage(this.getPackageName() );
//                    PendingIntent intent = PendingIntent.getActivity(
//                            this, 0,
//                            fullScreenIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//                    AlarmManager manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
//                    manager.set(AlarmManager.RTC, System.currentTimeMillis(), intent);
//                    System.exit(2);

                        if (BaseApplication.isActivityVisible()) {
                            mediaPlayer.start();
                            context.startActivity(new Intent(context, ZoomTransparentPopupActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    .putExtra("popup_type", mIntPopupType)
                                    .putExtra("popup_header", mStrHeader)
                                    .putExtra("popup_description", mStrDescription));
                            //startAlert(mIntPopupType, mStrHeader, mStrDescription);
                            //openNotificationPopup(mIntPopupType, mStrHeader, mStrDescription);
                        } else {

                            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                            String CHANNEL_ID = BuildConfig.APPLICATION_ID.concat("_notification_id");
                            String CHANNEL_NAME = BuildConfig.APPLICATION_ID.concat("_notification_name");
                            assert notificationManager != null;

                            NotificationChannel mChannel = notificationManager.getNotificationChannel(CHANNEL_ID);
                            if (mChannel == null) {
                                mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                                notificationManager.createNotificationChannel(mChannel);
                            }

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);

                            builder.setSmallIcon(R.mipmap.logo)
                                    .setContentTitle(context.getString(R.string.app_name))
                                    .setContentText("Click here to join inspection meeting")
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                                    .setCategory(NotificationCompat.CATEGORY_CALL)
                                    .setFullScreenIntent(openNotificationPopup(mIntPopupType, mStrHeader, mStrDescription), true)
                                    .setAutoCancel(true)
                                    .setOngoing(true);

                            Notification notify = builder.build();
                            notificationManager.notify(2, notify);
                        }
                    } catch (Exception e) {
                        Log.e("notify", e.getMessage());
                        e.printStackTrace();
                    }

                } else {
                    try {
                        mediaPlayer.start();
                    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationManager notificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                        String CHANNEL_ID = BuildConfig.APPLICATION_ID.concat("_notification_id");
                        String CHANNEL_NAME = BuildConfig.APPLICATION_ID.concat("_notification_name");
                        assert notificationManager != null;

                        NotificationChannel mChannel = notificationManager.getNotificationChannel(CHANNEL_ID);
                        if (mChannel == null) {
                            mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                            notificationManager.createNotificationChannel(mChannel);
                        }

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);

                        builder.setSmallIcon(R.mipmap.logo)
                                .setContentTitle(getString(R.string.app_name))
                                .setContentText("Click here to join inspection meeting")
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setCategory(NotificationCompat.CATEGORY_CALL)
                                .setFullScreenIntent(openNotificationPopup(mIntPopupType, mStrHeader, mStrDescription), true)
                                .setAutoCancel(true)
                                .setOngoing(true);

                        Notification notify = builder.build();
                        notificationManager.notify(2, notify);
                    }else {
                        NotificationManager notificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

                        builder.setSmallIcon(R.mipmap.logo)
                                .setContentTitle(getString(R.string.app_name))
                                .setContentText("Click here to join inspection meeting")
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setCategory(NotificationCompat.CATEGORY_CALL)
                                .setFullScreenIntent(openNotificationPopup(mIntPopupType, mStrHeader, mStrDescription), true)
                                .setAutoCancel(true)
                                .setOngoing(true);

                        Notification notify = builder.build();
                        notificationManager.notify(2, notify);
                    }*/
                        Log.d("TAG", BaseApplication.isActivityVisible() + "");
                        if (BaseApplication.isActivityVisible()) {
                            startActivity(new Intent(this, ZoomTransparentPopupActivity.class)
//                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY)
                                    .putExtra("popup_type", mIntPopupType)
                                    .putExtra("popup_header", mStrHeader)
                                    .putExtra("popup_description", mStrDescription));
                        } else {
                            startAlert(mIntPopupType, mStrHeader, mStrDescription);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }

        /**
         * Zoom notification for end meeting
         * */
        if (mStrHeader != null && mStrHeader.equalsIgnoreCase("Inspection meeting ended")){
            OneSignal.clearOneSignalNotifications();
            OneSignal.removeNotification(2);
            String CHANNEL_ID = BuildConfig.APPLICATION_ID + "_notification_id";
            NotificationManagerCompat.from(context).cancelAll();
            NotificationManagerCompat.from(context).deleteNotificationChannel(CHANNEL_ID);
        }
//        else if (mStrDescription != null && mStrDescription.equalsIgnoreCase("Congratulations! You have earned a reward.")) {
//            try {
//                Intent intent = new Intent(getApplicationContext(), OfferActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

//        if(mIntPopupType != 4 && ){
//            ActivityTransparentPopup.startActivityIntent(getBaseContext(), mIntPopupType, mStrHeader, mStrDescription);
//        }

//        if (mIntPopupType == 4 || mStrType.trim().equals("4")) {
//            //System.exit(0);
//            //Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://apps.hicare.in/uatapi/applicationlogic/gettaskdetail?taskid=" + taskid + ""));
//            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://apps.hicare.in/serviceaudit/lead/GetNextCall?Ticketid=" + taskid + ""));
//            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(i);
//        } else {
//            ActivityTransparentPopUp.startActivityIntent(getBaseContext(), mIntPopupType, mStrHeader, mStrDescription);
//        }
        //ActivityTransparentPopUp.startActivityIntent(getBaseContext(),mIntPopupType ,mStrHeader,mStrDescription);
        return false;
    }

    private void registerBroadcastReceiver() {

        final IntentFilter intentFilter = new IntentFilter();
        /** System Defined Broadcast */
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_USER_PRESENT);

        ZoomBroadcast zoomBroadcast = new ZoomBroadcast();

        context.registerReceiver(zoomBroadcast, intentFilter);
    }


    public void clearNotification(){
        Intent intent = new Intent(context, ZoomNotificationClearer.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + (1), pendingIntent);
    }
    public void startAlert(int mPopupType, String mPopupHeader, String mPopupDescription){
        Intent intent = new Intent(context, ZoomBroadcast.class);
        intent.putExtra("popup_type", mPopupType);
        intent.putExtra("popup_header", mPopupHeader);
        intent.putExtra("popup_description", mPopupDescription);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + (1), pendingIntent);
    }

    PendingIntent openNotificationPopup(int mIntPopupType, String mStrHeader, String mStrDescription) {
        Intent fullScreenIntent;
        if (mStrHeader.equalsIgnoreCase("Inspection Meeting")){
            Log.d("TAG", "Opening from pending intent");
            fullScreenIntent = new Intent(context, ZoomTransparentPopupActivity.class);
            fullScreenIntent.putExtra("popup_type", mIntPopupType);
            fullScreenIntent.putExtra("popup_header", mStrHeader);
            fullScreenIntent.putExtra("popup_description", mStrDescription);
        }else {
            fullScreenIntent = new Intent(context, ActivityTransparentPopup.class);
            fullScreenIntent.putExtra(ActivityTransparentPopup.INTENT_CONSTANT_ARG_POPUP_TYPE, mIntPopupType);
            fullScreenIntent.putExtra(ActivityTransparentPopup.INTENT_CONSTANT_ARG_POPUP_HEADER, mStrHeader);
            fullScreenIntent.putExtra(ActivityTransparentPopup.INTENT_CONSTANT_ARG_POPUP_DESCRIPTION, mStrDescription);
        }
//        fullScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK /*| Intent.FLAG_ACTIVITY_CLEAR_TASK*/ | Intent.FLAG_ACTIVITY_NO_HISTORY);
        return PendingIntent.getActivity(context, 0, fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void notificationOpened(OSNotificationOpenedResult result) {
        Log.d("TAG", "Opened");
        try {
            OSNotificationAction.ActionType actionType = result.getAction().getType();
            JSONObject data = result.getNotification().getAdditionalData();
            String mStrHeader = result.getNotification().getTitle();
            String mStrDescription = result.getNotification().getBody();
            Logger.d("Description ss :: mStrDescription " + mStrDescription);

            if (context != null) {
                if (mStrHeader.equalsIgnoreCase("Inspection Meeting")){
                    Log.d("TAG", "Opened M");
                    Intent intent = new Intent(context, ZoomTransparentPopupActivity.class);
                    intent.putExtra("popup_type", 0);
                    intent.putExtra("popup_header", mStrHeader);
                    intent.putExtra("popup_description", mStrDescription);
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                if (mStrDescription != null && mStrDescription.equalsIgnoreCase("Congratulations! You have earned a reward.")) {
                    Intent intent = new Intent(context, OfferActivity.class);
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else if(mStrDescription != null && (mStrDescription.equalsIgnoreCase("Your life is exausted kindly contact the service center.") || mStrDescription.equalsIgnoreCase("Your points are deducted. Click to know more."))){
                    Intent intent = new Intent(context, KarmaActivity.class);
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, HomeActivity.class);
                    intent.setAction(Intent.ACTION_MAIN);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
