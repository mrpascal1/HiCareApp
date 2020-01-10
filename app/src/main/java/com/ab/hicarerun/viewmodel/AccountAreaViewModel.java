package com.ab.hicarerun.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.ab.hicarerun.network.models.AttachmentModel.GetAttachmentList;
import com.ab.hicarerun.network.models.OnSiteModel.AccountsArea;
import com.ab.hicarerun.network.models.OnSiteModel.OnSiteArea;

/**
 * Created by Arjun Bhatt on 12/19/2019.
 */
public class AccountAreaViewModel implements Parcelable {
    private String id;
    private String name;
    private String accountId;
    private String Area;
    private String subArea;
    private String ServiceName;
    private String LastActivity;

    public AccountAreaViewModel() {
        this.id = "NA";
        this.name = "NA";
        this.accountId = "NA";
        this.Area = "NA";
        this.subArea = "NA";
        this.ServiceName = "NA";
        this.LastActivity = "NA";
    }


    protected AccountAreaViewModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        accountId = in.readString();
        Area = in.readString();
        subArea = in.readString();
        ServiceName = in.readString();
        LastActivity = in.readString();
    }

    public static final Creator<AccountAreaViewModel> CREATOR = new Creator<AccountAreaViewModel>() {
        @Override
        public AccountAreaViewModel createFromParcel(Parcel in) {
            return new AccountAreaViewModel(in);
        }

        @Override
        public AccountAreaViewModel[] newArray(int size) {
            return new AccountAreaViewModel[size];
        }
    };

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

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getSubArea() {
        return subArea;
    }

    public void setSubArea(String subArea) {
        this.subArea = subArea;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }


    public String getLastActivity() {
        return LastActivity;
    }

    public void setLastActivity(String lastActivity) {
        LastActivity = lastActivity;
    }

    public void clone(OnSiteArea area) {
        this.id = area.getId();
        this.name = area.getName();
        this.accountId = area.getAccountC();
        this.Area = area.getAreaTypeC();
        this.subArea = area.getAreaSubTypeC();
        this.ServiceName= area.getServiceNameC();
        this.LastActivity = area.getLastActivityOn_Text();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(accountId);
        parcel.writeString(Area);
        parcel.writeString(subArea);
        parcel.writeString(ServiceName);
        parcel.writeString(LastActivity);
    }
}


