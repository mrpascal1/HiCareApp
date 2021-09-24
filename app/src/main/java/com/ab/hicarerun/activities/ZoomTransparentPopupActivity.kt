package com.ab.hicarerun.activities

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ab.hicarerun.databinding.ActivityZoomTransparentPopupBinding
import us.zoom.sdk.*

class ZoomTransparentPopupActivity : AppCompatActivity() {

    lateinit var binding: ActivityZoomTransparentPopupBinding

    var popupType = 0
    var popupHeader = ""
    var popupDescription = ""
    val INTENT_CONSTANT_ARG_POPUP_TYPE = "popup_type"
    val INTENT_CONSTANT_ARG_POPUP_HEADER = "popup_header"
    val INTENT_CONSTANT_ARG_POPUP_DESCRIPTION = "popup_description"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityZoomTransparentPopupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        popupType = intent.getIntExtra(INTENT_CONSTANT_ARG_POPUP_TYPE, 0)
        popupHeader = intent.getStringExtra(INTENT_CONSTANT_ARG_POPUP_HEADER).toString()
        popupDescription = intent.getStringExtra(INTENT_CONSTANT_ARG_POPUP_DESCRIPTION).toString()

        initZoomSdk(this)
        setZoomNotification(this)
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
            }

            override fun onZoomAuthIdentityExpired() {
            }
        }
        sdk.initialize(context, listener, params)
    }
    private fun setZoomNotification(context: Context){
        val meetingService = ZoomSDK.getInstance().meetingService
        val options = JoinMeetingOptions()
        val params = JoinMeetingParams()

        val arrayString = popupDescription.split("\\|".toRegex())
        val id = arrayString[0].split(":")[1]
        val password = arrayString[1].split(":")[1]
        val autoStart = arrayString[2].split(":")[1]
        val title = arrayString[3].split(":")[1]
        val url = arrayString[4].split(":")[1]

        params.displayName = "Shahid"
        params.meetingNo = id
        params.password = password

        binding.meetingIdTv.text = id
        binding.nameTv.text = "Shahid"

        if (autoStart == "1"){
            meetingService.joinMeetingWithParams(context, params, options)
        }
        binding.joinBtn.setOnClickListener {
            meetingService.joinMeetingWithParams(context, params, options)
        }

        binding.cancelBtn.setOnClickListener {
            finish()
        }
    }
}
