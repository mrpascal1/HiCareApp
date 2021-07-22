package com.ab.hicarerun.handler;

import com.ab.hicarerun.network.models.ProfileModel.Profile;

public interface OnCallListItemClickHandler extends OnListItemClickHandler {
    void onPrimaryMobileClicked(int position);
    void onAlternateMobileClicked(int position);
    void onTelePhoneClicked(int position);
    void onTrackLocationIconClicked(int position);
    void onTechnicianHelplineClicked(int position);
    void onResourcePartnerPic(Profile profile);
    void onInstructionsClicked(int position);
}
