package com.ab.hicarerun.network.models.OnSiteModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 12/18/2019.
 */
public class SaveAccountAreaResponse {
    @SerializedName("IsSuccess") @Expose
    private Boolean IsSuccess;

    @SerializedName("Data") @Expose
    private Object Data;

    @SerializedName("ErrorMessage") @Expose
    private String ErrorMessage;


    public Boolean getSuccess() {
        return IsSuccess;
    }

    public void setSuccess(Boolean success) {
        IsSuccess = success;
    }

    public Object getData() {
        return Data;
    }

    public void setData(Object data) {
        Data = data;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }
}
