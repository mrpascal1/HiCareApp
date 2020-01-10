package com.ab.hicarerun.handler;

/**
 * Created by Arjun Bhatt on 12/16/2019.
 */
public interface OnAddActivityClickHandler extends OnListItemClickHandler {
    void onAddActivityClick(int position);
    void onNotDoneClick(int position);
}

