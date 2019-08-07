package com.ab.hicarerun.utils;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.ab.hicarerun.service.ServiceLocationSend;

public class HandShakeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {

        if ("HandshakeAction".equals(intent.getAction())) {
            try {
                Log.i("AutoHandshake", intent.getAction());

                context.startService(new Intent(context, ServiceLocationSend.class));

            } catch (Exception e) {
                Log.i("onRecieveHandshake", e.getMessage());
            }
        }
    }
}
