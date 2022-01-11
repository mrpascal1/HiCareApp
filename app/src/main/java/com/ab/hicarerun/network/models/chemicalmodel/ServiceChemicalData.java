package com.ab.hicarerun.network.models.chemicalmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 6/25/2021.
 */
public class ServiceChemicalData {
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
    @SerializedName("Area")
    @Expose
    private List<AreaData> area = null;

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

    public List<AreaData> getArea() {
        return area;
    }

    public void setArea(List<AreaData> area) {
        this.area = area;
    }


}
