package com.ab.hicarerun.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.ab.hicarerun.handler.Common;
import com.ab.hicarerun.handler.OtpReceivedInterface;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Arjun Bhatt on 6/24/2019.
 */
public class SMSListener extends BroadcastReceiver {
    private static final String TAG = "SmsBroadcastReceiver";
    OtpReceivedInterface otpReceiveInterface = null;

    public void setOnOtpListeners(OtpReceivedInterface otpReceiveInterface) {
        this.otpReceiveInterface = otpReceiveInterface;
    }

    public void onReceive(Context context, Intent intent) {
        try{
            if(SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
                Bundle extras = intent.getExtras();
                Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

                switch (status.getStatusCode()) {
                    case CommonStatusCodes.SUCCESS:
                        // Get SMS message contents
                        String message =
                                (String)extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                        // Extract one-time code as per the message
                        if (otpReceiveInterface != null) {
//                        String otp = message.replace("<#> is Your HiCare verification code.", "");
                            String otp = message.substring(4,10);
                            otpReceiveInterface.onOtpReceived(otp);
                        }
                        break;
                    case CommonStatusCodes.TIMEOUT:
                        // Waiting for SMS timed out (5 minutes)
                        //Send your message to the respected activity
                        if (otpReceiveInterface != null) {
                            otpReceiveInterface.onOtpTimeout();
                        }
                        break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
