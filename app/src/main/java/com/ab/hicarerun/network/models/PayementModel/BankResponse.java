package com.ab.hicarerun.network.models.PayementModel;

import com.ab.hicarerun.network.models.ChemicalModel.Chemicals;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 12/4/2019.
 */
public class BankResponse {

    @SerializedName("IsSuccess")
    @Expose
    private String IsSuccess;
    @SerializedName("com.ab.hicarerun.network.models.TSScannerModel.Data")
    @Expose
    private List<String> data = null;
    @SerializedName("ErrorMessage")
    @Expose
    private String ErrorMessage;

    public String getIsSuccess() {
        return IsSuccess;
    }

    public void setIsSuccess(String isSuccess) {
        IsSuccess = isSuccess;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }
}
