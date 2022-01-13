package com.ab.hicarerun.network.models.activitymodel;

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
    private String status;

    @SerializedName("Floor_No")
    @Expose
    private String floor;

    @SerializedName("Service_Code")
    @Expose
    private String Service_Code;

    @SerializedName("Chemical_Name")
    @Expose
    private String Chemical_Name = "2.0 ml";
    @SerializedName("ChemicalId")
    @Expose
    private int ChemicalId = 0;
    @SerializedName("ChemicalCode")
    @Expose
    private String ChemicalCode = "";
    @SerializedName("UserConsumedChemicalQuantity")
    @Expose
    private double UserConsumedChemicalQuantity = 0.0;
    @SerializedName("Chemical_Qty")
    @Expose
    private String Chemical_Qty;
    @SerializedName("Chemical_Unit")
    @Expose
    private String Chemical_Unit;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public String getService_Code() {
        return Service_Code;
    }

    public void setService_Code(String service_Code) {
        Service_Code = service_Code;
    }

    public String getChemical_Name() {
        return Chemical_Name;
    }

    public void setChemical_Name(String chemical_Name) {
        Chemical_Name = chemical_Name;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getChemical_Qty() {
        return Chemical_Qty;
    }

    public void setChemical_Qty(String chemical_Qty) {
        Chemical_Qty = chemical_Qty;
    }

    public String getChemical_Unit() {
        return Chemical_Unit;
    }

    public void setChemical_Unit(String chemical_Unit) {
        Chemical_Unit = chemical_Unit;
    }

    public int getChemicalId() {
        return ChemicalId;
    }

    public void setChemicalId(int chemicalId) {
        ChemicalId = chemicalId;
    }

    public String getChemicalCode() {
        return ChemicalCode;
    }

    public void setChemicalCode(String chemicalCode) {
        ChemicalCode = chemicalCode;
    }

    public double getUserConsumedChemicalQuantity() {
        return UserConsumedChemicalQuantity;
    }

    public void setUserConsumedChemicalQuantity(double userConsumedChemicalQuantity) {
        UserConsumedChemicalQuantity = userConsumedChemicalQuantity;
    }
}
