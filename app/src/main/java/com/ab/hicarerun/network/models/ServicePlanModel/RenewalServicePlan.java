package com.ab.hicarerun.network.models.ServicePlanModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 5/27/2020.
 */
public class RenewalServicePlan {
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
    @SerializedName("Parent_Plan_Id")
    @Expose
    private Integer parentPlanId;
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
    @SerializedName("Is_Recommended")
    @Expose
    private Boolean isRecommended;
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
    @SerializedName("Is_Active")
    @Expose
    private Boolean isActive;
    @SerializedName("OfferText")
    @Expose
    private String OfferText;
    @SerializedName("Created_On")
    @Expose
    private String createdOn;
    @SerializedName("DiscountAmount")
    @Expose
    private String DiscountAmount;
    @SerializedName("ServicePlanUnits")
    @Expose
    private Object servicePlanUnits;
    @SerializedName("PaymentModeList")
    @Expose
    private List<PaymentMode> paymentModeList = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentPlanId() {
        return parentPlanId;
    }

    public void setParentPlanId(Integer parentPlanId) {
        this.parentPlanId = parentPlanId;
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

    public Boolean getIsRecommended() {
        return isRecommended;
    }

    public void setIsRecommended(Boolean isRecommended) {
        this.isRecommended = isRecommended;
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

    public String getDiscountAmount() {
        return DiscountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        DiscountAmount = discountAmount;
    }
}
