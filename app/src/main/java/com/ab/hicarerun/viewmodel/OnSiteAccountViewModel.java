package com.ab.hicarerun.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.ab.hicarerun.network.models.OnSiteModel.Account;
import com.ab.hicarerun.network.models.OnSiteModel.OnSiteAccounts;
import com.ab.hicarerun.network.models.TechnicianGroomingModel.TechGroom;

/**
 * Created by Arjun Bhatt on 12/18/2019.
 */
public class OnSiteAccountViewModel implements Parcelable {
    private String AccountName;
    private String FlatNumber;
    private String BuildingName;
    private String LandMark;
    private String Locality;
    private String BillingStreet;
    private String PostalCode;
    private Double latitude;
    private Double longitude;

    public OnSiteAccountViewModel() {
        AccountName = "NA";
        FlatNumber = "NA";
        BuildingName = "NA";
        LandMark = "NA";
        Locality = "NA";
        BillingStreet = "NA";
        PostalCode = "NA";
        latitude = 0.0;
        longitude = 0.0;
    }


    protected OnSiteAccountViewModel(Parcel in) {
        AccountName = in.readString();
        FlatNumber = in.readString();
        BuildingName = in.readString();
        LandMark = in.readString();
        Locality = in.readString();
        BillingStreet = in.readString();
        PostalCode = in.readString();
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
    }

    public static final Creator<OnSiteAccountViewModel> CREATOR = new Creator<OnSiteAccountViewModel>() {
        @Override
        public OnSiteAccountViewModel createFromParcel(Parcel in) {
            return new OnSiteAccountViewModel(in);
        }

        @Override
        public OnSiteAccountViewModel[] newArray(int size) {
            return new OnSiteAccountViewModel[size];
        }
    };

    public String getAccountName() {
        return AccountName;
    }

    public void setAccountName(String accountName) {
        AccountName = accountName;
    }

    public String getFlatNumber() {
        return FlatNumber;
    }

    public void setFlatNumber(String flatNumber) {
        FlatNumber = flatNumber;
    }

    public String getBuildingName() {
        return BuildingName;
    }

    public void setBuildingName(String buildingName) {
        BuildingName = buildingName;
    }

    public String getLandMark() {
        return LandMark;
    }

    public void setLandMark(String landMark) {
        LandMark = landMark;
    }

    public String getLocality() {
        return Locality;
    }

    public void setLocality(String locality) {
        Locality = locality;
    }

    public String getBillingStreet() {
        return BillingStreet;
    }

    public void setBillingStreet(String billingStreet) {
        BillingStreet = billingStreet;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String postalCode) {
        PostalCode = postalCode;
    }


    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void clone(OnSiteAccounts account) {
        this.AccountName = account.getAccount().getName();
        this.FlatNumber = account.getAccount().getFlatNumberC();
        this.BuildingName = account.getAccount().getBuildingNameC();
        this.LandMark = account.getAccount().getLandmarkC();
        this.Locality = account.getAccount().getLocalitySuburbC();
        this.BillingStreet = account.getAccount().getBillingStreet();
        this.PostalCode = account.getAccount().getBillingPostalCode();
        this.latitude = account.getAccount().getLocationLatitudeS();
        this.longitude = account.getAccount().getLocationLongitudeS();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(AccountName);
        parcel.writeString(FlatNumber);
        parcel.writeString(BuildingName);
        parcel.writeString(LandMark);
        parcel.writeString(Locality);
        parcel.writeString(BillingStreet);
        parcel.writeString(PostalCode);
        if (latitude == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(latitude);
        }
        if (longitude == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(longitude);
        }
    }
}
