package com.ab.hicarerun.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.ab.hicarerun.activities.ZoomTransparentPopupActivity

class ZoomBroadcast : BroadcastReceiver() {
    val popupType = "popup_type"
    val popupHeader = "popup_header"
    val popupDescription = "popup_description"
    override fun onReceive(p0: Context, intent: Intent?) {
        Toast.makeText(p0, "Opening", Toast.LENGTH_SHORT).show()
        val type = intent?.getIntExtra(popupType, -1).toString().toInt()
        val header = intent?.getStringExtra(popupHeader).toString()
        val description = intent?.getStringExtra(popupDescription).toString()

        val zoomIntent = Intent(p0, ZoomTransparentPopupActivity::class.java)
        zoomIntent.putExtra(popupType, type)
        zoomIntent.putExtra(popupHeader, header)
        zoomIntent.putExtra(popupDescription, description)
        zoomIntent.setAction(Intent.ACTION_MAIN);
        zoomIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        zoomIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        p0.startActivity(zoomIntent)
    }
}