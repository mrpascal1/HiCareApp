package com.ab.hicarerun.handler;

/**
 * Created by Arjun Bhatt on 4/6/2020.
 */
public interface OnCheckListItemClickHandler {
    void onPositiveButtonClicked(int position);

    void onNegativeButtonClicked(int position);

    void onNotRequiredButtonClicked(int position);

    void onCameraClicked(int position);

}
