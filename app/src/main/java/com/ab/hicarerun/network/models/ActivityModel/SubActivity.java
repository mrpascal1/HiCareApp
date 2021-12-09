package com.ab.hicarerun.network.models.ActivityModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 7/5/2021.
 */
public class SubActivity {
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

    @SerializedName("ShowQR")
    @Expose
    private boolean showQR;

    @SerializedName("QRType")
    @Expose
    private String qRType;

    @SerializedName("Status")
    @Expose
    private Object status;

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

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public boolean isShowQR() {
        return showQR;
    }

    public void setShowQR(boolean showQR) {
        this.showQR = showQR;
    }

    public String getqRType() {
        return qRType;
    }

    public void setqRType(String qRType) {
        this.qRType = qRType;
    }
}
