package com.ab.hicarerun.network.models.onsitemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Arjun Bhatt on 12/18/2019.
 */
public class OnSiteAreaResponse extends RealmObject{
    @SerializedName("IsSuccess")
    @Expose
    private String IsSuccess;
    @SerializedName("Data")
    @Expose
    private RealmList<OnSiteArea> data = null;
    @SerializedName("ErrorMessage")
    @Expose
    private String ErrorMessage;

    public String getIsSuccess() {
        return IsSuccess;
    }

    public void setIsSuccess(String isSuccess) {
        IsSuccess = isSuccess;
    }

    public RealmList<OnSiteArea> getData() {
        return data;
    }

    public void setData(RealmList<OnSiteArea> data) {
        this.data = data;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }
}
