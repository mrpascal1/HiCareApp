package com.ab.hicarerun.network.models.ExtendRecentModel;

import android.os.Parcel;
import android.os.Parcelable;

import com.ab.hicarerun.network.models.OnSiteModel.OnSiteRecent;
import com.ab.hicarerun.network.models.OnSiteModel.RecentActivityDetails;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Arjun Bhatt on 12/31/2019.
 */
public class ChildRecent implements Parcelable {
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("AccountName")
    @Expose
    private String accountName;
    @SerializedName("AccountId")
    @Expose
    private String accountId;
    @SerializedName("AccountNo")
    @Expose
    private String accountNo;
    @SerializedName("TaskId")
    @Expose
    private String taskId;
    @SerializedName("TaskNo")
    @Expose
    private String taskNo;
    @SerializedName("ServiceRequestId")
    @Expose
    private String serviceRequestId;
    @SerializedName("ServiceRequestNo")
    @Expose
    private String serviceRequestNo;
    @SerializedName("ServiceType")
    @Expose
    private String ServiceType;
    @SerializedName("AreaType")
    @Expose
    private String areaType;
    @SerializedName("AreaSubType")
    @Expose
    private String areaSubType;
    @SerializedName("CreatedBy")
    @Expose
    private String createdBy;
    @SerializedName("ModifiedBy")
    @Expose
    private String modifiedBy;
    @SerializedName("AccountActivity")
    @Expose
    private String accountActivity;
    @SerializedName("ActivityDetail")
    @Expose
    private List<RecentActivityDetails> activityDetail = null;


    protected ChildRecent(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        accountName = in.readString();
        accountId = in.readString();
        accountNo = in.readString();
        taskId = in.readString();
        taskNo = in.readString();
        serviceRequestId = in.readString();
        serviceRequestNo = in.readString();
        ServiceType = in.readString();
        areaType = in.readString();
        areaSubType = in.readString();
        createdBy = in.readString();
        modifiedBy = in.readString();
        accountActivity = in.readString();
        activityDetail = in.createTypedArrayList(RecentActivityDetails.CREATOR);
    }

    public static final Creator<ChildRecent> CREATOR = new Creator<ChildRecent>() {
        @Override
        public ChildRecent createFromParcel(Parcel in) {
            return new ChildRecent(in);
        }

        @Override
        public ChildRecent[] newArray(int size) {
            return new ChildRecent[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getServiceRequestId() {
        return serviceRequestId;
    }

    public void setServiceRequestId(String serviceRequestId) {
        this.serviceRequestId = serviceRequestId;
    }

    public String getServiceRequestNo() {
        return serviceRequestNo;
    }

    public void setServiceRequestNo(String serviceRequestNo) {
        this.serviceRequestNo = serviceRequestNo;
    }

    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public String getAreaSubType() {
        return areaSubType;
    }

    public void setAreaSubType(String areaSubType) {
        this.areaSubType = areaSubType;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getAccountActivity() {
        return accountActivity;
    }

    public void setAccountActivity(String accountActivity) {
        this.accountActivity = accountActivity;
    }

    public List<RecentActivityDetails> getActivityDetail() {
        return activityDetail;
    }

    public void setActivityDetail(List<RecentActivityDetails> activityDetail) {
        this.activityDetail = activityDetail;
    }

    public String getServiceType() {
        return ServiceType;
    }

    public void setServiceType(String serviceType) {
        ServiceType = serviceType;
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
        parcel.writeString(accountName);
        parcel.writeString(accountId);
        parcel.writeString(accountNo);
        parcel.writeString(taskId);
        parcel.writeString(taskNo);
        parcel.writeString(serviceRequestId);
        parcel.writeString(serviceRequestNo);
        parcel.writeString(ServiceType);
        parcel.writeString(areaType);
        parcel.writeString(areaSubType);
        parcel.writeString(createdBy);
        parcel.writeString(modifiedBy);
        parcel.writeString(accountActivity);
        parcel.writeTypedList(activityDetail);
    }
}
