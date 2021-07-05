package com.ab.hicarerun.network.models.ChemicalModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 6/25/2021.
 */
public class ActivityData {
    @SerializedName("Activity_Id")
    @Expose
    private Integer activityId;
    @SerializedName("Area_Id")
    @Expose
    private Integer areaId;
    @SerializedName("Service_Code")
    @Expose
    private String serviceCode;
    @SerializedName("ServiceActivity")
    @Expose
    private String serviceActivity;
    @SerializedName("Chemical_Name")
    @Expose
    private String chemicalName;
    @SerializedName("Status")
    @Expose
    private String Status;

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getServiceActivity() {
        return serviceActivity;
    }

    public void setServiceActivity(String serviceActivity) {
        this.serviceActivity = serviceActivity;
    }

    public String getChemicalName() {
        return chemicalName;
    }

    public void setChemicalName(String chemicalName) {
        this.chemicalName = chemicalName;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
