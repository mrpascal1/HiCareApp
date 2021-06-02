package com.ab.hicarerun.handler;

import android.graphics.drawable.Drawable;

/**
 * Created by Arjun Bhatt on 3/5/2021.
 */
public interface CameraListener {
    void setCameraFrontFacing();

    void setCameraBackFacing();

    boolean isCameraFrontFacing();

    boolean isCameraBackFacing();

    void setFrontCameraId(String cameraId);

    void setBackCameraId(String cameraId);

    String getFrontCameraId();

    String getBackCameraId();

    void hideStatusBar();

    void showStatusBar();

    void hideStillshotWidgets();

    void showStillshotWidgets();

    void toggleViewStickersFragment();

    void addSticker(Drawable sticker);

    void setTrashIconSize(int width, int height);
}
