package com.ab.hicarerun.network.models.JeopardyModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CWFJeopardyResponse {
    @SerializedName("IsSuccess")
    @Expose
    private Boolean IsSuccess;

    @SerializedName("com.ab.hicarerun.network.models.TSScannerModel.Data")
    @Expose
    private String Data;

    @SerializedName("ResponseMessage")
    @Expose
    private String ResponseMessage;

    public Boolean getSuccess() {
        return IsSuccess;
    }

    public void setSuccess(Boolean success) {
        IsSuccess = success;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getResponseMessage() {
        return ResponseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        ResponseMessage = responseMessage;
    }

}
