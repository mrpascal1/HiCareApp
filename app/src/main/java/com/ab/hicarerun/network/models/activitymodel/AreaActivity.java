package com.ab.hicarerun.network.models.activitymodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 7/5/2021.
 */
public class AreaActivity {
    @SerializedName("Activity_Id")
    @Expose
    private Integer activityId;
    @SerializedName("Service_Activity_Id")
    @Expose
    private Integer Service_Activity_Id;
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
    @SerializedName("Chemical_Name")
    @Expose
    private String chemicalName;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("Activity")
    @Expose
    private List<SubActivity> activity = null;

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

    public String getChemicalName() {
        return chemicalName;
    }

    public void setChemicalName(String chemicalName) {
        this.chemicalName = chemicalName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<SubActivity> getActivity() {
        return activity;
    }

    public void setActivity(List<SubActivity> activity) {
        this.activity = activity;
    }

    public Integer getService_Activity_Id() {
        return Service_Activity_Id;
    }

    public void setService_Activity_Id(Integer service_Activity_Id) {
        Service_Activity_Id = service_Activity_Id;
    }
}
