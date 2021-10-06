package com.ab.hicarerun.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import com.ab.hicarerun.BaseApplication
import com.ab.hicarerun.databinding.ActivityZoomTransparentPopupBinding
import com.ab.hicarerun.network.models.LoginResponse
import com.ab.hicarerun.utils.ZoomBroadcast
import com.onesignal.BuildConfig
import com.onesignal.OneSignal
import io.realm.RealmResults
import us.zoom.sdk.*

class ZoomTransparentPopupActivity : AppCompatActivity() {

    lateinit var binding: ActivityZoomTransparentPopupBinding

    var popupType = 0
    var popupHeader = ""
    var popupDescription = ""
    val INTENT_CONSTANT_ARG_POPUP_TYPE = "popup_type"
    val INTENT_CONSTANT_ARG_POPUP_HEADER = "popup_header"
    val INTENT_CONSTANT_ARG_POPUP_DESCRIPTION = "popup_description"
    var empName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityZoomTransparentPopupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        popupType = intent.getIntExtra(INTENT_CONSTANT_ARG_POPUP_TYPE, 0)
        popupHeader = intent.getStringExtra(INTENT_CONSTANT_ARG_POPUP_HEADER).toString()
        popupDescription = intent.getStringExtra(INTENT_CONSTANT_ARG_POPUP_DESCRIPTION).toString()
        val broadcast = intent.getStringExtra("broadcast").toString()

        val loginResponse: RealmResults<LoginResponse> = BaseApplication.getRealm().where(
            LoginResponse::class.java).findAll()
        if (loginResponse != null && loginResponse.size > 0) {
            empName = loginResponse[0]?.userName.toString()
        }

        Log.d("TAG", "Notification Called $popupHeader and $popupDescription")

        if (broadcast == "yes"){
            setZoomNotification(this, false)
        }else{
            initZoomSdk(this)
        }

        binding.joinBtn.setOnClickListener {
            setZoomNotification(this, true)
        }

        binding.cancelBtn.setOnClickListener {
            finish()
        }
    }

    private fun initZoomSdk(context: Context){
        val sdk = ZoomSDK.getInstance()
        val params = ZoomSDKInitParams()
        params.appKey = "Ne76diUgLjJUm1SipY8gUC9vZAdQLiC60zRy"
        params.appSecret = "wIpM7lUCK54iyeYstPKyQTGXbnX3XRu6chtZ"
        params.domain = "zoom.us"
        params.enableLog = true

        val listener = object : ZoomSDKInitializeListener {
            override fun onZoomSDKInitializeResult(p0: Int, p1: Int) {
                Log.d("TAG", "Init")
                setZoomNotification(context, false)
            }

            override fun onZoomAuthIdentityExpired() {
                Log.d("TAG", "Exp")
            }
        }
        /*if (sdk.isInitialized){
            setZoomNotification(context, false)
        }else{
            val listener2 = object : ZoomSDKInitializeListener {
                override fun onZoomSDKInitializeResult(p0: Int, p1: Int) {
                    setZoomNotification(context, false)
                    Log.d("TAG", "Init")
                }

                override fun onZoomAuthIdentityExpired() {
                    Log.d("TAG", "Exp")
                }
            }
            sdk.initialize(context, listener2, params)
        }*/
        sdk.initialize(context, listener, params)
    }
    private fun setZoomNotification(context: Context, buttonClick: Boolean){
        val meetingService = ZoomSDK.getInstance()?.meetingService
        val options = JoinMeetingOptions()
        val params = JoinMeetingParams()
        meetingService?.addListener { meetingStatus, i, i2 ->
            if (i == 9 || meetingStatus == MeetingStatus.MEETING_STATUS_IDLE){
                finish()
            }
            Log.d("TAG", meetingStatus.name)
            Log.d("TAG-i", i.toString())
            Log.d("TAG-i2", i2.toString())
        }

        val arrayString = popupDescription.split("\\|".toRegex())
        val id = arrayString[0].split(":")[1]
        val password = arrayString[1].split(":")[1]
        val autoStart = arrayString[2].split(":")[1]
        val title = arrayString[3].split(":")[1]
        val url = arrayString[4].split(":")[1]

        params.displayName = empName
        params.meetingNo = id
        params.password = password

        binding.meetingIdTv.text = id
        binding.nameTv.text = empName


        if (!buttonClick) {
            if (autoStart == "1" || autoStart.toInt() == 1) {
                Log.d("TAG", "Notification Called")
                meetingService?.joinMeetingWithParams(context, params, options)
            }else{
                binding.joinBtn.isEnabled = true
                binding.joinBtn.text = "Join"
            }
        }else{
            meetingService?.joinMeetingWithParams(context, params, options)
        }

    }

    override fun onPause() {
        super.onPause()
        OneSignal.clearOneSignalNotifications()
        OneSignal.cancelNotification(2)
        val CHANNEL_ID = BuildConfig.APPLICATION_ID + "_notification_id"
        NotificationManagerCompat.from(this).cancelAll()
        NotificationManagerCompat.from(this).deleteNotificationChannel(CHANNEL_ID)
/*        val CHANNEL_ID = BuildConfig.APPLICATION_ID + "_notification_id"
        NotificationManagerCompat.from(this).deleteNotificationChannel(CHANNEL_ID)
        */
    }

    override fun onBackPressed() {

    }
}
