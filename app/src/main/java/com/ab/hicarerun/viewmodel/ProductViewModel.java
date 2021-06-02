package com.ab.hicarerun.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.ab.hicarerun.network.models.OnSiteModel.OnSiteRecent;
import com.ab.hicarerun.network.models.OnSiteModel.RecentActivityDetails;
import com.ab.hicarerun.network.models.ProductModel.ProductData;
import com.ab.hicarerun.network.models.ProductModel.ServicePlanUnits;

import java.util.List;

/**
 * Created by Arjun Bhatt on 5/28/2020.
 */
public class ProductViewModel implements Parcelable {
    private String Plan_Name;
    private String Service_Description;
    private String Image_URL;
    private String Discount;
    private String ActualOrderAmount;
    private String DiscountOrderAmount;
    private String RenewalServicePlans;
    private List<ServicePlanUnits> servicePlanUnits = null;

    public ProductViewModel() {
        Plan_Name = "NA";
        Service_Description = "NA";
        Image_URL = "NA";
        Discount = "NA";
        ActualOrderAmount = "NA";
        DiscountOrderAmount = "NA";
        RenewalServicePlans = "NA";
    }


    protected ProductViewModel(Parcel in) {
        Plan_Name = in.readString();
        Service_Description = in.readString();
        Image_URL = in.readString();
        Discount = in.readString();
        ActualOrderAmount = in.readString();
        DiscountOrderAmount = in.readString();
        RenewalServicePlans = in.readString();
        servicePlanUnits = in.createTypedArrayList(ServicePlanUnits.CREATOR);
    }

    public static final Creator<ProductViewModel> CREATOR = new Creator<ProductViewModel>() {
        @Override
        public ProductViewModel createFromParcel(Parcel in) {
            return new ProductViewModel(in);
        }

        @Override
        public ProductViewModel[] newArray(int size) {
            return new ProductViewModel[size];
        }
    };

    public String getPlan_Name() {
        return Plan_Name;
    }

    public void setPlan_Name(String plan_Name) {
        Plan_Name = plan_Name;
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

    public String getDiscountOrderAmount() {
        return DiscountOrderAmount;
    }

    public void setDiscountOrderAmount(String discountOrderAmount) {
        DiscountOrderAmount = discountOrderAmount;
    }

    public String getRenewalServicePlans() {
        return RenewalServicePlans;
    }

    public void setRenewalServicePlans(String renewalServicePlans) {
        RenewalServicePlans = renewalServicePlans;
    }

    public List<ServicePlanUnits> getServicePlanUnits() {
        return servicePlanUnits;
    }

    public void setServicePlanUnits(List<ServicePlanUnits> servicePlanUnits) {
        this.servicePlanUnits = servicePlanUnits;
    }


    public void clone(ProductData productData) {
        this.Plan_Name = productData.getPlanName();
        this.Service_Description = productData.getServiceDescription();
        this.Image_URL = productData.getImageUrl();
        this.Discount = productData.getDiscount();
        this.ActualOrderAmount = productData.getActualOrderAmount();
        this.DiscountOrderAmount = productData.getDiscountedOrderAmount();
        this.RenewalServicePlans = productData.getRenewalServicePlans();
        this.servicePlanUnits = productData.getServicePlanUnits();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Plan_Name);
        dest.writeString(Service_Description);
        dest.writeString(Image_URL);
        dest.writeString(Discount);
        dest.writeString(ActualOrderAmount);
        dest.writeString(DiscountOrderAmount);
        dest.writeString(RenewalServicePlans);
        dest.writeTypedList(servicePlanUnits);
    }
}


