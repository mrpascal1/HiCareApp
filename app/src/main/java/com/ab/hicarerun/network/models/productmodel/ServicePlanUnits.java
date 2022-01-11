package com.ab.hicarerun.network.models.productmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Arjun Bhatt on 5/28/2020.
 */
public class ServicePlanUnits implements Parcelable {
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Parent_Plan_Id")
    @Expose
    private Integer parentPlanId;
    @SerializedName("Unit")
    @Expose
    private String unit;
    @SerializedName("Quantity")
    @Expose
    private String quantity;
    @SerializedName("Price")
    @Expose
    private String price;
    @SerializedName("DiscountedPrice")
    @Expose
    private String DiscountedPrice;
    @SerializedName("TotalDiscount")
    @Expose
    private String TotalDiscount;
    @SerializedName("Is_Active")
    @Expose
    private Boolean isActive;
    @SerializedName("Created_On")
    @Expose
    private String createdOn;


    protected ServicePlanUnits(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        if (in.readByte() == 0) {
            parentPlanId = null;
        } else {
            parentPlanId = in.readInt();
        }
        unit = in.readString();
        quantity = in.readString();
        price = in.readString();
        DiscountedPrice = in.readString();
        TotalDiscount = in.readString();
        byte tmpIsActive = in.readByte();
        isActive = tmpIsActive == 0 ? null : tmpIsActive == 1;
        createdOn = in.readString();
    }

    public static final Creator<ServicePlanUnits> CREATOR = new Creator<ServicePlanUnits>() {
        @Override
        public ServicePlanUnits createFromParcel(Parcel in) {
            return new ServicePlanUnits(in);
        }

        @Override
        public ServicePlanUnits[] newArray(int size) {
            return new ServicePlanUnits[size];
        }
    };

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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getDiscountedPrice() {
        return DiscountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        DiscountedPrice = discountedPrice;
    }

    public String getTotalDiscount() {
        return TotalDiscount;
    }

    public void setTotalDiscount(String totalDiscount) {
        TotalDiscount = totalDiscount;
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
        if (parentPlanId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(parentPlanId);
        }
        dest.writeString(unit);
        dest.writeString(quantity);
        dest.writeString(price);
        dest.writeString(DiscountedPrice);
        dest.writeString(TotalDiscount);
        dest.writeByte((byte) (isActive == null ? 0 : isActive ? 1 : 2));
        dest.writeString(createdOn);
    }
}
