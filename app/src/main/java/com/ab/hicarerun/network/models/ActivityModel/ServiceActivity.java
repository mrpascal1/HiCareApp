package com.ab.hicarerun.network.models.ActivityModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 7/5/2021.
 */
public class ServiceActivity {
    @SerializedName("Activity_Id")
    @Expose
    private Integer activityId;
    @SerializedName("Service_Activity_Id")
    @Expose
    private Integer serviceActivityId;
    @SerializedName("Service_Activity_Name")
    @Expose
    private String serviceActivityName;
    @SerializedName("Status")
    @Expose
    private Object status;
    @SerializedName("Area_Ids")
    @Expose
    private String areaIds;
    @SerializedName("Area")
    @Expose
    private List<AreaActivity> area = null;

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getServiceActivityId() {
        return serviceActivityId;
    }

    public void setServiceActivityId(Integer serviceActivityId) {
        this.serviceActivityId = serviceActivityId;
    }

    public String getServiceActivityName() {
        return serviceActivityName;
    }

    public void setServiceActivityName(String serviceActivityName) {
        this.serviceActivityName = serviceActivityName;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public String getAreaIds() {
        return areaIds;
    }

    public void setAreaIds(String areaIds) {
        this.areaIds = areaIds;
    }

    public List<AreaActivity> getArea() {
        return area;
    }

    public void setArea(List<AreaActivity> area) {
        this.area = area;
    }
}
