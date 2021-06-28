package com.ab.hicarerun.network.models.ChemicalModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 6/25/2021.
 */
public class ServiceData {
    @SerializedName("Service")
    @Expose
    private String service;
    @SerializedName("Area")
    @Expose
    private List<AreaData> area = null;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public List<AreaData> getArea() {
        return area;
    }

    public void setArea(List<AreaData> area) {
        this.area = area;
    }
}
