package com.ab.hicarerun.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.ab.hicarerun.activities.LoginActivity;

public class AutoLogoutReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {

        if ("LogOutAction".equals(intent.getAction())) {
            try {
                Log.i("LogOutAuto", intent.getAction());
                Toast.makeText(context, "Logout", Toast.LENGTH_SHORT).show();
                SharedPreferencesUtility.savePrefBoolean(context, SharedPreferencesUtility.IS_USER_LOGIN,
                        false);
                context.startActivity(new Intent(context, LoginActivity.class));
            } catch (Exception e) {
                Log.i("onReceiveAutoLogout", e.getMessage());
            }


        }
    }
}