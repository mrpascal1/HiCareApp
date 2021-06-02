package com.ab.hicarerun.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import static android.app.slice.Slice.EXTRA_TOGGLE_STATE;

/**
 * Created by Arjun Bhatt on 9/25/2020.
 */
public class MyBroadCastReceiver extends BroadcastReceiver {

    public static int sReceivedCount = 0;
    public static String EXTRA_MESSAGE = "message";

    private static Uri sliceUri = Uri.parse("content://com.android.example.slicesample/count");

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.hasExtra(EXTRA_TOGGLE_STATE)) {
            Toast.makeText(context, "Toggled:  " + intent.getBooleanExtra(
                    EXTRA_TOGGLE_STATE, false),
                    Toast.LENGTH_LONG).show();
            sReceivedCount++;
            context.getContentResolver().notifyChange(sliceUri, null);
        }
    }
}

