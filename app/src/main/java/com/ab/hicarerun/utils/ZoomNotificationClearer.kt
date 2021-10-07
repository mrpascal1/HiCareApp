package com.ab.hicarerun.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import com.onesignal.OneSignal

class ZoomNotificationClearer: BroadcastReceiver() {
    override fun onReceive(p0: Context, p1: Intent?) {
        OneSignal.clearOneSignalNotifications()
    }
}