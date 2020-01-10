package com.ab.hicarerun.handler;

/**
 * Created by Arjun Bhatt on 12/24/2019.
 */
public interface OnRecentTaskClickHandler extends OnExpandListItemClickHandler {
    void onDeleteItemClicked(int parent, int Child);

    void onViewItemClicked(int parent, int child);
}
