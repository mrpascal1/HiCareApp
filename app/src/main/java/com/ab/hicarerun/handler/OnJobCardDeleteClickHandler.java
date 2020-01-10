package com.ab.hicarerun.handler;

/**
 * Created by Arjun Bhatt on 1/8/2020.
 */
public interface OnJobCardDeleteClickHandler extends OnExpandListItemClickHandler {
    void onDeleteJobCard(int parent, int child);
}
