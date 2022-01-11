package com.ab.hicarerun.network.models.trainingmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 11/19/2019.
 */
public class WelcomeVideoResponse {
    @SerializedName("IsSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("Data")
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
