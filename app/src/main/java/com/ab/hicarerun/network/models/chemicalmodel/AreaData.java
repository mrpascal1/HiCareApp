package com.ab.hicarerun.network.models.chemicalmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 6/25/2021.
 */
public class AreaData {
    @SerializedName("Activity_Id")
    @Expose
    private Integer activityId;
    @SerializedName("Area_Id")
    @Expose
    private Integer areaId;
    @SerializedName("Floor_No")
    @Expose
    private String floorNo;
    @SerializedName("Area_Name")
    @Expose
    private String areaName;
    @SerializedName("Services")
    @Expose
    private String services;
    @SerializedName("Status")
    @Expose
    private String Status;
    @SerializedName("Activity")
    @Expose
    private List<ActivityData> activity = null;

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

    public String getFloorNo() {
        return floorNo;
    }

    public void setFloorNo(String floorNo) {
        this.floorNo = floorNo;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public List<ActivityData> getActivity() {
        return activity;
    }

    public void setActivity(List<ActivityData> activity) {
        this.activity = activity;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
