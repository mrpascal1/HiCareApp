package com.ab.hicarerun.network.models.chemicalmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 6/25/2021.
 */
public class TowerData {
    @SerializedName("Tower")
    @Expose
    private Integer tower;
    @SerializedName("Services")
    @Expose
    private List<ServiceData> services = null;

    public Integer getTower() {
        return tower;
    }

    public void setTower(Integer tower) {
        this.tower = tower;
    }

    public List<ServiceData> getServices() {
        return services;
    }

    public void setServices(List<ServiceData> services) {
        this.services = services;
    }

}
