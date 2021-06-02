package com.ab.hicarerun.network.models.ServicePlanModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 5/30/2020.
 */
public class RenewOrder {


    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Unique_Transaction_Number__c")
    @Expose
    private Object uniqueTransactionNumberC;
    @SerializedName("Service_SP_Code__c")
    @Expose
    private String serviceSPCodeC;
    @SerializedName("Unit1__c")
    @Expose
    private String unit1C;
    @SerializedName("Sub_Type1__c")
    @Expose
    private String subType1C;
    @SerializedName("Quantity__c")
    @Expose
    private String quantityC;
    @SerializedName("Created_by_Profile__c")
    @Expose
    private String createdByProfileC;
    @SerializedName("Order_Number__c")
    @Expose
    private String orderNumberC;
    @SerializedName("Owner_Mobile_No__c")
    @Expose
    private String ownerMobileNoC;
    @SerializedName("Order_Value_Numeric__c")
    @Expose
    private Double orderValueNumericC;
    @SerializedName("Order_Generated_From__c")
    @Expose
    private String orderGeneratedFromC;
    @SerializedName("Source_Type__c")
    @Expose
    private String sourceTypeC;
    @SerializedName("Sub_Source__c")
    @Expose
    private String subSourceC;
    @SerializedName("End_Date__c")
    @Expose
    private String endDateC;
    @SerializedName("Start_Date__c")
    @Expose
    private String startDateC;
    @SerializedName("Status__c")
    @Expose
    private String statusC;
    @SerializedName("IsSelected")
    @Expose
    private Boolean isSelected;
    @SerializedName("UTM_Source__c")
    @Expose
    private Object uTMSourceC;
    @SerializedName("Standard_Value__c")
    @Expose
    private String standardValueC;
    @SerializedName("Order_Value__c")
    @Expose
    private Double orderValueC;
    @SerializedName("House_Type__r")
    @Expose
    private HouseType houseTypeR;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getUniqueTransactionNumberC() {
        return uniqueTransactionNumberC;
    }

    public void setUniqueTransactionNumberC(Object uniqueTransactionNumberC) {
        this.uniqueTransactionNumberC = uniqueTransactionNumberC;
    }

    public String getServiceSPCodeC() {
        return serviceSPCodeC;
    }

    public void setServiceSPCodeC(String serviceSPCodeC) {
        this.serviceSPCodeC = serviceSPCodeC;
    }

    public String getUnit1C() {
        return unit1C;
    }

    public void setUnit1C(String unit1C) {
        this.unit1C = unit1C;
    }

    public String getSubType1C() {
        return subType1C;
    }

    public void setSubType1C(String subType1C) {
        this.subType1C = subType1C;
    }

    public String getQuantityC() {
        return quantityC;
    }

    public void setQuantityC(String quantityC) {
        this.quantityC = quantityC;
    }

    public String getCreatedByProfileC() {
        return createdByProfileC;
    }

    public void setCreatedByProfileC(String createdByProfileC) {
        this.createdByProfileC = createdByProfileC;
    }

    public String getOrderNumberC() {
        return orderNumberC;
    }

    public void setOrderNumberC(String orderNumberC) {
        this.orderNumberC = orderNumberC;
    }

    public String getOwnerMobileNoC() {
        return ownerMobileNoC;
    }

    public void setOwnerMobileNoC(String ownerMobileNoC) {
        this.ownerMobileNoC = ownerMobileNoC;
    }

    public Double getOrderValueNumericC() {
        return orderValueNumericC;
    }

    public void setOrderValueNumericC(Double orderValueNumericC) {
        this.orderValueNumericC = orderValueNumericC;
    }

    public String getOrderGeneratedFromC() {
        return orderGeneratedFromC;
    }

    public void setOrderGeneratedFromC(String orderGeneratedFromC) {
        this.orderGeneratedFromC = orderGeneratedFromC;
    }

    public String getSourceTypeC() {
        return sourceTypeC;
    }

    public void setSourceTypeC(String sourceTypeC) {
        this.sourceTypeC = sourceTypeC;
    }

    public String getSubSourceC() {
        return subSourceC;
    }

    public void setSubSourceC(String subSourceC) {
        this.subSourceC = subSourceC;
    }

    public String getEndDateC() {
        return endDateC;
    }

    public void setEndDateC(String endDateC) {
        this.endDateC = endDateC;
    }

    public String getStartDateC() {
        return startDateC;
    }

    public void setStartDateC(String startDateC) {
        this.startDateC = startDateC;
    }

    public String getStatusC() {
        return statusC;
    }

    public void setStatusC(String statusC) {
        this.statusC = statusC;
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    public Object getUTMSourceC() {
        return uTMSourceC;
    }

    public void setUTMSourceC(Object uTMSourceC) {
        this.uTMSourceC = uTMSourceC;
    }

    public String getStandardValueC() {
        return standardValueC;
    }

    public void setStandardValueC(String standardValueC) {
        this.standardValueC = standardValueC;
    }

    public Double getOrderValueC() {
        return orderValueC;
    }

    public void setOrderValueC(Double orderValueC) {
        this.orderValueC = orderValueC;
    }

    public HouseType getHouseTypeR() {
        return houseTypeR;
    }

    public void setHouseTypeR(HouseType houseTypeR) {
        this.houseTypeR = houseTypeR;
    }
}
