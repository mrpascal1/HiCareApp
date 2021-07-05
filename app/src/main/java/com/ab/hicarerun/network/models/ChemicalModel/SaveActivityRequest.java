package com.ab.hicarerun.network.models.ChemicalModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 6/29/2021.
 */
public class SaveActivityRequest {
    @SerializedName("Activity_Id")
    @Expose
    private Integer activityId;
    @SerializedName("Area_Id")
    @Expose
    private Integer areaId;
    @SerializedName("Service_Type")
    @Expose
    private String serviceType;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("CompletionDateTime")
    @Expose
    private String completionDateTime;

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

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCompletionDateTime() {
        return completionDateTime;
    }

    public void setCompletionDateTime(String completionDateTime) {
        this.completionDateTime = completionDateTime;
    }
}
