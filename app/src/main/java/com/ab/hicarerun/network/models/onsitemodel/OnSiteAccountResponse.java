package com.ab.hicarerun.network.models.onsitemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 12/18/2019.
 */
public class OnSiteAccountResponse {
    @SerializedName("IsSuccess")
    @Expose
    private String IsSuccess;
    @SerializedName("Data")
    @Expose
    private List<OnSiteAccounts> data = null;
    @SerializedName("ErrorMessage")
    @Expose
    private String ErrorMessage;

    public String getIsSuccess() {
        return IsSuccess;
    }

    public void setIsSuccess(String isSuccess) {
        IsSuccess = isSuccess;
    }

    public List<OnSiteAccounts> getData() {
        return data;
    }

    public void setData(List<OnSiteAccounts> data) {
        this.data = data;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }
}
