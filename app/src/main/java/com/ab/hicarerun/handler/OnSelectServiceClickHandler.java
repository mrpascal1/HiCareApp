package com.ab.hicarerun.handler;

/**
 * Created by Arjun Bhatt on 12/23/2019.
 */
public interface OnSelectServiceClickHandler extends OnListItemClickHandler {
    void onRadioYesClicked(int position);
    void onRadioNoClicked(int position);
}
