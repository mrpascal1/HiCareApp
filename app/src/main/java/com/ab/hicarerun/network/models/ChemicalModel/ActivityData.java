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
    @SerializedName("ServiceActivity")
    @Expose
    private String serviceActivity;
    @SerializedName("Chemical_Name")
    @Expose
    private List<ActivityChemicalData> chemicalName = null;

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getServiceActivity() {
        return serviceActivity;
    }

    public void setServiceActivity(String serviceActivity) {
        this.serviceActivity = serviceActivity;
    }

    public List<ActivityChemicalData> getChemicalName() {
        return chemicalName;
    }

    public void setChemicalName(List<ActivityChemicalData> chemicalName) {
        this.chemicalName = chemicalName;
    }
}
