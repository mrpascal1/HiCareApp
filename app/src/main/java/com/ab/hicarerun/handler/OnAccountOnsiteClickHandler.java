package com.ab.hicarerun.handler;

/**
 * Created by Arjun Bhatt on 12/18/2019.
 */
public interface OnAccountOnsiteClickHandler {
    void onPrimaryMobileClicked(int position);
    void onAlternateMobileClicked(int position);
    void onTelePhoneClicked(int position);
    void onTrackLocationIconClicked(int position);
}
