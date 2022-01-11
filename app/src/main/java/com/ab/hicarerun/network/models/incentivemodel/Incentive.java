package com.ab.hicarerun.network.models.incentivemodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 10/15/2019.
 */
public class Incentive implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("region")
    @Expose
    private String region;
    @SerializedName("service_Center")
    @Expose
    private String serviceCenter;
    @SerializedName("tech_Name")
    @Expose
    private String techName;
    @SerializedName("mobile")
    @Expose
    private String mobile;

    @SerializedName("total_SV")
    @Expose
    private String totalSV;
    @SerializedName("Total_Point")
    @Expose
    private String totalPoint;
    @SerializedName("Total_Incentive")
    @Expose
    private String totalIncentive;
    @SerializedName("Incentive_Date")
    @Expose
    private String incentiveDate;

    @SerializedName("IncentiveMatrix") @Expose
    private List<Matrix> matrixList = null;

    protected Incentive(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        region = in.readString();
        serviceCenter = in.readString();
        techName = in.readString();
        mobile = in.readString();
        totalSV = in.readString();
        totalPoint = in.readString();
        totalIncentive = in.readString();
        incentiveDate = in.readString();
    }

    public static final Creator<Incentive> CREATOR = new Creator<Incentive>() {
        @Override
        public Incentive createFromParcel(Parcel in) {
            return new Incentive(in);
        }

        @Override
        public Incentive[] newArray(int size) {
            return new Incentive[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getServiceCenter() {
        return serviceCenter;
    }

    public void setServiceCenter(String serviceCenter) {
        this.serviceCenter = serviceCenter;
    }

    public String getTechName() {
        return techName;
    }

    public void setTechName(String techName) {
        this.techName = techName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTotalSV() {
        return totalSV;
    }

    public void setTotalSV(String totalSV) {
        this.totalSV = totalSV;
    }

    public String getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(String totalPoint) {
        this.totalPoint = totalPoint;
    }

    public String getTotalIncentive() {
        return totalIncentive;
    }

    public void setTotalIncentive(String totalIncentive) {
        this.totalIncentive = totalIncentive;
    }

    public String getIncentiveDate() {
        return incentiveDate;
    }

    public void setIncentiveDate(String incentiveDate) {
        this.incentiveDate = incentiveDate;
    }

    public List<Matrix> getMatrixList() {
        return matrixList;
    }

    public void setMatrixList(List<Matrix> matrixList) {
        this.matrixList = matrixList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(id);
        }
        parcel.writeString(region);
        parcel.writeString(serviceCenter);
        parcel.writeString(techName);
        parcel.writeString(mobile);
        parcel.writeString(totalSV);
        parcel.writeString(totalPoint);
        parcel.writeString(totalIncentive);
        parcel.writeString(incentiveDate);
    }
}
