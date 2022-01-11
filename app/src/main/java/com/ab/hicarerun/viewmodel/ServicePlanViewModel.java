package com.ab.hicarerun.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.ab.hicarerun.network.models.serviceplanmodel.RenewalServicePlan;

/**
 * Created by Arjun Bhatt on 5/27/2020.
 */
public class ServicePlanViewModel implements Parcelable {
    private Integer Id;
    private Integer Parent_Id_Plan;
    private String Plan_Name;
    private String Sp_Code;
    private String Service_Description;
    private String Image_URL;
    private String Discount;
    private String ActualOrderAmount;
    private String DiscountAmount;
    private String DiscountedOrderAmount;
    private Boolean Is_Recommended;
    private String OfferText;

    public ServicePlanViewModel() {
        Id = 0;
        Parent_Id_Plan = 0;
        Plan_Name = "NA";
        Sp_Code = "NA";
        Service_Description = "NA";
        Image_URL = "NA";
        Discount = "NA";
        ActualOrderAmount = "NA";
        DiscountedOrderAmount = "NA";
        DiscountAmount = "NA";
        Is_Recommended = false;
        OfferText = "NA";
    }

    protected ServicePlanViewModel(Parcel in) {
        if (in.readByte() == 0) {
            Id = null;
        } else {
            Id = in.readInt();
        }
        if (in.readByte() == 0) {
            Parent_Id_Plan = null;
        } else {
            Parent_Id_Plan = in.readInt();
        }
        Plan_Name = in.readString();
        Sp_Code = in.readString();
        Service_Description = in.readString();
        Image_URL = in.readString();
        Discount = in.readString();
        ActualOrderAmount = in.readString();
        DiscountedOrderAmount = in.readString();
        DiscountAmount = in.readString();
        byte tmpIs_Recommended = in.readByte();
        Is_Recommended = tmpIs_Recommended == 0 ? null : tmpIs_Recommended == 1;
        OfferText = in.readString();
    }

    public static final Creator<ServicePlanViewModel> CREATOR = new Creator<ServicePlanViewModel>() {
        @Override
        public ServicePlanViewModel createFromParcel(Parcel in) {
            return new ServicePlanViewModel(in);
        }

        @Override
        public ServicePlanViewModel[] newArray(int size) {
            return new ServicePlanViewModel[size];
        }
    };

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Integer getParent_Id_Plan() {
        return Parent_Id_Plan;
    }

    public void setParent_Id_Plan(Integer parent_Id_Plan) {
        Parent_Id_Plan = parent_Id_Plan;
    }

    public String getPlan_Name() {
        return Plan_Name;
    }

    public void setPlan_Name(String plan_Name) {
        Plan_Name = plan_Name;
    }

    public String getSp_Code() {
        return Sp_Code;
    }

    public void setSp_Code(String sp_Code) {
        Sp_Code = sp_Code;
    }

    public String getService_Description() {
        return Service_Description;
    }

    public void setService_Description(String service_Description) {
        Service_Description = service_Description;
    }

    public String getImage_URL() {
        return Image_URL;
    }

    public void setImage_URL(String image_URL) {
        Image_URL = image_URL;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getActualOrderAmount() {
        return ActualOrderAmount;
    }

    public void setActualOrderAmount(String actualOrderAmount) {
        ActualOrderAmount = actualOrderAmount;
    }

    public String getDiscountedOrderAmount() {
        return DiscountedOrderAmount;
    }

    public void setDiscountedOrderAmount(String discountedOrderAmount) {
        DiscountedOrderAmount = discountedOrderAmount;
    }

    public Boolean getIs_Recommended() {
        return Is_Recommended;
    }

    public void setIs_Recommended(Boolean is_Recommended) {
        Is_Recommended = is_Recommended;
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

    public void clone(RenewalServicePlan servicePlan) {
        this.Id = servicePlan.getId();
        this.Parent_Id_Plan = servicePlan.getParentPlanId();
        this.Plan_Name = servicePlan.getPlanName();
        this.Sp_Code = servicePlan.getSpCode();
        this.Service_Description = servicePlan.getServiceDescription();
//        this.Image_URL= servicePlan.getImageUrl();
        this.Discount = servicePlan.getDiscount();
        this.ActualOrderAmount = servicePlan.getActualOrderAmount();
        this.DiscountedOrderAmount = servicePlan.getDiscountedOrderAmount();
        this.Discount = servicePlan.getDiscount();
        this.Is_Recommended = servicePlan.getIsRecommended();
        this.OfferText = servicePlan.getOfferText();
        this.DiscountAmount = servicePlan.getDiscountAmount();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (Id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(Id);
        }
        if (Parent_Id_Plan == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(Parent_Id_Plan);
        }
        dest.writeString(Plan_Name);
        dest.writeString(Sp_Code);
        dest.writeString(Service_Description);
        dest.writeString(Image_URL);
        dest.writeString(Discount);
        dest.writeString(ActualOrderAmount);
        dest.writeString(DiscountedOrderAmount);
        dest.writeString(DiscountAmount);
        dest.writeByte((byte) (Is_Recommended == null ? 0 : Is_Recommended ? 1 : 2));
        dest.writeString(OfferText);
    }
}
