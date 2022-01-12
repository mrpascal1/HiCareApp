package com.ab.hicarerun.utils.notifications;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.onesignal.OneSignal;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.util.Objects;

public class OneSIgnalHelper extends Application {

     private String mStrUserID;

    public OneSIgnalHelper(final Context context) {
        // OneSignal Initialization
        OneSignal.initWithContext(context);
        OneSignal.setAppId("388766c1-4dc5-4e4d-a206-9b4f583d5d9d");
        OneSignal.unsubscribeWhenNotificationsAreDisabled(true);
        OneSignal.setNotificationOpenedHandler(new OneSignalSilentNotificationHandlerService(context));
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.setNotificationWillShowInForegroundHandler(event -> {

        });

        try{
            mStrUserID = Objects.requireNonNull(OneSignal.getDeviceState()).getUserId();
            Log.d("TAG-userid", mStrUserID);
        }catch (NullPointerException e){
            Log.d("TAG-userid", "Null");
        }

        /*fetchPlayerID(new OneSignal.OSGetTagsHandler() {
            @Override
            public void tagsAvailable(JSONObject tags) {
                Log.d("TAG-userid", tags.toString());
            }
        });*/
    }


    public synchronized String fetchPlayerID(OneSignal.OSGetTagsHandler mOneSIgnalIdshandler){
        if(mStrUserID==null){
            OneSignal.getDeviceState();
        }
        return mStrUserID;
    }

    public synchronized String getmStrUserID() {
        return mStrUserID;
    }

    public synchronized void setmStrUserID(String mStrUserID) {
        this.mStrUserID = mStrUserID;
    }
}
