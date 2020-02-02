package com.ab.hicarerun.network.models.TaskModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TaskChemicalList {
    @SerializedName("Id")
    @Expose
    private String Id;
    @SerializedName("Standard_Usage__c")
    @Expose
    private String Standard;
    @SerializedName("Original_Actual_Usage__c")
    @Expose
    private String Orignal;
    @SerializedName("Actual_Usage__c")
    @Expose
    private String Actual;
    @SerializedName("Consumption_U_O_M__c")
    @Expose
    private String Consumption;
    @SerializedName("CWFProductName__c")
    @Expose
    private String CWFProductName;
    @SerializedName("Is_Chemical_Changed__c")
    @Expose
    private Boolean IsChemicalChanged;


    public TaskChemicalList() {
        Id = "NA";
        Standard = "NA";
        Orignal = "NA";
        Actual = "NA";
        Consumption = "NA";
        CWFProductName = "NA";
        IsChemicalChanged = false;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getStandard() {
        return Standard;
    }

    public void setStandard(String standard) {
        Standard = standard;
    }

    public String getActual() {
        return Actual;
    }

    public void setActual(String actual) {
        Actual = actual;
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
