package com.ab.hicarerun.network.models.activitymodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 7/7/2021.
 */
public class SaveServiceActivity {
    @SerializedName("Activity_Id")
    @Expose
    private Integer activityId;
    @SerializedName("Service_Activity_Id")
    @Expose
    private String serviceActivityId;
    @SerializedName("Area_Id")
    @Expose
    private Object areaId;
    @SerializedName("Service_No")
    @Expose
    private Integer serviceNo;
    @SerializedName("Service_Type")
    @Expose
    private String serviceType;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("CompletionDateTime")
    @Expose
    private String completionDateTime;
    @SerializedName("FloorNo")
    @Expose
    private String floorNo;
    @SerializedName("TowerNo")
    @Expose
    private int towerNo;
    @SerializedName("AreaType")
    @Expose
    private String areaType;

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getServiceActivityId() {
        return serviceActivityId;
    }

    public void setServiceActivityId(String serviceActivityId) {
        this.serviceActivityId = serviceActivityId;
    }

    public Object getAreaId() {
        return areaId;
    }

    public void setAreaId(Object areaId) {
        this.areaId = areaId;
    }

    public Integer getServiceNo() {
        return serviceNo;
    }

    public void setServiceNo(Integer serviceNo) {
        this.serviceNo = serviceNo;
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

    public String getFloorNo() {
        return floorNo;
    }

    public void setFloorNo(String floorNo) {
        this.floorNo = floorNo;
    }

    public int getTowerNo() {
        return towerNo;
    }

    public void setTowerNo(int towerNo) {
        this.towerNo = towerNo;
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }
}
