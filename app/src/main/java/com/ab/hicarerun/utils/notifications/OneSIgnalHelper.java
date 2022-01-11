package com.ab.hicarerun.utils.notifications;

import android.app.Application;
import android.content.Context;

import com.onesignal.OneSignal;
import com.orhanobut.logger.Logger;

public class OneSIgnalHelper extends Application {

     private String mStrUserID ;

    public OneSIgnalHelper(final Context context) {
        // OneSignal Initialization
        /*OneSignal.initWithContext(context);
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationOpenedHandler(new OneSignalSilentNotificationHandlerService(context))
                .init();
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        fetchPlayerID(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                Logger.d("OneSignal", "User:" + userId);
                mStrUserID = userId;
                if (registrationId != null)
                    Logger.d("OneSignal", "registrationId:" + registrationId);
            }
        });*/
    }

    /*public synchronized String fetchPlayerID(OneSignal.IdsAvailableHandler mOneSIgnalIdshandler){
        if(mStrUserID==null){
            OneSignal.idsAvailable(mOneSIgnalIdshandler);
        }

        return mStrUserID;
    }*/

    public synchronized String getmStrUserID() {
        return mStrUserID;
    }

    public synchronized void setmStrUserID(String mStrUserID) {
        this.mStrUserID = mStrUserID;
    }
}
