package com.ab.hicarerun.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.ab.hicarerun.network.models.OnSiteModel.ActivityDetail;
import com.ab.hicarerun.network.models.OnSiteModel.OnSiteAccounts;
import com.ab.hicarerun.network.models.OnSiteModel.OnSiteRecent;
import com.ab.hicarerun.network.models.OnSiteModel.RecentActivityDetails;

import java.util.List;

/**
 * Created by Arjun Bhatt on 12/23/2019.
 */
public class RecentActivityViewModel implements Parcelable {
    private Integer Id;
    private String AccountName;
    private String AccountId;
    private String AccountNo;
    private String TaskId;
    private String TaskNo;
    private String ServiceRequestId;
    private String ServiceRequestNo;
    private String AreaType;
    private String AreaSubType;
    private String CreatedBy;
    private String ModifiedBy;
    private String ServiceType;
    private List<RecentActivityDetails> activityDetails = null;


    public RecentActivityViewModel() {
        Id = 0;
        AccountName = "NA";
        AccountId = "NA";
        AccountNo = "NA";
        TaskId = "NA";
        TaskNo = "NA";
        ServiceRequestId = "NA";
        ServiceRequestNo = "NA";
        AreaType = "NA";
        AreaSubType = "NA";
        CreatedBy = "NA";
        ModifiedBy = "NA";
        ServiceType = "NA";
    }


    protected RecentActivityViewModel(Parcel in) {
        if (in.readByte() == 0) {
            Id = null;
        } else {
            Id = in.readInt();
        }
        AccountName = in.readString();
        AccountId = in.readString();
        AccountNo = in.readString();
        TaskId = in.readString();
        TaskNo = in.readString();
        ServiceRequestId = in.readString();
        ServiceRequestNo = in.readString();
        AreaType = in.readString();
        AreaSubType = in.readString();
        CreatedBy = in.readString();
        ModifiedBy = in.readString();
        ServiceType = in.readString();
        activityDetails = in.createTypedArrayList(RecentActivityDetails.CREATOR);
    }

    public static final Creator<RecentActivityViewModel> CREATOR = new Creator<RecentActivityViewModel>() {
        @Override
        public RecentActivityViewModel createFromParcel(Parcel in) {
            return new RecentActivityViewModel(in);
        }

        @Override
        public RecentActivityViewModel[] newArray(int size) {
            return new RecentActivityViewModel[size];
        }
    };

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getAccountName() {
        return AccountName;
    }

    public void setAccountName(String accountName) {
        AccountName = accountName;
    }

    public String getAccountId() {
        return AccountId;
    }

    public void setAccountId(String accountId) {
        AccountId = accountId;
    }

    public String getAccountNo() {
        return AccountNo;
    }

    public void setAccountNo(String accountNo) {
        AccountNo = accountNo;
    }

    public String getTaskId() {
        return TaskId;
    }

    public void setTaskId(String taskId) {
        TaskId = taskId;
    }

    public String getTaskNo() {
        return TaskNo;
    }

    public void setTaskNo(String taskNo) {
        TaskNo = taskNo;
    }

    public String getServiceRequestId() {
        return ServiceRequestId;
    }

    public void setServiceRequestId(String serviceRequestId) {
        ServiceRequestId = serviceRequestId;
    }

    public String getServiceRequestNo() {
        return ServiceRequestNo;
    }

    public void setServiceRequestNo(String serviceRequestNo) {
        ServiceRequestNo = serviceRequestNo;
    }

    public String getAreaType() {
        return AreaType;
    }

    public void setAreaType(String areaType) {
        AreaType = areaType;
    }

    public String getAreaSubType() {
        return AreaSubType;
    }

    public void setAreaSubType(String areaSubType) {
        AreaSubType = areaSubType;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getModifiedBy() {
        return ModifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        ModifiedBy = modifiedBy;
    }

    public List<RecentActivityDetails> getActivityDetails() {
        return activityDetails;
    }

    public void setActivityDetails(List<RecentActivityDetails> activityDetails) {
        this.activityDetails = activityDetails;
    }

    public String getServiceType() {
        return ServiceType;
    }

    public void setServiceType(String serviceType) {
        ServiceType = serviceType;
    }



    public void clone(OnSiteRecent account) {
        this.Id = account.getId();
        this.AccountName = account.getAccountName();
        this.AccountId = account.getAccountId();
        this.AccountNo = account.getAccountNo();
        this.TaskId = account.getTaskId();
        this.TaskNo = account.getTaskNo();
        this.ServiceRequestId = account.getServiceRequestId();
        this.ServiceRequestNo = account.getServiceRequestNo();
        this.AreaType = account.getAreaType();
        this.AreaSubType = account.getAreaSubType();
        this.CreatedBy = account.getCreatedBy();
        this.ModifiedBy = account.getModifiedBy();
        this.ServiceType = account.getServiceType();
        this.activityDetails = account.getActivityDetail();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (Id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(Id);
        }
        parcel.writeString(AccountName);
        parcel.writeString(AccountId);
        parcel.writeString(AccountNo);
        parcel.writeString(TaskId);
        parcel.writeString(TaskNo);
        parcel.writeString(ServiceRequestId);
        parcel.writeString(ServiceRequestNo);
        parcel.writeString(AreaType);
        parcel.writeString(AreaSubType);
        parcel.writeString(CreatedBy);
        parcel.writeString(ModifiedBy);
        parcel.writeString(ServiceType);
        parcel.writeTypedList(activityDetails);
    }
}
