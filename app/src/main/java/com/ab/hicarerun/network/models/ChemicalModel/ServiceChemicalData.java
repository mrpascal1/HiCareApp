package com.ab.hicarerun.network.models.ChemicalModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 6/25/2021.
 */
public class ServiceChemicalData {
    @SerializedName("AreaType")
    @Expose
    private String areaType;
    @SerializedName("Tower")
    @Expose
    private List<TowerData> tower = null;

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public List<TowerData> getTower() {
        return tower;
    }

    public void setTower(List<TowerData> tower) {
        this.tower = tower;
    }

}
