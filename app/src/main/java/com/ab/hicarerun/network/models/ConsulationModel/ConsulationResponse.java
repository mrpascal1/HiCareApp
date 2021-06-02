package com.ab.hicarerun.network.models.ConsulationModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 7/18/2020.
 */
public class ConsulationResponse {

    @Expose
    @SerializedName("Param1")
    private boolean Param1;
    @Expose
    @SerializedName("ErrorMessage")
    private String Errormessage;
    @Expose
    @SerializedName("Data")
    private List<Data> Data;
    @Expose
    @SerializedName("IsSuccess")
    private boolean Issuccess;

    public boolean getParam1() {
        return Param1;
    }

    public void setParam1(boolean Param1) {
        this.Param1 = Param1;
    }

    public String getErrormessage() {
        return Errormessage;
    }

    public void setErrormessage(String Errormessage) {
        this.Errormessage = Errormessage;
    }

    public List<Data> getData() {
        return Data;
    }

    public void setData(List<Data> Data) {
        this.Data = Data;
    }

    public boolean getIssuccess() {
        return Issuccess;
    }

    public void setIssuccess(boolean Issuccess) {
        this.Issuccess = Issuccess;
    }


}
