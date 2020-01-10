package com.ab.hicarerun.network.models.OnSiteModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 12/18/2019.
 */
public class ActivityDetail {
    @SerializedName("ActivityId")
    @Expose
    private Integer activityId;
    @SerializedName("ServiceType")
    @Expose
    private String serviceType;
    @SerializedName("Lat")
    @Expose
    private String lat;
    @SerializedName("Lon")
    @Expose
    private String lon;
    @SerializedName("StartTime")
    @Expose
    private String startTime;
    @SerializedName("EndTime")
    @Expose
    private String endTime;
    @SerializedName("IsServiceDone")
    @Expose
    private Boolean isServiceDone;
    @SerializedName("ServiceNotDoneReason")
    @Expose
    private String serviceNotDoneReason;
    @SerializedName("CreatedBy")
    @Expose
    private Integer createdBy;
    @SerializedName("ModifiedBy")
    @Expose
    private Integer modifiedBy;


    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Boolean getIsServiceDone() {
        return isServiceDone;
    }

    public void setIsServiceDone(Boolean isServiceDone) {
        this.isServiceDone = isServiceDone;
    }

    public String getServiceNotDoneReason() {
        return serviceNotDoneReason;
    }

    public void setServiceNotDoneReason(String serviceNotDoneReason) {
        this.serviceNotDoneReason = serviceNotDoneReason;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

}
