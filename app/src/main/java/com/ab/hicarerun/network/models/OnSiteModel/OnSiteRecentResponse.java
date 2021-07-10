package com.ab.hicarerun.network.models.OnSiteModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 12/23/2019.
 */
public class OnSiteRecentResponse {
    @SerializedName("IsSuccess")
    @Expose
    private String IsSuccess;
    @SerializedName("com.ab.hicarerun.network.models.TSScannerModel.Data")
    @Expose
    private List<OnSiteHead> data = null;
    @SerializedName("ErrorMessage")
    @Expose
    private String ErrorMessage;

    public String getIsSuccess() {
        return IsSuccess;
    }

    public void setIsSuccess(String isSuccess) {
        IsSuccess = isSuccess;
    }

    public List<OnSiteHead> getData() {
        return data;
    }

    public void setData(List<OnSiteHead> data) {
        this.data = data;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }
}
