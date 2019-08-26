package com.ab.hicarerun.utils.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ab.hicarerun.service.ServiceLocationSend;
import com.ab.hicarerun.utils.HandShakeReceiver;
import com.ab.hicarerun.utils.SharedPreferencesUtility;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class CallServiceAfterReboot extends BroadcastReceiver {
    private long REPEATED_TIME = 900000;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            try {
                String time = SharedPreferencesUtility.getPrefString(context, SharedPreferencesUtility.PREF_INTERVAL);
                if(time!=null && time.length()>0){
                    REPEATED_TIME = Long.parseLong(time);
                }
                AlarmManager mAlarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                Intent restarIntent = new Intent(context, ServiceLocationSend.class);
                PendingIntent pendingUpdateIntent = PendingIntent.getService(context,
                        0, restarIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                Calendar futureDate = Calendar.getInstance();

                if (android.os.Build.VERSION.SDK_INT >= 19) {
                    mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, futureDate.getTime().getTime(), REPEATED_TIME, pendingUpdateIntent);
                } else {
                    mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, futureDate.getTime().getTime(), REPEATED_TIME, pendingUpdateIntent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}