package com.ab.hicarerun.network.models.TrainingModel;

import android.provider.MediaStore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 11/19/2019.
 */
public class WelcomeVideoResponse {
    @SerializedName("IsSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("com.ab.hicarerun.network.models.TSScannerModel.Data")
    @Expose
    private Videos data = null;

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public Videos getData() {
        return data;
    }

    public void setData(Videos data) {
        this.data = data;
    }
}
