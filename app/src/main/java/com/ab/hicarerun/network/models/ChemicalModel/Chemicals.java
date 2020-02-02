package com.ab.hicarerun.network.models.ChemicalModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Chemicals {
    @SerializedName("Id") @Expose
    private String Id;

    @SerializedName("Standard_Usage__c") @Expose
    private String Standard_Usage;

    @SerializedName("Original_Actual_Usage__c") @Expose
    private String Orignal;

    @SerializedName("Actual_Usage__c") @Expose
    private String Actual_Usage;

    @SerializedName("Consumption_U_O_M__c") @Expose
    private String Consumption;

    @SerializedName("CWFProductName__c") @Expose
    private String CWFProductName;

    @SerializedName("Task_Type__c") @Expose
    private String ChemType;

    @SerializedName("Service_Area__c") @Expose
    private String ServiceArea;

    @SerializedName("Is_Chemical_Changed__c") @Expose
    private Boolean IsChemicalChanged;

    public Chemicals() {
        Id = "NA";
        Standard_Usage = "NA";
        Orignal = "NA";
        Actual_Usage = "NA";
        Consumption = "NA";
        CWFProductName = "NA";
        ChemType = "NA";
        ServiceArea = "NA";
        IsChemicalChanged = false;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getStandard_Usage() {
        return Standard_Usage;
    }

    public void setStandard_Usage(String standard_Usage) {
        Standard_Usage = standard_Usage;
    }

    public String getActual_Usage() {
        return Actual_Usage;
    }

    public void setActual_Usage(String actual_Usage) {
        Actual_Usage = actual_Usage;
    }

    public String getConsumption() {
        return Consumption;
    }

    public void setConsumption(String consumption) {
        Consumption = consumption;
    }

    public String getCWFProductName() {
        return CWFProductName;
    }

    public void setCWFProductName(String CWFProductName) {
        this.CWFProductName = CWFProductName;
    }

    public String getChemType() {
        return ChemType;
    }

    public void setChemType(String chemType) {
        ChemType = chemType;
    }

    public String getServiceArea() {
        return ServiceArea;
    }

    public void setServiceArea(String serviceArea) {
        ServiceArea = serviceArea;
    }

    public String getOrignal() {
        return Orignal;
    }

    public void setOrignal(String orignal) {
        Orignal = orignal;
    }

    public Boolean getChemicalChanged() {
        return IsChemicalChanged;
    }

    public void setChemicalChanged(Boolean chemicalChanged) {
        IsChemicalChanged = chemicalChanged;
    }
}
