package com.ab.hicarerun.handler;

/**
 * Created by Arjun Bhatt on 5/31/2020.
 */
public interface OnBankClickHandler extends OnListItemClickHandler {
    void onBankNameClick(int position);
    void onBankDateClick(int position);
}