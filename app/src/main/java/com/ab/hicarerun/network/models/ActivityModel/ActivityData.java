package com.ab.hicarerun.network.models.ActivityModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 7/5/2021.
 */
public class ActivityData {
    @SerializedName("Activity_Id")
    @Expose
    private Integer activityId;
    @SerializedName("Tower")
    @Expose
    private Integer tower;
    @SerializedName("Tower_Name")
    @Expose
    private String towerName;
    @SerializedName("AreaType")
    @Expose
    private String areaType;
    @SerializedName("FloorList")
    @Expose
    private List<String> floorList = null;
    @SerializedName("Service_Activity")
    @Expose
    private List<ServiceActivity> serviceActivity = null;

    private int selectedPos;

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getTower() {
        return tower;
    }

    public void setTower(Integer tower) {
        this.tower = tower;
    }

    public String getTowerName() {
        return towerName;
    }

    public void setTowerName(String towerName) {
        this.towerName = towerName;
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public List<String> getFloorList() {
        return floorList;
    }

    public void setFloorList(List<String> floorList) {
        this.floorList = floorList;
    }

    public List<ServiceActivity> getServiceActivity() {
        return serviceActivity;
    }

    public void setServiceActivity(List<ServiceActivity> serviceActivity) {
        this.serviceActivity = serviceActivity;
    }

    public int getSelectedPos() {
        return selectedPos;
    }

    public void setSelectedPos(int selectedPos) {
        this.selectedPos = selectedPos;
    }
}
