package com.ab.hicarerun.network.models.handshakemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContinueHandShakeResponse {
    @SerializedName("IsSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("Data")
    @Expose
    private Boolean data;
    @SerializedName("ErrorMessage")
    @Expose
    private String ErrorMessage;

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public Boolean getData() {
        return data;
    }

    public void setData(Boolean data) {
        this.data = data;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }
}
