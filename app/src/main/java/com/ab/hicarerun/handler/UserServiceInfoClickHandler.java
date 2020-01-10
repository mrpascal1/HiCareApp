package com.ab.hicarerun.handler;

import android.view.View;

/**
 * Created by Arjun Bhatt on 11/28/2019.
 */
public interface UserServiceInfoClickHandler {
    void onIncompleteReasonClicked(View view);
    void onCalendarClicked(View view);
    void onPaymentLinkClicked(View view);
    void onUploadChequeClicked(View view);
    void onImageDeleteClicked(View view);
    void onBankNameClicked(View view);
}
