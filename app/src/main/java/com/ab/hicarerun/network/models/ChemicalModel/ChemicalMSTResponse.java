package com.ab.hicarerun.network.models.ChemicalModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 1/3/2020.
 */
public class ChemicalMSTResponse {
    @SerializedName("IsSuccess") @Expose
    private Boolean IsSuccess;
    @SerializedName("Data") @Expose
    private List<MSTChemicals> data = null;
    @SerializedName("ErrorMessage") @Expose
    private String ErrorMessage;

    public Boolean getSuccess() {
        return IsSuccess;
    }

    public void setSuccess(Boolean success) {
        IsSuccess = success;
    }

    public List<MSTChemicals> getData() {
        return data;
    }

    public void setData(List<MSTChemicals> data) {
        this.data = data;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }
}
