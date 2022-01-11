package com.ab.hicarerun.network.models.serviceplanmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 5/27/2020.
 */
public class PlanData {
    @SerializedName("AccountId")
    @Expose
    private String AccountId;
    @SerializedName("OrderNo")
    @Expose
    private String OrderNo;
    @SerializedName("TaskId")
    @Expose
    private String TaskId;
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Plan_Name")
    @Expose
    private String planName;
    @SerializedName("Sp_Code")
    @Expose
    private String spCode;
    @SerializedName("Service_Description")
    @Expose
    private String serviceDescription;
    @SerializedName("Image_Url")
    @Expose
    private String imageUrl;
    @SerializedName("Service_Type")
    @Expose
    private String serviceType;
    @SerializedName("Is_Active")
    @Expose
    private Boolean isActive;
    @SerializedName("Created_On")
    @Expose
    private String createdOn;
    @SerializedName("Is_Fixed_Discount")
    @Expose
    private Boolean isFixedDiscount;
    @SerializedName("Discount")
    @Expose
    private String discount;
    @SerializedName("ActualOrderAmount")
    @Expose
    private String actualOrderAmount;
    @SerializedName("DiscountedOrderAmount")
    @Expose
    private String discountedOrderAmount;
    @SerializedName("DiscountAmount")
    @Expose
    private String DiscountAmount;
    @SerializedName("OfferText")
    @Expose
    private String OfferText;
    @SerializedName("RenewalServicePlans")
    @Expose
    private List<RenewalServicePlan> renewalServicePlans = null;
    @SerializedName("PaymentModeList")
    @Expose
    private List<PaymentMode> paymentModeList = null;
    @SerializedName("ServicePlanUnits")
    @Expose
    private Object servicePlanUnits;

    @SerializedName("AverageRating")
    @Expose
    private Float AverageRating;

    @SerializedName("Complaints")
    @Expose
    private Integer Complaints;

    @SerializedName("TotalServices")
    @Expose
    private Integer TotalServices;

    @SerializedName("CompletedServices")
    @Expose
    private Integer CompletedServices;

    @SerializedName("TechScript")
    @Expose
    private String TechScript;

    @SerializedName("NotRenewalReasons")
    @Expose
    private List<NotRenewalReasons> renewalReasonsList = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getSpCode() {
        return spCode;
    }

    public void setSpCode(String spCode) {
        this.spCode = spCode;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public Boolean getIsFixedDiscount() {
        return isFixedDiscount;
    }

    public void setIsFixedDiscount(Boolean isFixedDiscount) {
        this.isFixedDiscount = isFixedDiscount;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getActualOrderAmount() {
        return actualOrderAmount;
    }

    public void setActualOrderAmount(String actualOrderAmount) {
        this.actualOrderAmount = actualOrderAmount;
    }

    public String getDiscountedOrderAmount() {
        return discountedOrderAmount;
    }

    public void setDiscountedOrderAmount(String discountedOrderAmount) {
        this.discountedOrderAmount = discountedOrderAmount;
    }

    public List<RenewalServicePlan> getRenewalServicePlans() {
        return renewalServicePlans;
    }

    public void setRenewalServicePlans(List<RenewalServicePlan> renewalServicePlans) {
        this.renewalServicePlans = renewalServicePlans;
    }

    public Object getServicePlanUnits() {
        return servicePlanUnits;
    }

    public void setServicePlanUnits(Object servicePlanUnits) {
        this.servicePlanUnits = servicePlanUnits;
    }

    public String getAccountId() {
        return AccountId;
    }

    public void setAccountId(String accountId) {
        AccountId = accountId;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getTaskId() {
        return TaskId;
    }

    public void setTaskId(String taskId) {
        TaskId = taskId;
    }

    public List<PaymentMode> getPaymentModeList() {
        return paymentModeList;
    }

    public void setPaymentModeList(List<PaymentMode> paymentModeList) {
        this.paymentModeList = paymentModeList;
    }

    public String getOfferText() {
        return OfferText;
    }

    public void setOfferText(String offerText) {
        OfferText = offerText;
    }

    public Float getAverageRating() {
        return AverageRating;
    }

    public void setAverageRating(Float averageRating) {
        AverageRating = averageRating;
    }

    public Integer getComplaints() {
        return Complaints;
    }

    public void setComplaints(Integer complaints) {
        Complaints = complaints;
    }

    public String getTechScript() {
        return TechScript;
    }

    public void setTechScript(String techScript) {
        TechScript = techScript;
    }

    public List<NotRenewalReasons> getRenewalReasonsList() {
        return renewalReasonsList;
    }

    public void setRenewalReasonsList(List<NotRenewalReasons> renewalReasonsList) {
        this.renewalReasonsList = renewalReasonsList;
    }

    public Integer getTotalServices() {
        return TotalServices;
    }

    public void setTotalServices(Integer totalServices) {
        TotalServices = totalServices;
    }

    public Integer getCompletedServices() {
        return CompletedServices;
    }

    public void setCompletedServices(Integer completedServices) {
        CompletedServices = completedServices;
    }

    public String getDiscountAmount() {
        return DiscountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        DiscountAmount = discountAmount;
    }
}
