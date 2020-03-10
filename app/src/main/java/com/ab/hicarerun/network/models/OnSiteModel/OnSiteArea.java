package com.ab.hicarerun.network.models.OnSiteModel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.RealmObject;

/**
 * Created by Arjun Bhatt on 12/18/2019.
 */
public class OnSiteArea extends RealmObject implements Parcelable {
    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Account__c")
    @Expose
    private String accountC;
    @SerializedName("Area_Type__c")
    @Expose
    private String areaTypeC;
    @SerializedName("Area_Sub_Type__c")
    @Expose
    private String areaSubTypeC;
    @SerializedName("Service_Name__c")
    @Expose
    private String serviceNameC;
    @SerializedName("LastActivityOn_Text")
    @Expose
    private String LastActivityOn_Text;
    @SerializedName("ActivityDetail")
    @Expose
    private RealmList<String> activityDetail = null;

    @SerializedName("TotalCompletedCount")
    @Expose
    private Integer TotalCompletedCount;

    public OnSiteArea() {
    }


    protected OnSiteArea(Parcel in) {
        id = in.readString();
        name = in.readString();
        accountC = in.readString();
        areaTypeC = in.readString();
        areaSubTypeC = in.readString();
        serviceNameC = in.readString();
        LastActivityOn_Text = in.readString();
        if (in.readByte() == 0) {
            TotalCompletedCount = null;
        } else {
            TotalCompletedCount = in.readInt();
        }
    }

    public static final Creator<OnSiteArea> CREATOR = new Creator<OnSiteArea>() {
        @Override
        public OnSiteArea createFromParcel(Parcel in) {
            return new OnSiteArea(in);
        }

        @Override
        public OnSiteArea[] newArray(int size) {
            return new OnSiteArea[size];
        }
    };

    public String getLastActivityOn_Text() {
        return LastActivityOn_Text;
    }

    public void setLastActivityOn_Text(String lastActivityOn_Text) {
        LastActivityOn_Text = lastActivityOn_Text;
    }

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

    public String getAccountC() {
        return accountC;
    }

    public void setAccountC(String accountC) {
        this.accountC = accountC;
    }

    public String getAreaTypeC() {
        return areaTypeC;
    }

    public void setAreaTypeC(String areaTypeC) {
        this.areaTypeC = areaTypeC;
    }

    public String getAreaSubTypeC() {
        return areaSubTypeC;
    }

    public void setAreaSubTypeC(String areaSubTypeC) {
        this.areaSubTypeC = areaSubTypeC;
    }

    public String getServiceNameC() {
        return serviceNameC;
    }

    public void setServiceNameC(String serviceNameC) {
        this.serviceNameC = serviceNameC;
    }

    public RealmList<String> getActivityDetail() {
        return activityDetail;
    }

    public void setActivityDetail(RealmList<String> activityDetail) {
        this.activityDetail = activityDetail;
    }

    public Integer getTotalCompletedCount() {
        return TotalCompletedCount;
    }

    public void setTotalCompletedCount(Integer totalCompletedCount) {
        TotalCompletedCount = totalCompletedCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(accountC);
        parcel.writeString(areaTypeC);
        parcel.writeString(areaSubTypeC);
        parcel.writeString(serviceNameC);
        parcel.writeString(LastActivityOn_Text);
        if (TotalCompletedCount == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(TotalCompletedCount);
        }
    }


}
