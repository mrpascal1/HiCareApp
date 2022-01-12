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
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
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
import com.ab.hicarerun.BuildConfig;
import com.onesignal.OSMutableNotification;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OSNotificationReceivedEvent;
import com.onesignal.OneSignal;
import com.onesignal.OneSignalNotificationManager;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.math.BigInteger;

public class OneSignalSilentNotificationHandlerService implements OneSignal.OSRemoteNotificationReceivedHandler, OneSignal.OSNotificationOpenedHandler {

    private Context context;
    String notificationId;

    public OneSignalSilentNotificationHandlerService(Context context) {
        this.context = context.getApplicationContext();
    }

    public OneSignalSilentNotificationHandlerService() {

    }

    @Override
    public void remoteNotificationReceived(Context ctx, OSNotificationReceivedEvent notificationReceivedEvent) {
        Log.d("TAG", "...Here we go ...");
        Log.d("TAG", "...Magic begins ...");
        this.context = ctx;
        OSNotification notification = notificationReceivedEvent.getNotification();
        OSMutableNotification mutableNotification = notification.mutableCopy();
        mutableNotification.setExtender(builder -> {
            // Sets the accent color to Green on Android 5+ devices.
            // Accent color controls icon and action buttons on Android 5+. Accent color does not change app title on Android 10+
            builder.setColor(new BigInteger("FF00FF00", 16).intValue());
            // Sets the notification Title to Red
            Spannable spannableTitle = new SpannableString(notification.getTitle());
            spannableTitle.setSpan(new ForegroundColorSpan(Color.RED),0,notification.getTitle().length(),0);
            builder.setContentTitle(spannableTitle);
            // Sets the notification Body to Blue
            Spannable spannableBody = new SpannableString(notification.getBody());
            spannableBody.setSpan(new ForegroundColorSpan(Color.BLUE),0,notification.getBody().length(),0);
            builder.setContentText(spannableBody);
            //Force remove push from Notification Center after 30 seconds
            builder.setTimeoutAfter(30000);
            return builder;
        });

        notificationReceivedEvent.complete(mutableNotification);

        notificationId = notification.getNotificationId();
        String mStrHeader = notification.getTitle();
        String mStrDescription = notification.getBody();
        int mIntPopupType = 0;
        final JSONObject mJsonObjectAdditionalData = notification.getAdditionalData();
        Log.i("TAG", "Received Notification Data: ");
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

                    NotificationManager notificationManager =
                            (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

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
                            .setContentTitle(ctx.getString(R.string.app_name))
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
                    ctx.startActivity(new Intent(ctx, ActivityTransparentPopup.class)
                            .putExtra(ActivityTransparentPopup.INTENT_CONSTANT_ARG_POPUP_TYPE, mIntPopupType)
                            .putExtra(ActivityTransparentPopup.INTENT_CONSTANT_ARG_POPUP_HEADER, mStrHeader)
                            .putExtra(ActivityTransparentPopup.INTENT_CONSTANT_ARG_POPUP_DESCRIPTION, mStrDescription));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        /* Zoom Meeting notification starter */
        Log.d("TAG", "HEADER: "+mStrHeader);
        if (mStrHeader != null && mStrHeader.equalsIgnoreCase("Inspection Meeting")) {

            if (!BaseApplication.inAMeeting) {

                //Uri soundUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.mu_inspection_request2);
                MediaPlayer mediaPlayer = MediaPlayer.create(ctx, R.raw.mu_inspection_request2);
                mediaPlayer.start();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    try {

                        if (BaseApplication.isActivityVisible()) {
                            mediaPlayer.start();
                            ctx.startActivity(new Intent(ctx, ZoomTransparentPopupActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    .putExtra("popup_type", mIntPopupType)
                                    .putExtra("popup_header", mStrHeader)
                                    .putExtra("popup_description", mStrDescription));
                        } else {

                            NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

                            String CHANNEL_ID = BuildConfig.APPLICATION_ID.concat("_notification_id");
                            String CHANNEL_NAME = BuildConfig.APPLICATION_ID.concat("_notification_name");
                            assert notificationManager != null;

                            NotificationChannel mChannel = notificationManager.getNotificationChannel(CHANNEL_ID);
                            if (mChannel == null) {
                                mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                                notificationManager.createNotificationChannel(mChannel);
                            }

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, CHANNEL_ID);

                            builder.setSmallIcon(R.mipmap.logo)
                                    .setContentTitle(ctx.getString(R.string.app_name))
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
                        Log.d("TAG", BaseApplication.isActivityVisible() + "");
                        if (BaseApplication.isActivityVisible()) {
                            ctx.startActivity(new Intent(ctx, ZoomTransparentPopupActivity.class)
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
            NotificationManagerCompat.from(ctx).cancelAll();
            NotificationManagerCompat.from(ctx).deleteNotificationChannel(CHANNEL_ID);
        }
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
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                    /*intent.setAction(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
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
