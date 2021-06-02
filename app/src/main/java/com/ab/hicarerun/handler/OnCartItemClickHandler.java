package com.ab.hicarerun.handler;

/**
 * Created by Arjun Bhatt on 6/3/2020.
 */
public interface OnCartItemClickHandler {
    void onDeleteCartClicked(int position);

    void onAddQuantityClicked(int position);

    void onSubstractQuantityClicked(int position);
}
