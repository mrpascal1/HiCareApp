package com.ab.hicarerun.network.models.ChemicalModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 6/25/2021.
 */
public class AreaData {
    @SerializedName("ServiceArea")
    @Expose
    private String serviceArea;
    @SerializedName("Activity")
    @Expose
    private List<ActivityData> activity = null;

    public String getServiceArea() {
        return serviceArea;
    }

    public void setServiceArea(String serviceArea) {
        this.serviceArea = serviceArea;
    }

    public List<ActivityData> getActivity() {
        return activity;
    }

    public void setActivity(List<ActivityData> activity) {
        this.activity = activity;
    }
}
