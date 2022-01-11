package com.ab.hicarerun.network.models.generalmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 11/19/2019.
 */
public class OnSiteOtpResponse {
    @SerializedName("IsSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("Data")
    @Expose
    private OnSiteOtp data;
    @SerializedName("ErrorMessage")
    @Expose
    private String ErrorMessage;


    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public OnSiteOtp getData() {
        return data;
    }

    public void setData(OnSiteOtp data) {
        this.data = data;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }
}
