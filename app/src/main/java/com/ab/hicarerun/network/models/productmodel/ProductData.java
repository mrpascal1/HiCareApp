package com.ab.hicarerun.network.models.productmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 5/28/2020.
 */
public class ProductData implements Parcelable {
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
    @SerializedName("RenewalServicePlans")
    @Expose
    private String renewalServicePlans;
    @SerializedName("ServicePlanUnits")
    @Expose
    private List<ServicePlanUnits> servicePlanUnits = null;

    protected ProductData(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        planName = in.readString();
        spCode = in.readString();
        serviceDescription = in.readString();
        imageUrl = in.readString();
        serviceType = in.readString();
        byte tmpIsActive = in.readByte();
        isActive = tmpIsActive == 0 ? null : tmpIsActive == 1;
        createdOn = in.readString();
        byte tmpIsFixedDiscount = in.readByte();
        isFixedDiscount = tmpIsFixedDiscount == 0 ? null : tmpIsFixedDiscount == 1;
        discount = in.readString();
        actualOrderAmount = in.readString();
        discountedOrderAmount = in.readString();
        renewalServicePlans = in.readString();
        servicePlanUnits = in.createTypedArrayList(ServicePlanUnits.CREATOR);
    }

    public static final Creator<ProductData> CREATOR = new Creator<ProductData>() {
        @Override
        public ProductData createFromParcel(Parcel in) {
            return new ProductData(in);
        }

        @Override
        public ProductData[] newArray(int size) {
            return new ProductData[size];
        }
    };

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

    public String getRenewalServicePlans() {
        return renewalServicePlans;
    }

    public void setRenewalServicePlans(String renewalServicePlans) {
        this.renewalServicePlans = renewalServicePlans;
    }

    public List<ServicePlanUnits> getServicePlanUnits() {
        return servicePlanUnits;
    }

    public void setServicePlanUnits(List<ServicePlanUnits> servicePlanUnits) {
        this.servicePlanUnits = servicePlanUnits;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(planName);
        dest.writeString(spCode);
        dest.writeString(serviceDescription);
        dest.writeString(imageUrl);
        dest.writeString(serviceType);
        dest.writeByte((byte) (isActive == null ? 0 : isActive ? 1 : 2));
        dest.writeString(createdOn);
        dest.writeByte((byte) (isFixedDiscount == null ? 0 : isFixedDiscount ? 1 : 2));
        dest.writeString(discount);
        dest.writeString(actualOrderAmount);
        dest.writeString(discountedOrderAmount);
        dest.writeString(renewalServicePlans);
        dest.writeTypedList(servicePlanUnits);
    }
}
