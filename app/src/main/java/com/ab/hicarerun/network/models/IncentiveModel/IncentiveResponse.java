package com.ab.hicarerun.network.models.IncentiveModel;

import com.ab.hicarerun.network.models.ProfileModel.Profile;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 10/15/2019.
 */
public class IncentiveResponse {
    @SerializedName("IsSuccess")
    @Expose
    private Boolean IsSuccess;

    @SerializedName("Data")
    @Expose
    private IncentiveData Data;

    @SerializedName("ErrorMessage")
    @Expose
    private String ErrorMessage;

    public Boolean getSuccess() {
        return IsSuccess;
    }

    public void setSuccess(Boolean success) {
        IsSuccess = success;
    }

    public IncentiveData getData() {
        return Data;
    }

    public void setData(IncentiveData data) {
        Data = data;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }
}
