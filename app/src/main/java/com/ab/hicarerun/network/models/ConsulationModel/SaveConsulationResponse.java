package com.ab.hicarerun.network.models.ConsulationModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 7/20/2020.
 */
public class SaveConsulationResponse {

    @Expose
    @SerializedName("Param1")
    private boolean param1;
    @Expose
    @SerializedName("ErrorMessage")
    private String errormessage;
    @Expose
    @SerializedName("Data")
    private boolean data;
    @Expose
    @SerializedName("IsSuccess")
    private boolean issuccess;

    public boolean getParam1() {
        return param1;
    }

    public void setParam1(boolean param1) {
        this.param1 = param1;
    }

    public String getErrormessage() {
        return errormessage;
    }

    public void setErrormessage(String errormessage) {
        this.errormessage = errormessage;
    }

    public boolean getData() {
        return data;
    }

    public void setData(boolean data) {
        this.data = data;
    }

    public boolean getIssuccess() {
        return issuccess;
    }

    public void setIssuccess(boolean issuccess) {
        this.issuccess = issuccess;
    }
}
