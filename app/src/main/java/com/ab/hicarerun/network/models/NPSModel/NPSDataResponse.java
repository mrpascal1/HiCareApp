package com.ab.hicarerun.network.models.NPSModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 3/4/2020.
 */
public class NPSDataResponse {

    @SerializedName("IsSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("Data")
    @Expose
    private NPSData data;
    @SerializedName("ErrorMessage")
    @Expose
    private String errorMessage;
    @SerializedName("Param1")
    @Expose
    private Boolean param1;
    @SerializedName("ResponseMessage")
    @Expose
    private Object responseMessage;

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public NPSData getData() {
        return data;
    }

    public void setData(NPSData data) {
        this.data = data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Boolean getParam1() {
        return param1;
    }

    public void setParam1(Boolean param1) {
        this.param1 = param1;
    }

    public Object getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(Object responseMessage) {
        this.responseMessage = responseMessage;
    }
}
