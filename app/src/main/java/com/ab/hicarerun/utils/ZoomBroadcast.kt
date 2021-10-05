package com.ab.hicarerun.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.ab.hicarerun.activities.ZoomTransparentPopupActivity
import us.zoom.sdk.ZoomSDK
import us.zoom.sdk.ZoomSDKInitParams
import us.zoom.sdk.ZoomSDKInitializeListener

class ZoomBroadcast : BroadcastReceiver() {
    val popupType = "popup_type"
    val popupHeader = "popup_header"
    val popupDescription = "popup_description"
    override fun onReceive(context: Context, intent: Intent?) {
        Toast.makeText(context, "Inspection call...", Toast.LENGTH_SHORT).show()
        val type = intent?.getIntExtra(popupType, -1).toString().toInt()
        val header = intent?.getStringExtra(popupHeader).toString()
        val description = intent?.getStringExtra(popupDescription).toString()

        val sdk = ZoomSDK.getInstance()
        val params = ZoomSDKInitParams()
        params.appKey = "Ne76diUgLjJUm1SipY8gUC9vZAdQLiC60zRy"
        params.appSecret = "wIpM7lUCK54iyeYstPKyQTGXbnX3XRu6chtZ"
        params.domain = "zoom.us"
        params.enableLog = true

        val listener = object : ZoomSDKInitializeListener {
            override fun onZoomSDKInitializeResult(p0: Int, p1: Int) {
                Log.d("TAG", "Init")
                val zoomIntent = Intent(context, ZoomTransparentPopupActivity::class.java)
                zoomIntent.putExtra("broadcast", "yes")
                zoomIntent.putExtra(popupType, type)
                zoomIntent.putExtra(popupHeader, header)
                zoomIntent.putExtra(popupDescription, description)
                zoomIntent.setAction(Intent.ACTION_MAIN);
                zoomIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                zoomIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(zoomIntent)
            }

            override fun onZoomAuthIdentityExpired() {
                Log.d("TAG", "Exp")
            }
        }
        sdk.initialize(context, listener, params)
    }
}